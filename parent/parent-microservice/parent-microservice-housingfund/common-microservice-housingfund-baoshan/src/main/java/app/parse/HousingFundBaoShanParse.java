package app.parse;

import app.domain.HousingFundCommonUnit;
import com.microservice.dao.entity.crawler.housing.baoshan.HousingBaoShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.baoshan.HousingBaoShanUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmy on 2018/5/10.
 */

@Component
public class HousingFundBaoShanParse {
    public HousingBaoShanUserInfo userInfoParser(String html, TaskHousing taskHousing) {
        HousingBaoShanUserInfo housingBaoShanUserInfo= null;
        JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
        String taskid = taskHousing.getTaskid().trim();
        //个人基本信息
        String accname = jsob.getString("accname");
        String certinum = jsob.getString("certinum");
        String marstatus = HousingFundCommonUnit.getMarstatus(jsob.getString("marstatus"));
        String profession = HousingFundCommonUnit.getOccupation(jsob.getString("occupation"));//职业
        String phoneNumber = jsob.getString("handset");
        String email = jsob.getString("email");
        String isSmsOpen = "";
        String dac = jsob.getString("dac");
        if (dac.substring(0,1).equals("1")) {
            isSmsOpen = "已开通";
        } else {
            isSmsOpen = "未开通";
        }
        String opnaccdate = jsob.getString("opnaccdate");
        if (opnaccdate.equals("1899-12-31")) {
            opnaccdate = "";
        }
        String perInsurNum = jsob.getString("indisoicode");
        String isAuth = HousingFundCommonUnit.getUserLevel(jsob.getString("userLevel"));
        String homeAddress = jsob.getString("famaddr");

        //个人账户信息
        String accnum = jsob.getString("accnum");
        String indiaccstate = HousingFundCommonUnit.getAccState(jsob.getString("indiaccstate"));
        String unitaccnum =  jsob.getString("unitaccnum");
        String accganization = HousingFundCommonUnit.getAccinstcode(jsob.getString("accinstcode"));
        String unitaccname = jsob.getString("unitaccname");
        String balance = jsob.getString("bal");
        String freezeBalance = jsob.getString("frzamt");
        String regularBalance = jsob.getString("keepbal");
        String dueBalance = jsob.getString("increbal");
        String payBaseNum = jsob.getString("basenum");
        String payAmountMonth = jsob.getString("monpaysum");//页面显示百分比
        String unitProp = jsob.getString("unitprop");//页面显示百分比
        String indiProp =jsob.getString("indiprop");
        String stopPayDate = jsob.getString("payendym");
        String lastExtractDate = jsob.getString("lastdrawdate");
        if (lastExtractDate.equals("1899-12-31")) {
            lastExtractDate = "";
        }
        String extractTotal = jsob.getString("dpdrawamt");
        String extractCount = jsob.getString("drawtimes");
        String loansCount = jsob.getString("dploantimes");
        String badCreditCount = jsob.getString("dpbadcretimes")+jsob.getString("dpdrawcheattimes")+jsob.getString("dplncheattimes");

        //个人托收信息
        String bankcode = HousingFundCommonUnit.getBankcode(jsob.getString("bankcode"));
        String bankaccount = jsob.getString("bankaccnum");
        String emittingBankAccName = jsob.getString("bankaccnm");
        String emittingDate = jsob.getString("cstcollday");
        String checkid = jsob.getString("checkid");
        String cstcollflag = jsob.getString("cstcollflag");
        if (checkid.equals("1")) {//网厅、微信、APP
            checkid = "1";
            cstcollflag = "2";
        } else if (checkid.equals("2")){//托收
            checkid = "2";
            cstcollflag = "1";
        } else if (checkid.equals("3")) {//既有网厅也有托收
            checkid = "1";
            cstcollflag = "1";
        } else if (checkid.equals("")){
            checkid = "2";
        }
        String extGatheringIsSign = HousingFundCommonUnit.getCheckid(checkid);
        String perEmittingIsSign = HousingFundCommonUnit.getCheckid(cstcollflag);

        //缴存月数
        String repaymonths = jsob.getString("repaymonths");
        String paymonths = jsob.getString("paymonths");

        housingBaoShanUserInfo = new HousingBaoShanUserInfo();
        housingBaoShanUserInfo.setTaskid(taskid);
        housingBaoShanUserInfo.setAccName(accname);
        housingBaoShanUserInfo.setCertiNum(certinum);
        housingBaoShanUserInfo.setMaritalStatus(marstatus);
        housingBaoShanUserInfo.setProfession(profession);
        housingBaoShanUserInfo.setPhoneNumber(phoneNumber);
        housingBaoShanUserInfo.setEmail(email);
        housingBaoShanUserInfo.setIsSmsOpen(isSmsOpen);
        housingBaoShanUserInfo.setPerOpenData(opnaccdate);
        housingBaoShanUserInfo.setPerInsurNum(perInsurNum);
        housingBaoShanUserInfo.setIsAuth(isAuth);
        housingBaoShanUserInfo.setHomeAddress(homeAddress);

        housingBaoShanUserInfo.setAccnum(accnum);
        housingBaoShanUserInfo.setIndiaccstate(indiaccstate);
        housingBaoShanUserInfo.setUnitAccnum(unitaccnum);
        housingBaoShanUserInfo.setAccGanization(accganization);
        housingBaoShanUserInfo.setUnitaccname(unitaccname);
        housingBaoShanUserInfo.setBalance(balance);
        housingBaoShanUserInfo.setFreezeBalance(freezeBalance);
        housingBaoShanUserInfo.setRegularBalance(regularBalance);
        housingBaoShanUserInfo.setDueBalance(dueBalance);
        housingBaoShanUserInfo.setPayBaseNum(payBaseNum);
        housingBaoShanUserInfo.setPayAmountMonth(payAmountMonth);
        housingBaoShanUserInfo.setUnitProp(unitProp);
        housingBaoShanUserInfo.setIndiProp(indiProp);
        housingBaoShanUserInfo.setStopPayDate(stopPayDate);
        housingBaoShanUserInfo.setLastExtractDate(lastExtractDate);
        housingBaoShanUserInfo.setExtractTotal(extractTotal);
        housingBaoShanUserInfo.setExtractCount(extractCount);
        housingBaoShanUserInfo.setLoansCount(loansCount);
        housingBaoShanUserInfo.setBadCreditCount(badCreditCount);

        housingBaoShanUserInfo.setBankCode(bankcode);
        housingBaoShanUserInfo.setBankAccount(bankaccount);
        housingBaoShanUserInfo.setEmittingBankAccName(emittingBankAccName);
        housingBaoShanUserInfo.setEmittingDate(emittingDate);
        housingBaoShanUserInfo.setExtGatheringIsSign(extGatheringIsSign);
        housingBaoShanUserInfo.setPerEmittingIsSign(perEmittingIsSign);

        housingBaoShanUserInfo.setRePayMonths(repaymonths);
        housingBaoShanUserInfo.setPayMonths(paymonths);

        return housingBaoShanUserInfo;
    }

