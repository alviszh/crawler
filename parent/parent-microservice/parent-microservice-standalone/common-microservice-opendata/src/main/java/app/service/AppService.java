package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.EnvironmentDef;

import app.commontracerlog.TracerLog;
import app.dao.developer.AppProductListRepository;
import app.dao.developer.AppRepository;
import app.dao.developer.CallbackParamstRepository;
import app.dao.developer.ProductRepository;
import app.dao.oauth.OauthClientDetailsRepository;
import app.entity.developer.App;
import app.entity.developer.AppProductList;
import app.entity.developer.CallbackParams;
import app.entity.developer.Product;
import app.entity.developer.enums.ProductStatus;
import app.entity.oauth.OauthClientDetails;
import app.entity.security.SUser;
import app.exceptiondetail.ExUtils;
import app.utils.HttpUtil;
import app.utils.SetOptUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 应用
 * 
 * @author tz
 *
 */
@Component
@Service
public class AppService {
	
	@Autowired
	private SUserService suserService;
	@Autowired
	private AppRepository appRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private AppProductListRepository appProductListRepository;
	@Autowired
	private CallbackParamstRepository callbackParamstRepository;
	
	@Autowired
	private OauthClientDetailsRepository oauthClientDetailsRepository;
	
	@Value("${app.password.salt}")
	private String salt;
	
	private String authorized_grant_types = "password,authorization_code,refresh_token,client_credentials";
	
	private String scope = "read,write";
	
	private String icon = "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTQwIiBoZWlnaHQ9IjE0MCIgdmlld0JveD0iMCAwIDE0MCAxNDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzE0MHgxNDAKQ3JlYXRlZCB3aXRoIEhvbGRlci5qcyAyLjYuMC4KTGVhcm4gbW9yZSBhdCBodHRwOi8vaG9sZGVyanMuY29tCihjKSAyMDEyLTIwMTUgSXZhbiBNYWxvcGluc2t5IC0gaHR0cDovL2ltc2t5LmNvCi0tPjxkZWZzPjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+PCFbQ0RBVEFbI2hvbGRlcl8xNjVjNzE1Njg0YiB0ZXh0IHsgZmlsbDojQUFBQUFBO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1mYW1pbHk6QXJpYWwsIEhlbHZldGljYSwgT3BlbiBTYW5zLCBzYW5zLXNlcmlmLCBtb25vc3BhY2U7Zm9udC1zaXplOjEwcHQgfSBdXT48L3N0eWxlPjwvZGVmcz48ZyBpZD0iaG9sZGVyXzE2NWM3MTU2ODRiIj48cmVjdCB3aWR0aD0iMTQwIiBoZWlnaHQ9IjE0MCIgZmlsbD0iI0VFRUVFRSIvPjxnPjx0ZXh0IHg9IjQ0LjA1NDY4NzUiIHk9Ijc0LjUiPjE0MHgxNDA8L3RleHQ+PC9nPjwvZz48L3N2Zz4=";

	private String request_grant_type = "client_credentials";

	private String request_scope = "read";
	
	@Value("${xdsj.auth.authAccessTokenUrl}")
	private String authAccessTokenUrl;
	
	@Autowired
	private TracerLog tracer;
	
	/**
	 * 添加应用
	 * @param suser
	 * @param app
	 * @param positions
	 */
	public void addApp(SUser suser,App app,String[] positions) {
		
		SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String appId =formatter.format(new Date());
		
		UUID uuid  =  UUID.randomUUID();
		String test_uuid = uuid.toString().replace("-", "");
		
		UUID uuid2  =  UUID.randomUUID();
		String prod_uuid = uuid2.toString().replace("-", "");
		
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		String test_client_secret = "{bcrypt}"+ bc.encode(test_uuid);
		String prod_client_secret = "{bcrypt}"+ bc.encode(prod_uuid);
		
		OauthClientDetails oauthClientDetails = new OauthClientDetails();
		oauthClientDetails.setClient_secret(test_client_secret);
		oauthClientDetails.setAuthorized_grant_types(authorized_grant_types);
		oauthClientDetails.setScope(scope);
		oauthClientDetails.setAccess_token_validity(120);
		oauthClientDetails.setRefresh_token_validity(5184000);
		oauthClientDetails = oauthClientDetailsRepository.save(oauthClientDetails);
		
		
		
		OauthClientDetails oauthClientDetails2 = new OauthClientDetails();
		oauthClientDetails2.setClient_secret(prod_client_secret);
		oauthClientDetails2.setAuthorized_grant_types(authorized_grant_types);
		oauthClientDetails2.setScope(scope);
		oauthClientDetails2.setAccess_token_validity(120);
		oauthClientDetails2.setRefresh_token_validity(5184000);
		oauthClientDetails2 = oauthClientDetailsRepository.save(oauthClientDetails2);
		
		
		String test_token = "";
		String data_test = "?grant_type="+request_grant_type+"&scope="+request_scope+"&client_id="+oauthClientDetails.getClient_id()+"&client_secret="+test_uuid;
		try {
			String result = "";
			result = HttpUtil.sendPostHttp(authAccessTokenUrl+data_test, "");
			tracer.addTag("getaccess_token_test_result", result);
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.has("access_token")) {
				test_token = jsonObj.getString("access_token");
			}
		} catch (Exception e) {
		     tracer.addTag("getaccess_token", ExUtils.getEDetail(e));
		}
		
