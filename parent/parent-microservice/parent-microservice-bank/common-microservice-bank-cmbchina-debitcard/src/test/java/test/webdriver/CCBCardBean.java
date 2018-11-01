package test.webdriver;

public class CCBCardBean {
	
	public String l_acc_no;
	public String l_acc_no_u;
	public String l_acc_al;
	public String l_branch;
	public String l_branchcode;
	public String l_acc_sign;
	public String l_acc_type;
	public String l_cur_desc;
	public String l_acc_e;
	public String l_userid;
	public String l_username;
	
	@Override
	public String toString() {
		String str =  "&l_acc_no=" + l_acc_no + ", l_acc_no_u=" + l_acc_no_u + ", l_acc_al=" + l_acc_al
				+ ", l_branch=" + l_branch + ", l_branchcode=" + l_branchcode + ", l_acc_sign=" + l_acc_sign
				+ ", l_acc_type=" + l_acc_type + ", l_cur_desc=" + l_cur_desc + ", l_acc_e=" + l_acc_e + ", l_userid="
				+ l_userid + ", l_username=" + l_username;
		return str.replaceAll(", ", "&");
	}
	public String getL_acc_no() {
		return l_acc_no;
	}
	public void setL_acc_no(String l_acc_no) {
		this.l_acc_no = l_acc_no;
	}
	public String getL_acc_no_u() {
		return l_acc_no_u;
	}
	public void setL_acc_no_u(String l_acc_no_u) {
		this.l_acc_no_u = l_acc_no_u;
	}
	public String getL_acc_al() {
		return l_acc_al;
	}
	public void setL_acc_al(String l_acc_al) {
		this.l_acc_al = l_acc_al;
	}
	public String getL_branch() {
		return l_branch;
	}
	public void setL_branch(String l_branch) {
		this.l_branch = l_branch;
	}
	public String getL_branchcode() {
		return l_branchcode;
	}
	public void setL_branchcode(String l_branchcode) {
		this.l_branchcode = l_branchcode;
	}
	public String getL_acc_sign() {
		return l_acc_sign;
	}
	public void setL_acc_sign(String l_acc_sign) {
		this.l_acc_sign = l_acc_sign;
	}
	public String getL_acc_type() {
		return l_acc_type;
	}
	public void setL_acc_type(String l_acc_type) {
		this.l_acc_type = l_acc_type;
	}
	public String getL_cur_desc() {
		return l_cur_desc;
	}
	public void setL_cur_desc(String l_cur_desc) {
		this.l_cur_desc = l_cur_desc;
	}
	public String getL_acc_e() {
		return l_acc_e;
	}
	public void setL_acc_e(String l_acc_e) {
		this.l_acc_e = l_acc_e;
	}
	public String getL_userid() {
		return l_userid;
	}
	public void setL_userid(String l_userid) {
		this.l_userid = l_userid;
	}
	public String getL_username() {
		return l_username;
	}
	public void setL_username(String l_username) {
		this.l_username = l_username;
	}

}
