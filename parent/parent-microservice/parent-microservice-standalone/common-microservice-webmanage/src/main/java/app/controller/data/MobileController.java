	package app.controller.data;

import app.client.MobileETLClient;
import app.client.MobileTaskClient;
import app.entity.mobileETL.WebData;
import app.enums.CbConfModule;
import app.service.TaskService;
import app.utils.JsonArrayUtil;
import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/data/mobile")
public class MobileController {

    @Autowired
    private MobileETLClient mobileETLClient;
    @Autowired
    private MobileTaskClient mobileTaskClient;
    @Autowired
    private JsonArrayUtil jsonArrayUtil;


    /**
     * 进入任务列表页
     * @param model
     * @return
     */
    @RequestMapping("/toMobileTask")
    public String toMobileTask(Model model){
        model.addAttribute("module", 1);
        /*PageInfo<TaskMobile> pages = mobileTaskClient.getTaskMobilePages(1, 5);
        System.out.println("******"+pages);
        System.out.println("******"+pages.getContent().size());*/
        return "data/mobile/mobiletasklist";
    }

    /**
     * 分页查询运营商任务列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/tasks/getTaskPages", method= RequestMethod.POST)
    public @ResponseBody
    PageInfo<TaskMobile> getTaskPages(@RequestParam(value = "currentPage") int currentPage,
                                   @RequestParam(value = "pageSize") int pageSize,
                                   @RequestParam(value = "taskid") String taskid,
                                    @RequestParam(value = "mobilenum") String mobilenum) {
        currentPage--; //Page的页数从0开始
        PageInfo<TaskMobile> tasksPage = mobileTaskClient.getTaskMobilePages(currentPage, pageSize, taskid, mobilenum);

        /*//根据条件查询
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("taskid",taskid);
        paramMap.put("mobilenum",mobilenum);

        Page<TaskMobile> tasksPage = taskService.getMobileTaskByParams(paramMap, currentPage, pageSize);*/
        System.out.println("******getTaskPages:"+tasksPage);
        return tasksPage;
    }

    /**
     * 进入数据详情页
     * @return
     */
    @RequestMapping(value = "/toMobileTaskDetail", method = RequestMethod.POST)
    public String mobileinfo(Model model,@RequestParam(value = "taskidSearch") String taskid){
        WebData datas = mobileETLClient.mobileInfo(taskid, null);//查询运营商的数据详情（ETL）
        model.addAttribute("taskid",taskid);
        model.addAttribute("datas", datas);
        return "data/mobile/mobiletaskdetail";
    }

    /**
     * 查询运营商的数据详情（ETL）
     * @param taskid
     * @param mobilenum
     * @return
     */
    /*@RequestMapping(value = "/mobileTaskDetail" , method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody WebData mobileInfo(@RequestParam(name = "taskid") String taskid,
                             @RequestParam(name = "mobilenum",required = false) String mobilenum){
        WebData mobileInfo = mobileETLClient.mobileInfo(taskid, mobilenum);
        System.out.println("++++++++++++++++++++++++");
        System.out.println(mobileInfo);
        return mobileInfo;
    }*/


    /**
     * 进入统计图标页
     * @param model
     * @return
     */
    @RequestMapping("/toMobileTaskChart")
    public String toMobileTaskChart(Model model){
        model.addAttribute("module", 1);
        return "data/mobile/mobiletaskchart";
    }

    /**
     * 获取加载图表的数据（线性）
     * 根据创建时间获取运营商的调用量
     * @return
     */
    @RequestMapping(value = "/lineData" , method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody String lineData(){
        System.out.println("加载图表数据++++++++++++++++++++++++");
        List mobileTaskStatistics = mobileTaskClient.lineData();
        System.out.println(mobileTaskStatistics);

        if (mobileTaskStatistics != null) {
            int size = mobileTaskStatistics.size();
            JSONObject jsonObject = new JSONObject();
            Integer[] amount = new Integer[size];
            String[] time = new String[size];
            for (int i = 0; i < size; i++) {
                List obj = (List) mobileTaskStatistics.get(i);
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
     * 根据创建时间获取运营商的调用量
     * @return
     */
    @RequestMapping(value = "/pieData" , method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody String pieData(){
        System.out.println("加载图表数据++++++++++++++++++++++++饼图");
        List result = mobileTaskClient.pieData();
        System.out.println("result="+result);

        if (result != null) {
            int size = result.size();
            List<Map<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                List obj = (List) result.get(i);
                int am = (int) obj.get(0);//调用量
                String str = (String) obj.get(1);//调用量
//                Long am = (Long) obj[0];//调用量
//                String str = obj[1] + ""; //运营商
                if (str.equals("CMCC")) {
                    str = "中国移动";
                } else if (str.equals("UNICOM")) {
                    str = "中国联通";
                } else if (str.equals("CHINA_TELECOM")) {
                    str = "中国电信";
                }

                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("name", str);
                dataMap.put("y", am);
                data.add(dataMap);
            }
            JSONArray jsonArray = jsonArrayUtil.listMapToJsonArray(data);
            System.out.println("==pie===" + jsonArray.toString());
            return jsonArray.toString();
        } else {
            return null;
        }
    }

}