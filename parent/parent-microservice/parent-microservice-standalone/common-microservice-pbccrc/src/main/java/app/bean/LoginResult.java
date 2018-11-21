package app.bean;

import com.crawler.aws.json.HttpProxyRes;
import org.openqa.selenium.WebDriver;

/**
 * Created by Administrator on 2018/11/20.
 */
public class LoginResult {

    private WebDriver driver;
    private HttpProxyRes httpProxyRes;
    private String resultJson;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public HttpProxyRes getHttpProxyRes() {
        return httpProxyRes;
    }

    public void setHttpProxyRes(HttpProxyRes httpProxyRes) {
        this.httpProxyRes = httpProxyRes;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "driver=" + driver +
                ", httpProxyRes=" + httpProxyRes +
                ", resultJson='" + resultJson + '\'' +
                '}';
    }
}
