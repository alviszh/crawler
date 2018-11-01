package app.filter;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import app.commontracerlog.TracerLog;

/*
pre filters are executed before the request is routed,
route filters can handle the actual routing of the request,
post filters are executed after the request has been routed, and
error filters execute if an error occurs in the course of handling the request.
 * **/

/*
filterType() returns a String that stands for the type of the filter---in this case, pre, or it could be route for a routing filter.
filterOrder() gives the order in which this filter will be executed, relative to other filters.
shouldFilter() contains the logic that determines when to execute this filter (this particular filter will always be executed).
run() contains the functionality of the filter.
 * */
public class SimpleFilter extends ZuulFilter {

	@Autowired
	private TracerLog tracerLog;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletResponse servletResponse = context.getResponse();
		HttpServletRequest servletRequest = context.getRequest();
		
		// RequestContext ctx = RequestContext.getCurrentContext();
		// HttpServletRequest request = ctx.getRequest();
		// log.info(String.format("%s request to %s", request.getMethod(),
		// request.getRequestURL().toString()));
/*
		

		// app secret
		String secret = "27c7e4bc518c48d095d9caf544771876";

		try {
			String payload = servletRequest.getReader().lines().collect(Collectors.joining());
			String hmac256 = base64Hmac256(payload, secret);
			servletResponse.addHeader("X-Xidian-Signature", hmac256);
			tracerLog.addTag("HttpServletRequest post payload", payload);
			tracerLog.addTag("HttpServletRequest post X-Xidian-Signature", hmac256);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		servletResponse.addHeader("X-Sample", UUID.randomUUID().toString());

		return null;
	}

	// https://docs.51datakey.com/docs/carrier/350
	// X-Xidian-Signature 消息签名，防止数据在回调过程中被篡改
	// payload为request body内容
	// secret由我司为客户分配
	/*public static String base64Hmac256(String payload, String secret) {
		try {
			Mac sha256Hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
			sha256Hmac.init(secretKey);
			return Base64.encodeBase64String(sha256Hmac.doFinal(payload.getBytes()));
		} catch (Exception ignored) {
			return "";
		}
	}*/

}
