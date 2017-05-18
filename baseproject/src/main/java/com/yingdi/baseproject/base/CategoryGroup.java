package com.yingdi.baseproject.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类列表
 * 
 * @author Usher D
 *
 */
public class CategoryGroup {
	private List<AnBoCategory> cats = new ArrayList<AnBoCategory>();
	private String version;

	public List<AnBoCategory> getCats() {
		return cats;
	}

	public void setCats(List<AnBoCategory> cats) {
		this.cats = cats;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
