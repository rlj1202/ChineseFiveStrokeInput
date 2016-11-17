package redlaboratory.unihan;

public enum DataType {
	// IRG Sources
	kCompatibilityVariant(TableType.IRG_Sources, "kCompatibilityVariant", 0),
	kIICore(TableType.IRG_Sources, "kIICore", 1),
	kIRG_GSource(TableType.IRG_Sources, "kIRG_GSource", 2),
	kIRG_HSource(TableType.IRG_Sources, "kIRG_HSource", 3),
	kIRG_JSource(TableType.IRG_Sources, "kIRG_JSource", 4),
	kIRG_KPSource(TableType.IRG_Sources, "kIRG_KPSource", 5),
	kIRG_KSource(TableType.IRG_Sources, "kIRG_KSource", 6),
	kIRG_MSource(TableType.IRG_Sources, "kIRG_MSource", 7),
	kIRG_TSource(TableType.IRG_Sources, "kIRG_TSource", 8),
	kIRG_USource(TableType.IRG_Sources, "kIRG_USource", 9),
	kIRG_VSource(TableType.IRG_Sources, "kIRG_VSource", 10),
	kRSUnicode(TableType.IRG_Sources, "kRSUnicode", 11),
	
	// Dictionary Indices
	kCheungBauerIndex(TableType.Dictionary_Indices, "kCheungBauerIndex", 0),
	kCowles(TableType.Dictionary_Indices, "kCowles", 1),
	kDaeJaweon(TableType.Dictionary_Indices, "kDaeJaweon", 2),
	kFennIndex(TableType.Dictionary_Indices, "kFennIndex", 3),
	kGSR(TableType.Dictionary_Indices, "kGSR", 4),
	kHanYu(TableType.Dictionary_Indices, "kHanYu", 5),
	kIRGDaeJaweon(TableType.Dictionary_Indices, "kIRGDaeJaweon", 6),
	kIRGDaiKanwaZiten(TableType.Dictionary_Indices, "kIRGDaiKanwaZiten", 7),
	kIRGHanyuDaZidian(TableType.Dictionary_Indices, "kIRGHanyuDaZidian", 8),
	kIRGKangXi(TableType.Dictionary_Indices, "kIRGKangXi", 9),
	kKangXi(TableType.Dictionary_Indices, "kKangXi", 10),
	kKarlgren(TableType.Dictionary_Indices, "kKarlgren", 11),
	kLau(TableType.Dictionary_Indices, "kLau", 12),
	kMatthews(TableType.Dictionary_Indices, "kMatthews", 13),
	kMeyerWempe(TableType.Dictionary_Indices, "kMeyerWempe", 14),
	kMorohashi(TableType.Dictionary_Indices, "kMorohashi", 15),
	kNelson(TableType.Dictionary_Indices, "kNelson", 16),
	kSBGY(TableType.Dictionary_Indices, "kSBGY", 17),
	
	// Dictionary-like Data
	kCangjie(TableType.Dictionary_like_Data, "kCangjie", 0),
	kCheungBauer(TableType.Dictionary_like_Data, "kCheungBauer", 1),
	kCihaiT(TableType.Dictionary_like_Data, "kCihaiT", 2),
	kFenn(TableType.Dictionary_like_Data, "kFenn", 3),
	kFourCornerCode(TableType.Dictionary_like_Data, "kFourCornerCode", 4),
	kFrequency(TableType.Dictionary_like_Data, "kFrequency", 5),
	kGradeLevel(TableType.Dictionary_like_Data, "kGradeLevel", 6),
	kHDZRadBreak(TableType.Dictionary_like_Data, "kHDZRadBreak", 7),
	kHKGlyph(TableType.Dictionary_like_Data, "kHKGlyph", 8),
	kPhonetic(TableType.Dictionary_like_Data, "kPhonetic", 9),
	kTotalStrokes(TableType.Dictionary_like_Data, "kTotalStrokes", 10),
	
