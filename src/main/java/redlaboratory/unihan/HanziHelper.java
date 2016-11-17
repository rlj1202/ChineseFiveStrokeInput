package redlaboratory.unihan;

public class HanziHelper {
	
	public static boolean isHanzi(char c) {
		if ((0x4E00 <= c && c <= 0x9FFF)// Common
				| (0x3400 <= c && c <= 0x4DFF)// Rare
				| (0x20000 <= c && c <= 0x2A6DF)// Rare, historic
				| (0x2A700 <= c && c <= 0x2B73F)// Rare, historic
				| (0x2B710 <= c && c <= 0x2B81F)// Uncommon, some in current use
				| (0x2B820 <= c && c <= 0x2CEAF)// Rare, historic
				| (0xF900 <= c && c <= 0xFAFF)// Duplicate, unifiable variants, corporate characters
				| (0x2F800 <= c && c <= 0x2FA1F)) {// Unifiable variants
			return true;
		}
		
		return false;
	}
	
	public static String getPinyin(char c, String def) {
		if (!isHanzi(c)) return def;
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.Readings);
		
		return uni.searchValue(c, DataType.kMandarin, def).split(" ")[0];
	}
	
	public static boolean isAbleToBeCompatible(char c) {
		if (!isHanzi(c)) return false;
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.IRG_Sources);
		
		return uni.hasDataType(c, DataType.kCompatibilityVariant);
	}

	public static boolean isTraditional(char c) {
		if (!isHanzi(c)) return false;
		
		c = toCompatibleChar(c);
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.Variants);
		
		return uni.hasDataType(c, DataType.kSimplifiedVariant);
	}
	
	public static boolean isSimplefied(char c) {
		if (!isHanzi(c)) return false;
		
		c = toCompatibleChar(c);
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.Variants);
		
		return uni.hasDataType(c, DataType.kTraditionalVariant);
	}
	
	public static char toCompatibleChar(char c) {
		if (!isHanzi(c)) return c;
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.IRG_Sources);
		String res = uni.searchValue(c, DataType.kCompatibilityVariant, null);
		
		if (res != null) {
			return (char) Integer.decode("0x".concat(res.substring(2))).intValue();
		} else {
			return c;
		}
	}
	
	public static char toSimplefiedChar(char c) {
		if (!isHanzi(c)) return c;
		
		c = toCompatibleChar(c);
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.Variants);
		String res = uni.searchValue(c, DataType.kSimplifiedVariant, null);
		
		if (res != null) {
			return (char) Integer.decode("0x".concat(res.substring(2))).intValue();
		} else {
			return c;
		}
	}
	
	public static char toTraditionalChar(char c) {
		if (!isHanzi(c)) return c;
		
		c = toCompatibleChar(c);
		
		JUnihan uni = JUnihan.getInstance();
		uni.initialize(TableType.Variants);
		String res = uni.searchValue(c, DataType.kTraditionalVariant, null);
		
		if (res != null) {
			return (char) Integer.decode("0x".concat(res.substring(2))).intValue();
		} else {
			return c;
		}
	}
	
}
