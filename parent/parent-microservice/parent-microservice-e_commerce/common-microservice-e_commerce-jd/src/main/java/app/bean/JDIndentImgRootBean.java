package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndentImg;

/**
 * Auto-generated: 2018-08-29 16:59:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JDIndentImgRootBean {

	private List<JDIndentImg> JDIndentImg;

	public void setJDIndentImg(List<JDIndentImg> JDIndentImg) {
		this.JDIndentImg = JDIndentImg;
	}

	public List<JDIndentImg> getJDIndentImg() {
		return JDIndentImg;
	}

	@Override
	public String toString() {
		return "JDIndentImgRootBean [JDIndentImg=" + JDIndentImg + "]";
	}

}