		String prod_token = "";
		String data_prod = "?grant_type="+request_grant_type+"&scope="+request_scope+"&client_id="+oauthClientDetails2.getClient_id()+"&client_secret="+prod_uuid;
		try {
			String result = "";
			result = HttpUtil.sendPostHttp(authAccessTokenUrl+data_prod, "");
			tracer.addTag("getaccess_token_prod_result", result);
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.has("access_token")) {
				prod_token = jsonObj.getString("access_token");
			}
		} catch (Exception e) {
			tracer.addTag("getaccess_token", ExUtils.getEDetail(e));
		}
		
		
		app.setAppId(appId);
		
		app.setTest_clientId(oauthClientDetails.getClient_id());
		app.setProd_clientId(oauthClientDetails2.getClient_id());
		app.setTest_client_secret(test_uuid);
		app.setProd_client_secret(prod_uuid);
		app.setTest_token(test_token);
		app.setProd_token(prod_token);
		
		app.setsUserId(suser.getId());
		if(StringUtils.isBlank(app.getIcon())){
			app.setIcon(icon);
		}
		app = appRepository.save(app);
		String resource_ids = "";
		String authorities = "";
		if (null != positions && positions.length > 0) {
			for (int i = 0; i < positions.length; i++) {
				AppProductList appProductList = new AppProductList();
				Product product = productRepository.getOne(Long.valueOf(positions[i]));
				appProductList.setProduct(product);
				appProductList.setApp(app);
				// 默认环境为沙箱
				appProductList.setAppmode(EnvironmentDef.SANDBOX);
				appProductListRepository.save(appProductList);
				String flag = product.getFlag();
				resource_ids += flag+",";
				authorities +="AUTH_"+flag.toUpperCase()+",";
			}
		}
		if (resource_ids.length() > 1) {
			resource_ids = resource_ids.substring(0, resource_ids.length() - 1);
		}
		if (authorities.length() > 1) {
			authorities = authorities.substring(0, authorities.length() - 1);
		}
		oauthClientDetails.setResource_ids(resource_ids);
		oauthClientDetails.setAuthorities(authorities);
		oauthClientDetails = oauthClientDetailsRepository.save(oauthClientDetails);
		oauthClientDetails2.setResource_ids(resource_ids);
		oauthClientDetails2.setAuthorities(authorities);
		oauthClientDetails2 = oauthClientDetailsRepository.save(oauthClientDetails2);
		
	}
	
	/**
	 * 查询所有产品
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Product> queryProduct() {
//		List<Product> list = productRepository.findByProductStatusOrderById(ProductStatus.Online);
		List<Product> list = productRepository.findByProductStatusNotOrderById(ProductStatus.OffLine);
		return list;
	}
	/**
	 * 查询所有应用
	 * @param suser
	 * @param appName
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<App> queryApp(SUser suser,String appName) {
		List<App> list = new ArrayList<App>();
		if(appName == null){
			appName = "";
		}
		list = appRepository.findBysUserIdAndAppNameContainingOrderById(suser.getId(), appName);
		return list;
	}
	
	/**
	 * 查询应用详情
	 * @param suser
	 * @param appName
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public App queryAppdetails(SUser suser,String appId,String mode) {
		App app = appRepository.findBysUserIdAndAppId(suser.getId(), appId);
		if (mode.equals("all")) {
			return app;
		}
		List<AppProductList> productlist = app.getProductlist();
		Iterator<AppProductList> it = productlist.iterator();
		while(it.hasNext()){
			AppProductList x = it.next();
		    if(!x.getAppmode().equals(mode)){
		        it.remove();
		    }
		}
		return app;
	}
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Product> differenceSet(App app) {
		List<Product> list = productRepository.findByProductStatusNotOrderById(ProductStatus.OffLine);
		List<Product> listProduct = new ArrayList<>();
		List<AppProductList> productlist = app.getProductlist();
		for (AppProductList appProductList : productlist) {
			listProduct.add(appProductList.getProduct());
		}
		List<Product> calcuateResidualList = SetOptUtils.calcuateResidualList(list, listProduct);
		return calcuateResidualList;
	}
	
	
	/**
	 * 添加应用
	 * @param suser
	 * @param app
	 * @param positions
	 */
	public App addProduct(App app,String[] positions) {
		if (null != positions && positions.length > 0) {
			for (int i = 0; i < positions.length; i++) {
				AppProductList appProductList = new AppProductList();
				Product product = productRepository.getOne(Long.valueOf(positions[i]));
				appProductList.setProduct(product);
				appProductList.setApp(app);
				// 默认环境为沙箱
				appProductList.setAppmode(EnvironmentDef.SANDBOX);
				appProductListRepository.save(appProductList);
			}
		}
		return app;
	}
	
	
	/**
	 * 申请上线
	 * @param app
	 * @param positions
	 */
	public void applyOnline(App app,String[] positions) {
		if (null != positions && positions.length > 0) {
			app.setState(ProductStatus.Euditing);
			appRepository.save(app);
			for (int i = 0; i < positions.length; i++) {
				AppProductList appProductList = appProductListRepository.getOne(Long.valueOf(positions[i]));
				if(appProductList.getProductStatus().equals(ProductStatus.Online)||appProductList.getProductStatus().equals(ProductStatus.Euditing))
					continue;
				appProductList.setProductStatus(ProductStatus.Euditing);
				appProductListRepository.save(appProductList);
				
				AppProductList appProductListprod = new AppProductList();
				appProductListprod.setApp(app);
				appProductListprod.setProduct(appProductList.getProduct());
				appProductListprod.setProductStatus(appProductList.getProductStatus());
				appProductListprod.setAppmode(EnvironmentDef.PROD);
				appProductListRepository.save(appProductListprod);
			}
		}
	}
	
	
	/**
	 * appProductList 配置修改
	 * @param appId
	 * @param appProductListId
	 * @param taskUrl
	 * @param loginNoticeUrl
	 * @param billNoticeUrl
	 * @param reportNoticeUrl
	 * @return
	 */
	public AppProductList modifyAppproductlistdetails(String appId,Long appProductListId, String task_notice_url, String login_notice_url,
			String crawler_notice_url, String report_notice_url,String param) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = queryAppdetails(suser, appId, "all");
		AppProductList appProductList = appProductListRepository.findById(appProductListId).get();
		appProductList.setTask_notice_url(task_notice_url);
		appProductList.setLogin_notice_url(login_notice_url);
		appProductList.setCrawler_notice_url(crawler_notice_url);
		appProductList.setReport_notice_url(report_notice_url);
		List<CallbackParams> list =new ArrayList<>();
		JSONArray array=JSONArray.fromObject(param);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj=JSONObject.fromObject(array.get(i));
			if(obj!=null&&obj.containsKey("callbackParamsId1")
					&&obj.containsKey("callbackParamsKey1")
					&&obj.containsKey("callbackParamsValue1")){
				String callbackParamsId1 = obj.getString("callbackParamsId1");
				String  paramsKey = obj.getString("callbackParamsKey1");
				String  paramsValue = obj.getString("callbackParamsValue1");
				CallbackParams callbackParams = new CallbackParams();
				if(StringUtils.isNotBlank(callbackParamsId1)){
					Long id = Long.valueOf(callbackParamsId1);
					callbackParams.setId(id);
					callbackParams.setAppProductListId(appProductListId);
					callbackParams.setParamsKey(paramsKey);
					callbackParams.setParamsValue(paramsValue);
					callbackParamstRepository.save(callbackParams);
					list.add(callbackParams);
					
				}else if (StringUtils.isNotBlank(paramsKey)
						&& StringUtils.isNotBlank(paramsValue)) {
					callbackParams.setAppProductListId(appProductListId);
					callbackParams.setParamsKey(paramsKey);
					callbackParams.setParamsValue(paramsValue);
					callbackParamstRepository.save(callbackParams);
					list.add(callbackParams);
				}
			}
		}
		appProductList.setCallbackparams(list);
		appProductListRepository.save(appProductList);
		return appProductList;
	}
	

	/**
	 * 
	 * @param app
	 * @param mode
	 * @return
	 */
	public OauthClientDetails findOauthClientDetails(App app,String mode) {
		String client_id = app.getTest_clientId();
		if(EnvironmentDef.PROD.equals(mode)){
			client_id = app.getProd_clientId();
		}
		OauthClientDetails oauthClientDetails = oauthClientDetailsRepository.findById(client_id).get();
		
		return oauthClientDetails;
	}
	
	
	public Map<String, Object> requestnoticeurl(String url,String appId,Long appProductListId,String request_body_param,String param) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		
		
		
		return result;
	}
	
}
