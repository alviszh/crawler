package app.service.aop;

import com.crawler.pbccrc.json.PbccrcJsonBean;

/**
 * 征信爬取接口
 *
 */
public interface ICrawler {

    /**
     * 开始爬取
     *
     *
     * */
    public String getAllData(PbccrcJsonBean pbccrcJsonBean);


    /**
     * 完成爬取
     *
     *
     * */
//    public String getAllDataDone(String taskId);
}
