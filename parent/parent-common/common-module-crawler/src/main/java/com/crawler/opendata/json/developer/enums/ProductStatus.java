package com.crawler.opendata.json.developer.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 产品状态
 * @author evin
 *
 */
public enum ProductStatus {
	
	Development("开发中"),Euditing("待审核"),Online("已上线"),OffLine("下线"),Default("-");

	private String name;

	private ProductStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static Map<ProductStatus, String> getMap() {
		Map<ProductStatus, String> map = new LinkedHashMap<ProductStatus, String>();
		for (ProductStatus xchangeType : values()) {
			map.put(xchangeType, xchangeType.getName());
		}
		return map;
	}
}
