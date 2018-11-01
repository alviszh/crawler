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
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaCreditCardBillDetails;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaCreditCardBillGeneral;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaCreditCardIntegral;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaCreditCardUserInfo;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaCreditCardBillDetailsRepository;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaCreditCardBillGeneralRepository;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaCreditCardIntegralRepository;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaCreditCardUserInfoRepository;

import app.commontracerlog.TracerLog;


@Component
public class ParserService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private CmbChinaCreditCardUserInfoRepository cmbChinaCreditCardUserInfoRepository;

	@Autowired
	private CmbChinaCreditCardBillGeneralRepository cmbChinaCreditCardBillGeneralRepository;

	@Autowired
	private CmbChinaCreditCardBillDetailsRepository cmbChinaCreditCardBillDetailsRepository;

	@Autowired
	private CmbChinaCreditCardIntegralRepository cmbChinaCreditCardIntegralRepository;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	/**
	 * 获取个人信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void userInfoParser(String html, String taskid) {
		tracerLog.addTag("ParserService.userInfoParser---解析:", taskid);
		try {
			Document doc = Jsoup.parse(html);
			Elements trxyed = doc.getElementById("ucCmQueryCustomInfo0_trxyed").getElementsByTag("td");
			// 信用额度--------人民币
			String trxyedRMB = trxyed.get(1).text();
			// 信用额度--------美元
			String trxyedDollar = trxyed.get(2).text();

			Elements trkyed = doc.getElementById("ucCmQueryCustomInfo0_trkyed").getElementsByTag("td");
			// 可用额度--------人民币
			String trkyedRMB = trkyed.get(1).text();
			// 可用额度--------美元
			String trkyedDollar = trkyed.get(2).text();

			Elements tryjxjed = doc.getElementById("ucCmQueryCustomInfo0_tryjxjed").getElementsByTag("td");
			// 预借现金可用额度--------人民币
			String tryjxjedRMB = tryjxjed.get(1).text();
			// 预借现金可用额度--------美元
			String tryjxjedDollar = tryjxjed.get(2).text();
			// 每月账单日
			String lmyzd = doc.getElementById("ucCmQueryCustomInfo0_LMYZD").text();
			// 本期到期还款日
			String lbqdqhk = doc.getElementById("ucCmQueryCustomInfo0_LBQDQHK").text();
			// 卡号
			String idNum = "";
			// 主卡/附属卡
			String cardType = "";
			// 联名卡别
			String cardStyle = "";
			// 持卡人姓名
			String name = "";
			// 开卡标志
			String cardState = "";
			Elements elementsByTag = doc.getElementById("ucCmQueryCustomInfo0_dgReckoningSet").getElementsByTag("tr");
			for (Element element : elementsByTag) {
				Elements elementsByTag2 = element.getElementsByTag("td");
				if (elementsByTag2.size() > 4) {
					// 卡号
					idNum = elementsByTag2.get(0).text();
					if (!"卡号".equals(idNum)) {
						// 主卡/附属卡
						cardType = elementsByTag2.get(1).text();
						// 联名卡别
						cardStyle = elementsByTag2.get(2).text();
						// 持卡人姓名
						name = elementsByTag2.get(3).text();
						// 开卡标志
						cardState = elementsByTag2.get(4).text();
						CmbChinaCreditCardUserInfo cmbChinaCreditCardUserInfo = new CmbChinaCreditCardUserInfo(taskid,
								trxyedRMB, trxyedDollar, trkyedRMB, trkyedDollar, tryjxjedRMB, tryjxjedDollar, lmyzd,
								lbqdqhk, idNum, cardType, cardStyle, name, cardState);
						cmbChinaCreditCardUserInfoRepository.save(cmbChinaCreditCardUserInfo);
					}
				}
			}
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
	 * 获取月账单信息
	 * 
	 * @param html
	 * @param taskid
	 */
	public void billGeneralParser(String html, String taskid) {
		tracerLog.addTag("ParserService.getBillGeneral---解析:", taskid);
		try {
			Document doc = Jsoup.parse(html);
			List<CmbChinaCreditCardBillGeneral> list = new ArrayList<CmbChinaCreditCardBillGeneral>();
			CmbChinaCreditCardBillGeneral cmbChinaCreditCardBillGeneral = null;
			Element elementById = doc.getElementById("dgReckoningInfo1");
			Elements elementsByTag = elementById.getElementsByTag("tr");
			for (Element element : elementsByTag) {
				Elements elementsByTag2 = element.getElementsByTag("td");
				if (elementsByTag2.size() > 4) {
					// 账单月份
					String billMonth = elementsByTag2.get(0).text();
					// 人民币应还总额
					String repaymentSumRMB = elementsByTag2.get(1).text();
					// 人民币最低还款额
					String repaymentMinRMB = elementsByTag2.get(2).text();
					// 美元应还总额
					String repaymentSumDollar = elementsByTag2.get(3).text();
					// 美元最低还款额
					String repaymentMinDollar = elementsByTag2.get(4).text();
					cmbChinaCreditCardBillGeneral = new CmbChinaCreditCardBillGeneral(taskid, billMonth,
							repaymentSumRMB, repaymentMinRMB, repaymentSumDollar, repaymentMinDollar);
					list.add(cmbChinaCreditCardBillGeneral);
				}
			}
			cmbChinaCreditCardBillGeneralRepository.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.userInfoParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}
	}

	/**
	 * 交易明细
	 * 
	 * @param html
	 * @param taskid
	 * @param yearmonth
	 */
	public void billDetailsParser(String html, String taskid, String yearmonth) {
		tracerLog.addTag("ParserService.getBillGeneral----" + yearmonth + "--解析:", taskid);
		try {
			List<CmbChinaCreditCardBillDetails> list = new ArrayList<CmbChinaCreditCardBillDetails>();
			CmbChinaCreditCardBillDetails cmbChinaCreditCardBillDetails = null;
			Document doc = Jsoup.parse(html);
			// 2017年
			String loopBand = "loopBand2";
			if (yearmonth.contains("2016")) {
				// 2016年
				loopBand = "loopBand1";
			}
			Element loopBand2 = doc.getElementById(loopBand);
			Element table = loopBand2.getElementsByTag("table").get(0);
			Elements elementsBytable = table.getElementsByTag("span");
			for (Element element : elementsBytable) {
				// 2017年
				String fixBand = "fixBand15";
				if (yearmonth.contains("2016")) {
					// 2016年
					fixBand = "fixBand28";
				}
				Element elementById = element.getElementById(fixBand);
				if (null != elementById) {
					Elements elementsByTag = elementById.getElementsByTag("div");
					if (elementsByTag.size() == 7) {
						// 交易日
						String tradingDay = elementsByTag.get(0).text();
						// 记账日
						String billingDay = elementsByTag.get(1).text();
						// 交易摘要
						String summary = elementsByTag.get(2).text();
						// 人民币金额
						String rmbAmount = elementsByTag.get(3).text();
						// 卡号末四位
						String endfourNum = elementsByTag.get(4).text();
						// 交易地点
						String tradingPlace = elementsByTag.get(5).text();
						// 交易地金额
						String amountTransaction = elementsByTag.get(6).text();
						cmbChinaCreditCardBillDetails = new CmbChinaCreditCardBillDetails(taskid, tradingDay,
								billingDay, summary, rmbAmount, endfourNum, tradingPlace, amountTransaction);
						list.add(cmbChinaCreditCardBillDetails);
					}
				}
			}
			cmbChinaCreditCardBillDetailsRepository.saveAll(list);
			tracerLog.addTag(taskid + "----" + yearmonth + "共解析出交易流水", list.size() + "条");
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.billDetailsParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}

	}

	/**
	 * 积分详情
	 * 
	 * @param html
	 * @param taskid
	 * @param yearmonth
	 */
	public void integralParser(String html, String taskid, String yearmonth) {

		tracerLog.addTag("ParserService.integralParser------解析:", taskid);
		try {
			CmbChinaCreditCardIntegral cmbChinaCreditCardIntegral = null;
			Document doc = Jsoup.parse(html);

			Element elementById = doc.getElementById("UpdatePanel2");
			Elements elementsByTag = elementById.getElementsByTag("tr");
			if (elementsByTag.size() > 1) {
				Elements elementsByTag2 = elementsByTag.get(1).getElementsByTag("td");
				if (elementsByTag2.size() == 10) {
					// 积分名称
					String name = elementsByTag2.get(0).text();
					// 积分管理模式
					String model = elementsByTag2.get(1).text();
					// 当期刷卡积分
					String payCardIntegral = elementsByTag2.get(2).text();
					// 当期调整积分
					String adjustedIntegral = elementsByTag2.get(3).text();
					// 当期奖励积分
					String rewardIntegral = elementsByTag2.get(4).text();
					// 当期新增积分
					String addIntegral = elementsByTag2.get(5).text();
					// 当期兑换积分
					String exchangeIntegral = elementsByTag2.get(6).text();
					// 当前可用积分
					String useIntegral = elementsByTag2.get(7).text();
					// 最近即将失效积分
					String invalidIntegral = elementsByTag2.get(8).text();
					// 失效日期
					String invalidDate = elementsByTag2.get(9).text();

					cmbChinaCreditCardIntegral = new CmbChinaCreditCardIntegral(taskid, name, model, payCardIntegral,
							adjustedIntegral, rewardIntegral, addIntegral, exchangeIntegral, useIntegral,
							invalidIntegral, invalidDate, yearmonth);
					cmbChinaCreditCardIntegralRepository.save(cmbChinaCreditCardIntegral);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ParserService.integralParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}

	}

}
