package app.bean;


public class UnicomErrorMessage {

   private String respCode;
   private String respDesc;
   public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
    public String getRespCode() {
        return respCode;
    }

   public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }
    public String getRespDesc() {
        return respDesc;
    }
	@Override
	public String toString() {
		return "UnicomErrorMessage [respCode=" + respCode + ", respDesc=" + respDesc + "]";
	}
    
    

}