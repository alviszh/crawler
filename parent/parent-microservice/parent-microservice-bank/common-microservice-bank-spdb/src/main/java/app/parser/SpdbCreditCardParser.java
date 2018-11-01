package app.parser;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardBillDetail;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardBillGeneral;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardGeneralInfo;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardInstallment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpdbCreditCardParser {

    public List<SpdbCreditCardInstallment> installmentsParser(TaskBank taskBank, String html) {
        List<SpdbCreditCardInstallment> installmentList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table_comm]");
        if (tables != null && tables.size() > 0) {
            for (Element table : tables) {
                Elements trs = table.select("tr");
                if (trs != null && trs.size() > 0) {
                    for (int i = 1; i < trs.size(); i++) {
                        String html1 = trs.html();
//                        System.out.println("==============html1=" + html1);
                        Elements tds = trs.get(i).select("td");
                        if (tds.size() == 7) {
                            SpdbCreditCardInstallment installment = new SpdbCreditCardInstallment();
                            installment.setOrderType(tds.get(0).text());
                            installment.setProductName(tds.get(1).text());
                            installment.setMoney(tds.get(2).text());
                            installment.setInstallmentNum(tds.get(3).text());
                            installment.setApplicatTime(tds.get(4).text());
                            installment.setRepayAmountMonth(tds.get(5).text());
                            installment.setFeeTotal(tds.get(6).text());
                            installment.setTaskid(taskBank.getTaskid());
                            installmentList.add(installment);
                        }
                    }
                }
            }
        }
        return installmentList;
    }

    //账户基本信息（第一部分）
    public SpdbCreditCardGeneralInfo generalInfoParser( String html) {
        SpdbCreditCardGeneralInfo spdbCreditCardGeneralInfo = new SpdbCreditCardGeneralInfo();
        Document doc = Jsoup.parse(html);
        String customerNum = doc.select("td:contains(客户号)+td").first().text();
        String idType = doc.select("td:contains(证件类型)+td").first().text();
        String idNum = doc.select("td:contains(证件号码)+td").first().text();
        spdbCreditCardGeneralInfo.setCustomerNum(customerNum);
        spdbCreditCardGeneralInfo.setIdType(idType);
        spdbCreditCardGeneralInfo.setIdNum(idNum);
        return spdbCreditCardGeneralInfo;
    }

    //账户基本信息（第二部分）
    public SpdbCreditCardGeneralInfo acctInfoParser( String html) {
        SpdbCreditCardGeneralInfo spdbCreditCardGeneralInfo = new SpdbCreditCardGeneralInfo();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table_comm]");
        if (tables != null && tables.size() > 0) {
            for (Element table : tables) {
                Elements trs = table.select("tr");
                if (trs != null && trs.size() > 0) {
                    for (int i = 3; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        if (tds != null && tds.size()==3) {
                            spdbCreditCardGeneralInfo .setName(tds.get(0).text());
                            spdbCreditCardGeneralInfo.setToday(tds.get(1).text());
                            spdbCreditCardGeneralInfo.setAccount(tds.get(2).text());
                        }
                    }
                }
            }
        }

        //账户信息
        String creditLimit = doc.select("td:contains(信用额度)+td").first().text();
        String essayLimit = doc.select("td:contains(取现额度)+td").first().text();
        String canUseLimit = doc.select("td:contains(可用额度)+td").first().text();
        String monthlyBillDay = doc.select("td:contains(每月账单日)+td").first().text();
        spdbCreditCardGeneralInfo.setCreditLimit(creditLimit);
        spdbCreditCardGeneralInfo.setEssayLimit(essayLimit);
        spdbCreditCardGeneralInfo.setCanUseLimit(canUseLimit);
        spdbCreditCardGeneralInfo.setMonthlyBillDay(monthlyBillDay);

        return spdbCreditCardGeneralInfo;
    }

    //账单明细
    public List<SpdbCreditCardBillDetail> billDetailParser(TaskBank taskBank, String html, String billsMonth) {
        List<SpdbCreditCardBillDetail> billDetails = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table_comm]");
        if (tables != null && tables.size() > 0) {
            for (Element table : tables) {
                Elements trs = table.select("tr");
                if (trs != null && trs.size() > 0) {
                    for (int i = 1; i < trs.size(); i++) {
                        String html1 = trs.html();
//                        System.out.println("==============html1=" + html1);
                        Elements tds = trs.get(i).select("td");
                        if (tds.size() == 6) {
                            SpdbCreditCardBillDetail billDetail = new SpdbCreditCardBillDetail();
                            billDetail.setBillMonth(billsMonth);
                            billDetail.setTradeDate(tds.get(0).text());
                            billDetail.setTallyDate(tds.get(1).text());
                            billDetail.setTranSummary(tds.get(2).text());
                            billDetail.setTransSum(tds.get(3).text());
                            billDetail.setCardNum(tds.get(4).text());
                            billDetail.setCardFlag(tds.get(5).text());
                            billDetail.setTaskid(taskBank.getTaskid());
                            billDetails.add(billDetail);
                        }
                    }
                }
            }
        }
        return billDetails;
    }

    //账单信息
    public SpdbCreditCardBillGeneral billGeneralParser(TaskBank taskBank, String html, String billsMonth) {
        SpdbCreditCardBillGeneral spdbCreditCardBillGeneral = new SpdbCreditCardBillGeneral();
        Document doc = Jsoup.parse(html);
        String payAmount = doc.select("td:contains(本期应还款余额)+td").first().text();
        String minPayAmount = doc.select("td:contains(本期最低还款额)+td").first().text();
        String dueDate = doc.select("td:contains(到期还款日)+td").first().text();
        spdbCreditCardBillGeneral.setBillMonth(billsMonth);
        spdbCreditCardBillGeneral.setPayAmount(payAmount);
        spdbCreditCardBillGeneral.setMinPayAmount(minPayAmount);
        spdbCreditCardBillGeneral.setDueDate(dueDate);
        spdbCreditCardBillGeneral.setTaskid(taskBank.getTaskid());

        return spdbCreditCardBillGeneral;
    }


    public Map<String, String > getParamParser( String html) {
        Document doc = Jsoup.parse(html);
        String billsMonth = doc.select("input[name=BillsMonth]").get(0).val();
        String currencyType = doc.select("input[name=CurrencyType]").get(0).val();

        int i = html.indexOf("var pageCurrentRecord");
        int j = html.indexOf("\n", i);
        String pcr = html.substring(i, j);
        String bookedDate = pcr.substring(pcr.indexOf("BookedDate"), pcr.indexOf('"',pcr.indexOf("BookedDate"))).split("\\|")[1];
        String turnoverSign = pcr.substring(pcr.indexOf("TurnoverSign"), pcr.indexOf('"', pcr.indexOf("TurnoverSign"))).split("\\|")[1];
        String registerDate = pcr.substring(pcr.indexOf("RegisterDate"), pcr.indexOf('"',pcr.indexOf("RegisterDate"))).split("\\|")[1];
        String transJnlNo = pcr.substring(pcr.indexOf("TransJnlNo"), pcr.indexOf('"',pcr.indexOf("TransJnlNo"))).split("\\|")[1];
        String consumeTime = pcr.substring(pcr.indexOf("ConsumeTime"), pcr.indexOf('"',pcr.indexOf("ConsumeTime"))).split("\\|")[1];

        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("billsMonth", billsMonth);
        paramMap.put("currencyType", currencyType);
        paramMap.put("bookedDate", bookedDate);
        paramMap.put("turnoverSign",turnoverSign);
        paramMap.put("registerDate",registerDate);
        paramMap.put("transJnlNo",transJnlNo);
        paramMap.put("consumeTime",consumeTime);
        return paramMap;
    }
}
