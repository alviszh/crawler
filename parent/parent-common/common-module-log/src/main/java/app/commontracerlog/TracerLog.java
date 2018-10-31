package app.commontracerlog;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;

import brave.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TracerLog {
	
    @Autowired
    private Tracer tracer;
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * 1.好多人习惯去console里面看日志，因此封装一下tracer，可以在zipkin里面存日志，有可以在console 打印，参考TracerLog类
	 * 2.好多人没注意key不能重复的问题，因此在封装的日志类里面的key加上一个时间戳（世界戳不用太长，截取后7位应该就够了）
     * 3.时间戳加在key中也可以优化zipkin的日志显示顺序，在zipkin中日志从上到下也正好匹配我们程序的从上到下 
     * */
    public void addTag(String key, String value) {
        log.info("[KEY]:{},[VALUE]{}", key, value);  //把 log.info 注释删除了，希望在console里面看到日志以便调试，by meidi 20180725
        long currentTimeMillis = System.currentTimeMillis();
		//将long型数据转换为String
		String timeStamp = String.valueOf(currentTimeMillis);
		//获取时间戳的后六位
		timeStamp = timeStamp.substring(timeStamp.length()-7, timeStamp.length());
		if(null!=value){
			this.tracer.currentSpan().tag("【"+timeStamp+"】"+key, value);
		}
		
    }
    
    public void output(String key, String value) {
        log.info("[KEY]:{},[VALUE]{}", key, value);
        long currentTimeMillis = System.currentTimeMillis();
		//将long型数据转换为String
		String timeStamp = String.valueOf(currentTimeMillis);
		//获取时间戳的后六位
		timeStamp = timeStamp.substring(timeStamp.length()-7, timeStamp.length());
		if(null!=value){
			this.tracer.currentSpan().tag("【"+timeStamp+"】"+key, value);
		}
		
    }
    
    public void output2(String key, String value) {
//        log.info("[KEY]:{},[VALUE]{}", key, value);
        long currentTimeMillis = System.currentTimeMillis();
		//将long型数据转换为String
		String timeStamp = String.valueOf(currentTimeMillis);
		//获取时间戳的后六位
		timeStamp = timeStamp.substring(timeStamp.length()-7, timeStamp.length());
		if(null!=value){
			this.tracer.currentSpan().tag("【"+timeStamp+"】"+key, value);
		}
		
    }
    //如下方法不添加时间戳，用于定义zinkin查询日志时key=value切入点
   /**
     * 将value 用<xmp>标签包裹起来，适用于value 是html文本的情况
     * */
    public void addTagWrap(String key, String value) {
        log.info("[KEY]:{},[VALUE]{}", key, value);
        long currentTimeMillis = System.currentTimeMillis();
		//将long型数据转换为String
		String timeStamp = String.valueOf(currentTimeMillis);
		//获取时间戳的后六位
		timeStamp = timeStamp.substring(timeStamp.length()-7, timeStamp.length());
		if(null!=value){
			this.tracer.currentSpan().tag("【"+timeStamp+"】"+key, "<xmp>"+value+"</xmp>");
		}
    }
    
    /**
     * 将value 用<xmp>标签包裹起来，适用于value 是html文本的情况
     * */
    public void System(String key, String value) {
        log.info("[KEY]:{},[VALUE]{}", key, value);
    }


    public void qryKeyValue(String key, String value){
    	if(null!=value){
    		this.tracer.currentSpan().tag(key, value);
		}
    }
}