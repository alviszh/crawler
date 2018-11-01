package Test;

import java.net.URLEncoder;

public class TestUrl {

	public static void main(String[] args) throws Exception {
		String encode = URLEncoder.encode("{header={'code'=0,'message'={'title'='','detail'=''}},body={dataStores={''={rowSet={'primary'=[],'filter'=[],'delete'=[]},name='',pageNumber=1,pageSize=10,recordCount=0,rowSetName='nn_apply.web_v_ac20',condition='[WEB_V_AC20_AAC001]='13955854' and WEB_V_AC20.AAE114 ='1''}},parameters={'synCount'='true'}}}", "UTF-8");
		System.out.println(encode);
	}
}
