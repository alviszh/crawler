package com.crawler.taxation.json;

public class BasicUserTaxation {

    private String name;		//姓名

    private String idnum;		//身份证号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

	@Override
	public String toString() {
		return "BasicUserTaxation [name=" + name + ", idnum=" + idnum + "]";
	}

}
