package test.pbccrc;

import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.LoginData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/18.
 */
public class test {
    public static final String getloginpageURL = "http://54.223.56.183/data/api/pbccrc/getloginpage";

    public static void main(String[] args) {
//        PbccrcClient pbccrcClient = Feign.builder().target(PbccrcClient.class,"http://54.223.56.183");
//        System.out.println(pbccrcClient.getLoginpage());

        /*try {
            String  getloginpage = getloginpage();
//            System.out.println(getloginpage);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Map<String, Object> map = new HashMap<>();
        map.put("amount", "0");
        map.put("abortDay", "2036.6.6");
        double amount = Double.parseDouble(map.get("amount").toString());
        System.out.println(amount);
        System.out.println(map);
        Object test = map.get("test");
        System.out.println("test:"+test);

        String abortDay = map.get("abortDay").toString();//到期日期
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
        try {
            int monthSpace = getMonthSpace("2016.5.6", "2036.6.6");
            System.out.println("monthSpace:"+monthSpace);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double d = 12;
        BigDecimal b = new BigDecimal(d);
        double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(df);

    }

    public static int getMonthSpace(String str1, String str2)
            throws ParseException {
        int result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(str1));
        aft.setTime(sdf.parse(str2));
        int month = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int year = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        result =month + year;
        System.out.println(Math.abs(result));
        return result == 0 ? 1 : Math.abs(result);
    }


    public static String getloginpage() throws IOException {
        String result = null;
        try {
            URL url = new URL(getloginpageURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");

            conn.setRequestMethod("GET");
            conn.setReadTimeout(6000);
            conn.setConnectTimeout(6000);

            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String charset = "UTF-8";
                Pattern pattern = Pattern.compile("charset=\\S*");
                Matcher matcher = pattern.matcher(conn.getContentType());
                if (matcher.find()) {
                    charset = matcher.group().replace("charset=", "");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
//                System.out.println(result);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
               /* Result<ResultData> data = gson.fromJson(result, new TypeToken<Result<ResultData>>(){}.getType());
                System.out.println(data);*/

                Result<LoginData> loginDataResult = gson.fromJson(result, new TypeToken<Result<LoginData>>() {}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*JSONObject jsonObjs = JSONObject.fromObject(result);
        String data = jsonObjs.getString("data");
        jsonObjs = JSONObject.fromObject(data);
        String cookies = jsonObjs.getString("cookies");
        String codeImageUrl = jsonObjs.getString("codeImageUrl");
        System.out.println(cookies);
        System.out.println(codeImageUrl);*/
        LoginData loginData = new LoginData();
        /*loginData.setCookies(cookies);
        loginData.setCodeImageUrl(codeImageUrl);*/
        return result;
    }

}
