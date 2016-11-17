package redlaboratory.unihan;

public enum TableType {
	IRG_Sources("Unihan_IRGSources"),
	Dictionary_Indices("Unihan_DictionaryIndices"),
	Dictionary_like_Data("Unihan_DictionaryLikeData"),
	Other_Mappings("Unihan_OtherMappings"),
	Radical_stroke_Indices("Unihan_RadicalStrokeCounts"),
	Readings("Unihan_Readings"),
	Variants("Unihan_Variants");
	
	private String fileName;
	
	private TableType(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
