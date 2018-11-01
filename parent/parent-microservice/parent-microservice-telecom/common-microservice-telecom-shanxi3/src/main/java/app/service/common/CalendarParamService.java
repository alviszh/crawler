package app.service.common;
/**
 * @Description 测试生成日期拼接字符串
 * @author sln
 * @date 2017年8月18日 下午3:25:20
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CalendarParamService {	
	//获取从上个月开始的6个月,时间格式为：yyyyMM
	public static List<String>  getMonth(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();	
		calendar.setTime(new Date());
		List<String> list=new ArrayList<String>();
		for(int i=1;i<=6;i++){
			 calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
			 list.add(dateFormat.format(calendar.getTime()));
		}
		return list;
	}
	//==========================分界线=====================================
	//获取从本月开始的6个月,时间格式为：yyyyMM
	public static List<String>  getMonthIncludeThis(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();	
		calendar.setTime(new Date());
		List<String> list=new ArrayList<String>();
		for(int i=0;i<=5;i++){
			 list.add(dateFormat.format(calendar.getTime()));
			 calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		}
		return list;
	}
	//==========================分界线=====================================
	//某个月的第一天
	public static String getFirstMonthdate(int i){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		//某月的第一天
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
//		System.out.println(i+"某个月第一天："+format.format(calendar.getTime()));
		return format.format(calendar.getTime());
	}
	//某个月的最后一天
	public static String getLastMonthdate(int i){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//		System.out.println(i+"某个月最后一天："+format.format(calendar.getTime()));
		return format.format(calendar.getTime()); 
	}
	//=======================分解线=============================
	public static String getPresentDate(){
		Date d = new Date();  
        System.out.println(d);  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
}

