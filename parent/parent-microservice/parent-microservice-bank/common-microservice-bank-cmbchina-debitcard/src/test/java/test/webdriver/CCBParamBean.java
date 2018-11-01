package test.webdriver;

public class CCBParamBean {
	
	public String INOUTFLAG;
	public String QUEFlag;
	public String AMTDOWN;
	public String AMTUP;
	public String USERID;
	public String SEND_USERID;
	public String STR_USERID;
	public String BRANCHID;
	
	public String PAGE;
	public String CURRENT_PAGE;
	public String PDT_CODE;
	public String BANK_NAME;
	public String ACC_TYPE_NAME;
	public String CUR_NAME;
	public String ACC_ALIAS;
	public String A_STR;
	public String A_STR_TEMP;
	
	public String IS_UPDATE;
	public String UPDATE_DETAIL;
	public String v_acc;
	public String v_sign;
	public String DEPOSIT_BKNO;
	public String SEQUENCE_NO;
	public String ACCTYPE2;
	public String LUGANGTONG;
	public String CURRENCE_NAME;
	public String B_STR;
		
	public String v_acc2;
	public String v_sign2;
	public String QUERY_ACC_DETAIL_FLAG;
	public String v_acc_type;
	public String ACC_SIGN;
	public String ACC_SIGN_TEM;
	public String COMPLETENESS;
	public String FILESEARCHSTR;
	public String zcAllTmp;
	public String srAllTmp;
	public String clientFileName;

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
		String str =  "INOUTFLAG=" + INOUTFLAG + "&QUEFlag=" + QUEFlag + "&AMTDOWN=" + AMTDOWN + "&AMTUP="
				+ AMTUP + "&USERID=" + USERID + ", SEND_USERID=" + SEND_USERID + ", STR_USERID=" + STR_USERID
				+ ", BRANCHID=" + BRANCHID + ", PAGE=" + PAGE + ", CURRENT_PAGE=" + CURRENT_PAGE + ", PDT_CODE="
				+ PDT_CODE + ", BANK_NAME=" + BANK_NAME + ", ACC_TYPE_NAME=" + ACC_TYPE_NAME + ", CUR_NAME=" + CUR_NAME
				+ ", ACC_ALIAS=" + ACC_ALIAS + ", A_STR=" + A_STR + ", A_STR_TEMP=" + A_STR_TEMP + ", IS_UPDATE="
				+ IS_UPDATE + ", UPDATE_DETAIL=" + UPDATE_DETAIL + ", v_acc=" + v_acc + ", v_sign=" + v_sign
				+ ", DEPOSIT_BKNO=" + DEPOSIT_BKNO + ", SEQUENCE_NO=" + SEQUENCE_NO + ", ACCTYPE2=" + ACCTYPE2
				+ ", LUGANGTONG=" + LUGANGTONG + ", CURRENCE_NAME=" + CURRENCE_NAME + ", B_STR=" + B_STR + ", v_acc2="
				+ v_acc2 + ", v_sign2=" + v_sign2 + ", QUERY_ACC_DETAIL_FLAG=" + QUERY_ACC_DETAIL_FLAG + ", v_acc_type="
				+ v_acc_type + ", ACC_SIGN=" + ACC_SIGN + ", ACC_SIGN_TEM=" + ACC_SIGN_TEM + ", COMPLETENESS="
				+ COMPLETENESS + ", FILESEARCHSTR=" + FILESEARCHSTR + ", zcAllTmp=" + zcAllTmp + ", srAllTmp="
				+ srAllTmp + ", clientFileName=" + clientFileName + ", l_acc_no=" + l_acc_no + ", l_acc_no_u="
				+ l_acc_no_u + ", l_acc_al=" + l_acc_al + ", l_branch=" + l_branch + ", l_branchcode=" + l_branchcode
				+ ", l_acc_sign=" + l_acc_sign + ", l_acc_type=" + l_acc_type + ", l_cur_desc=" + l_cur_desc
				+ ", l_acc_e=" + l_acc_e + ", l_userid=" + l_userid + ", l_username=" + l_username;
		return str.replaceAll(", ", "&");
	}
	
	public String getINOUTFLAG() {
		return INOUTFLAG;
	}
	public void setINOUTFLAG(String iNOUTFLAG) {
		INOUTFLAG = iNOUTFLAG;
	}
	public String getQUEFlag() {
		return QUEFlag;
	}
	public void setQUEFlag(String qUEFlag) {
		QUEFlag = qUEFlag;
	}
	public String getAMTDOWN() {
		return AMTDOWN;
	}
	public void setAMTDOWN(String aMTDOWN) {
		AMTDOWN = aMTDOWN;
	}
	public String getAMTUP() {
		return AMTUP;
	}
	public void setAMTUP(String aMTUP) {
		AMTUP = aMTUP;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getSEND_USERID() {
		return SEND_USERID;
	}
	public void setSEND_USERID(String sEND_USERID) {
		SEND_USERID = sEND_USERID;
	}
	public String getSTR_USERID() {
		return STR_USERID;
	}
	public void setSTR_USERID(String sTR_USERID) {
		STR_USERID = sTR_USERID;
	}
	public String getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(String bRANCHID) {
		BRANCHID = bRANCHID;
	}
	public String getPAGE() {
		return PAGE;
	}
	public void setPAGE(String pAGE) {
		PAGE = pAGE;
	}
	public String getCURRENT_PAGE() {
		return CURRENT_PAGE;
	}
	public void setCURRENT_PAGE(String cURRENT_PAGE) {
		CURRENT_PAGE = cURRENT_PAGE;
	}
	public String getPDT_CODE() {
		return PDT_CODE;
	}
	public void setPDT_CODE(String pDT_CODE) {
		PDT_CODE = pDT_CODE;
	}
	public String getBANK_NAME() {
		return BANK_NAME;
	}
	public void setBANK_NAME(String bANK_NAME) {
		BANK_NAME = bANK_NAME;
	}
	public String getACC_TYPE_NAME() {
		return ACC_TYPE_NAME;
	}
	public void setACC_TYPE_NAME(String aCC_TYPE_NAME) {
		ACC_TYPE_NAME = aCC_TYPE_NAME;
	}
	public String getCUR_NAME() {
		return CUR_NAME;
	}
	public void setCUR_NAME(String cUR_NAME) {
		CUR_NAME = cUR_NAME;
	}
	public String getACC_ALIAS() {
		return ACC_ALIAS;
	}
	public void setACC_ALIAS(String aCC_ALIAS) {
		ACC_ALIAS = aCC_ALIAS;
	}
	public String getA_STR() {
		return A_STR;
	}
	public void setA_STR(String a_STR) {
		A_STR = a_STR;
	}
	public String getA_STR_TEMP() {
		return A_STR_TEMP;
	}
	public void setA_STR_TEMP(String a_STR_TEMP) {
		A_STR_TEMP = a_STR_TEMP;
	}
	public String getIS_UPDATE() {
		return IS_UPDATE;
	}
	public void setIS_UPDATE(String iS_UPDATE) {
		IS_UPDATE = iS_UPDATE;
	}
	public String getUPDATE_DETAIL() {
		return UPDATE_DETAIL;
	}
	public void setUPDATE_DETAIL(String uPDATE_DETAIL) {
		UPDATE_DETAIL = uPDATE_DETAIL;
	}
	public String getV_acc() {
		return v_acc;
	}
	public void setV_acc(String v_acc) {
		this.v_acc = v_acc;
	}
	public String getV_sign() {
		return v_sign;
	}
	public void setV_sign(String v_sign) {
		this.v_sign = v_sign;
	}
	public String getDEPOSIT_BKNO() {
		return DEPOSIT_BKNO;
	}
	public void setDEPOSIT_BKNO(String dEPOSIT_BKNO) {
		DEPOSIT_BKNO = dEPOSIT_BKNO;
	}
	public String getSEQUENCE_NO() {
		return SEQUENCE_NO;
	}
	public void setSEQUENCE_NO(String sEQUENCE_NO) {
		SEQUENCE_NO = sEQUENCE_NO;
	}
	public String getACCTYPE2() {
		return ACCTYPE2;
	}
	public void setACCTYPE2(String aCCTYPE2) {
		ACCTYPE2 = aCCTYPE2;
	}
	public String getLUGANGTONG() {
		return LUGANGTONG;
	}
	public void setLUGANGTONG(String lUGANGTONG) {
		LUGANGTONG = lUGANGTONG;
	}
	public String getCURRENCE_NAME() {
		return CURRENCE_NAME;
	}
	public void setCURRENCE_NAME(String cURRENCE_NAME) {
		CURRENCE_NAME = cURRENCE_NAME;
	}
	public String getB_STR() {
		return B_STR;
	}
	public void setB_STR(String b_STR) {
		B_STR = b_STR;
	}
	public String getV_acc2() {
		return v_acc2;
	}
	public void setV_acc2(String v_acc2) {
		this.v_acc2 = v_acc2;
	}
	public String getV_sign2() {
		return v_sign2;
	}
	public void setV_sign2(String v_sign2) {
		this.v_sign2 = v_sign2;
	}
	public String getQUERY_ACC_DETAIL_FLAG() {
		return QUERY_ACC_DETAIL_FLAG;
	}
	public void setQUERY_ACC_DETAIL_FLAG(String qUERY_ACC_DETAIL_FLAG) {
		QUERY_ACC_DETAIL_FLAG = qUERY_ACC_DETAIL_FLAG;
	}
	public String getV_acc_type() {
		return v_acc_type;
	}
	public void setV_acc_type(String v_acc_type) {
		this.v_acc_type = v_acc_type;
	}
	public String getACC_SIGN() {
		return ACC_SIGN;
	}
	public void setACC_SIGN(String aCC_SIGN) {
		ACC_SIGN = aCC_SIGN;
	}
	public String getACC_SIGN_TEM() {
		return ACC_SIGN_TEM;
	}
	public void setACC_SIGN_TEM(String aCC_SIGN_TEM) {
		ACC_SIGN_TEM = aCC_SIGN_TEM;
	}
	public String getCOMPLETENESS() {
		return COMPLETENESS;
	}
	public void setCOMPLETENESS(String cOMPLETENESS) {
		COMPLETENESS = cOMPLETENESS;
	}
	public String getFILESEARCHSTR() {
		return FILESEARCHSTR;
	}
	public void setFILESEARCHSTR(String fILESEARCHSTR) {
		FILESEARCHSTR = fILESEARCHSTR;
	}
	public String getZcAllTmp() {
		return zcAllTmp;
	}
	public void setZcAllTmp(String zcAllTmp) {
		this.zcAllTmp = zcAllTmp;
	}
	public String getSrAllTmp() {
		return srAllTmp;
	}
	public void setSrAllTmp(String srAllTmp) {
		this.srAllTmp = srAllTmp;
	}
	public String getClientFileName() {
		return clientFileName;
	}
	public void setClientFileName(String clientFileName) {
		this.clientFileName = clientFileName;
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
