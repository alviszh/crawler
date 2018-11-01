package app.bean2;
/**   
*    
* 项目名称：common-microservice-eureka-china-unicom   
* 类名称：SucessResultBean   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年3月8日 下午5:36:54   
* @version        
*/
public class SucessResultBean {

	private String resultCode;
	
	private String msg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SucessResultBean [resultCode=" + resultCode + ", msg=" + msg + "]";
	}
	
	
}
