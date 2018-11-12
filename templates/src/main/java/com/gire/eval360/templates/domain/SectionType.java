package com.gire.eval360.templates.domain;

public enum SectionType {
	
	TEXT("text"),
	QUESTIONS("questions");
	
    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
    
	SectionType(String text){
        this.text = text;

	}

}
