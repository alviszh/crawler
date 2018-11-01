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
import com.microservice.dao.entity.crawler.bank.psbcchina.PsbcChinaDebitCardBillDetails;
import com.microservice.dao.entity.crawler.bank.psbcchina.PsbcChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.psbcchina.PsbcChinaDebitCardBillDetailsRepository;
import com.microservice.dao.repository.crawler.bank.psbcchina.PsbcChinaDebitCardUserInfoRepository;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class ParserService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private PsbcChinaDebitCardUserInfoRepository psbcChinaDebitCardUserInfoRepository;

	@Autowired
	private PsbcChinaDebitCardBillDetailsRepository psbcChinaDebitCardBillDetailsRepository;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	/**
	 * 获取个人信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void userInfoParser(String userHtml, String taskid) {
		tracerLog.addTag("ParserService.userInfoParser---解析:", taskid);
		try {
			Document doc = Jsoup.parse(userHtml);
			Element elementById = doc.getElementById("sign");
			Elements state = elementById.getElementsByClass("state");
			// 签约标志
			String sign_sign = state.text();
			elementById.getElementsByClass("state").empty();
			state.empty();
			// 卡号/账号
			String card_number = elementById.text();
			// 卡类型
			String card_type = doc.getElementById("accountTypeShow").text();
			// 别名
			String aliases = getNextLabelByKeyword(doc, "别名");
			// 账户类型
			String account_type = getNextLabelByKeyword(doc, "账户类型");
			// 开户机构
			String opening_institution = getNextLabelByKeyword(doc, "开户机构");
			// 币种
			String currency = "";
			// 账户余额
			String balance = "";
			// 可用余额
			String available_balance = "";
			// 开户日期
			String account_opening = "";
			// 账户状态
			String account_state = "";
			try {
				Elements elementsByTag = doc.getElementById("sublists").getElementsByTag("tbody").get(0)
						.getElementsByTag("tr").get(0).getElementsByTag("td");
				if (elementsByTag.size() > 5) {
					// 币种
					currency = elementsByTag.get(0).text();
					// 账户余额
					balance = elementsByTag.get(1).text();
					// 可用余额
					available_balance = elementsByTag.get(2).text();
					// 开户日期
					account_opening = elementsByTag.get(3).text();
					// 账户状态
					account_state = elementsByTag.get(4).text();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			PsbcChinaDebitCardUserInfo psbcChinaDebitCardUserInfo = new PsbcChinaDebitCardUserInfo(taskid, card_number,
					aliases, card_type, currency, balance, available_balance, account_state, sign_sign,
					opening_institution, account_type, account_opening);
			psbcChinaDebitCardUserInfoRepository.save(psbcChinaDebitCardUserInfo);
			tracerLog.addTag("个人信息保存成功", taskid);
			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_LOGIN_DOING.getDescription(),
					taskid);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.userInfoParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}
		taskBankStatusService.updateTaskBankUserinfo(500, BankStatusCode.BANK_LOGIN_DOING.getDescription(),
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
			List<PsbcChinaDebitCardBillDetails> list = new ArrayList<PsbcChinaDebitCardBillDetails>();
			PsbcChinaDebitCardBillDetails psbcChinaDebitCardBillDetails = null;
			
			JSONObject jsonObj = JSONObject.fromObject(html);
			String ctn = jsonObj.getString("transDetailList1");
			JSONArray jsonArray = JSONArray.fromObject(ctn);
			for (Object object : jsonArray) {
				JSONObject obj = JSONObject.fromObject(object);
				// 流水号
				String serial_number = obj.getString("flowNo");
				// 交易日期
				String tran_date = obj.getString("tranDate");
				// 摘要
				String trans_decription = obj.getString("sumry");
				//收入/支出
				/**
				 * in_out=='1'-------收入
				 * in_out=='2'-------支出
				 */
				String in_out = obj.getString("inOut");
				if (in_out.equals("1")) {
					in_out = "收入";
				} else if (in_out.equals("2")) {
					in_out = "支出";
				}
				// 交易金额
				String money = obj.getString("tranAmt");
				// 账户余额
				String balance = obj.getString("balance");
//				psbcChinaDebitCardBillDetails = new PsbcChinaDebitCardBillDetails(taskid, serial_number, tran_date,
//						trans_decription, money, balance);
				psbcChinaDebitCardBillDetails = new PsbcChinaDebitCardBillDetails(taskid, serial_number, tran_date,
						trans_decription, in_out, money, balance);
				list.add(psbcChinaDebitCardBillDetails);
			}
			psbcChinaDebitCardBillDetailsRepository.saveAll(list);
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
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("label:contains(" + keyword + ")");
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
