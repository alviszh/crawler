package app.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zmy on 2018/3/2.
 */
@Component
public class JsonArrayUtil {
    /**
     * 数组转换为json数组
     *
     * @param array
     * @return
     * @throws JSONException
     */
    public JSONArray arrayToJsonArray(Object[] array) throws JSONException {
        if (array != null) {
            JSONArray jsonArray = new JSONArray();
            for (Object d : array) {
                jsonArray.add(d);
            }
            return jsonArray;
        }
        return null;
    }

    /**
     *
     * @param data
     * @return
     * @throws JSONException
     */
    public static JSONArray listMapToJsonArray(List<Map<String,Object>> data) throws JSONException {
        if (data != null) {
            JSONArray jsonArray = new JSONArray();
            for (Object d : data) {
                jsonArray.add(d);
            }
            return jsonArray;
        }
        return null;
    }
}
