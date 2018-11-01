package app.service.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.BasicUserHousingRepository;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.aop.impl.CrawlerImpl;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.basic")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.basic")
public class HousingBasicService {
	public static final Logger log = LoggerFactory.getLogger(HousingBasicService.class);

	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	
	@Autowired
	protected BasicUserHousingRepository basicUserHousingRepository;
	
	@Autowired
	protected TracerLog tracer;
	
	@Autowired
	private CrawlerImpl crawlerImpl;


	protected  Gson gs = new Gson();
	
	protected LoginAndGetCommon loginAndGetCommon = new LoginAndGetCommon();
	
	protected TaskHousing findTaskHousing(String taskId){
		return taskHousingRepository.findByTaskid(taskId);
	}
	
	protected void updateUserInfoStatusByTaskid(String description, Integer code,String taskId){
		 taskHousingRepository.updateUserInfoStatusByTaskid(description, code, taskId);;
	}
	
	protected void updatePayStatusByTaskid(String description,Integer code,String taskId){
		 taskHousingRepository.updatePayStatusByTaskid(description, code, taskId);;
	}
	
	public TaskHousing updateTaskHousing(String taskId) {
		log.info("---------------------CrawlerStatusMobileService updateTaskMobile---------------------");

		tracer.addTag("updateTaskMobile", "CrawlerStatusMobileService updateTaskMobile");
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(taskId);
		
		log.info("---------------------taskHousing updateTaskMobile---------------------"+taskHousing.toString());
		tracer.addTag("updateTaskMobile", taskHousing.toString());
		if (null != taskHousing.getUserinfoStatus() && null != taskHousing.getPaymentStatus()) {
			
			log.info("CrawlerStatusHousingService working----------------------");
			
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
			taskHousing.setFinished(true);
			taskHousingRepository.save(taskHousing);
			log.info("---------------------updatetaskHousing success---------------------");
			tracer.addTag("updatetaskHousing", "updatetaskHousing success");
			crawlerImpl.getAllDataDone(taskId);
		}
		return taskHousing;

	}
	
	public void save(TaskHousing taskHousing){
		taskHousingRepository.save(taskHousing);
	}
	
	/**
	 * sln
	 * 
	 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
	 * 利用IO流保存验证码成功后，将完整路径信息一并返回
	 * 
	 * 此方法针对部分网站加载登录页面时，图片验证码刷新不出来的情况
	 * 
	 * @param page
	 * @param imagePath
	 * @return
	 * @throws Exception
	 */
	
	public  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) {  
	        int temp = 0;  
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}
	
	/**
     * @param taskHousing
     * @param webClient
     * @return
     * @Des 更新task表（success 登陆成功,cookie入库）
     */
    public TaskHousing changeLoginStatusSuccess(TaskHousing taskHousing, WebClient webClient) {

    	taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());

        String cookies = CommonUnit.transcookieToJson(webClient);
        taskHousing.setCookies(cookies);
        taskHousing = taskHousingRepository.save(taskHousing);
        return taskHousing;
    }
}