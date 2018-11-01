package app.bean;

import java.util.List;

public class PageBean {

	//url  入参
	private String url;
	//正文  入参
	private String content_in;
	//正文  返回值
	private String content_return;
	//正文摘要  返回值
	private List<String> content_summary_return;
	
	//提示信息
	private String message;
	
	//微博关键字
	private String keyword;
	
	
	//////////////////////////////////// 业务入参实体定义/////////////////////////////////////
	// 文章标签
	// 文章标题（GBK编码），最大80字节
	private String title_tag;
	// 文章内容（GBK编码），最大65535字节
	private String content_tag;

	// 文章类别
	// 文章标题（GBK编码），最大80字节
	private String title_type;
	// 文章内容（GBK编码），最大65535字节
	private String content_type;

	// 评论观点抽取
	// 评论内容（GBK编码），最大10240字节
	private String text_comment;
	// 评论行业类型，默认为4（餐饮美食）
	private String type_comment;

	// 情感倾向分析
	// 文本内容（GBK编码），最大2048字节
	private String text_emotion;

	// 短文本相似度
	// 待比较文本1（GBK编码），最大512字节
	private String text_1_text;
	// 待比较文本2（GBK编码），最大512字节
	private String text_2_text;
	/*
	 * 关于模型选择
	 * 
	 * 短文本相似度接口，可由用户自主选择合适的模型：
	 * 
	 * BOW（词包）模型
	 * 
	 * 基于bag of words的BOW模型，特点是泛化性强，效率高，比较轻量级，适合任务：输入序列的 term
	 * “确切匹配”、不关心序列的词序关系，对计算效率有很高要求；
	 * 
	 * GRNN（循环神经网络）模型
	 * 
	 * 基于recurrent，擅长捕捉短文本“跨片段”的序列片段关系，适合任务：对语义泛化要求很高，对输入语序比较敏感的任务；
	 * 
	 * CNN（卷积神经网络）模型
	 * 
	 * 模型语义泛化能力介于 BOW/RNN 之间，对序列输入敏感，相较于 GRNN 模型的一个显著优点是计算效率会更高些。
	 */
	// 默认为"BOW"，可选"BOW"、"CNN"与"GRNN"
	private String model_text;

	// 词义相似度
	// 词1（GBK编码），最大64字节
	private String word_1_word;
	// 词2（GBK编码），最大64字节
	private String word_2_word;
	// 预留字段，可选择不同的词义相似度模型。默认值为0，目前仅支持mode=0
	private String mode_word;

	//////////////////////////////////// 业务出参实体定义/////////////////////////////////////

	// 文章标签
	// 内容标签
	private String tag_tag;
	// 权重值，取值范围[0,1]
	private String score_tag;

	// 文章类别
	// 一级分类
	private List<PageBean> lv1_tag_list;
	// 二级分类
	private List<PageBean> lv2_tag_list;
	// 类别标签
	private String tag_type;
	// 类别标签对应得分，范围0-1
	private String score_type;

	// 评论观点抽取
	// 匹配上的属性词
	private String prop_comment;
	// 匹配上的描述词
	private String adj_comment;
	// 该情感搭配的极性（0表示消极，1表示中性，2表示积极）
	private String sentiment_comment;
	// 该情感搭配在句子中的开始位置
	private String begin_pos_comment;
	// 该情感搭配在句子中的结束位置
	private String end_pos_comment;
	// 对应于该情感搭配的短句摘要
	private String abstract_comment;

	// 情感倾向分析
	// 表示情感极性分类结果
	private String sentiment_emotion;
	// 表示分类的置信度，取值范围[0,1]
	private String confidence_emotion;
	// 表示属于积极类别的概率 ，取值范围[0,1]
	private String positive_prob_emotion;
	// 表示属于消极类别的概率，取值范围[0,1]
	private String negative_prob_emotion;

	// 短文本相似度
	// 相似度结果取值(0,1]，分数越高说明相似度越高
	private String score_text;

	// 词义相似度
	// 相似度结果，(0,1]，分数越高说明相似度越高
	private String score_word;

	public String getTitle_tag() {
		return title_tag;
	}

	public void setTitle_tag(String title_tag) {
		this.title_tag = title_tag;
	}

	public String getContent_tag() {
		return content_tag;
	}

	public void setContent_tag(String content_tag) {
		this.content_tag = content_tag;
	}

	public String getTitle_type() {
		return title_type;
	}

