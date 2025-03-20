package de.qwyt.housecontrol.tyche.util;

public enum Symbole {
	ARROW_RIGHT("\u2192"),
	WEB_ARROW_RIGHT("\u279C");
	
	private String unicode;
	
	private Symbole(String unicode) {
		this.unicode = unicode;
	}
	
	@Override
	public String toString() {
		return unicode;
	}
}
