import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardTransFlow;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */
public class testDebitCard {
    public static void main(String[] args) {
        /*String m = getCreditBeforeYear(2);
        System.out.println(m);

        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String todate = f.format(new Date());
        System.out.println(todate);*/

        String json = "{\"QueryNumber\":\"20\",\"EndDate\":\"20180201\",\"BeginDate\":\"20160202\",\"LoopResult\":[{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2016/03/21  03:50:06\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"16季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"1\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"35006\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.07\",\"Balance\":\"          86.75\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2016/06/21  03:23:25\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"16季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"2\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"32325\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.07\",\"Balance\":\"          86.82\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2016/09/21  04:16:36\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"16季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"3\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"41636\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.07\",\"Balance\":\"          86.89\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2016/12/21  05:19:01\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"16季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"4\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"51901\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.07\",\"Balance\":\"          86.96\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/01/17  10:24:14\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"汇入外LZ17011700157791\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"5\",\"CnterBankNo\":\"\",\"BusinessId\":\"_encry_E7ED5E606156A986609E497D19BF48C73E04A0FFCCAAD081C4F00B6AA0E75060AD75BD4ABD285530D7826A587404B84C8CEAD73E2F853B7BB88CB11994EB0BC92644C04C5EF81794538BBD41F01F0B496B1CC64D0DF4B594D2E9C230709625FF\",\"OccursTime\":\"102414\",\"CnterBankName\":\"\",\"RemitFlag\":\"14\",\"MTCN\":\"\",\"TxAmount\":\"       120.00\",\"Balance\":\"         206.96\"},{\"CrDtIndicator\":\"0\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/01/17  10:28:19\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"网上支付-支付宝\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"6\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"102819\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"       200.00\",\"Balance\":\"           6.96\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/01/18  10:54:00\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"汇入外LZ17011800185073\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"7\",\"CnterBankNo\":\"\",\"BusinessId\":\"_encry_99FE94BF2A2256B8EC37B9D7490FB7DDCA31A263B82A2C45ECB4F018647B8E650026B60B24C73575364EF1BEA27AEACBBC4660C60B92117EAC5B8DCA8B59D644FD3CCE5C3364B256D348D651922DB75157DE39607B0CEBC2DAC55B59D6066BB2\",\"OccursTime\":\"105400\",\"CnterBankName\":\"\",\"RemitFlag\":\"14\",\"MTCN\":\"\",\"TxAmount\":\"      7110.77\",\"Balance\":\"        7117.73\"},{\"CrDtIndicator\":\"0\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/01/18  10:54:48\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"网上支付-支付宝\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"8\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"105448\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"      7110.00\",\"Balance\":\"           7.73\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/03/21  04:24:14\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"17季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"9\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"42414\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.02\",\"Balance\":\"           7.75\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/06/21  03:09:06\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"17季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"10\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"30906\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.01\",\"Balance\":\"           7.76\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/09/21  03:31:18\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"17季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"11\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"33118\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.01\",\"Balance\":\"           7.77\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/12/01  10:43:37\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"支付宝-alipay:余额宝提\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"12\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"104337\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"        50.00\",\"Balance\":\"          57.77\"},{\"CrDtIndicator\":\"0\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/12/01  14:02:18\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"转存(三月)定期\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"13\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"140218\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"        50.00\",\"Balance\":\"           7.77\"},{\"CrDtIndicator\":\"1\",\"TranCnterNm\":\"\",\"AuthNo\":\"\",\"OccursDate\":\"2017/12/21  04:42:49\",\"ApplNo\":\"\",\"AppSeqNo\":\"\",\"TxType\":\"17季息\",\"TranCnterAcctNo\":\"\",\"SeqNo\":\"14\",\"CnterBankNo\":\"\",\"BusinessId\":\"\",\"OccursTime\":\"44249\",\"CnterBankName\":\"\",\"RemitFlag\":\"0\",\"MTCN\":\"\",\"TxAmount\":\"         0.01\",\"Balance\":\"           7.78\"}],\"BeginNumber\":\"1\",\"_TransactionId\":\"QueryHistory\",\"ResponseCode\":\"AAAAAAA\",\"errmsg\":\"\",\"ResponseMsg\":null}\n";
        System.out.println(json);
        System.out.println(parserJson(json,"11"));
    }


    public static String getCreditBeforeYear(int i){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -i);
        String beforeYear = f.format(c.getTime());
        return beforeYear;
    }

    public static  List<SpdbDebitCardTransFlow> parserJson(String json, String acctNo){
        List<SpdbDebitCardTransFlow> transFlowList = new ArrayList<>();
        JSONObject jsonObject =  JSONObject.fromObject(json);
        JSONArray loopResults = jsonObject.getJSONArray("LoopResult");

        for (int i = 0; i < loopResults.size(); i++) {
            JSONObject loopResult = loopResults.getJSONObject(i);
            System.out.println("=="+ loopResult);
            SpdbDebitCardTransFlow transFlow = new SpdbDebitCardTransFlow();
//            transFlow.setTaskid(taskBank.getTaskid());
            transFlow.setNo(loopResult.getString("SeqNo"));
            transFlow.setTransTime(loopResult.getString("OccursDate"));
            transFlow.setSummary(loopResult.getString("TxType"));
            //获取正负号字段值（区分存入、取出）
            String crDtIndicator = loopResult.getString("CrDtIndicator").trim();
            String depositAmount = "";
            String takeAmount = "";
            if (crDtIndicator.equals("1")) {
                depositAmount = loopResult.getString("TxAmount").trim();
            } else if (crDtIndicator.equals("0")) {
                takeAmount = loopResult.getString("TxAmount").trim();
            }
            transFlow.setDepositAmount(depositAmount);
            transFlow.setTakeAmount(takeAmount);
            transFlow.setBalance(loopResult.getString("Balance").trim());
            transFlow.setAcctNo(acctNo);
            transFlowList.add(transFlow);
        }
        return transFlowList;
    }
}
