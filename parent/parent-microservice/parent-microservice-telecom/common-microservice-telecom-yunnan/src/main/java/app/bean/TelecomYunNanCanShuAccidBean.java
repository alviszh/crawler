package app.bean;

public class TelecomYunNanCanShuAccidBean {

	private String prodid;
	private String accid ;
	private String aeracode ;
	private String prodtype ;
	public String getProdid() {
		return prodid;
	}
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	public String getAeracode() {
		return aeracode;
	}
	public void setAeracode(String aeracode) {
		this.aeracode = aeracode;
	}
	public String getProdtype() {
		return prodtype;
	}
	public void setProdtype(String prodtype) {
		this.prodtype = prodtype;
	}
	@Override
	public String toString() {
		return "TelecomYunNanCanShuAccidBean [prodid=" + prodid + ", accid=" + accid + ", aeracode=" + aeracode
				+ ", prodtype=" + prodtype + "]";
	}
	
	
}
