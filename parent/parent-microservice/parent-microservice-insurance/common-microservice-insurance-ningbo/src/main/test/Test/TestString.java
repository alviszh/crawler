package Test;

import net.sf.json.JSONObject;

public class TestString {

	public static void main(String[] args) {
		String a = "{\"result\":\"{\"AAZ500\":\"B41537138\",\"AAE010\":\"6217213901013943697\",\"AZA103\":\"中国\",\"AAZ503\":\"20131029\",\"AAC004\":\"1\",\"AAZ220\":\"315300\",\"AAE004\":\"13606783047\",\"AAZ502\":\"1\",\"AAE006\":\"宁波市鄞州区景寓路666号名汇大厦806室\",\"AAE005\":\"87173302\",\"AAE008\":\"102\",\"AAZ510\":\"1\"}\",\"ret\":\"1\"}";
		JSONObject fromObject = JSONObject.fromObject(a);
		System.out.println(fromObject);
	}
}
