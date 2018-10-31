package app.social.bean;

public class ProcessBBSBean {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessBBSBean [secondUrlCss=" + secondUrlCss + ", secondUrlRegex=" + secondUrlRegex
				+ ", mainTitleRule=" + mainTitleRule + ", mainTimeCss=" + mainTimeCss + ", mainTimeRegex="
				+ mainTimeRegex + ", mainAuthorRule=" + mainAuthorRule + ", threadElementRule=" + threadElementRule
				+ ", threadContentRule=" + threadContentRule + ", url=" + url + ", seedLinkId=" + seedLinkId
				+ ", BBSType=" + BBSType + "]";
	}

	/*爬取所需规则*/
	public String secondUrlCss;					//第二级链接（css）
	public String secondUrlRegex;				//第二级链接（正则）
	public String mainTitleRule;				//主贴标题规则

	public String mainTimeCss;					//主贴发布时间规则（css）
	public String mainTimeRegex;				//主贴发布时间规则（正则）	
	public String mainAuthorRule;				//主贴发布者规则
	public String threadElementRule;			//获取每个回帖包含所有信息所在的element
	public String threadContentRule;			//回帖内容
	public String url;							//
	public Long seedLinkId;					//种子链接的关联id
	
	/*关联字段*/
	public String BBSType;						//规则所属论坛类型
	
//	public ProcessBBSBean(String secondUrlCss, String secondUrlRegex, String mainTitleRule, String mainTimeCss,
//			String mainTimeRegex, String mainAuthorRule, String threadElementRule, String threadContentRule, String url,
//			Long seedLinkId, String bBSType) {
//		super();
//		this.secondUrlCss = secondUrlCss;
//		this.secondUrlRegex = secondUrlRegex;
//		this.mainTitleRule = mainTitleRule;
//		this.mainTimeCss = mainTimeCss;
//		this.mainTimeRegex = mainTimeRegex;
//		this.mainAuthorRule = mainAuthorRule;
//		this.threadElementRule = threadElementRule;
//		this.threadContentRule = threadContentRule;
//		this.url = url;
//		this.seedLinkId = seedLinkId;
//		BBSType = bBSType;
//	}
	public String getSecondUrlCss() {
		return secondUrlCss;
	}

	public void setSecondUrlCss(String secondUrlCss) {
		this.secondUrlCss = secondUrlCss;
	}

	public String getSecondUrlRegex() {
		return secondUrlRegex;
	}

	public void setSecondUrlRegex(String secondUrlRegex) {
		this.secondUrlRegex = secondUrlRegex;
	}

	public String getMainTitleRule() {
		return mainTitleRule;
	}

	public void setMainTitleRule(String mainTitleRule) {
		this.mainTitleRule = mainTitleRule;
	}

	public String getMainTimeCss() {
		return mainTimeCss;
	}

	public void setMainTimeCss(String mainTimeCss) {
		this.mainTimeCss = mainTimeCss;
	}

	public String getMainTimeRegex() {
		return mainTimeRegex;
	}

	public void setMainTimeRegex(String mainTimeRegex) {
		this.mainTimeRegex = mainTimeRegex;
	}

	public String getMainAuthorRule() {
		return mainAuthorRule;
	}

	public void setMainAuthorRule(String mainAuthorRule) {
		this.mainAuthorRule = mainAuthorRule;
	}

	public String getThreadElementRule() {
		return threadElementRule;
	}

	public void setThreadElementRule(String threadElementRule) {
		this.threadElementRule = threadElementRule;
	}

	public String getThreadContentRule() {
		return threadContentRule;
	}

	public void setThreadContentRule(String threadContentRule) {
		this.threadContentRule = threadContentRule;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSeedLinkId() {
		return seedLinkId;
	}

	public void setSeedLinkId(Long seedLinkId) {
		this.seedLinkId = seedLinkId;
	}

	public String getBBSType() {
		return BBSType;
	}

	public void setBBSType(String bBSType) {
		BBSType = bBSType;
	}




}
