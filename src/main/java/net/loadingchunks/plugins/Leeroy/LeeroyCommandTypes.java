package net.loadingchunks.plugins.Leeroy;

public enum LeeroyCommandTypes {
	PLAYER("Player"),
	INTEGER("Integer"),
	STRING("String");
	
	private String str;
	
	LeeroyCommandTypes(String str) {
		this.str = str;
	}
	
	public String getString() {
		return this.str;
	}
	
	public static LeeroyCommandTypes fromString(String text) {
		if(text != null) {
			for(LeeroyCommandTypes c : LeeroyCommandTypes.values()) {
				if(text.equalsIgnoreCase(c.str)) {
					return c;
				}
			}
		}
		return null;
	}
}
