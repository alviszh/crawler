package app.controller;

import com.crawler.insurance.json.TaskInsurance;
import app.client.insur.*;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/h5/insur")
public class InsuranceCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(InsuranceCrawlerController.class);

    @Autowired
    private InsurShijiazhuangClient insurShijiazhuangClient;
    @Autowired
    private InsurBeiJingClient insurBeiJingClient;
    @Autowired
    private InsurShanghaiClient insurShanghaiClient;
    @Autowired
    private InsurGuangzhouClient insurGuangzhouClient;
    @Autowired
    private InsurShenzhenClient insurShenzhenClient;
    @Autowired
    private InsurFoshanClient insurFoshanClient;
    @Autowired
    private InsurNingboClient insurNingboClient;
    @Autowired
    private InsurChengduClient insurChengduClient;
    @Autowired
    private InsurYantaiClient insurYantaiClient;
    @Autowired
    private InsurQingdaoClient insurQingdaoClient;
    @Autowired
    private InsurZiboClient insurZiboClient;
    @Autowired
    private InsurTianjinClient insurTianjinClient;
    @Autowired
    private InsurDongguanClient insurDongguanClient;
    @Autowired
    private InsurChongqingClient insurChongqingClient;
    @Autowired
    private InsurChangchunClient insurChangchunClient;
    @Autowired
    private InsurSuzhouClient insurSuzhouClient;
    @Autowired
    private InsurShenyangClient insurShenyangClient;
    @Autowired
    private InsurXiamenClient insurXiamenClient;
    @Autowired
    private InsurHaerbinClient insurHaerbinClient;
    @Autowired
    private InsurNanjingClient insurNanjingClient;
    @Autowired
    private InsurWenzhouClient insurWenzhouClient;
    @Autowired
    private InsurZhengzhouClient insurZhengzhouClient;
    @Autowired
    private InsurDaqingClient insurDaqingClient;
    @Autowired
    private InsurNanchangClient insurNanchangClient;
    @Autowired
    private InsurTaiyuanClient insurTaiyuanClient;
    @Autowired
    private InsurWuxiClient insurWuxiClient;
    @Autowired
    private InsurQuanzhouClient insurQuanzhouClient;
    @Autowired
    private InsurGuiyangClient insurGuiyangClient;
    @Autowired
    private InsurHunanClient insurHunanClient;
    @Autowired
    private InsurHefeiClient insurHefeiClient;
    @Autowired
    private InsurWuhanClient insurWuhanClient;
    @Autowired
    private InsurHangzhouClient insurHangzhouClient;
    @Autowired
    private InsurXuzhouClient insurXuzhouClient;
    @Autowired
    private InsurSzJilinClient insurSzJilinClient;
    @Autowired
    private InsurHengshuiClient insurHengshuiClient;
    @Autowired
    private InsurTielingClient insurTielingClient;
    @Autowired
    private InsurJiaozuoClient insurJiaozuoClient;
    @Autowired
    private InsurJiyuanClient insurJiyuanClient;
    @Autowired
    private InsurLuoyangClient insurLuoyangClient;
    @Autowired
    private InsurAnshanClient insurAnshanClient;
    @Autowired
    private InsurDalianClient insurDalianClient;
    @Autowired
    private InsurLiuzhouClient insurLiuzhouClient;
    @Autowired
    private InsurWeihaiClient insurWeihaiClient;
    @Autowired
    private InsurTaianClient insurTaianClient;
    @Autowired
    private InsurBinzhouClient insurBinzhouClient;
    @Autowired
    private InsurPanjinClient insurPanjinClient;
    @Autowired
    private InsurDezhouClient insurDezhouClient;
    @Autowired
    private InsurJiningClient insurJiningClient;
    @Autowired
    private InsurWeifangClient insurWeifangClient;
    @Autowired
    private InsurLinyiClient insurLinyiClient;
    @Autowired
    private InsurLanzhouClient insurLanzhouClient;
    @Autowired
    private InsurChangzhouClient insurChangzhouClient;
    @Autowired
    private InsurSuqianClient insurSuqianClient;
    @Autowired
    private InsurShangqiuClient insurShangqiuClient;
    @Autowired
    private InsurZhenjiangClient insurZhenjiangClient;
    @Autowired
    private InsurRizhaoClient insurRizhaoClient;
    @Autowired
    private InsurSzHubeiClient insurSzHubeiClient;
    @Autowired
    private InsurGuilinClient insurGuilinClient;
    @Autowired
    private InsurLiaochengClient insurLiaochengClient;
    @Autowired
    private InsurNeimengguClient insurNeimengguClient;
    @Autowired
    private InsurShanxiClient insurShanxiClient;
    @Autowired
    private InsurShanxi3Client insurShanxi3Client;
    @Autowired
    private InsurJinanClient insurJinanClient;
    @Autowired
    private InsurHeilongjiangClient insurHeilongjiangClient;
    @Autowired
    private InsurSichuanClient insurSichuanClient;
    @Autowired
    private InsurXinjiangClient insurXinjiangClient;
    @Autowired
    private InsurYunnanClient insurYunnanClient;
    @Autowired
    private InsurZhejiangClient insurZhejiangClient;
    @Autowired
    private InsurTaizhouClient insurTaizhouClient;
    @Autowired
    private InsurGuigangClient insurGuigangClient;
    @Autowired
    private InsurLuzhouClient insurLuzhouClient;
    @Autowired
    private InsurMianyangClient insurMianyangClient;
    @Autowired
    private InsurNanningClient insurNanningClient;
    @Autowired
    private InsurYulinClient insurYulinClient;
    @Autowired
    private InsurLiangshanyizuClient insurLiangshanyizuClient;
    @Autowired
    private InsurShaoxingClient insurShaoxingClient;
    @Autowired
    private InsurHuaianClient insurHuaianClient;
    @Autowired
    private InsurYangzhouClient insurYangzhouClient;
    @Autowired
    private InsurHuzhouClient insurHuzhouClient;
    @Autowired
    private InsurZhuhaiClient insurZhuhaiClient;
    @Autowired
    private InsurFujianClient insurFujianClient;
    @Autowired
    private InsurHaikouClient insurHaikouClient;
    @Autowired
    private InsurBengbuClient insurBengbuClient;
    @Autowired
    private InsurZhanjiangClient insurZhanjiangClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    TaskInsurance login(Model model,InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurancea = new TaskInsurance();
        String city = insuranceRequestParameters.getCity();
        if (city.equals("上海市")) {
            log.info("-----------上海社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShanghaiClient.login(insuranceRequestParameters);

        } else if (city.equals("深圳市")) {
            log.info("-----------深圳社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShenzhenClient.login(insuranceRequestParameters);

        } else if (city.equals("广州市")) {
            log.info("-----------广州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurGuangzhouClient.login(insuranceRequestParameters);

        } else if (city.equals("石家庄市")) {
            log.info("-----------石家庄社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShijiazhuangClient.login(insuranceRequestParameters);
        } else if (city.equals("北京市")) {
            log.info("-----------北京社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurBeiJingClient.login(insuranceRequestParameters);
        } else if (city.equals("佛山市")) {
            log.info("-----------佛山社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurFoshanClient.login(insuranceRequestParameters);
        } else if (city.equals("宁波市")) {
            log.info("-----------宁波社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurNingboClient.login(insuranceRequestParameters);//登录、爬虫总接口
        } else if (city.equals("成都市")) {
            log.info("-----------成都社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurChengduClient.login(insuranceRequestParameters);
        } else if (city.equals("烟台市")) {
            log.info("-----------烟台社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurYantaiClient.login(insuranceRequestParameters);
        } else if (city.equals("青岛市")) {
            log.info("-----------青岛社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurQingdaoClient.login(insuranceRequestParameters);
        } else if (city.equals("淄博市")) {
            log.info("-----------淄博社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurZiboClient.login(insuranceRequestParameters);
        } else if (city.equals("天津市")) {
            log.info("-----------天津社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTianjinClient.login(insuranceRequestParameters);
        } else if (city.equals("东莞市")) {
            log.info("-----------东莞社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurDongguanClient.login(insuranceRequestParameters);
        } else if (city.equals("重庆市")) {
            log.info("-----------重庆社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurChongqingClient.login(insuranceRequestParameters);
        } else if (city.equals("长春市")) {
            log.info("-----------长春社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurChangchunClient.login(insuranceRequestParameters);
        } else if (city.equals("苏州市")) {
            log.info("-----------苏州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurSuzhouClient.login(insuranceRequestParameters);
        } else if (city.equals("沈阳市")) {
            log.info("-----------沈阳社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShenyangClient.login(insuranceRequestParameters);
        } else if (city.equals("厦门市")) {
            log.info("-----------厦门社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurXiamenClient.login(insuranceRequestParameters);
        } else if (city.equals("哈尔滨市")) {
            log.info("-----------哈尔滨社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHaerbinClient.login(insuranceRequestParameters);
        } else if (city.equals("南京市")) {
            log.info("-----------南京社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurNanjingClient.login(insuranceRequestParameters);
        }
        else if (city.equals("温州市")) {
            log.info("-----------温州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurWenzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("郑州市")) {
            log.info("-----------郑州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurZhengzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("大庆市")) {
            log.info("-----------大庆社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurDaqingClient.login(insuranceRequestParameters);
        }
        else if (city.equals("南昌市")) {
            log.info("-----------南昌社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurNanchangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("太原市")) {
            log.info("-----------太原社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTaiyuanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("无锡市")) {
            log.info("-----------无锡社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurWuxiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("泉州市")) {
            log.info("-----------泉州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurQuanzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("贵阳市")) {
            log.info("-----------贵阳社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurGuiyangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("湖南省")) {
            log.info("-----------湖南社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHunanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("合肥市")) {
            log.info("-----------合肥社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHefeiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("武汉市")) {
            log.info("-----------武汉社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurWuhanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("杭州市")) {
            log.info("-----------杭州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHangzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("徐州市")) {
            log.info("-----------徐州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurXuzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("徐州市")) {
            log.info("-----------徐州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurXuzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("吉林省")) {
            log.info("-----------吉林社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurSzJilinClient.login(insuranceRequestParameters);
        }
        else if (city.equals("衡水市")) {
            log.info("-----------衡水社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHengshuiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("铁岭市")) {
            log.info("-----------铁岭社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTielingClient.login(insuranceRequestParameters);
        }
        else if (city.equals("焦作市")) {
            log.info("-----------焦作社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurJiaozuoClient.login(insuranceRequestParameters);
        }
        else if (city.equals("济源市")) {
            log.info("-----------济源社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurJiyuanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("洛阳市")) {
            log.info("-----------洛阳社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLuoyangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("鞍山市")) {
            log.info("-----------鞍山社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurAnshanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("大连市")) {
            log.info("-----------大连社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurDalianClient.login(insuranceRequestParameters);
        }
        else if (city.equals("柳州市")) {
            log.info("-----------柳州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLiuzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("威海市")) {
            log.info("-----------威海社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurWeihaiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("泰安市")) {
            log.info("-----------泰安社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTaianClient.login(insuranceRequestParameters);
        }
        else if (city.equals("滨州市")) {
            log.info("-----------滨州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurBinzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("盘锦市")) {
            log.info("-----------盘锦社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurPanjinClient.login(insuranceRequestParameters);
        }
        else if (city.equals("德州市")) {
            log.info("-----------德州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurDezhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("济宁市")) {
            log.info("-----------济宁社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurJiningClient.login(insuranceRequestParameters);
        }
        else if (city.equals("潍坊市")) {
            log.info("-----------潍坊社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurWeifangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("临沂市")) {
            log.info("-----------临沂社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLinyiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("兰州市")) {
            log.info("-----------兰州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLanzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("常州市")) {
            log.info("-----------常州社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurChangzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("宿迁市")) {
            log.info("-----------宿迁社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurSuqianClient.login(insuranceRequestParameters);
        }
        else if (city.equals("商丘市")) {
            log.info("-----------商丘社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShangqiuClient.login(insuranceRequestParameters);
        }
        else if (city.equals("镇江市")) {
            log.info("-----------镇江社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurZhenjiangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("日照市")) {
            log.info("-----------日照社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurRizhaoClient.login(insuranceRequestParameters);
        }
        else if (city.equals("湖北省")) {
            log.info("-----------湖北省社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurSzHubeiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("桂林市")) {
            log.info("-----------桂林市社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurGuilinClient.login(insuranceRequestParameters);
        }
        else if (city.equals("聊城市")) {
            log.info("-----------聊城市社保 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLiaochengClient.login(insuranceRequestParameters);
        }
        else if (city.equals("内蒙古自治区")) {
            log.info("-----------内蒙古自治区 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurNeimengguClient.login(insuranceRequestParameters);
        }
        else if (city.equals("山西省")) {
            log.info("-----------山西省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShanxiClient.login(insuranceRequestParameters);
        }
        else if (city.equals("陕西省")) {
            log.info("-----------陕西省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShanxi3Client.login(insuranceRequestParameters);
        }
        else if (city.equals("济南市")) {
            log.info("-----------济南市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurJinanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("黑龙江省")) {
            log.info("-----------黑龙江省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHeilongjiangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("云南省")) {
            log.info("-----------云南省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurYunnanClient.login(insuranceRequestParameters);
        }
        else if (city.equals("浙江省")) {
            log.info("-----------浙江省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurZhejiangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("泰州市")) {
            log.info("-----------泰州市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTaizhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("贵港市")) {
            log.info("-----------贵港市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurGuigangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("泸州市")) {
            log.info("-----------泸州市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLuzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("绵阳市")) {
            log.info("-----------绵阳市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurMianyangClient.login(insuranceRequestParameters);
        }
        else if (city.equals("南宁市")) {
            log.info("-----------南宁市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurNanningClient.login(insuranceRequestParameters);
        }
        else if (city.equals("玉林市")) {
            log.info("-----------玉林市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurYulinClient.login(insuranceRequestParameters);
        }
        else if (city.equals("凉山彝族市")) {
            log.info("-----------凉山彝族自治州 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurLiangshanyizuClient.login(insuranceRequestParameters);
        }
        else if (city.equals("绍兴市")) {
            log.info("-----------绍兴市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurShaoxingClient.login(insuranceRequestParameters);
        }
        else if (city.equals("淮安市")) {
            log.info("-----------淮安市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHuaianClient.login(insuranceRequestParameters);
        }
        else if (city.equals("扬州市")) {
            log.info("-----------扬州市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurYangzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("湖州市")) {
            log.info("-----------湖州市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHuzhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("泰州市")) {
            log.info("-----------泰州市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurTaizhouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("福建省")) {
            log.info("-----------福建省 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurFujianClient.login(insuranceRequestParameters);
        }
        else if (city.equals("海口市")) {
            log.info("-----------海口市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHaikouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("海口市")) {
            log.info("-----------海口市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurHaikouClient.login(insuranceRequestParameters);
        }
        else if (city.equals("蚌埠市")) {
            log.info("-----------蚌埠市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurBengbuClient.login(insuranceRequestParameters);
        }
        else if (city.equals("湛江市")) {
            log.info("-----------湛江市 登录------------" + insuranceRequestParameters);
            taskInsurancea = insurZhanjiangClient.login(insuranceRequestParameters);
        }

        log.info("-----------taskInsurancea------------" + taskInsurancea);
        return  taskInsurancea;
    }

    @PostMapping(value = "/crawler")
    public @ResponseBody
    TaskInsurance crawler(Model model,InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurancea = new TaskInsurance();
        String city = insuranceRequestParameters.getCity();
        if (city.equals("上海市")) {
            log.info("-----------上海社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShanghaiClient.crawler(insuranceRequestParameters);

        } else if (city.equals("深圳市")) {
            log.info("-----------深圳社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShenzhenClient.crawler(insuranceRequestParameters);

        } else if (city.equals("广州市")) {
            log.info("-----------广州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurGuangzhouClient.crawler(insuranceRequestParameters);

        } else if (city.equals("石家庄市")) {
            log.info("-----------石家庄社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShijiazhuangClient.crawler(insuranceRequestParameters);
        } else if (city.equals("北京市")) {
            log.info("-----------北京社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurBeiJingClient.crawler(insuranceRequestParameters);
        } else if (city.equals("佛山市")) {
            log.info("-----------佛山社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurFoshanClient.crawler(insuranceRequestParameters);
        } else if (city.equals("成都市")) {
            log.info("-----------成都社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurChengduClient.crawler(insuranceRequestParameters);
        } else if (city.equals("烟台市")) {
            log.info("-----------烟台社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurYantaiClient.crawler(insuranceRequestParameters);
        } else if (city.equals("青岛市")) {
            log.info("-----------青岛社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurQingdaoClient.crawler(insuranceRequestParameters);
        } else if (city.equals("淄博市")) {
            log.info("-----------淄博社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurZiboClient.crawler(insuranceRequestParameters);
        } else if (city.equals("天津市")) {
            log.info("-----------天津社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurTianjinClient.crawler(insuranceRequestParameters);
        } else if (city.equals("东莞市")) {
            log.info("-----------东莞社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurDongguanClient.crawler(insuranceRequestParameters);
        } else if (city.equals("重庆市")) {
            log.info("-----------重庆社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurChongqingClient.crawler(insuranceRequestParameters);
        } else if (city.equals("长春市")) {
            log.info("-----------长春社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurChangchunClient.crawler(insuranceRequestParameters);
        } else if (city.equals("苏州市")) {
            log.info("-----------苏州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurSuzhouClient.crawler(insuranceRequestParameters);
        } else if (city.equals("沈阳市")) {
            log.info("-----------沈阳社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShenyangClient.crawler(insuranceRequestParameters);
        } else if (city.equals("厦门市")) {
            log.info("-----------厦门社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurXiamenClient.crawler(insuranceRequestParameters);
        } else if (city.equals("哈尔滨市")) {
            log.info("-----------哈尔滨社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHaerbinClient.crawler(insuranceRequestParameters);
        } else if (city.equals("南京市")) {
            log.info("-----------南京社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurNanjingClient.crawler(insuranceRequestParameters);
        } else if (city.equals("温州市")) {
            log.info("-----------温州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurWenzhouClient.crawler(insuranceRequestParameters);
        } else if (city.equals("郑州市")) {
            log.info("-----------郑州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurZhengzhouClient.crawler(insuranceRequestParameters);
        } else if (city.equals("大庆市")) {
            log.info("-----------大庆社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurDaqingClient.crawler(insuranceRequestParameters);
        } else if (city.equals("南昌市")) {
            log.info("-----------南昌社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurNanchangClient.crawler(insuranceRequestParameters);
        } else if (city.equals("太原市")) {
            log.info("-----------太原社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurTaiyuanClient.crawler(insuranceRequestParameters);
        } else if (city.equals("无锡市")) {
            log.info("-----------无锡社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurWuxiClient.crawler(insuranceRequestParameters);
        } else if (city.equals("泉州市")) {
            log.info("-----------泉州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurQuanzhouClient.crawler(insuranceRequestParameters);
        } else if (city.equals("贵阳市")) {
            log.info("-----------贵阳社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurGuiyangClient.crawler(insuranceRequestParameters);
        } else if (city.equals("湖南省")) {
            log.info("-----------湖南社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHunanClient.crawler(insuranceRequestParameters);
        } else if (city.equals("合肥市")) {
            log.info("-----------合肥社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHefeiClient.crawler(insuranceRequestParameters);
        }else if (city.equals("武汉市")) {
            log.info("-----------武汉社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurWuhanClient.crawler(insuranceRequestParameters);
        }else if (city.equals("杭州市")) {
            log.info("-----------杭州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHangzhouClient.crawler(insuranceRequestParameters);
        }else if (city.equals("徐州市")) {
            log.info("-----------杭州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurXuzhouClient.crawler(insuranceRequestParameters);
        }else if (city.equals("宁波市")) {
            log.info("-----------宁波社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurNingboClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("吉林省")) {
            log.info("-----------吉林社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurSzJilinClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("衡水市")) {
            log.info("-----------衡水社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHengshuiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("铁岭市")) {
            log.info("-----------铁岭社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurTielingClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("焦作市")) {
            log.info("-----------焦作社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurJiaozuoClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("济源市")) {
            log.info("-----------济源社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurJiyuanClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("洛阳市")) {
            log.info("-----------洛阳社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurLuoyangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("威海市")) {
            log.info("-----------威海社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurWeihaiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("滨州市")) {
            log.info("-----------滨州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurBinzhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("盘锦市")) {
            log.info("-----------盘锦社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurPanjinClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("德州市")) {
            log.info("-----------德州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurDezhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("济宁市")) {
            log.info("-----------济宁社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurJiningClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("潍坊市")) {
            log.info("-----------潍坊社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurWeifangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("临沂市")) {
            log.info("-----------临沂社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurLinyiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("兰州市")) {
            log.info("-----------兰州社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurLanzhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("商丘市")) {
            log.info("-----------商丘社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShangqiuClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("镇江市")) {
            log.info("-----------镇江社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurZhenjiangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("日照市")) {
            log.info("-----------日照社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurRizhaoClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("湖北省")) {
            log.info("-----------湖北省社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurSzHubeiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("桂林市")) {
            log.info("-----------桂林市社保 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurGuilinClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("内蒙古自治区")) {
            log.info("-----------内蒙古自治区 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurNeimengguClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("山西省")) {
            log.info("-----------山西省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShanxiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("黑龙江省")) {
            log.info("-----------黑龙江省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHeilongjiangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("四川省")) {
            log.info("-----------四川省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurSichuanClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("新疆维吾尔自治区")) {
            log.info("-----------新疆 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurXinjiangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("云南省")) {
            log.info("-----------云南省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurYunnanClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("云南省")) {
            log.info("-----------云南省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurYunnanClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("浙江省")) {
            log.info("-----------浙江省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurZhejiangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("泰州市")) {
            log.info("-----------泰州市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurTaizhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("贵港市")) {
            log.info("-----------贵港市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurGuigangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("泸州市")) {
            log.info("-----------泸州市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurLuzhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("绵阳市")) {
            log.info("-----------绵阳市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurMianyangClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("南宁市")) {
            log.info("-----------南宁市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurNanningClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("玉林市")) {
            log.info("-----------玉林市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurYulinClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("凉山彝族自治州")) {
            log.info("-----------凉山彝族自治州 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurLiangshanyizuClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("绍兴市")) {
            log.info("-----------绍兴市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurShaoxingClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("淮安市")) {
            log.info("-----------淮安市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHuaianClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("扬州市")) {
            log.info("-----------扬州市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurYangzhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("湖州市")) {
            log.info("-----------湖州市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHuzhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("泰州市")) {
            log.info("-----------泰州市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurTaizhouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("珠海市")) {
            log.info("-----------珠海市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurZhuhaiClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("福建省")) {
            log.info("-----------福建省 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurFujianClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("海口市")) {
            log.info("-----------海口市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurHaikouClient.crawler(insuranceRequestParameters);
        }
        else if (city.equals("蚌埠市")) {
            log.info("-----------蚌埠市 爬取数据------------" + insuranceRequestParameters);
            taskInsurancea = insurBengbuClient.crawler(insuranceRequestParameters);
        }

        log.info("-----------taskInsurancea------------" + taskInsurancea);
        return  taskInsurancea;
    }

    @PostMapping(value = "/verifySMS")
    public @ResponseBody
    TaskInsurance verifySMS(Model model,InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurancea = new TaskInsurance();
        String city = insuranceRequestParameters.getCity();
        if (city.equals("滨州市")) {
            taskInsurancea = insurBinzhouClient.verifySMS(insuranceRequestParameters);
        }
        else if (city.equals("德州市")) {
            taskInsurancea = insurDezhouClient.verifySMS(insuranceRequestParameters);
        }
        else if (city.equals("济宁市")) {
            taskInsurancea = insurJiningClient.verifySMS(insuranceRequestParameters);
        }
        return taskInsurancea;
    }

    /**
     * 获取图片验证码（烟台市）
     * @return
     */
    @PostMapping(value = "/imgCode")
    public @ResponseBody
    List<InsuranceRequestParameters> imgCode(InsuranceRequestParameters insuranceRequestParameters){
        log.info("===========InsuranceYantaiImageInfo==================" + insuranceRequestParameters);
        List<InsuranceRequestParameters>  result = insurYantaiClient.verificationCode(insuranceRequestParameters);
        for (InsuranceRequestParameters insur : result) {
            log.info("================烟台图片验证码=========" + insur.getBase64());
        }
        log.info("================InsuranceRequestParameters========="+result.toString());
        return result;
    }
}
