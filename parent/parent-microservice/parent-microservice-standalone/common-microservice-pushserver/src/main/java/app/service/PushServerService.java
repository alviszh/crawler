package app.service;


import app.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class PushServerService {

    public Map<String, Object> requestnoticeurl(String url,String request_body_param,String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        /*Map<String, String> map = new HashMap<>();
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/json;charset=utf-8");
        map.put("X-Xidian-Profile", "test");
        map.put("X-Xidian-Event", "task");
        map.put("X-Xidian-Type", "carrier");
        map.put("X-Xidian-Signature", "IKBAfrRQnb7CMpsbQnFJVgN1LGzPgXdzYjU2uUGqRxw=");
        map.put("X-Xidian-Uid", "52590a85-bdfd-4cf7-9f62-6ef846871547");*/

        String url_param = "?";
        JSONArray array=JSONArray.fromObject(param);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj=JSONObject.fromObject(array.get(i));
            if(obj!=null&&obj.containsKey("callbackParamsKey1")
                    &&obj.containsKey("callbackParamsValue1")){
                String  paramsKey = obj.getString("callbackParamsKey1");
                String  paramsValue = obj.getString("callbackParamsValue1");
                if (StringUtils.isNotBlank(paramsKey)
                        && StringUtils.isNotBlank(paramsValue)) {
                    url_param += paramsKey+"=" + paramsValue+"&";
                }
            }
        }
        if (url_param.length() >= 1) {
            url_param = url_param.substring(0, url_param.length() - 1);
        }
        try {
            result = HttpUtil.sendPostHttp(url + url_param, request_body_param, null);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
