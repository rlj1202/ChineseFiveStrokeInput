package redlaboratory.chineseinput;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import redlaboratory.chineseinput.strokedatabase.DefaultStrokeDatabase;
import redlaboratory.chineseinput.strokedatabase.Hanzi;
import redlaboratory.chineseinput.strokedatabase.StrokeDatabase;
import redlaboratory.chineseinput.strokedatabase.StrokeDatabaseLoader;
import redlaboratory.globalinputmanager.GlobalHook;
import redlaboratory.globalinputmanager.GlobalInput;
import redlaboratory.globalinputmanager.hookEvent.GlobalEventAdapter;
import redlaboratory.globalinputmanager.hookEvent.GlobalKeyboardEvent;
import redlaboratory.unihan.DataType;
import redlaboratory.unihan.HanziHelper;
import redlaboratory.unihan.JUnihan;
import redlaboratory.unihan.TableType;

public class Core {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		Core core = new Core();
		core.start();
	}
	
	public class GUI extends JFrame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 9145910770616774702L;
		
		public JTextField strokeTextField;
		
		public JButton strokeH;
		public JButton strokeS;
		public JButton strokeP;
		public JButton strokeN;
		public JButton strokeZ;
		public JButton backspace;
		public JButton clear;
		public JButton chineseInputModeToggle;
		
		public JButton[] hanzis;
		
		public JTextField textField;
		public JButton convertHanziToPinyin;
		public JButton convertHanziToHangul;
		public JButton convertTraditionToSimplefied;
		public JButton convertSimplefiedToTradition;
		
		public GUI(String title, int width, int height) {
			super(title);
			
			initialize(width, height);
		}
		
		private void initialize(int width, int height) {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(width, height);
			this.setResizable(false);
			if (this.isAlwaysOnTopSupported()) this.setAlwaysOnTop(true);
			else System.out.println("AlwaysOnTop is not supported.");
			
			strokeTextField = new JTextField();
			strokeTextField.setEditable(false);
			strokeTextField.setFont(new Font("Dialog", Font.BOLD, 25));
			
			strokeH = new JButton("\u4E00");
			strokeS = new JButton("\u4E28");
			strokeP = new JButton("\u4E3F");
			strokeN = new JButton("\u4E36");
			strokeZ = new JButton("\u4E5B");
			backspace = new JButton("backspace");
			clear = new JButton("clear");
			chineseInputModeToggle = new JButton("Input Toggle");
			
			hanzis = new JButton[10];
			for (int i = 0; i < 10; i++) {
				hanzis[i] = new JButton();
//				Font font = hanzis[i].getFont();
//				hanzis[i].setFont(new Font(font.getName(), font.getStyle(), 25));
//				hanzis[i].setFont(new Font("msyhl", font.getStyle(), 25));
				hanzis[i].setFont(new Font("Microsoft JhengHei", Font.PLAIN, 25));
			}
			
			textField = new JTextField();
			convertHanziToPinyin = new JButton("pinyin");
			convertHanziToHangul = new JButton("hangul");
			convertTraditionToSimplefied = new JButton("simplefy");
			convertSimplefiedToTradition = new JButton("tradition");
			
			strokeH.addActionListener((event) -> {Core.this.buttonInput("strokeH");});
			strokeS.addActionListener((event) -> {Core.this.buttonInput("strokeS");});
			strokeP.addActionListener((event) -> {Core.this.buttonInput("strokeP");});
			strokeN.addActionListener((event) -> {Core.this.buttonInput("strokeN");});
			strokeZ.addActionListener((event) -> {Core.this.buttonInput("strokeZ");});
			backspace.addActionListener((event) -> {Core.this.buttonInput("backspace");});
			clear.addActionListener((event) -> {Core.this.buttonInput("clear");});
			chineseInputModeToggle.addActionListener((event) -> {Core.this.buttonInput("chineseInputModeToggle");});
			convertHanziToPinyin.addActionListener((event) -> {Core.this.buttonInput("convertHanziToPinyin");});
			convertHanziToHangul.addActionListener((event) -> {Core.this.buttonInput("convertHanziToHangul");});
			convertTraditionToSimplefied.addActionListener((event) -> {Core.this.buttonInput("convertTraditionToSimplefied");});
			convertSimplefiedToTradition.addActionListener((event) -> {Core.this.buttonInput("convertSimplefiedToTradition");});
			
			for (int i = 0; i < 10; i++) {
				int index = i;
				hanzis[i].addActionListener((event) -> {Core.this.buttonInput("hanzi" + index);});
			}
			
			RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 5);
			rl.setFill(true);
			rl.setFillGap(10);
			this.setLayout(rl);
			
			this.add(strokeTextField, new Float(1));
			JPanel buttonPanel = new JPanel(new GridLayout(4, 5, 2, 2));
			{
				buttonPanel.add(chineseInputModeToggle);
				buttonPanel.add(strokeH);
				buttonPanel.add(strokeS);
				buttonPanel.add(strokeP);
				buttonPanel.add(backspace);
				
				buttonPanel.add(Box.createHorizontalGlue());
				buttonPanel.add(strokeN);
				buttonPanel.add(strokeZ);
				buttonPanel.add(Box.createHorizontalGlue());
				buttonPanel.add(clear);
				
				for (JButton hanzi : hanzis) buttonPanel.add(hanzi);
			}
			this.add(buttonPanel, new Float(4));
			this.add(textField, new Float(0.6));
			JPanel convertPanel = new JPanel(new GridLayout(1, 4, 2, 2));
			{
				convertPanel.add(convertHanziToPinyin);
				convertPanel.add(convertHanziToHangul);
				convertPanel.add(convertTraditionToSimplefied);
				convertPanel.add(convertSimplefiedToTradition);
			}
			this.add(convertPanel, new Float(0.6));
		}
		
	}
	
	private GUI gui;
	private String title = "Chinese 5stroke-based Input tool.";
	private int width = 500;
	private int height = 330;
	
	private boolean chineseInputMode;
	private List<Integer> chineseInputKeys = Arrays.asList(
			GlobalKeyboardEvent.VK_I, GlobalKeyboardEvent.VK_O, GlobalKeyboardEvent.VK_P,
			GlobalKeyboardEvent.VK_K, GlobalKeyboardEvent.VK_L);
	
	private StrokeDatabase strokeDatabaseA;
	private StrokeDatabase strokeDatabaseB;
	private Comparator<Hanzi> compA;
	private Comparator<Hanzi> compB;
	private JUnihan junihan;
	
	private int searchOffset;
	
	private String strokes;
	private List<Hanzi> currentHanzis;
	
	public Core() {
		compA = (a, b) -> {
			if (a.stroke.length() < b.stroke.length()) return 1;
			if (a.stroke.length() > b.stroke.length()) return -1;
			
			Function<Character, Integer> priority = (c) -> {
				switch (c) {
				case 'h': return 0;
				case 's': return 1;
				case 'p': return 2;
				case 'n': return 3;
				case 'z': return 4;
				default: return -1;
				}
			};
			
			for (int i = 0; i < a.stroke.length(); i++) {
				int ac = priority.apply(a.stroke.charAt(i));
				int bc = priority.apply(b.stroke.charAt(i));
				
				if (ac == bc) continue;
				if (ac < bc) return 1;
				if (ac > bc) return -1;
			}
			
			return 0;
		};
		
		compB = (a, b) -> {
			int maxLength = Math.max(a.stroke.length(), b.stroke.length());
			
			Function<Character, Integer> priority = (c) -> {
				switch (c) {
				case 'h': return 0;
				case 's': return 1;
				case 'p': return 2;
				case 'n': return 3;
				case 'z': return 4;
				default: return -1;
				}
			};
			
			for (int i = 0; i < maxLength; i++) {
				int ac = -1;
				int bc = -1;
				
				if (i < a.stroke.length()) ac = priority.apply(a.stroke.charAt(i));
				if (i < b.stroke.length()) bc = priority.apply(b.stroke.charAt(i));
				
				if (ac == bc) continue;
				if (ac < bc) return 1;
				if (ac > bc) return -1;
			}
			
			return 0;
		};
		
		try {
			strokeDatabaseA = new DefaultStrokeDatabase(StrokeDatabaseLoader.load(Core.class.getResourceAsStream("tableA.txt"), "utf-8").sort(compA).getHanzis(), compA);
			strokeDatabaseB = new DefaultStrokeDatabase(StrokeDatabaseLoader.load(Core.class.getResourceAsStream("tableB.txt"), "utf-8").sort(compB).getHanzis(), compB);
			
			junihan = JUnihan.getInstance();
			junihan.initialize(TableType.Readings);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gui = new GUI(title, width, height);
		
		chineseInputMode = false;
		
		searchOffset = 0;
		
		strokes = "";
		currentHanzis = new ArrayList<Hanzi>();
	}
	
	public void start() {
		GlobalHook.create();
		GlobalHook.registerKeyboardEventHandler(new GlobalEventAdapter() {
			
			@Override
			public void keyPressed(GlobalKeyboardEvent event) {
				System.out.println("	message pressed: " + event.getVirtureKeyCode() + ", " + event.isAltDowned() + ", " + event.isCtrlDowned());
				
				if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_F9) {
					event.setCanceled(true);
					new Thread(() -> {gui.chineseInputModeToggle.doClick();}).start();
					return;
				}
				
				if (chineseInputMode) {
					if (chineseInputKeys.contains(event.getVirtureKeyCode())) {
						event.setCanceled(true);
						
						for (int i = 0; i < chineseInputKeys.size(); i++) {
							if (chineseInputKeys.get(i) != event.getVirtureKeyCode()) continue;
							
							if (i == 0) new Thread(() -> {gui.strokeH.doClick();}).start();
							else if (i == 1) new Thread(() -> {gui.strokeS.doClick();}).start();
							else if (i == 2) new Thread(() -> {gui.strokeP.doClick();}).start();
							else if (i == 3) new Thread(() -> {gui.strokeN.doClick();}).start();
							else if (i == 4) new Thread(() -> {gui.strokeZ.doClick();}).start();
						}
					}
					
					if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_BACKSPACE) {
						if (!strokes.isEmpty()) {
							event.setCanceled(true);
							new Thread(() -> {gui.backspace.doClick();}).start();
						}
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_ENTER) {
						if (!strokes.isEmpty()) {
							event.setCanceled(true);
							new Thread(() -> {gui.hanzis[0].doClick();}).start();
						}
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_OEM_4) {
						event.setCanceled(true);
						new Thread(() -> {searchOffset--; update();}).start();
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_OEM_6) {
						event.setCanceled(true);
						new Thread(() -> {searchOffset++; update();}).start();
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_DELETE) {
						event.setCanceled(true);
						new Thread(() -> {gui.clear.doClick();}).start();
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_OEM_PERIOD) {
						event.setCanceled(true);
						GlobalInput.typeString("\u3002");
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_OEM_COMMA) {
						event.setCanceled(true);
						GlobalInput.typeString("\u3001");
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_1 && event.isShiftDowned()) {
						event.setCanceled(true);
						GlobalInput.typeString("\uFF01");
					} else if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_OEM_2 && event.isShiftDowned()) {
						event.setCanceled(true);
						GlobalInput.typeString("\uFF1F");
					}
				}
			}
			
			@Override
			public void keyReleased(GlobalKeyboardEvent event) {
				if (chineseInputMode) {
					if (event.getVirtureKeyCode() == GlobalKeyboardEvent.VK_BACKSPACE) {
						if (!strokes.isEmpty()) {
							event.setCanceled(true);
							new Thread(() -> {gui.backspace.doClick();}).start();
						}
					}
				}
			}
			
		});
		
		gui.setVisible(true);
		gui.setLocationRelativeTo(null);
		gui.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				GlobalHook.destroy();
			}
			
		});
		
		update();
	}
	
	public void update() {
		gui.strokeTextField.setText(
				strokes
				.replace('h', '\u4E00')
				.replace('s', '\u4E28')
				.replace('p', '\u4E3F')
				.replace('n', '\u4E36')
				.replace('z', '\u4E5B')
				);
		
		for (int i = 0; i < 10; i++) {
			gui.hanzis[i].setText("-");
		}
		
		currentHanzis.clear();
		
		if (strokes != null && !strokes.isEmpty()) {
			List<Hanzi> hanzisA = strokeDatabaseA.getHanzis(strokeDatabaseA.searchHanzi(strokes) + searchOffset, 5);
			List<Hanzi> hanzisB = strokeDatabaseB.getHanzis(strokeDatabaseB.searchHanzi(strokes) + searchOffset, 5);
			hanzisA.removeIf(Objects::isNull);
			hanzisB.removeIf(Objects::isNull);
			currentHanzis.addAll(hanzisA);
			currentHanzis.addAll(hanzisB);
		}
		
		for (int i = 0; i < currentHanzis.size(); i++) {
			gui.hanzis[i].setText(
					"<html>" +
							"<center>" +
									currentHanzis.get(i).character + "<br>" +
									"<font size=\"3\">\"" + junihan.searchValue(currentHanzis.get(i).character, DataType.kHangul, "-") + "\"</font>" +
							"</center>" +
					"</html>");
		}
		
		gui.chineseInputModeToggle.setText(
				"<html>" +
						"<center>" +
								"Input Toggle<br>" +
								((chineseInputMode) ? ("On") : ("Off")) +
						"</center>" +
				"</html>"
				);
	}
	
	private void buttonInput(String buttonName) {
		if (buttonName.equals("strokeH")) {
			strokes += "h";
		} else if (buttonName.equals("strokeS")) {
			strokes += "s";
		} else if (buttonName.equals("strokeP")) {
			strokes += "p";
		} else if (buttonName.equals("strokeN")) {
			strokes += "n";
		} else if (buttonName.equals("strokeZ")) {
			strokes += "z";
		} else if (buttonName.equals("backspace")) {
			if (!strokes.isEmpty()) strokes = strokes.substring(0, strokes.length() - 1);
			if (strokes.isEmpty()) searchOffset = 0;
		} else if (buttonName.equals("clear")) {
			strokes = "";
			searchOffset = 0;
		} else if (buttonName.equals("chineseInputModeToggle")) {
			chineseInputMode = !chineseInputMode;
		} else if (buttonName.startsWith("hanzi")) {
			int index = Integer.parseInt(buttonName.substring(5));
			
			Hanzi hanzi = currentHanzis.get(index);
			GlobalInput.typeString(String.valueOf(hanzi.character));
			strokes = "";
			searchOffset = 0;
		} else if (buttonName.equals("convertHanziToPinyin")) {
			String res = gui.textField.getText().chars()
					.mapToObj((c) -> {return (char) c;})
					.map((c) -> {return (HanziHelper.isHanzi(c)) ? (HanziHelper.getPinyin(c, String.valueOf(c)).concat(" ")) : (String.valueOf(c));})
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
			gui.textField.setText(res);
		} else if (buttonName.equals("convertHanziToHangul")) {
			String res = gui.textField.getText().chars()
					.mapToObj((c) -> {return (char) c;})
					.map((c) -> {JUnihan.getInstance().initialize(TableType.Readings); if (!HanziHelper.isHanzi(c)) return String.valueOf(c); return JUnihan.getInstance().searchValue(c, DataType.kHangul, String.valueOf(c)).substring(0, 1);})
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
			gui.textField.setText(res);
		} else if (buttonName.equals("convertTraditionToSimplefied")) {
			String res = gui.textField.getText().chars()
					.mapToObj((c) -> {return (char) c;})
					.map((c) -> {return HanziHelper.toSimplefiedChar(c);})
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
			gui.textField.setText(res);
		} else if (buttonName.equals("convertSimplefiedToTradition")) {
			String res = gui.textField.getText().chars()
					.mapToObj((c) -> {return (char) c;})
					.map((c) -> {return HanziHelper.toTraditionalChar(c);})
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
			gui.textField.setText(res);
		}
		
		update();
	}
	
}
