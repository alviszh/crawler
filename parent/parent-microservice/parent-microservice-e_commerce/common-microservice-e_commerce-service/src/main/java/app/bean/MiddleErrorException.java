package app.bean;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：CodeErrorException 类描述： 创建人：hyx
 * 创建时间：2018年6月4日 下午2:50:10
 * 
 * @version
 */
public class MiddleErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MiddleErrorException() {
		super();
	}

	public MiddleErrorException(String msg) {
		super(msg);
	}
}
