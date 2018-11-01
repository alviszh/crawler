package app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import app.bean.NewsBean;

/**
 * 新闻处理器
 * @author zz
 *
 */
public class NewsHandle {
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 处理时间
	 * @param createTime
	 * @param publishTime
	 * @return
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	public String dealWithTime(String createTime, String publishTime) throws NumberFormatException, ParseException {
		if(StringUtils.isNotBlank(publishTime)){
			Matcher m = Pattern.compile("\\d+").matcher(publishTime);
			if(publishTime.contains("分钟") | publishTime.contains("分")){	//48分钟前
				if(m.find()){				
					return getTimeByDay(Integer.valueOf("-"+m.group()),createTime);
				}else{
					return createTime;
				}
			}else if(publishTime.contains("小时") | publishTime.contains("时")){//9小时前
				if(m.find()){				
					return getTimeByHour(Integer.valueOf("-"+m.group()),createTime);
				}else{
					return createTime;
				}
			}else if(publishTime.contains("昨天")){//昨天
				return getTimeByDay(-1,createTime);
			}else if(publishTime.contains("天")){//2天前
				if(m.find()){				
					return getTimeByDay(Integer.valueOf("-"+m.group()),createTime);
				}else{
					return createTime;
				}
			}else if(publishTime.contains("刚刚")){//刚刚
				return getTimeByNow(createTime);
			}else if(publishTime.contains("上午")){//18-4-6 上午2:18
				String formateTime = publishTime.replace("上午", "").substring(3);
				return formatDate(formateTime);
			}else if(publishTime.contains("下午")){//18-4-5 下午4:57
				String formateTime = publishTime.replace("下午", "").substring(3);
				return addDateMinut(formatDate(formateTime),12);
			}
			else{
				return formatDate(publishTime);
			}
		}else{//没有发布时间
			return getTimeByNow(createTime);
		}
	}
	
	/**
	 * 来源处理
	 * @param source
	 * @return
	 */
	public NewsBean dealWithSource(String source) {
		NewsBean newsBean = new NewsBean();
		
		//来自:\s{0,1}[\u4e00-\u9fa5]{1,9}|来源：\s{0,1}[\u4e00-\u9fa5]{1,9}|发布：\s{0,1}[\u4e00-\u9fa5]{1,9}
		String regex1 = "来自(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}|来源(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}|发布(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}";
		//2018-04-18 17:04   2018年3月31日   2014-03-27   2018/5/9  2018-05-09 09:17:12  2018-5-9
		String regex2 = "\\d{4}(年|-|/)[0-9]{1,2}(月|-|/)[0-9]{1,2}(日)*( )*([0-9]{1,2}(时|:)[0-9]{1,2}(分)*(:)*[0-9]{1,2}(秒)*)*";
		//评分：7分
		String regex3 = "评分：[0-9]{1,2}分";
		
		Matcher m = Pattern.compile(regex1).matcher(source);	
		Matcher m1 = Pattern.compile(regex2).matcher(source);
		
		String sourceEnd = "";
		String publishTime = "";
		
		if(m.find()){
			sourceEnd = formatSource(m.group());
		}else if(source.contains("|")){//发布时间: 2018-4-11 11:11 |浏览量：872 | 评论: 4

		}else{
			String regex2Str = source.replaceAll(regex2, "");
			String finalStr = regex2Str.replaceAll(regex3, "").trim();
			sourceEnd =  formatSource(finalStr);
		}
		
		if(m1.find()){
			publishTime = formatDate(m1.group());
		}
		
		newsBean.setPublishTime(publishTime);
		newsBean.setSource(sourceEnd);
		return newsBean;
	}
	
