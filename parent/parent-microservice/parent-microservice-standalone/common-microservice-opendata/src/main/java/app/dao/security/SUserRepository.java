package app.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.security.SUser;


public interface SUserRepository extends JpaRepository<SUser, Long> {

    SUser findByLoginNameAndPassword(String loginName, String password);

    SUser findUserByLoginName(String loginName);

}
