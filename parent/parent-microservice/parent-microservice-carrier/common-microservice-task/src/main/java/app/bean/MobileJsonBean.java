package app.bean;

import java.io.Serializable;

/**
 * @Description: MobileJsonBean
 * @author zzhen
 * @date 2017年6月19日 上午10:21:40
 */
public class MobileJsonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5115154845415106526L;
	public String mobileNum;
	public String mobileOperator;
	public boolean spec;
	public String username;
	public String idnum;
	public Long id;

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getMobileOperator() {
		return mobileOperator;
	}

	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSpec() {
		return spec;
	}

	public void setSpec(boolean spec) {
		this.spec = spec;
	}

	@Override
	public String toString() {
		return "MobileJsonBean [mobileNum=" + mobileNum + ", mobileOperator=" + mobileOperator + ", spec=" + spec
				+ ", username=" + username + ", idnum=" + idnum + ", id=" + id + "]";
	}

}
