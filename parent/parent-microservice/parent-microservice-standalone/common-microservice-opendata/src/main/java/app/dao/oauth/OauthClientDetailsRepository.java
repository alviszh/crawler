package app.dao.oauth;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.oauth.OauthClientDetails;


public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, String> {
	
}