	// Other Mappings
	kBigFive(TableType.Other_Mappings, "kBigFive", 0),
	kCCCII(TableType.Other_Mappings, "kCCCII", 1),
	kCNS1986(TableType.Other_Mappings, "kCNS1986", 2),
	kCNS1992(TableType.Other_Mappings, "kCNS1992", 3),
	kEACC(TableType.Other_Mappings, "kEACC", 4),
	kGB0(TableType.Other_Mappings, "kGB0", 5),
	kGB1(TableType.Other_Mappings, "kGB1", 6),
	kGB3(TableType.Other_Mappings, "kGB3", 7),
	kGB5(TableType.Other_Mappings, "kGB5", 8),
	kGB7(TableType.Other_Mappings, "kGB7", 9),
	kGB8(TableType.Other_Mappings, "kGB8", 10),
	kHKSCS(TableType.Other_Mappings, "kHKSCS", 11),
	kIBMJapan(TableType.Other_Mappings, "kIBMJapan", 12),
	kJa(TableType.Other_Mappings, "kJa", 13),
	kJis0(TableType.Other_Mappings, "kJis0", 14),
	kJis1(TableType.Other_Mappings, "kJis1", 15),
	kJIS0213(TableType.Other_Mappings, "kJIS0213", 16),
	kKPS0(TableType.Other_Mappings, "kKPS0", 17),
	kKPS1(TableType.Other_Mappings, "kKPS1", 18),
	kKSC0(TableType.Other_Mappings, "kKSC0", 19),
	kKSC1(TableType.Other_Mappings, "kKSC1", 20),
	kMainlandTelegraph(TableType.Other_Mappings, "kMainlandTelegraph", 21),
	kPseudoGB1(TableType.Other_Mappings, "kPseudoGB1", 22),
	kTaiwanTelegraph(TableType.Other_Mappings, "kTaiwanTelegraph", 23),
	kXerox(TableType.Other_Mappings, "kXerox", 24),
	
	// Radical-stroke Counts
	kRSAdobe_Japan1_6(TableType.Radical_stroke_Indices, "kRSAdobe_Japan1_6", 0),
	kRSJapanese(TableType.Radical_stroke_Indices, "kRSJapanese", 1),
	kRSKangXi(TableType.Radical_stroke_Indices, "kRSKangXi", 2),
	kRSKanWa(TableType.Radical_stroke_Indices, "kRSKanWa", 3),
	kRSKorean(TableType.Radical_stroke_Indices, "kRSKorean", 4),
	
	// Readings
	kCantonese(TableType.Readings, "kCantonese", 0),
	kDefinition(TableType.Readings, "kDefinition", 1),
	kHangul(TableType.Readings, "kHangul", 2),
	kHanyuPinlu(TableType.Readings, "kHanyuPinlu", 3),
	kHanyuPinyin(TableType.Readings, "kHanyuPinyin", 4),
	kJapaneseKun(TableType.Readings, "kJapaneseKun", 5),
	kJapaneseOn(TableType.Readings, "kJapaneseOn", 6),
	kKorean(TableType.Readings, "kKorean", 7),
	kMandarin(TableType.Readings, "kMandarin", 8),
	kTang(TableType.Readings, "kTang", 9),
	kVietnamese(TableType.Readings, "kVietnamese", 10),
	kXHC1983(TableType.Readings, "kXHC1983", 11),
	
	// Variants
	kSemanticVariant(TableType.Variants, "kSemanticVariant", 0),
	kSimplifiedVariant(TableType.Variants, "kSimplifiedVariant", 1),
	kSpecializedSemanticVariant(TableType.Variants, "kSpecializedSemanticVariant", 2),
	kTraditionalVariant(TableType.Variants, "kTraditionalVariant", 3),
	kZVariant(TableType.Variants, "kZVariant", 4);
	
	private TableType tableType;
	private String name;
	private int order;
	
	private DataType(TableType tableType, String name, int order) {
		this.tableType = tableType;
		this.name = name;
		this.order = order;
	}
	
	public TableType getTableType() {
		return tableType;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOrder() {
		return order;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