    public List<HousingBaoShanDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
        List<HousingBaoShanDetailAccount> list = null;
        HousingBaoShanDetailAccount housingBaoShanDetailAccount=null;
        JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
        System.out.println("====jsonArray:"+jsonArray);
        if (jsonArray != null && !"".equals(jsonArray)) {
            int size = jsonArray.size();
            if (size > 0) {
                list = new ArrayList<HousingBaoShanDetailAccount>();
                for (int i = 0; i < size; i++) {
                    JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
                    housingBaoShanDetailAccount = new HousingBaoShanDetailAccount();
                    housingBaoShanDetailAccount.setTaskid(taskHousing.getTaskid().trim());
                    housingBaoShanDetailAccount.setTransdate(jsob.getString("transdate"));
                    housingBaoShanDetailAccount.setAmount(jsob.getString("amt1"));
                    housingBaoShanDetailAccount.setBalance(jsob.getString("basenumber"));
//                housingBaoShanDetailAccount.setSummary(HousingFundCommonUnit.getSummary(jsob.getString("freeuse4")));
                    housingBaoShanDetailAccount.setSummary(jsob.getString("freeuse4"));
                    housingBaoShanDetailAccount.setBegindate(jsob.getString("begindatec"));
                    housingBaoShanDetailAccount.setEnddate(jsob.getString("enddatec"));
                    list.add(housingBaoShanDetailAccount);
                }
            }
        }
        return list;
    }
}
