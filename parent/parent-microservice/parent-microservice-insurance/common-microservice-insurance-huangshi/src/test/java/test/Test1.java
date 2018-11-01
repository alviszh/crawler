package test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test1 {

	public static void main(String[] args) {
		String strJson = "{'personIds':'','success':true}";
		JsonParser jsonParser = new JsonParser();
		JsonObject obj = (JsonObject)jsonParser.parse(strJson);
		boolean success = obj.get("success").getAsBoolean();
		String personIds = obj.get("personIds").getAsString();
	}
}
