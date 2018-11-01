package test.api;

import app.gongan.service.GongAnClient;
import feign.Feign; 

public class GonganTest {

	public static void main(String[] args) {
		
		/*GongAnClient gongAnClient = Feign.builder().encoder(new FormEncoder())
                .decoder(new FeignDecoder()).target(GongAnClient.class, "http://10.150.33.112:9003"); */
		/*
		String token = "2ba16f69-4b9a-4623-9c4e-2ca453989d39";
		IdAuthRequest idAuthRequest = new IdAuthRequest();
		idAuthRequest.setCustomerCretNum("420106198410028419");
		idAuthRequest.setCustomerName("梅荻");
		
		IdAuthBean idAuthBean = gongAnClient.getIdAuthBean(idAuthRequest);
		
		idAuthBean.getResult();
		*/
		
	}

}
