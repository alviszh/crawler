package app.test;


/**
 * @description:
 * @author: sln 
 * @date: 2018年1月19日 下午6:27:44 
 */
public class SplitTest {
	public static void main(String[] args) {
		String str = " 职工姓名 聂亚琼 个人账号 01010634000662公积金余额 1,450.00身份证号 410928199010016042公积金卡号单位名称 河南成就人力资源有限公司单位账号 01010634开户日期 2017-08-09缴至年月 201712 月均工资 1,450.00单位月缴额 145.00 个人月缴额 145.00 月缴存额 290.00 单位缴交率 0.1000 个人缴交率 0.1000上年结转额 0.00 本年补缴额 0.00 本年汇缴额 1,450.00个人账户状态 正常 本年结算利息 0.00本年支取额 0.00 是否冻结 否 是否有异地贷 无异地贷款 是否有中心贷 无贷款 是否月对冲   否";
		
		String substring = str.substring(str.lastIndexOf("职工姓名")+"职工姓名".length(),str.indexOf("个人账号"));
		substring = str.substring(str.lastIndexOf("个人账号")+"个人账号".length(),str.indexOf("公积金余额"));
		
		
		substring=str.substring(str.indexOf("是否月对冲")+"是否月对冲".length(),str.length()).trim();
		System.out.println(substring);
	}
}
