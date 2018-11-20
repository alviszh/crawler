package app.utils;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    
    public static String sendPostHttp(String url, String data) throws Exception {
		HttpURLConnection con = HttpURLConnection.class.cast(new URL(url)
				.openConnection());
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type","text/plain");
		con.setRequestProperty("Content-Encoding", "UTF-8");
		con.setRequestProperty("Connection","close");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		byte[] dataBytes = data.getBytes("UTF-8");
		OutputStream out = con.getOutputStream();
		out.write(dataBytes);
		out.flush();
		out.close();
		Reader input = new InputStreamReader(con.getInputStream());
		StringBuilder sb = new StringBuilder();
		int i = -1;
		char[] buffer = new char[1024];
		while ((i = input.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, i));
		}
		input.close();
		return sb.toString();
	}
    
    
	public static Map<String, Object> sendPostHttp(String url, String data, Map<String, String> map){
		String errorStr = "";
		int status = 500;
		String response = "";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HttpURLConnection con = HttpURLConnection.class.cast(new URL(url).openConnection());
			con.setRequestMethod("POST");
			if (null != map) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			
			byte[] dataBytes = data.getBytes("UTF-8");
			OutputStream out = con.getOutputStream();
			out.write(dataBytes);
			out.flush();
			out.close();
			Reader input = new InputStreamReader(con.getInputStream());
			StringBuilder sb = new StringBuilder();
			int i = -1;
			char[] buffer = new char[1024];
			while ((i = input.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, i));
			}
			status = con.getResponseCode();
//			status =  new Integer(con.getResponseCode()).toString(); 
			input.close();
			response = sb.toString();
		} catch (Exception e) {
			errorStr = e.getMessage();
			e.printStackTrace();
		}
		result.put("errorStr", errorStr);
		result.put("response", response);
		result.put("status", status);
		return result;

	}
    
    
}
