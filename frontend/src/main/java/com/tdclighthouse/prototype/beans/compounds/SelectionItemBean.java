package com.tdclighthouse.prototype.beans.compounds;


public class SelectionItemBean {

	protected String key;
	protected String label;
	
	public SelectionItemBean(String key, String label) {
		this.key = key;
		this.label = label;
	}

	public final String getKey() {
		return key;
	}

	public final String getLabel() {
		return label;
	}

}
