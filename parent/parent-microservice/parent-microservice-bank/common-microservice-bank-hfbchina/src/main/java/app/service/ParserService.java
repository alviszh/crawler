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
import com.microservice.dao.entity.crawler.bank.hfbchina.HfbChinaDebitCardBillDetails;
import com.microservice.dao.entity.crawler.bank.hfbchina.HfbChinaDebitCardDeposit;
import com.microservice.dao.entity.crawler.bank.hfbchina.HfbChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.hfbchina.HfbChinaDebitCardBillDetailsRepository;
import com.microservice.dao.repository.crawler.bank.hfbchina.HfbChinaDebitCardDepositRepository;
import com.microservice.dao.repository.crawler.bank.hfbchina.HfbChinaDebitCardUserInfoRepository;

import app.commontracerlog.TracerLog;

@Component
public class ParserService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private HfbChinaDebitCardUserInfoRepository hfbChinaDebitCardUserInfoRepository;

	@Autowired
	private HfbChinaDebitCardDepositRepository hfbChinaDebitCardDepositRepository;

	@Autowired
	private HfbChinaDebitCardBillDetailsRepository hfbChinaDebitCardBillDetailsRepository;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	/**
	 * 获取个人信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void userInfoParser(String userinfo, String cardInfo, String cardhtml, String taskid) {
		tracerLog.addTag("ParserService.userInfoParser---解析:", taskid);
		try {

			// 登录首页源码
			Document doc = Jsoup.parse(userinfo);
			// 卡片持有人
			String card_holder = doc.getElementById("m_userName").text();
			if (card_holder.contains("！")) {
				card_holder = card_holder.replaceAll("！", "");
			}

			String mobile = doc.getElementsByClass("customerMobile").text();

			// 账户查询页面
			Document table = Jsoup.parse(cardInfo);

			Elements accountInfoTable = table.getElementsByClass("accountInfoTable");
			Document docTable = Jsoup.parse(accountInfoTable.toString());

			String aliases = getNextLabelByKeyword(docTable, "账户别名");

			String signing_mode = getNextLabelByKeyword(docTable, "签约方式");

			String credential_state = getNextLabelByKeyword(docTable, "凭证状态");

			String deposit_bank = getNextLabelByKeyword(docTable, "开户行");

			Document doccard = Jsoup.parse(cardhtml);

			String account = doccard.getElementById("account").text();

			String balance = doccard.getElementById("amount").text();

			String currency_type = doccard.getElementById("currencyType").text();

			String account_state = doccard.getElementById("accountState").text();

			List<HfbChinaDebitCardDeposit> list = new ArrayList<HfbChinaDebitCardDeposit>();

			HfbChinaDebitCardDeposit hfbChinaDebitCardDeposit = null;
			try {
				Element timeDepositTable = doccard.getElementById("timeDepositTable");
				Elements tobody = timeDepositTable.getElementsByTag("tbody");
				if (tobody.size() > 0) {
					Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
					for (Element element : elementsByTag) {
						Elements elementsByTag2 = element.getElementsByTag("td");
						// 子账号
						String subaccount = elementsByTag2.get(0).text();
						// 存款本金
						String deposit = elementsByTag2.get(1).text();
						// 币种
						String currency = elementsByTag2.get(2).text();
						// 存期
						String storge_period = elementsByTag2.get(3).text();
						// 存入日
						String deposit_date = elementsByTag2.get(4).text();
						// 到期日
						String due_date = elementsByTag2.get(5).text();
						// 存款利率
						String interest_rate = elementsByTag2.get(6).text();
						// 账户状态
						String account_stateDeposit = elementsByTag2.get(7).text();

						hfbChinaDebitCardDeposit = new HfbChinaDebitCardDeposit(taskid, subaccount, deposit, currency,
								storge_period, deposit_date, due_date, interest_rate, account_stateDeposit, "", "", "",
								"", "整存整取");

						list.add(hfbChinaDebitCardDeposit);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Element installmentDepositTable = doccard.getElementById("installmentDepositTable");
				Elements tobody = installmentDepositTable.getElementsByTag("tbody");
				if (tobody.size() > 0) {
					Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
					for (Element element : elementsByTag) {
						Elements elementsByTag2 = element.getElementsByTag("td");
						// 子账号
						String subaccount = elementsByTag2.get(0).text();
						// 账户金额
						String account_amount = elementsByTag2.get(1).text();
						// 币种
						String currency = elementsByTag2.get(2).text();
						// 每次存取金额
						String each_access_amount = elementsByTag2.get(3).text();
						// 本期应存金额
						String amount_deposit = elementsByTag2.get(4).text();
						// 存期
						String storge_period = elementsByTag2.get(5).text();
						// 存入日
						String deposit_date = elementsByTag2.get(6).text();
						// 到期日
						String due_date = elementsByTag2.get(7).text();
						// 存款利率
						String interest_rate = elementsByTag2.get(8).text();
						// 是否违约
						String contract = elementsByTag2.get(9).text();
						// 账户状态
						String account_stateDeposit = elementsByTag2.get(10).text();

						hfbChinaDebitCardDeposit = new HfbChinaDebitCardDeposit(taskid, subaccount, "", currency,
								storge_period, deposit_date, due_date, interest_rate, account_stateDeposit,
								account_amount, each_access_amount, amount_deposit, contract, "零存整取");
						list.add(hfbChinaDebitCardDeposit);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			HfbChinaDebitCardUserInfo hfbChinaDebitCardUserInfo = new HfbChinaDebitCardUserInfo(taskid, card_holder,
					mobile, aliases, signing_mode, credential_state, deposit_bank, account, balance, currency_type,
					account_state);
			hfbChinaDebitCardUserInfoRepository.save(hfbChinaDebitCardUserInfo);
			hfbChinaDebitCardDepositRepository.saveAll(list);
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
			List<HfbChinaDebitCardBillDetails> list = new ArrayList<HfbChinaDebitCardBillDetails>();
			HfbChinaDebitCardBillDetails hfbChinaDebitCardBillDetails = null;

			Document table = Jsoup.parse(html);

			Element timeDepositTable = table.getElementById("selfDetialTable");
			Elements tobody = timeDepositTable.getElementsByTag("tbody");
			if (tobody.size() > 0) {
				Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
				for (Element element2 : elementsByTag) {
					Elements elementsByTag2 = element2.getElementsByTag("td");
					if (elementsByTag2.size() > 7) {
						// 交易时间
						String tran_date = elementsByTag2.get(0).text();
						// 交易渠道
						String tran_channel = elementsByTag2.get(1).text();
						// 对方账号
						String opposite_card_number = elementsByTag2.get(2).text();
						// 对方户名
						String opposite_name = elementsByTag2.get(3).text();
						// 对方行名
						String opposite_bank_name = elementsByTag2.get(4).text();
						// 收付
						String currentaccount_type = elementsByTag2.get(5).text();
						// 金额
						String money = elementsByTag2.get(6).text();
						// 余额
						String balance = elementsByTag2.get(7).text();
						// 用途
						String purpose = elementsByTag2.get(8).text();
						hfbChinaDebitCardBillDetails = new HfbChinaDebitCardBillDetails(taskid, tran_date, tran_channel,
								opposite_card_number, opposite_name, opposite_bank_name, currentaccount_type, money,
								balance, purpose);
						list.add(hfbChinaDebitCardBillDetails);
					}
				}
			}
			hfbChinaDebitCardBillDetailsRepository.saveAll(list);
			tracerLog.addTag(taskid + "----共解析出交易流水", list.size() + "条");
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.billDetailsParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}

	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
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
