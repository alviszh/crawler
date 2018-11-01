package app.gongan.service;

import app.gongan.bean.reponse.TokenStatus;
import feign.Headers;
import feign.Param;
import feign.RequestLine;


interface BaseApi<V> {

	@RequestLine("GET /uaa/oauth/check_token?token={token}&appKey={appKey}")
	TokenStatus getTokenStatus(@Param("token") String token,@Param("appKey") String appKey);

}