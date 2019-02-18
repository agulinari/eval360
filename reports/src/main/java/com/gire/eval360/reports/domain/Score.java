package com.gire.eval360.reports.domain;

public enum Score {
	
	SMALL("tdsmall"), MEDIUM("tdmedium"), LARGE("tdlarge");
	
	private final String cssClass;
	
	private Score(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public String getCssClass() {
		return cssClass;
	}

}
