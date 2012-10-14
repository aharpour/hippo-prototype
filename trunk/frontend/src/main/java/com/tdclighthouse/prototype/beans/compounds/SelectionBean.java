package com.tdclighthouse.prototype.beans.compounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectionBean {

	private final Map<String, String> labelsMap;
	private final List<SelectionItemBean> items = new ArrayList<SelectionItemBean>();;

	public SelectionBean(Map<String, String> labelsMap, Object propertyValue) {
		this.labelsMap = labelsMap;
		if (propertyValue != null) {
			if (propertyValue.getClass().isArray()) {
				String[] keys = (String[]) propertyValue;
				for (String key : keys) {
					addSelectionItemBean(labelsMap, key);
				}
			} else {
				String key = (String) propertyValue;
				addSelectionItemBean(labelsMap, key);
			}
		}
	}

	private void addSelectionItemBean(Map<String, String> labelsMap, String key) {
		items.add(new SelectionItemBean(key, labelsMap.get(key)));
	}

	public Map<String, String> getLabelsMap() {
		return labelsMap;
	}

	public List<SelectionItemBean> getItems() {
		return items;
	}

	public SelectionItemBean getFirstItem() {
		SelectionItemBean result = null;
		if (items.size() > 0) {
			result = items.get(0);
		}
		return result;
	}

}
