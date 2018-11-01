package app.entity.oauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * oauth
 * @author tz
 *
 */
@Entity
@Table(name = "oauth_client_details")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class OauthClientDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5205534395454807471L;

	private String client_id;
	
	/**
	 * 产品集合，如：mobile,pbccrc（逗号隔开）
	 */
	private String resource_ids;
	
	/**
	 * 秘钥
	 */
	private String client_secret;
	
	private String scope;
	
	private String authorized_grant_types;

	private String web_server_redirect_uri;
	
	private String authorities;

	/**
	 * access_token
	 */
	private int access_token_validity;
	
	/**
	 * refresh_token
	 */
	private int refresh_token_validity;
	
	private String additional_information;
	
	private String autoapprove;

	
	@Id  
	@GeneratedValue(generator = "jpa-uuid")
    @Column(length = 256)
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@Column(length = 256)
	public String getResource_ids() {
		return resource_ids;
	}

	public void setResource_ids(String resource_ids) {
		this.resource_ids = resource_ids;
	}

	@Column(length = 256)
	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	@Column(length = 256)
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(length = 256)
	public String getAuthorized_grant_types() {
		return authorized_grant_types;
	}

	public void setAuthorized_grant_types(String authorized_grant_types) {
		this.authorized_grant_types = authorized_grant_types;
	}

	@Column(length = 256)
	public String getWeb_server_redirect_uri() {
		return web_server_redirect_uri;
	}

	public void setWeb_server_redirect_uri(String web_server_redirect_uri) {
		this.web_server_redirect_uri = web_server_redirect_uri;
	}

	@Column(length = 256)
	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public int getAccess_token_validity() {
		return access_token_validity;
	}

	public void setAccess_token_validity(int access_token_validity) {
		this.access_token_validity = access_token_validity;
	}

	public int getRefresh_token_validity() {
		return refresh_token_validity;
	}

	public void setRefresh_token_validity(int refresh_token_validity) {
		this.refresh_token_validity = refresh_token_validity;
	}

	@Column(length = 4096)
	public String getAdditional_information() {
		return additional_information;
	}

	public void setAdditional_information(String additional_information) {
		this.additional_information = additional_information;
	}

	@Column(length = 256)
	public String getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}
	
	

}