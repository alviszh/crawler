package app.service;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.baoshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.baoshan"})
public class HousingFundBaoShanService extends HousingBasicService implements ICrawlerLogin {

    @Autowired
    private HousingFundBaoShanCrawlerService housingFundBaoShanCrawlerService;

    private String loginUrl = "https://www.bsgjj.com/bswsyyt/index.jsp";
    private String vericodeUrl = "https://www.bsgjj.com/bswsyyt/vericode.jsp";
    private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器


    @Override
    public TaskHousing login(MessageLoginForHousing messageLoginForHousing){
        TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
        try {
            WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
            WebClient webClient = WebCrawler.getInstance().getWebClient();
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage hPage = webClient.getPage(webRequest);
            if(null!=hPage){
                //此处请求图片验证码链接
                webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
                Page page = webClient.getPage(webRequest);
                String imagePath = getImagePath(page);
                System.out.println("imagePath:"+ imagePath);
                tracer.addTag("图片验证码路径", "imagePath:"+imagePath);
                String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
                HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']");
                HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']");
                loginPassword.setText(messageLoginForHousing.getPassword().trim());
                HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']");
                HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']");
                loginName.setText(messageLoginForHousing.getNum().trim());
                validateCode.setText(code);
                HtmlPage logonPage= submitbt.click();
                if(null!=logonPage){
                    String html=logonPage.asXml();
                    tracer.addTag("模拟点击登陆后获取的页面是", html);
                    if(html.contains("个人基本信息查询")){
                        tracer.qryKeyValue("登录成功，欢迎来到首页面", taskHousing.getTaskid());
                        System.out.println("登录成功，欢迎来到首页面");
                        String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
                        System.out.println("存储起来的cookie是："+cookieString);
                        taskHousing.setCookies(cookieString);
                        taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
                        taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
                        taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
                        taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getCode());
                        taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getMessage());
                        taskHousing.setLogintype(messageLoginForHousing.getLogintype());
                        save(taskHousing);
                        Thread.sleep(1000);
                    }else{
                        Document doc = Jsoup.parse(html);
                        String errorMsg= doc.getElementsByClass("text").get(0).text();  //获取页面中可能出现的错误信息
                        tracer.addTag("登录失败时，跳转页面后，页面上的提示信息是："+errorMsg, taskHousing.getTaskid());
                        System.out.println("获取的错误信息是："+errorMsg);
                        //通过调研，发现图片验证码输入错误，这几个公积金网站的提示都是一样的
                        if(errorMsg.contains("您输入的验证码与图片不符")){
                            captchaErrorCount++;
                            tracer.addTag("action.login.auth.imageErrorCount", "这是第"+captchaErrorCount+"次因图片验证码识别错误重新调用登录方法");
                            //图片验证码识别错误，重试三次登录
                            if(captchaErrorCount>3){
                                tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+captchaErrorCount, taskHousing.getTaskid());
                                System.out.println("操作失败:进行身份校验时出错:您输入的验证码与图片不符");
                                taskHousing.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhase());
                                taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhasestatus());
                                taskHousing.setDescription("进行身份校验时出错:您输入的验证码与图片不符");
                                save(taskHousing);
                            }else{
                                login(messageLoginForHousing);
                            }
                        }else{
                            System.out.println("登录失败，错误："+errorMsg);
                            tracer.addTag(taskHousing.getTaskid()+"登录失败，错误：",errorMsg);
                            taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
                            taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
                            taskHousing.setDescription(errorMsg);
                            save(taskHousing);
                        }
                    }
                }
            }
        } catch (Exception e) {
            tracer.addTag("action.login.taskid===>e",taskHousing.getTaskid()+"  "+e.toString());
            System.out.println("打印出来的异常信息是："+e.toString());
            taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
            taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
            taskHousing.setDescription("登录失败，公积金网站系统繁忙，请稍后再试！");
            save(taskHousing);
        }
        return taskHousing;
    }

    @Async
    @Override
    public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
        TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
        try {
            housingFundBaoShanCrawlerService.getUserInfo(taskHousing);
        } catch (Exception e) {
            tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
            taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",500,taskHousing.getTaskid());
        }
        return taskHousing;
    }

    // 利用IO流保存验证码成功后，返回验证码图片保存路径
    public static String getImagePath(Page page) throws Exception {
        File imageFile = getImageCustomPath();
        String imgagePath = imageFile.getAbsolutePath();
        InputStream inputStream = page.getWebResponse().getContentAsStream();
        FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
        if (inputStream != null && outputStream != null) {
            int temp = 0;
            while ((temp = inputStream.read()) != -1) { // 开始拷贝
                outputStream.write(temp); // 边读边写
            }
            outputStream.close();
            inputStream.close(); // 关闭输入输出流
        }
        return imgagePath;
    }


    // 创建验证码图片保存路径
    public static File getImageCustomPath() {
        String path = "";
        if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
            path = System.getProperty("user.dir") + "/verifyCodeImage/";
        } else {
            path = System.getProperty("user.home") + "/verifyCodeImage/";
        }
        File parentDirFile = new File(path);
        parentDirFile.setReadable(true); //
        parentDirFile.setWritable(true); //
        if (!parentDirFile.exists()) {
            System.out.println("==========创建文件夹==========");
            parentDirFile.mkdirs();
        }
        String imageName = UUID.randomUUID().toString() + ".jpg";
        File codeImageFile = new File(path + "/" + imageName);
        codeImageFile.setReadable(true); //
        codeImageFile.setWritable(true, false); //
        return codeImageFile;
    }

    @Override
    public TaskHousing getAllDataDone(String taskId) {
        return null;
    }
}
