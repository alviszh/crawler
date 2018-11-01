package app.service.common;

/**
 * @Description 根据一些json数据返回的代码，来确定中文名称
 * @author sln
 * @date 2017年8月28日 下午4:09:46
 */
public class CodeTypeEnum {
	/**
	 * 根据积分类型代码来确定积分类型名称
	 * @param typeValue
	 * @return
	 */
	public static String getIntegraTypeName(String typeValue) {
		String typeName = null;
		if ("1".equals(typeValue) || "10".equals(typeValue)) {
			typeName = "消费积分";
		} else if ("2".equals(typeValue)) {
			typeName = "在网积分";
		} else if ("3".equals(typeValue)) {
			typeName = "品牌奖励积分";
		} else if ("4".equals(typeValue)) {
			typeName = "缴费奖励积分";
		} else if ("5".equals(typeValue)) {
			typeName = "信誉积分";
		} else if ("11".equals(typeValue)) {
			typeName = "奖励积分";
		} else {
			typeName = "无具体积分类型"; // 自己添加的
		}
		return typeName;
	}
	//===================如下内容用于余额变动明细中，根据代码获取具体的中文名称============================
	
	public static String getBalanceChangeName(String balanceChangeType){
		String balanceChangeName = null;
		if("0".equals(balanceChangeType)){
			balanceChangeName="现金充值";
		}else if("1".equals(balanceChangeType)){
			balanceChangeName="预存返还";
		}else if("2".equals(balanceChangeType)){
			balanceChangeName="增费";
		}else if("3".equals(balanceChangeType)){
			balanceChangeName="退费";
		}else if("4".equals(balanceChangeType)){
			balanceChangeName="积分兑换";
		}else if("5".equals(balanceChangeType)){
			balanceChangeName="话费支出";
		}else if("6".equals(balanceChangeType)){
			balanceChangeName="代收费";
		}else if("7".equals(balanceChangeType)){
			balanceChangeName="余额失效";
		}else if("8".equals(balanceChangeType)){
			balanceChangeName="其他支出";
		}else if("9".equals(balanceChangeType)){
			balanceChangeName="坏账";
		}else{
			balanceChangeName="其他";   //自己添加的
		}
		return balanceChangeName;
	}
	
	
	public static String getBalanceTypeName(String balanceTypeFlag){
		
		String balanceTypeName = null;
		if("0".equals(balanceTypeFlag)){
			balanceTypeName = "通用余额";
		}else if("1".equals(balanceTypeFlag)){
			balanceTypeName = "专用余额";
		}else{
			balanceTypeName = "其他";
		}
		return balanceTypeName;
	}
	
	//===================如下内容用于充值记录中，根据代码获取具体的中文名称============================
	//充值渠道名字
	public static String getChargeChannelName(String payChannelId){
		String chargeChannelName="其他";   //默认是其他
		if("0".equals(payChannelId)){
			chargeChannelName="营业厅";
		}else if("1".equals(payChannelId)){
			chargeChannelName="网厅";
		}else if("2".equals(payChannelId)){
			chargeChannelName="欢GO客户端";
		}else if("3".equals(payChannelId)){
			chargeChannelName="翼支付";
		}else if("4".equals(payChannelId)){
			chargeChannelName="第三方支付";
		}else if("5".equals(payChannelId)){
			chargeChannelName="自助缴费";
		}else if("6".equals(payChannelId)){
			chargeChannelName="银行";
		}else if("7".equals(payChannelId)){
			chargeChannelName="其他";
		}
		return chargeChannelName;
	}
	//获取缴费方式
	public static String getChargeWayName(String paymentMethod){
		String payType="现金";   //默认是现金
		if("11".equals(paymentMethod)){
			payType="现金";
		}else if("12".equals(paymentMethod)){
			payType="支票";
		}else if("14".equals(paymentMethod)){
			payType="代缴";
		}else if("16".equals(paymentMethod)){
			payType="套餐促销(预存、返还、赠送)费用";
		}else if("17".equals(paymentMethod)){
			payType="托收";
		}else if("18".equals(paymentMethod)){
			payType="空中充值";
		}else if("19".equals(paymentMethod)){
			payType="银行卡";
		}else if("20".equals(paymentMethod)){
			payType = "充值卡";
		}
		return payType;
	}
	
	//====================根据首页的成长值判断    =============================
	public static String getStarlevel(int growPoint){
		String level="";
		if(growPoint>=600 && growPoint<1000){
			level="1星";
		}else if(growPoint>=1000 && growPoint<1500){
			level="2星";
		}else if(growPoint>=1500 && growPoint<2300){
			level="3星";
		}else if(growPoint>=2300 && growPoint<4000){
			level="4星";
		}else if(growPoint>=4000 && growPoint<6000){
			level="5星";
		}else if(growPoint>=6000 && growPoint<8000){
			level="6星";
		}else if(growPoint>=8000){
			level="7星";
		}
		return level;
	}
	
	//====================天津网站    星级获取     根据自己网站的定义方式=============================
	public static String getTjStarlevel(String memberLevel){
		String starLevel = "1星";
		if("3100".equals(memberLevel)){
			starLevel = "1星";
		}else if ("3200".equals(memberLevel)){
			starLevel = "2星";
		}else if("3300".equals(memberLevel)){
			starLevel = "3星";
		}else if("3400".equals(memberLevel)){
			starLevel = "4星";
		}else if("3500".equals(memberLevel)){
			starLevel = "5星";
		}else if("3600".equals(memberLevel)){
			starLevel = "6星";
		}else if("3700".equals(memberLevel)){
			starLevel = "7星";
		}
		return starLevel;
	}
	
	//====================天津网站    星级积分倍数    根据自己网站的定义方式=============================
	public static String getStarMultiple(String memberLevel){
		String pointTimes = "1倍";
		if("3100".equals(memberLevel)){
			pointTimes = "1倍";
		}else if ("3200".equals(memberLevel)){
			pointTimes = "1.5倍";
		}else if("3300".equals(memberLevel)){
			pointTimes = "2倍";
		}else if("3400".equals(memberLevel)){
			pointTimes = "2倍";
		}else if("3500".equals(memberLevel)){
			pointTimes = "3倍";
		}else if("3600".equals(memberLevel)){
			pointTimes = "4倍";
		}else if("3700".equals(memberLevel)){
			pointTimes = "5倍";
		}
		return pointTimes;
	}
}
