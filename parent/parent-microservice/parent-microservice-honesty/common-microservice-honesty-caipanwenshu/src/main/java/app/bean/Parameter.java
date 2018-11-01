package app.bean;

public class Parameter {

	private String guid;
	
	private String vl5x;
	
	private String number;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVl5x() {
		return vl5x;
	}

	public void setVl5x(String vl5x) {
		this.vl5x = vl5x;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Parameter [guid=" + guid + ", vl5x=" + vl5x + ", number=" + number + "]";
	}
	
	
	
}
