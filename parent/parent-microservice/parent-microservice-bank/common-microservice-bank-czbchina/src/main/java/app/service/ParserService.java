package app.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.czbchina.CzbChinaDebitCardBillDetails;
import com.microservice.dao.entity.crawler.bank.czbchina.CzbChinaDebitCardDeposit;
import com.microservice.dao.entity.crawler.bank.czbchina.CzbChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.czbchina.CzbChinaDebitCardBillDetailsRepository;
import com.microservice.dao.repository.crawler.bank.czbchina.CzbChinaDebitCardDepositRepository;
import com.microservice.dao.repository.crawler.bank.czbchina.CzbChinaDebitCardUserInfoRepository;

import app.commontracerlog.TracerLog;

@Component
public class ParserService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private CzbChinaDebitCardUserInfoRepository czbChinaDebitCardUserInfoRepository;

	@Autowired
	private CzbChinaDebitCardDepositRepository czbChinaDebitCardDepositRepository;

	@Autowired
	private CzbChinaDebitCardBillDetailsRepository czbChinaDebitCardBillDetailsRepository;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	/**
	 * 获取个人信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void userInfoParser(String userHtml, String deposittimeHtml, String cardNumber, String taskid) {
		tracerLog.addTag("ParserService.userInfoParser---解析:", taskid);
		try {
			String deposit_time_user = "";
			Document deposit_doc = Jsoup.parse(deposittimeHtml);
//			Element depositTR0 = deposit_doc.getElementById(cardNumber+"depositTR0");
			Element depositTR0 = deposit_doc.getElementById("zcDiv");
			Elements elementsByTag = depositTR0.getElementsByTag("td");
			if (elementsByTag.size() > 6) {
				//开户/认购日
				deposit_time_user = elementsByTag.get(5).text();
			}
			List<CzbChinaDebitCardDeposit> list = new ArrayList<CzbChinaDebitCardDeposit>();
			CzbChinaDebitCardDeposit czbChinaDebitCardDeposit = null;
			Document doc = Jsoup.parse(userHtml);

			Elements bg1 = doc.getElementsByClass("bg1");
			if (bg1.size() > 0) {
				Elements td = bg1.get(0).getElementsByTag("td");
				// 卡号/账号
				String card_number = td.get(0).text();
				// 账户类型
				String card_type = td.get(1).text();
				// 账户别名
				String aliases = td.get(2).text();
				// 币种
				String currency = td.get(3).text();
				// 钞/汇
				String banknote_remittance = td.get(4).text();
				// 当前余额
				String balance = td.get(5).text();
				// 可用余额
				String available_balance = td.get(6).text();
				CzbChinaDebitCardUserInfo czbChinaDebitCardUserInfo = new CzbChinaDebitCardUserInfo(taskid, card_number,
						card_type, aliases, currency, banknote_remittance, balance, available_balance,
						deposit_time_user);
				czbChinaDebitCardUserInfoRepository.save(czbChinaDebitCardUserInfo);
			}
			
			if (bg1.size() > 1) {
				for (int i = 1; i < bg1.size(); i++) {
					Elements td = bg1.get(i).getElementsByTag("td");
					// 子账户序号
					String num = td.get(0).text();
					// 账户类型
					String card_type = td.get(1).text();
					// 币种
					String currency = td.get(2).text();
					// 钞/汇
					String banknote_remittance = td.get(3).text();
					// 余额
					String balance = td.get(4).text();
					// 年利率（%）
					String interest_rate = td.get(5).text();
					// 存期
					String storge_period = td.get(6).text();
					// 开户日
					String deposit_time = td.get(7).text();
					// 到期日
					String interest_dnddate = td.get(8).text();
					// 状态
					String state = td.get(9).text();
					czbChinaDebitCardDeposit = new CzbChinaDebitCardDeposit(taskid, num, card_type, currency,
							banknote_remittance, balance, interest_rate, storge_period, deposit_time, interest_dnddate,
							state);
					list.add(czbChinaDebitCardDeposit);
				}
			}
			
			czbChinaDebitCardDepositRepository.saveAll(list);
			tracerLog.addTag("个人信息，定期存款信息保存成功", taskid);
			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					taskid);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.userInfoParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}
		taskBankStatusService.updateTaskBankUserinfo(500, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
				taskid);

	}
	
	/**
	 * 获取个人信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void userInfoParser(String userHtml, String cardNumber, String taskid) {
		tracerLog.addTag("ParserService.userInfoParser---解析:", taskid);
		try {
			List<CzbChinaDebitCardDeposit> list = new ArrayList<CzbChinaDebitCardDeposit>();
			CzbChinaDebitCardDeposit czbChinaDebitCardDeposit = null;
			Document doc = Jsoup.parse(userHtml);
			// 卡号/账号
			String card_number = doc.getElementsByClass("num").get(0).text();
			// 账户类型
			String card_type_z = doc.getElementsByClass("card").get(0).text();
			// 账户别名
			String aliases =  doc.getElementsByClass("name").get(0).text();
			String deposit_time_user = getNextLabelByKeyword(doc, "开户日期");

			Elements accountTable = doc.getElementsByClass("u-accountTable");
			if (accountTable.size() > 0) {
				Elements td = accountTable.get(0).getElementsByTag("td");
				// 币种
				String currency = td.get(1).text();
				// 钞/汇
				String banknote_remittance = td.get(2).text();
				// 当前余额
				String balance = td.get(3).text();
				// 可用余额
				String available_balance = td.get(4).text();
				CzbChinaDebitCardUserInfo czbChinaDebitCardUserInfo = new CzbChinaDebitCardUserInfo(taskid, card_number,
						card_type_z, aliases, currency, banknote_remittance, balance, available_balance,
						deposit_time_user);
				czbChinaDebitCardUserInfoRepository.save(czbChinaDebitCardUserInfo);
			}
			
			if (accountTable.size() > 1) {
				Elements tr = accountTable.get(1).getElementsByTag("tr");
				for (int i = 1; i < tr.size(); i++) {
					Elements td = tr.get(i).getElementsByTag("td");
					// 子账户序号
					String num = td.get(0).text();
					// 账户类型
					String card_type = td.get(1).text();
					// 币种
					String currency = td.get(2).text();
					// 钞/汇
					String banknote_remittance = td.get(3).text();
					// 余额
					String balance = td.get(4).text();
					// 年利率（%）
					String interest_rate = td.get(5).text();
					// 存期
					String storge_period = td.get(6).text();
					// 开户日
					String deposit_time = td.get(7).text();
					// 到期日
					String interest_dnddate = td.get(8).text();
					// 状态
					String state = td.get(9).text();
					czbChinaDebitCardDeposit = new CzbChinaDebitCardDeposit(taskid, num, card_type, currency,
							banknote_remittance, balance, interest_rate, storge_period, deposit_time, interest_dnddate,
							state);
					list.add(czbChinaDebitCardDeposit);
				}
			}
			
			czbChinaDebitCardDepositRepository.saveAll(list);
			tracerLog.addTag("个人信息，定期存款信息保存成功", taskid);
			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					taskid);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.userInfoParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}
		taskBankStatusService.updateTaskBankUserinfo(500, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
				taskid);

	}

	/**
	 * 交易明细
	 * 
	 * @param html
	 * @param taskid
	 * @param yearmonth
	 */
	public void billDetailsParser(String html, String taskid) {
		tracerLog.addTag("ParserService.billDetailsParser------解析:", taskid);
		try {
			List<CzbChinaDebitCardBillDetails> list = new ArrayList<CzbChinaDebitCardBillDetails>();
			CzbChinaDebitCardBillDetails czbChinaDebitCardBillDetails = null;
			Document doc = Jsoup.parse(html);
			Elements elementsByClass = doc.getElementsByClass("f-detailTable");
			Elements tr = elementsByClass.get(0).getElementsByTag("tr");
			for (Element element : tr) {
				Elements td = element.getElementsByTag("td");
				if(td.size()==7){
					// 序号
					String num = "";
					// 交易时间
					String tran_date = td.get(0).text();
					// 发生额（元）
					String fee = td.get(1).text();
					// 当前余额（元）
					String balance = td.get(2).text();
					// 摘要
					String trans_decription = td.get(3).text();
					// 对方名称
					String opposite_name = td.get(4).text();
					// 对方账号
					String opposite_card_number = td.get(5).text();
					// 对方开户行
					String opposite_bank_name = td.get(6).text();
					czbChinaDebitCardBillDetails = new CzbChinaDebitCardBillDetails(taskid, num, tran_date, fee,
							balance, trans_decription, opposite_name, opposite_card_number, opposite_bank_name);
					list.add(czbChinaDebitCardBillDetails);
				}
			}
			czbChinaDebitCardBillDetailsRepository.saveAll(list);
			tracerLog.addTag(taskid + "----共解析出交易流水", list.size() + "条");
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.billDetailsParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}

	}
	
	public String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("span:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

}
