package com.gire.eval360.templates.domain;

public enum ItemType {

	RATING("rating"),
	COMBO("combo"),
	CHECKBOX("checkbox"),
	OPTIONS("options"),
	TEXTBOX("textbox");
	
	
    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
    
    ItemType(String text){
        this.text = text;

	}
}
