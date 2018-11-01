package app.controller;

import com.crawler.housingfund.json.TaskHousingfund;
import app.client.fund.*;
import com.crawler.housingfund.json.MessageLoginForHousing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/h5/fund")
public class HousingfundCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(HousingfundCrawlerController.class);

    @Autowired
    private FundBeijingClient fundBeijingClient;
    @Autowired
    private FundFoshanClient fundFoshanClient;
    @Autowired
    private FundNanchangClient fundNanchangClient;
    @Autowired
    private FundShijiazhuangClient fundShijiazhuangClient;
    @Autowired
    private FundQingdaoClient fundQingdaoClient;
    @Autowired
    private FundNingboClient fundNingboClient;
    @Autowired
    private FundTianjinClient fundTianjinClient;
    @Autowired
    private FundChongqingClient fundChongqingClient;
    @Autowired
    private FundHefeiClient fundHefeiClient;
    @Autowired
    private FundWuxiClient fundWuxiClient;
    @Autowired
    private FundHaerbinClient fundHaerbinClient;
    @Autowired
    private FundHangzhouClient fundHangzhouClient;
    @Autowired
    private FundShenyangClient fundShenyangClient;
    @Autowired
    private FundZhengzhouClient fundZhengzhouClient;
    @Autowired
    private FundGuiyangClient fundGuiyangClient;
    @Autowired
    private FundWenzhouClient fundWenzhouClient;
    @Autowired
    private FundXiamenClient fundXiamenClient;
    @Autowired
    private FundShanghaiClient fundShanghaiClient;
    @Autowired
    private FundChengduClient fundChengduClient;
    @Autowired
    private FundDongguanClient fundDongguanClient;
    @Autowired
    private FundShenzhenClient fundShenzhenClient;
    @Autowired
    private FundYantaiClient fundYantaiClient;
    @Autowired
    private FundTaiyuanClient fundTaiyuanClient;
    @Autowired
    private FundXuzhouClient fundXuzhouClient;
    @Autowired
    private FundChangchunClient fundChangchunClient;
    @Autowired
    private FundChangshaClient fundChangshaClient;
    @Autowired
    private FundDaqingClient fundDaqingClient;
    @Autowired
    private FundNanjingClient fundNanjingClient;
    @Autowired
    private FundAnqingClient fundAnqingClient;
    @Autowired
    private FundDalianClient fundDalianClient;
    @Autowired
    private FundTielingClient fundTielingClient;
    @Autowired
    private FundLuoyangClient fundLuoyangClient;
    @Autowired
    private FundXuchangClient fundXuchangClient;
    @Autowired
    private FundXianyangClient fundXianyangClient;
    @Autowired
    private FundYananClient fundYananClient;
    @Autowired
    private FundRizhaoClient fundRizhaoClient;
    @Autowired
    private FundMianyangClient fundMianyangClient;
    @Autowired
    private FundBaojiClient fundBaojiClient;
    @Autowired
    private FundLiaochengClient fundLiaochengClient;
    @Autowired
    private FundChuxiongClient fundChuxiongClient;
    @Autowired
    private FundYanbianClient fundYanbianClient;
    @Autowired
    private FundHeiheClient fundHeiheClient;
    @Autowired
    private FundPanjinClient fundPanjinClient;
    @Autowired
    private FundBinzhouClient fundBinzhouClient;
    @Autowired
    private FundTaianClient fundTaianClient;
    @Autowired
    private FundYibinClient fundYibinClient;
    @Autowired
    private FundJinhuaClient fundJinhuaClient;
    @Autowired
    private FundSuqianClient fundSuqianClient;
    @Autowired
    private FundXinxiangClient fundXinxiangClient;
    @Autowired
    private FundBaishanClient fundBaishanClient;
    @Autowired
    private FundYangzhouClient fundYangzhouClient;
    @Autowired
    private FundHuzhouClient fundHuzhouClient;
    @Autowired
    private FundJiningClient fundJiningClient;
    @Autowired
    private FundHengshuiClient fundHengshuiClient;
    @Autowired
    private FundXingtaiClient fundXingtaiClient;
    @Autowired
    private FundJinanClient fundJinanClient;
    @Autowired
    private FundNanchongClient fundNanchongClient;
    @Autowired
    private FundDezhouClient fundDezhouClient;
    @Autowired
    private FundXiangyangClient fundXiangyangClient;
    @Autowired
    private FundChangzhiClient fundChangzhiClient;
    @Autowired
    private FundPuyangClient fundPuyangClient;
    @Autowired
    private FundLiangshanClient fundLiangshanClient;
    @Autowired
    private FundDandongClient fundDandongClient;
    @Autowired
    private FundHuhehaoteClient fundHuhehaoteClient;
    @Autowired
    private FundLishuiClient fundLishuiClient;
    @Autowired
    private FundKunmingClient fundKunmingClient;
    @Autowired
    private FundWeihaiClient fundWeihaiClient;
    @Autowired
    private FundLinyiClient fundLinyiClient;
    @Autowired
    private FundJiamusiClient fundJiamusiClient;
    @Autowired
    private FundMudanjiangClient fundMudanjiangClient;
    @Autowired
    private FundYichunClient fundYichunClient;
    @Autowired
    private FundGuilinClient fundGuilinClient;
    @Autowired
    private FundYuxiClient fundYuxiClient;
    @Autowired
    private FundLiuzhouClient fundLiuzhouClient;
    @Autowired
    private FundChifengClient fundChifengClient;
    @Autowired
    private FundFuzhouClient fundFuzhouClient;
    @Autowired
    private FundJiuquanClient fundJiuquanClient;
    @Autowired
    private FundJiyuanClient fundJiyuanClient;
    @Autowired
    private FundSuizhouClient fundSuizhouClient;
    @Autowired
    private FundDatongClient fundDatongClient;
    @Autowired
    private FundJilinClient fundJilinClient;
    @Autowired
    private FundTonghuaClient fundTonghuaClient;
    @Autowired
    private FundWeifangClient fundWeifangClient;
    @Autowired
    private FundBaodingClient fundBaodingClient;
    @Autowired
    private FundQiqihaerClient fundQiqihaerClient;
    @Autowired
    private FundCangzhouClient fundCangzhouClient;
    @Autowired
    private FundTangshanClient fundTangshanClient;
    @Autowired
    private FundXianClient fundXianClient;
    @Autowired
    private FundZhumadianClient fundZhumadianClient;
    @Autowired
    private FundLeshanClient fundLeshanClient;
    @Autowired
    private FundSipingClient fundSipingClient;
    @Autowired
    private FundLuoheClient fundLuoheClient;
    @Autowired
    private FundHulunbeierClient fundHulunbeierClient;
    @Autowired
    private FundTongliaoClient fundTongliaoClient;
    @Autowired
    private FundQinzhouClient fundQinzhouClient;
    @Autowired
    private FundWuhaiClient fundWuhaiClient;
    @Autowired
    private FundYinchuanClient fundYinchuanClient;
    @Autowired
    private FundYulinClient fundYulinClient;
    @Autowired
    private FundQujingClient fundQujingClient;
    @Autowired
    private FundWuzhouClient fundWuzhouClient;
    @Autowired
    private FundTaizhouClient fundTaizhouClient;
    @Autowired
    private FundShangqiuClient fundShangqiuClient;
    @Autowired
    private FundHhhnzyzzzqClient fundHhhnzyzzzqClient;
    @Autowired
    private FundJiaozuoClient fundJiaozuoClient;
    @Autowired
    private FundGanzhouClient fundGanzhouClient;
    @Autowired
    private FundGuigangClient fundGuigangClient;
    @Autowired
    private FundXiningClient fundXiningClient;
    @Autowired
    private FundShangraoClient fundShangraoClient;
    @Autowired
    private FundYiwuClient fundYiwuClient;
    @Autowired
    private FundDaliClient fundDaliClient;
    @Autowired
    private FundZhaotongClient fundZhaotongClient;
    @Autowired
    private FundZaozhuangClient fundZaozhuangClient;
    @Autowired
    private FundSuzhouClient fundSuzhouClient;
    @Autowired
    private FundWuhanClient fundWuhanClient;
    @Autowired
    private FundGuangzhouClient fundGuangzhouClient;
    @Autowired
    private FundZiboClient fundZiboClient;
    @Autowired
    private FundWenshanClient fundWenshanClient;
    @Autowired
    private FundLuzhouClient fundLuzhouClient;
    @Autowired
    private FundSuiningClient fundSuiningClient;
    @Autowired
    private FundXinyuClient fundXinyuClient;
    @Autowired
    private FundZhaoqingClient fundZhaoqingClient;
    @Autowired
    private FundZhanjiangClient fundZhanjiangClient;
    @Autowired
    private FundBengbuClient fundBengbuClient;
    @Autowired
    private FundZhuzhouClient fundZhuzhouClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    TaskHousingfund login(Model model,MessageLoginForHousing messageLoginForHousing) {
        TaskHousingfund taskHousingfunda = new TaskHousingfund();
        String city = messageLoginForHousing.getCity();
        if (city.equals("天津市")) {
            log.info("-----------天津公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTianjinClient.login(messageLoginForHousing);
        }
        else if (city.equals("宁波市")) {
            log.info("-----------宁波公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundNingboClient.login(messageLoginForHousing);
        }
        else if (city.equals("青岛市")) {
            log.info("-----------青岛公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundQingdaoClient.login(messageLoginForHousing);
        }
        else if (city.equals("合肥市")) {
            log.info("-----------合肥公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHefeiClient.login(messageLoginForHousing);
        }
        else if (city.equals("哈尔滨市")) {
            log.info("-----------哈尔滨公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHaerbinClient.login(messageLoginForHousing);
        }
        else if (city.equals("哈尔滨市")) {
            log.info("-----------哈尔滨公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHaerbinClient.login(messageLoginForHousing);
        }
        else if (city.equals("沈阳市")) {
            log.info("-----------沈阳公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundShenyangClient.login(messageLoginForHousing);
        }
        else if (city.equals("贵阳市")) {
            log.info("-----------贵阳公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundGuiyangClient.login(messageLoginForHousing);
        }
        else if (city.equals("成都市")) {
            log.info("-----------成都公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundChengduClient.login(messageLoginForHousing);
        }
        else if (city.equals("徐州市")) {
            log.info("-----------徐州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("大庆市")) {
            log.info("-----------大庆公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundDaqingClient.login(messageLoginForHousing);
        }
        else if (city.equals("南京市")) {
            log.info("-----------南京公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundNanjingClient.login(messageLoginForHousing);
        }
        else if (city.equals("大连市")) {
            log.info("-----------大连公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundDalianClient.login(messageLoginForHousing);
        }
        else if (city.equals("抚州市")) {
            log.info("-----------抚州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundFuzhouClient.login(messageLoginForHousing);
        }

        else if (city.equals("洛阳市")) {
            log.info("-----------洛阳公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLuoyangClient.login(messageLoginForHousing);
        }
        else if (city.equals("铁岭市")) {
            log.info("-----------铁岭公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTielingClient.login(messageLoginForHousing);
        }
        else if (city.equals("梧州市")) {
            log.info("-----------梧州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("咸阳市")) {
            log.info("-----------咸阳公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXianyangClient.login(messageLoginForHousing);
        }
        else if (city.equals("许昌市")) {
            log.info("-----------许昌公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXuchangClient.login(messageLoginForHousing);
        }
        else if (city.equals("郑州市")) {
            log.info("-----------郑州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZhengzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("邢台市")) {
            log.info("-----------邢台公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXingtaiClient.login(messageLoginForHousing);
        }
        else if (city.equals("衡水市")) {
            log.info("-----------衡水公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHengshuiClient.login(messageLoginForHousing);
        }
        else if (city.equals("济南市")) {
            log.info("-----------济南公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJinanClient.login(messageLoginForHousing);
        }
        else if (city.equals("济宁市")) {
            log.info("-----------济宁公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJiningClient.login(messageLoginForHousing);
        }
        else if (city.equals("南充市")) {
            log.info("-----------南充公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundNanchongClient.login(messageLoginForHousing);
        }
        else if (city.equals("德州市")) {
            log.info("-----------德州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundDezhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("长治市")) {
            log.info("-----------长治公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundChangzhiClient.login(messageLoginForHousing);
        }
        else if (city.equals("濮阳市")) {
            log.info("-----------濮阳公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundPuyangClient.login(messageLoginForHousing);
        }
        else if (city.equals("凉山彝族自治州")) {
            log.info("-----------凉山彝族自治州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLiangshanClient.login(messageLoginForHousing);
        }
        else if (city.equals("丹东市")) {
            log.info("-----------丹东市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundDandongClient.login(messageLoginForHousing);
        }
        else if (city.equals("呼和浩特市")) {
            log.info("-----------呼和浩特市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHuhehaoteClient.login(messageLoginForHousing);
        }
        else if (city.equals("丽水市")) {
            log.info("-----------丽水市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLishuiClient.login(messageLoginForHousing);
        }
        else if (city.equals("昆明市")) {
            log.info("-----------昆明市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundKunmingClient.login(messageLoginForHousing);
        }
        else if (city.equals("威海市")) {
            log.info("-----------威海市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWeihaiClient.login(messageLoginForHousing);
        }
        else if (city.equals("临沂市")) {
            log.info("-----------临沂市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLinyiClient.login(messageLoginForHousing);
        }
        else if (city.equals("佳木斯市")) {
            log.info("-----------佳木斯市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJiamusiClient.login(messageLoginForHousing);
        }
        else if (city.equals("牡丹江市")) {
            log.info("-----------牡丹江市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundMudanjiangClient.login(messageLoginForHousing);
        }
        else if (city.equals("宜春市")) {
            log.info("-----------宜春市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundYichunClient.login(messageLoginForHousing);
        }
        else if (city.equals("桂林市")) {
            log.info("-----------桂林市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundGuilinClient.login(messageLoginForHousing);
        }
        else if (city.equals("玉溪市")) {
            log.info("-----------玉溪市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundYuxiClient.login(messageLoginForHousing);
        }
        else if (city.equals("柳州市")) {
            log.info("-----------柳州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLiuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("赤峰市")) {
            log.info("----------赤峰市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundChifengClient.login(messageLoginForHousing);
        }
        else if (city.equals("济源市")) {
            log.info("----------济源市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJiyuanClient.login(messageLoginForHousing);
        }
        else if (city.equals("吉林市")) {
            log.info("----------吉林市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJilinClient.login(messageLoginForHousing);
        }
        else if (city.equals("通化市")) {
            log.info("----------通化市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTonghuaClient.login(messageLoginForHousing);
        }
        else if (city.equals("潍坊市")) {
            log.info("----------潍坊市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWeifangClient.login(messageLoginForHousing);
        }
        else if (city.equals("保定市")) {
            log.info("----------保定市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundBaodingClient.login(messageLoginForHousing);
        }
        else if (city.equals("沧州市")) {
            log.info("----------沧州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundCangzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("唐山市")) {
            log.info("----------唐山市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTangshanClient.login(messageLoginForHousing);
        }
        else if (city.equals("西安市")) {
            log.info("----------西安市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXianClient.login(messageLoginForHousing);
        }
        else if (city.equals("驻马店市")) {
            log.info("----------驻马店市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZhumadianClient.login(messageLoginForHousing);
        }
        else if (city.equals("乐山市")) {
            log.info("----------乐山市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLeshanClient.login(messageLoginForHousing);
        }
        else if (city.equals("四平市")) {
            log.info("----------四平市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundSipingClient.login(messageLoginForHousing);
        }
        else if (city.equals("漯河市")) {
            log.info("----------漯河市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLuoheClient.login(messageLoginForHousing);
        }
        else if (city.equals("呼伦贝尔市")) {
            log.info("----------呼伦贝尔市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLuoheClient.login(messageLoginForHousing);
        }
        else if (city.equals("通辽市")) {
            log.info("----------通辽市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTongliaoClient.login(messageLoginForHousing);
        }
        else if (city.equals("钦州市")) {
            log.info("----------钦州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundQinzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("乌海市")) {
            log.info("----------乌海市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWuhaiClient.login(messageLoginForHousing);
        }
        else if (city.equals("银川市")) {
            log.info("----------银川市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundYinchuanClient.login(messageLoginForHousing);
        }
        else if (city.equals("台州市")) {
            log.info("----------台州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundTaizhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("商丘市")) {
            log.info("----------商丘市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundShangqiuClient.login(messageLoginForHousing);
        }
        else if (city.equals("红河哈尼族彝族自治州")) {
            log.info("----------红河哈尼族彝族自治州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHhhnzyzzzqClient.login(messageLoginForHousing);
        }
        else if (city.equals("焦作市")) {
            log.info("----------焦作市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundJiaozuoClient.login(messageLoginForHousing);
        }
        else if (city.equals("贵港市")) {
            log.info("----------贵港市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundGuigangClient.login(messageLoginForHousing);
        }
        else if (city.equals("义乌市")) {
            log.info("----------义乌市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundYiwuClient.login(messageLoginForHousing);
        }
        else if (city.equals("大理市")) {
            log.info("----------大理市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundDaliClient.login(messageLoginForHousing);
        }
        else if (city.equals("枣庄市")) {
            log.info("----------枣庄市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZaozhuangClient.login(messageLoginForHousing);
        }
        else if (city.equals("苏州市")) {
            log.info("----------苏州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundSuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("武汉市")) {
            log.info("----------武汉市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWuhanClient.login(messageLoginForHousing);
        }
        else if (city.equals("广州市")) {
            log.info("----------广州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundGuangzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("文山壮族苗族自治州")) {
            log.info("----------文山壮族苗族自治州公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundWenshanClient.login(messageLoginForHousing);
        }
        else if (city.equals("泸州市")) {
            log.info("----------泸州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundLuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("遂宁市")) {
            log.info("----------遂宁市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundSuiningClient.login(messageLoginForHousing);
        }
        else if (city.equals("新余市")) {
            log.info("----------新余市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundXinyuClient.login(messageLoginForHousing);
        }
        else if (city.equals("肇庆市")) {
            log.info("----------肇庆市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZhaoqingClient.login(messageLoginForHousing);
        }
        else if (city.equals("湖州市")) {
            log.info("----------湖州市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundHuzhouClient.login(messageLoginForHousing);
        }
        else if (city.equals("湛江市")) {
            log.info("----------湛江市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZhanjiangClient.login(messageLoginForHousing);
        }
        else if (city.equals("蚌埠市")) {
            log.info("----------蚌埠市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundBengbuClient.login(messageLoginForHousing);
        }
        else if (city.equals("株洲市")) {
            log.info("----------株洲市公积金 登录------------" + messageLoginForHousing);
            taskHousingfunda = fundZhuzhouClient.login(messageLoginForHousing);
        }

        log.info("-----------taskInsurancea------------" + taskHousingfunda);
        return  taskHousingfunda;
    }


    @PostMapping(value = "/crawler")
    public @ResponseBody
    TaskHousingfund crawler(Model model,MessageLoginForHousing messageLoginForHousing) {
        TaskHousingfund taskHousingfunda = new TaskHousingfund();
        String city = messageLoginForHousing.getCity();

        System.out.println("city is:"+city);
        if (city.equals("北京市")) {
            log.info("-----------北京公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundBeijingClient.crawler(messageLoginForHousing);
        } else if (city.equals("佛山市")) {
            log.info("-----------佛山公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundFoshanClient.crawler(messageLoginForHousing);

        } else if (city.equals("重庆市")) {
            log.info("-----------重庆公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChongqingClient.crawler(messageLoginForHousing);

        } else if (city.equals("南昌市")) {
            log.info("-----------南昌公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundNanchangClient.crawler(messageLoginForHousing);

        } else if (city.equals("石家庄市")) {
            log.info("-----------南昌公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShijiazhuangClient.crawler(messageLoginForHousing);

        } else if (city.equals("青岛市")) {
            log.info("-----------青岛公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundQingdaoClient.crawler(messageLoginForHousing);

        } else if (city.equals("宁波市")) {
            log.info("-----------宁波公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundNingboClient.crawler(messageLoginForHousing);

        } else if (city.equals("合肥市")) {
            log.info("-----------合肥公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHefeiClient.crawler(messageLoginForHousing);

        } else if (city.equals("天津市")) {
            log.info("-----------天津公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTianjinClient.crawler(messageLoginForHousing);

        } else if (city.equals("无锡市")) {
            log.info("-----------无锡公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWuxiClient.crawler(messageLoginForHousing);

        } else if (city.equals("哈尔滨市")) {
            log.info("-----------哈尔滨公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHaerbinClient.crawler(messageLoginForHousing);

        } else if (city.equals("杭州市")) {
            log.info("-----------杭州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHangzhouClient.crawler(messageLoginForHousing);

        } else if (city.equals("沈阳市")) {
            log.info("-----------沈阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShenyangClient.crawler(messageLoginForHousing);

        } else if (city.equals("郑州市")) {
            log.info("-----------郑州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhengzhouClient.crawler(messageLoginForHousing);

        } else if (city.equals("贵阳市")) {
            log.info("-----------贵阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundGuiyangClient.crawler(messageLoginForHousing);

        } else if (city.equals("温州市")) {
            log.info("-----------温州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWenzhouClient.crawler(messageLoginForHousing);

        } else if (city.equals("厦门市")) {
            log.info("-----------厦门公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXiamenClient.crawler(messageLoginForHousing);

        } else if (city.equals("上海市")) {
            log.info("-----------上海公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShanghaiClient.crawler(messageLoginForHousing);

        } else if (city.equals("成都市")) {
            log.info("-----------成都公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChengduClient.crawler(messageLoginForHousing);

        } else if (city.equals("东莞市")) {
            log.info("-----------东莞公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDongguanClient.crawler(messageLoginForHousing);

        } else if (city.equals("深圳市")) {
            log.info("-----------深圳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShenzhenClient.crawler(messageLoginForHousing);

        } else if (city.equals("烟台市")) {
            log.info("-----------烟台公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYantaiClient.crawler(messageLoginForHousing);

        } else if (city.equals("太原市")) {
            log.info("-----------太原公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTaiyuanClient.crawler(messageLoginForHousing);

        } else if (city.equals("徐州市")) {
            log.info("-----------徐州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXuzhouClient.crawler(messageLoginForHousing);

        } else if (city.equals("长春市")) {
            log.info("-----------长春公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChangchunClient.crawler(messageLoginForHousing);

        } else if (city.equals("长沙市")) {
            log.info("-----------长沙公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChangshaClient.crawler(messageLoginForHousing);

        } else if (city.equals("大庆市")) {
            log.info("-----------大庆公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDaqingClient.crawler(messageLoginForHousing);

        } else if (city.equals("南京市")) {
            log.info("-----------南京公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundNanjingClient.crawler(messageLoginForHousing);

        } else if (city.equals("安庆市")) {
            log.info("-----------安庆公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundAnqingClient.crawler(messageLoginForHousing);

        }
        else if (city.equals("大连市")) {
            log.info("-----------大连公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDalianClient.crawler(messageLoginForHousing);

        }
        else if (city.equals("铁岭市")) {
            log.info("-----------铁岭公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTielingClient.crawler(messageLoginForHousing);

        }
        else if (city.equals("洛阳市")) {
            log.info("-----------洛阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLuoyangClient.crawler(messageLoginForHousing);

        }
        else if (city.equals("许昌市")) {
            log.info("-----------许昌公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXuchangClient.crawler(messageLoginForHousing);

        }
        else if (city.equals("咸阳市")) {
            log.info("-----------咸阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXianyangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("延安市")) {
            log.info("-----------延安公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYananClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("日照市")) {
            log.info("-----------日照公积金 爬取数据-------------" + messageLoginForHousing);
            taskHousingfunda = fundRizhaoClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("宝鸡市")) {
            log.info("-----------宝鸡公积金 爬取数据-------------" + messageLoginForHousing);
            taskHousingfunda = fundBaojiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("聊城市")) {
            log.info("-----------聊城公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLiaochengClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("绵阳市")) {
            log.info("-----------绵阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundMianyangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("楚雄市")) {
            log.info("-----------楚雄公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChuxiongClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("延边朝鲜族自治州")) {
            log.info("-----------延边朝鲜族自治州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYanbianClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("黑河市")) {
            log.info("-----------黑河公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHeiheClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("盘锦市")) {
            log.info("-----------盘锦公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundPanjinClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("滨州市")) {
            log.info("-----------滨州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundBinzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("泰安市")) {
            log.info("-----------泰安公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTaianClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("宜宾市")) {
            log.info("-----------宜宾公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYibinClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("金华市")) {
            log.info("-----------金华公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJinhuaClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("宿迁市")) {
            log.info("-----------宿迁公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundSuqianClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("新乡市")) {
            log.info("-----------新乡公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXinxiangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("白山市")) {
            log.info("-----------白山公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundBaishanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("扬州市")) {
            log.info("-----------扬州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYangzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("济宁市")) {
            log.info("-----------济宁公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJiningClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("济南市")) {
            log.info("-----------济南公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJinanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("衡水市")) {
            log.info("-----------衡水公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHengshuiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("南充市")) {
            log.info("-----------南充公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundNanchongClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("邢台市")) {
            log.info("-----------邢台公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXingtaiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("德州市")) {
            log.info("-----------德州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDezhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("襄阳市")) {
            log.info("-----------襄阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXiangyangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("濮阳市")) {
            log.info("-----------濮阳公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundPuyangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("凉山彝族自治州")) {
            log.info("-----------凉山公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLiangshanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("丹东市")) {
            log.info("-----------丹东公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDandongClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("呼和浩特市")) {
            log.info("-----------呼和浩特公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHuhehaoteClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("昆明市")) {
            log.info("-----------昆明市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundKunmingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("威海市")) {
            log.info("-----------威海市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWeihaiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("临沂市")) {
            log.info("-----------临沂市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLinyiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("佳木斯市")) {
            log.info("-----------佳木斯市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJiamusiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("牡丹江市")) {
            log.info("-----------牡丹江市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundMudanjiangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("柳州市")) {
            log.info("-----------柳州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLiuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("赤峰市")) {
            log.info("-----------赤峰市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundChifengClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("抚州市")) {
            log.info("-----------抚州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundFuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("酒泉市")) {
            log.info("-----------酒泉市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJiuquanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("随州市")) {
            log.info("-----------随州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundSuizhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("大同市")) {
            log.info("-----------大同市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDatongClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("吉林市")) {
            log.info("-----------吉林市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJilinClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("通化市")) {
            log.info("-----------通化市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTonghuaClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("潍坊市")) {
            log.info("-----------潍坊市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWeifangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("保定市")) {
            log.info("-----------保定市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundBaodingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("齐齐哈尔市")) {
            log.info("-----------齐齐哈尔市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundQiqihaerClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("唐山市")) {
            log.info("-----------唐山市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTangshanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("西安市")) {
            log.info("-----------西安市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXianClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("驻马店市")) {
            log.info("-----------驻马店市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhumadianClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("乐山市")) {
            log.info("-----------乐山市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLeshanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("四平市")) {
            log.info("-----------四平市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundSipingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("呼伦贝尔市")) {
            log.info("-----------呼伦贝尔市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHulunbeierClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("通辽市")) {
            log.info("-----------通辽市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTongliaoClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("钦州市")) {
            log.info("-----------钦州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundQinzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("银川市")) {
            log.info("-----------银川市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYinchuanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("榆林市")) {
            log.info("-----------榆林市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYulinClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("曲靖市")) {
            log.info("-----------曲靖市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundQujingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("梧州市")) {
            log.info("-----------梧州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("台州市")) {
            log.info("-----------台州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundTaizhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("商丘市")) {
            log.info("-----------商丘市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShangqiuClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("红河哈尼族彝族自治州")) {
            log.info("-----------红河哈尼族彝族自治州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHhhnzyzzzqClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("焦作市")) {
            log.info("-----------焦作市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundJiaozuoClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("贵港市")) {
            log.info("-----------贵港市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundGuigangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("赣州市")) {
            log.info("-----------赣州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundGanzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("西宁市")) {
            log.info("-----------西宁市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXiningClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("上饶市")) {
            log.info("-----------上饶市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundShangraoClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("义乌市")) {
            log.info("-----------义乌市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundYiwuClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("大理市")) {
            log.info("-----------大理市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundDaliClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("昭通市")) {
            log.info("-----------昭通市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhaotongClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("枣庄市")) {
            log.info("-----------枣庄市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZaozhuangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("苏州市")) {
            log.info("-----------苏州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundSuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("武汉市")) {
            log.info("-----------武汉市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWuhanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("广州市")) {
            log.info("-----------广州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundGuangzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("淄博市")) {
            log.info("-----------淄博市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZiboClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("文山壮族苗族自治州")) {
            log.info("-----------文山壮族苗族自治州公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundWenshanClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("泸州市")) {
            log.info("-----------泸州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("遂宁市")) {
            log.info("-----------遂宁市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundSuiningClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("新余市")) {
            log.info("-----------新余市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundXinyuClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("肇庆市")) {
            log.info("-----------肇庆市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhaoqingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("肇庆市")) {
            log.info("-----------肇庆市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhaoqingClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("丽水市")) {
            log.info("-----------丽水市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundLishuiClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("湖州市")) {
            log.info("-----------湖州市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundHuzhouClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("湛江市")) {
            log.info("-----------湛江市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhanjiangClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("蚌埠市")) {
            log.info("-----------蚌埠市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundBengbuClient.crawler(messageLoginForHousing);
        }
        else if (city.equals("株洲市")) {
            log.info("-----------株洲市公积金 爬取数据------------" + messageLoginForHousing);
            taskHousingfunda = fundZhuzhouClient.crawler(messageLoginForHousing);
        }
        System.out.println("taskHousingfunda is:"+taskHousingfunda);

        log.info("-----------taskHousingfunda------------" + taskHousingfunda);
        return  taskHousingfunda;
    }

    @PostMapping(value = "/getcode")
    public @ResponseBody
    TaskHousingfund getcode(Model model,MessageLoginForHousing messageLoginForHousing) {
        TaskHousingfund taskHousingfunda = new TaskHousingfund();
        String city = messageLoginForHousing.getCity();
        if (city.equals("温州市")) {
            log.info("-----------温州公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundWenzhouClient.getcode(messageLoginForHousing);
        }
        if (city.equals("武汉市")) {
            log.info("-----------武汉公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundWuhanClient.getcode(messageLoginForHousing);
        }


        System.out.println("taskHousingfunda is:"+taskHousingfunda);

        log.info("-----------taskHousingfunda------------" + taskHousingfunda);
        return  taskHousingfunda;
    }


    @PostMapping(value = "/setcode")
    public @ResponseBody
    TaskHousingfund setcode(Model model,MessageLoginForHousing messageLoginForHousing) {
        TaskHousingfund taskHousingfunda = new TaskHousingfund();
        String city = messageLoginForHousing.getCity();
        if (city.equals("温州市")) {
            log.info("-----------温州公积金 验证 短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundWenzhouClient.setcode(messageLoginForHousing);

        }
        if (city.equals("潍坊市")) {
            log.info("----------潍坊公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundWeifangClient.setcode(messageLoginForHousing);
        }
        if (city.equals("唐山市")) {
            log.info("----------唐山公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundTangshanClient.setcode(messageLoginForHousing);
        }
        if (city.equals("乐山市")) {
            log.info("----------乐山公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundLeshanClient.setcode(messageLoginForHousing);
        }
        if (city.equals("银川市")) {
            log.info("----------银川公积金 获取短信验证码------------" + messageLoginForHousing);
            taskHousingfunda = fundYinchuanClient.setcode(messageLoginForHousing);
        }

        System.out.println("taskHousingfunda is:"+taskHousingfunda);

        log.info("-----------taskHousingfunda------------" + taskHousingfunda);
        return  taskHousingfunda;
    }

}