	/**
	 * 来源格式化
	 * @param source
	 * @return
	 */
	public String formatSource(String source){
		return source.replace("来自:", "").replace("来源：", "").replace("发布：", "").trim();
	}

	
	/**
	 * 各种时间规则转换
	 * @param dateStr
	 * @return
	 */
	@SuppressWarnings("finally")
	public String formatDate(String dateStr){
		  
	    HashMap<String, String> dateRegFormat = new HashMap<String, String>();
	  //2014年3月12日 13时5分34秒，2014-03-12 12:05:34，2014/3/12 12:5:34
	    dateRegFormat.put(
	        "^(1|2)(0|9)\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$",
	        "yyyy-MM-dd-HH-mm-ss");
	  //2014-03-12 12:05
	    dateRegFormat.put("(1|2)(0|9)\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}",
	        "yyyy-MM-dd-HH-mm");
	  //2014-03-12 12
	    dateRegFormat.put("^(1|2)(0|9)\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$",
	        "yyyy-MM-dd-HH");
	  //2014-03-12
	    dateRegFormat.put("^(1|2)(0|9)\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");
	  //2014-03
	    dateRegFormat.put("^(1|2)(0|9)\\d{2}\\D+\\d{2}$", "yyyy-MM");
	  //2014
	    dateRegFormat.put("^(1|2)(0|9)\\d{2}$", "yyyy");//2014
	    dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");//20140312120534
	    dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");//201403121205
	    dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");//2014031212
	    dateRegFormat.put("^\\d{8}$", "yyyyMMdd");//20140312
	    dateRegFormat.put("^\\d{6}$", "yyyyMM");//201403
	    dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$",
	        "yyyy-MM-dd-HH-mm-ss");//13:05:34 拼接当前日期
	    dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");//13:05 拼接当前日期
	    dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");//14.10.18(年.月.日)
	    dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");//30.12(日.月) 拼接当前年份
	    dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");//12.21.2013(日.月.年)
	    dateRegFormat.put("\\d+(月|-|/)\\d+(日|/|-)*( )*\\d+(时|:)\\d+", "yyyy-MM-dd-HH-mm");//4月8日15:28
	    dateRegFormat.put("(1|2)(0|9)\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}(日)*", "yyyy-MM-dd");//2017年8-12 2017年9月19日  2018/5/11  2018-05/11
	  
	    String curDate =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    DateFormat formatter2;
	    String dateReplace;
	    String strSuccess="";
	    try {
	      for (String key : dateRegFormat.keySet()) {
	        if (Pattern.compile(key).matcher(dateStr).matches()) {
	          formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
	          if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$")
	              || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {//13:05:34 或 13:05 拼接当前日期
	            dateStr = curDate + "-" + dateStr;
	          } else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {//21.1 (日.月) 拼接当前年份
	            dateStr = curDate.substring(0, 4) + "-" + dateStr;
	          } else if(key.equals("\\d+(月|-|/)\\d+(日|/|-)*( )*\\d+(时|:)\\d+")){
	        	dateStr = curDate.substring(0, 4) + "-" + dateStr;  
	          }
	          dateReplace = dateStr.replaceAll("\\D+", "-");
	          // System.out.println(dateRegExpArr[i]);
	          strSuccess = sdf.format(formatter2.parse(dateReplace));
	          break;
	        }
	      }
	    } catch (Exception e) {
	    	System.out.println("日期格式有误");
	    	return null;
	    } finally {
	    	return strSuccess;
	    }
	  }

	 
    /**
     * 获取指定时间之前或之后几小时 hour
     * @param hour
     * @return
     * @throws ParseException 
     */
    public String getTimeByHour(int hour,String createTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(createTime));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return sdf.format(calendar.getTime());
    }

   
    /**
     * 获取指定时间之前或之后几分钟 minute
     * @param minute
     * @return
     * @throws ParseException 
     */
    public String getTimeByMinute(int minute, String createTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(createTime)); 
        calendar.add(Calendar.MINUTE, minute);
        return sdf.format(calendar.getTime());
    }

   
    /**
     * 获取指定时间之前或之后几天 day
     * @param day
     * @return
     * @throws ParseException 
     */
    public String getTimeByDay(int day, String createTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(createTime)); 
        calendar.add(Calendar.DATE, day);
        return sdf.format(calendar.getTime());
    }
    
    
    /**
     * 获取时间。例：刚刚
     * @param createTime
     * @return
     * @throws ParseException
     */
    public String getTimeByNow(String createTime) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(sdf.parse(createTime));    	
    }
    
    /**
     * 给时间加上几个小时
     * @param day 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static String addDateMinut(String day, int hour){   
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;   
        try {   
            date = format.parse(day);   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }   
        if (date == null)  { 
        	return "";           	
        }
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.HOUR, hour);// 24小时制   
        date = cal.getTime();   
        cal = null;   
        return format.format(date);   

    }
    
    
    public static void main(String[] args) {
    	
//    	try {
//			String time = dealWithTime(sdf.format(new Date()),"2018-05/11");
//			System.out.println(time);
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}

 
}