	public void setTitle_type(String title_type) {
		this.title_type = title_type;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getText_comment() {
		return text_comment;
	}

	public void setText_comment(String text_comment) {
		this.text_comment = text_comment;
	}

	public String getType_comment() {
		return type_comment;
	}

	public void setType_comment(String type_comment) {
		this.type_comment = type_comment;
	}

	public String getText_emotion() {
		return text_emotion;
	}

	public void setText_emotion(String text_emotion) {
		this.text_emotion = text_emotion;
	}

	public String getText_1_text() {
		return text_1_text;
	}

	public void setText_1_text(String text_1_text) {
		this.text_1_text = text_1_text;
	}

	public String getText_2_text() {
		return text_2_text;
	}

	public void setText_2_text(String text_2_text) {
		this.text_2_text = text_2_text;
	}

	public String getModel_text() {
		return model_text;
	}

	public void setModel_text(String model_text) {
		this.model_text = model_text;
	}

	public String getWord_1_word() {
		return word_1_word;
	}

	public void setWord_1_word(String word_1_word) {
		this.word_1_word = word_1_word;
	}

	public String getWord_2_word() {
		return word_2_word;
	}

	public void setWord_2_word(String word_2_word) {
		this.word_2_word = word_2_word;
	}

	public String getMode_word() {
		return mode_word;
	}

	public void setMode_word(String mode_word) {
		this.mode_word = mode_word;
	}

	public String getTag_tag() {
		return tag_tag;
	}

	public void setTag_tag(String tag_tag) {
		this.tag_tag = tag_tag;
	}

	public String getScore_tag() {
		return score_tag;
	}

	public void setScore_tag(String score_tag) {
		this.score_tag = score_tag;
	}

	public String getTag_type() {
		return tag_type;
	}

	public void setTag_type(String tag_type) {
		this.tag_type = tag_type;
	}

	public String getScore_type() {
		return score_type;
	}

	public void setScore_type(String score_type) {
		this.score_type = score_type;
	}

	public String getProp_comment() {
		return prop_comment;
	}

	public void setProp_comment(String prop_comment) {
		this.prop_comment = prop_comment;
	}

	public String getAdj_comment() {
		return adj_comment;
	}

	public void setAdj_comment(String adj_comment) {
		this.adj_comment = adj_comment;
	}

	public String getSentiment_comment() {
		return sentiment_comment;
	}

	public void setSentiment_comment(String sentiment_comment) {
		this.sentiment_comment = sentiment_comment;
	}

	public String getBegin_pos_comment() {
		return begin_pos_comment;
	}

	public void setBegin_pos_comment(String begin_pos_comment) {
		this.begin_pos_comment = begin_pos_comment;
	}

	public String getEnd_pos_comment() {
		return end_pos_comment;
	}

	public void setEnd_pos_comment(String end_pos_comment) {
		this.end_pos_comment = end_pos_comment;
	}

	public String getAbstract_comment() {
		return abstract_comment;
	}

	public void setAbstract_comment(String abstract_comment) {
		this.abstract_comment = abstract_comment;
	}

	public String getSentiment_emotion() {
		return sentiment_emotion;
	}

	public void setSentiment_emotion(String sentiment_emotion) {
		this.sentiment_emotion = sentiment_emotion;
	}

	public String getConfidence_emotion() {
		return confidence_emotion;
	}

	public void setConfidence_emotion(String confidence_emotion) {
		this.confidence_emotion = confidence_emotion;
	}

	public String getPositive_prob_emotion() {
		return positive_prob_emotion;
	}

	public void setPositive_prob_emotion(String positive_prob_emotion) {
		this.positive_prob_emotion = positive_prob_emotion;
	}

	public String getNegative_prob_emotion() {
		return negative_prob_emotion;
	}

	public void setNegative_prob_emotion(String negative_prob_emotion) {
		this.negative_prob_emotion = negative_prob_emotion;
	}

	public String getScore_text() {
		return score_text;
	}

	public void setScore_text(String score_text) {
		this.score_text = score_text;
	}

	public String getScore_word() {
		return score_word;
	}

	public void setScore_word(String score_word) {
		this.score_word = score_word;
	}

	public List<PageBean> getLv1_tag_list() {
		return lv1_tag_list;
	}

	public void setLv1_tag_list(List<PageBean> lv1_tag_list) {
		this.lv1_tag_list = lv1_tag_list;
	}

	public List<PageBean> getLv2_tag_list() {
		return lv2_tag_list;
	}

	public void setLv2_tag_list(List<PageBean> lv2_tag_list) {
		this.lv2_tag_list = lv2_tag_list;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent_return() {
		return content_return;
	}

	public void setContent_return(String content_return) {
		this.content_return = content_return;
	}

	public String getContent_in() {
		return content_in;
	}

	public void setContent_in(String content_in) {
		this.content_in = content_in;
	}

	public List<String> getContent_summary_return() {
		return content_summary_return;
	}

	public void setContent_summary_return(List<String> content_summary_return) {
		this.content_summary_return = content_summary_return;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PageBean [url=" + url + ", content_in=" + content_in + ", content_return=" + content_return
				+ ", content_summary_return=" + content_summary_return + ", message=" + message + ", keyword=" + keyword
				+ ", title_tag=" + title_tag + ", content_tag=" + content_tag + ", title_type=" + title_type
				+ ", content_type=" + content_type + ", text_comment=" + text_comment + ", type_comment=" + type_comment
				+ ", text_emotion=" + text_emotion + ", text_1_text=" + text_1_text + ", text_2_text=" + text_2_text
				+ ", model_text=" + model_text + ", word_1_word=" + word_1_word + ", word_2_word=" + word_2_word
				+ ", mode_word=" + mode_word + ", tag_tag=" + tag_tag + ", score_tag=" + score_tag + ", lv1_tag_list="
				+ lv1_tag_list + ", lv2_tag_list=" + lv2_tag_list + ", tag_type=" + tag_type + ", score_type="
				+ score_type + ", prop_comment=" + prop_comment + ", adj_comment=" + adj_comment
				+ ", sentiment_comment=" + sentiment_comment + ", begin_pos_comment=" + begin_pos_comment
				+ ", end_pos_comment=" + end_pos_comment + ", abstract_comment=" + abstract_comment
				+ ", sentiment_emotion=" + sentiment_emotion + ", confidence_emotion=" + confidence_emotion
				+ ", positive_prob_emotion=" + positive_prob_emotion + ", negative_prob_emotion="
				+ negative_prob_emotion + ", score_text=" + score_text + ", score_word=" + score_word + "]";
	}

	
	
}
