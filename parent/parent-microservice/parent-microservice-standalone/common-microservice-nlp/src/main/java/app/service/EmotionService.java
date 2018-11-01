package app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.hankcs.hanlp.HanLP;

import app.bean.PageBean;
import app.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class EmotionService {

	public List<PageBean> articleTag(@RequestBody PageBean page) {

		String token = getAuth();
		System.out.println(token);
		List<PageBean> list = new ArrayList<PageBean>();
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 文章标签url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/keyword";
			try {

				String title_tag = page.getTitle_tag().trim();
				System.out.println("文章标题:" + title_tag);
				String content_tag = page.getContent_tag().trim();
				System.out.println("文章内容:" + content_tag);

				String params = "{\"title\":\"" + title_tag + "\",\"content\": \"" + content_tag + "\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("文章标签解析失败！");
				} else {
					System.out.println("文章标签解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String items = json.getString("items").trim();
					JSONArray array = JSONArray.fromObject(items);
					for (int i = 0; i < array.size(); i++) {
						String string = array.get(i).toString();
						JSONObject json2 = JSONObject.fromObject(string);
						PageBean pageBean = new PageBean();
						// 权重值
						String score = json2.getString("score").trim();
						System.out.println("权重值-----" + score);
						// 内容标签
						String tag = json2.getString("tag").trim();
						System.out.println("内容标签-----" + tag);
						pageBean.setScore_tag(score);
						pageBean.setTag_tag(tag);
						list.add(pageBean);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public PageBean articleType(@RequestBody PageBean page) {
		String token = getAuth();
		System.out.println(token);
		List<PageBean> lv1_tag_list0 = new ArrayList<PageBean>();
		List<PageBean> lv2_tag_list0 = new ArrayList<PageBean>();
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 文章类别url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/topic";
			try {
				String title_type = page.getTitle_type().trim();
				System.out.println("文章标题:" + title_type);
				String content_type = page.getContent_type().trim();
				System.out.println("文章内容:" + content_type);

				String params = "{\"title\":\"" + title_type + "\",\"content\": \"" + content_type + "\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("文章类别解析失败！");
				} else {
					System.out.println("文章类别解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String item = json.getString("item").trim();
					JSONObject json2 = JSONObject.fromObject(item);

					// 一级分类结果
					String lv1_tag_list = json2.getString("lv1_tag_list").trim();
					JSONArray array0 = JSONArray.fromObject(lv1_tag_list);
					for (int i = 0; i < array0.size(); i++) {
						String trim = array0.get(i).toString().trim();
						JSONObject json3 = JSONObject.fromObject(trim);
						PageBean pageBean = new PageBean();
						// 类别标签对应得分
						String score = json3.getString("score").trim();
						System.out.println("类别标签对应得分:" + score);
						// 类别标签
						String tag = json3.getString("tag").trim();
						System.out.println("类别标签:" + tag);
						pageBean.setScore_type(score);
						pageBean.setTag_type(tag);
						lv1_tag_list0.add(pageBean);
					}

					// 二级分类结果
					String lv2_tag_list = json2.getString("lv2_tag_list").trim();
					JSONArray array = JSONArray.fromObject(lv2_tag_list);
					for (int i = 0; i < array.size(); i++) {
						String string = array.get(i).toString().trim();
						JSONObject json3 = JSONObject.fromObject(string);
						PageBean pageBean = new PageBean();
						// 类别标签对应得分
						String score = json3.getString("score").trim();
						System.out.println("类别标签对应得分:" + score);
						// 类别标签
						String tag = json3.getString("tag").trim();
						System.out.println("类别标签:" + tag);
						pageBean.setScore_type(score);
						pageBean.setTag_type(tag);
						lv2_tag_list0.add(pageBean);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	public PageBean commentPointExtract(@RequestBody PageBean page) {
		String token = getAuth();

		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 评论观点抽取url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v2/comment_tag";
			try {

				String text_comment = page.getText_comment().trim();
				System.out.println("内容:" + text_comment);
				String type_comment = page.getType_comment().trim();
				System.out.println("类型:" + type_comment);

				String params = "{\"text\":\"" + text_comment + "\",\"type\":" + type_comment + "}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("评论观点抽取解析失败！");
				} else {
					System.out.println("评论观点抽取解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String items = json.getString("items").trim();
					JSONArray array = JSONArray.fromObject(items);
					String string = array.get(0).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					// 匹配上的属性词
					String prop = json2.getString("prop").trim();
					System.out.println("匹配上的属性词:" + prop);
					// 匹配上的描述词
					String adj = json2.getString("adj").trim();
					System.out.println("匹配上的描述词:" + adj);
					// 该情感搭配的极性
					String sentiment = json2.getString("sentiment").trim();
					System.out.println("该情感搭配的极性:" + sentiment);
					// 该情感搭配在句子中的开始位置
					String begin_pos = json2.getString("begin_pos").trim();
					System.out.println("该情感搭配在句子中的开始位置:" + begin_pos);
					// 该情感搭配在句子中的结束位置
					String end_pos = json2.getString("end_pos").trim();
					System.out.println("该情感搭配在句子中的结束位置:" + end_pos);
					// 对应于该情感搭配的短句摘要
					String abstract1 = json2.getString("abstract").trim();
					System.out.println("对应于该情感搭配的短句摘要:" + abstract1);
					page.setProp_comment(prop);
					page.setAdj_comment(adj);
					page.setSentiment_comment(sentiment);
					page.setBegin_pos_comment(begin_pos);
					page.setEnd_pos_comment(end_pos);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	public PageBean emotion(@RequestBody PageBean page) {
		String token = getAuth();

		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 情感倾向分析url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify";
			try {

				String text_emotion = page.getText_emotion().trim();
				System.out.println("情感倾向分析:" + text_emotion);
				String params = "{\"text\": \"" + text_emotion + "\" }";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("情感倾向分析解析失败！");
				} else {
					System.out.println("情感倾向分析解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String items = json.getString("items").trim();
					JSONArray array = JSONArray.fromObject(items);
					String string = array.get(0).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					// 表示情感极性分类结果
					String sentiment = json2.getString("sentiment").trim();
					System.out.println("表示情感极性分类结果-----" + sentiment);
					// 表示分类的置信度
					String confidence = json2.getString("confidence").trim();
					System.out.println("表示分类的置信度-----" + confidence);
					// 表示属于积极类别的概率
					String positive_prob = json2.getString("positive_prob").trim();
					System.out.println("表示属于积极类别的概率-----" + positive_prob);
					// 表示属于消极类别的概率
					String negative_prob = json2.getString("negative_prob").trim();
					System.out.println("表示属于消极类别的概率-----" + negative_prob);
					page.setSentiment_emotion(sentiment);
					page.setConfidence_emotion(confidence);
					page.setPositive_prob_emotion(positive_prob);
					page.setNegative_prob_emotion(negative_prob);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	public PageBean textSimilarity(@RequestBody PageBean page) {
		String token = getAuth();

		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 短文本相似度url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet";
			try {
				String text_1_text = page.getText_1_text().trim();
				System.out.println("短文本相似度:" + text_1_text);
				String text_2_text = page.getText_2_text().trim();
				System.out.println("短文本相似度:" + text_2_text);

				String params = "{\"text_1\":\"" + text_1_text + "\",\"text_2\":\"" + text_2_text + "\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("短文本相似度解析失败！");
				} else {
					System.out.println("短文本相似度解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					// 相似度结果
					String score = json.getString("score").trim();
					System.out.println("相似度结果：" + score);
					page.setScore_text(score);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	public PageBean wordSimilarity(@RequestBody PageBean page) {
		String token = getAuth();

		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 词义相似度url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v2/word_emb_sim";
			try {
				String word_1_word = page.getWord_1_word().trim();
				System.out.println("词义相似度入参:" + word_1_word);
				String word_2_word = page.getWord_2_word();
				System.out.println("词义相似度入参:" + word_2_word);

				String params = "{\"word_1\":\"" + word_1_word + "\",\"word_2\":\"" + word_2_word + "\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("词义相似度解析失败！");
				} else {
					System.out.println("词义相似度解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					// 相似度结果
					String score = json.getString("score").trim();
					System.out.println("相似度结果：" + score);
					page.setScore_word(score);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page;
	}

	/**
	 * 获取权限token
	 */
	public static String getAuth() {
		// API Key
		String clientId = "cHkuUalqtd3YuKdwMfWy7De8";
		// Secret Key
		String clientSecret = "3eIDnPRRmVlFCSjt6QX3UjKq7qwVeQFz";
		return getAuth(clientId, clientSecret);
	}

	public static String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
    //自动摘要
	public PageBean summary(PageBean page) {
		System.out.println("自动摘要-----"+page.toString());
		//入参的正文
		String content_in = page.getContent_in().trim();
		System.out.println("入参的正文-----"+content_in);
		if(StringUtils.isNotBlank(content_in)){
			Document doc = Jsoup.parse(content_in);
			String text = doc.text();
			List<String> sentenceList = HanLP.extractSummary(text, 3);
			System.out.println(sentenceList);
			page.setContent_summary_return(sentenceList);
			return page;
		}else{
			System.out.println("入参的正文为空！");
			return page;
		}
	}
}
