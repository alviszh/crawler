package app.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.client.InsuranceETLClient;
import app.client.InsuranceTaskClient;
import app.entity.insuranceETL.WebData;
import app.utils.JsonArrayUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Controller
@RequestMapping("/data/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceETLClient insuranceETLClient;
    @Autowired
    private InsuranceTaskClient insuranceTaskClient;
    @Autowired
    private JsonArrayUtil jsonArrayUtil;
    
    /**
     * 进入任务列表页
     * @param model
     * @return
     */
    @RequestMapping("/toInsuranceTask")
    public String toInsuranceTask(Model model){
        model.addAttribute("module", 2);
        /*PageInfo<TaskMobile> pages = mobileTaskClient.getTaskMobilePages(1, 5);
        System.out.println("******"+pages);
        System.out.println("******"+pages.getContent().size());*/
        return "data/insurance/insurancetasklist";
    }
    
    /**
     * 分页查询社保任务列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/tasks/getInsuranceTaskPages", method= RequestMethod.POST)
    public @ResponseBody
    PageInfo<TaskInsurance> getTaskPages(@RequestParam(value = "currentPage") int currentPage,
                                   @RequestParam(value = "pageSize") int pageSize,
                                   @RequestParam(value = "taskid") String taskid) {
        currentPage--; //Page的页数从0开始
        PageInfo<TaskInsurance> tasksPage = insuranceTaskClient.getTaskInsurancePages(currentPage, pageSize, taskid);
        
//        System.out.println("******getTaskPages:"+tasksPage);
        return tasksPage;
    }
    
    /**
     * 进入数据详情页
     * @return
     */
    @RequestMapping(value = "/toInsuranceTaskDetail", method = RequestMethod.POST)
    public String insuranceinfo(Model model,@RequestParam(value = "taskidSearch") String taskid){
        WebData datas = insuranceETLClient.insuranceInfo(taskid, null);//查询社保的数据详情（ETL）
        model.addAttribute("taskid",taskid);
        model.addAttribute("datas", datas);
        return "data/insurance/insurancetaskdetail";
    }
    
    
    /**
     * 进入统计图标页
     * @param model
     * @return
     */
    @RequestMapping("/toInsuranceTaskChart")
    public String toInsuranceTaskChart(Model model){
        model.addAttribute("module", 2);
        return "data/insurance/insurancetaskchart";
    }

    /**
     * 获取加载图表的数据（线性）
     * 根据创建时间获取社保的调用量
     * @return
     */
    @RequestMapping(value = "/lineData" , method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody String lineData(){
//        System.out.println("加载图表数据++++++++++++++++++++++++");
        List insuranceTaskStatistics = insuranceTaskClient.lineData();
//        System.out.println(insuranceTaskStatistics);

        if (insuranceTaskStatistics != null) {
            int size = insuranceTaskStatistics.size();
            JSONObject jsonObject = new JSONObject();
            Integer[] amount = new Integer[size];
            String[] time = new String[size];
            for (int i = 0; i < size; i++) {
                List obj = (List) insuranceTaskStatistics.get(i);
                int am = (int) obj.get(0);//调用量
                String str = obj.get(1) + ""; //task创建日期
                if (str != null && !"".equals(str)) {
                    amount[i] = am;
                    time[i] = str;
                }
            }
            jsonObject.put("amount", jsonArrayUtil.arrayToJsonArray(amount));
            jsonObject.put("time", jsonArrayUtil.arrayToJsonArray(time));
            System.out.println("=====" + jsonObject);
            return jsonObject.toString();
        } else {
            return null;
        }
    }

    /**
     * 获取加载图表的数据（饼图）
     * 根据创建时间获取的调用量
     * @return
     */
    @RequestMapping(value = "/pieData" , method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody String pieData(){
        System.out.println("加载图表数据++++++++++++++pie++++++++++");
        List insuranceTaskStatistics = insuranceTaskClient.pieData();
        System.out.println(insuranceTaskStatistics);

        if (insuranceTaskStatistics != null) {
            int size = insuranceTaskStatistics.size();
            JSONObject jsonObject = new JSONObject();
            Integer[] amount = new Integer[size];
            String[] city = new String[size];
            for (int i = 0; i < size; i++) {
                List obj = (List) insuranceTaskStatistics.get(i);
                int am = (int) obj.get(0);//调用量
                String str = obj.get(1) + ""; //task创建日期
                if (str != null && !"".equals(str)) {
                    amount[i] = am;
                    city[i] = str;
                }
            }
            jsonObject.put("amount", jsonArrayUtil.arrayToJsonArray(amount));
            jsonObject.put("city", jsonArrayUtil.arrayToJsonArray(city));
            System.out.println("=====" + jsonObject);
            return jsonObject.toString();
        } else {
            return null;
        }
    }
}
