package app.bean;

public enum CityCode {
	
	
	CITY_YULIN("榆林市",5),
	CITY_YANAN("延安市",3),
	CITY_BAOJI("宝鸡市",0);
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	private String city;
	private Integer code;
	
	private CityCode(String city, Integer code){
		this.city = city;
		this.code = code;
		
	}

}
