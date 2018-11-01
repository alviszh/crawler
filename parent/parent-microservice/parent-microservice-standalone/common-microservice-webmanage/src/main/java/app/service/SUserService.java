package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.security.SUser;
import com.microservice.dao.repository.crawler.security.SUserRepository;

/**
 * 用户登录Service
 * @author rongshengxu
 *
 */
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.security")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.security")
public class SUserService {

    @Autowired
    private SUserRepository suserRepository;

    public List<SUser> findAll() {

        return suserRepository.findAll();

    }

    public SUser create(SUser user) {
        return suserRepository.save(user);

    }


    public SUser findUserById(Long id) {
        return suserRepository.getOne(id);

    }

    public SUser login(String loginName, String password) {
        return suserRepository.findByLoginNameAndPassword(loginName, password);

    }


    public SUser update(SUser user) {
        return suserRepository.save(user);

    }

    public void deleteUser(Long id) {
        suserRepository.deleteById(id);
    }


    public SUser findUserByLoginName(String loginName) {
        return suserRepository.findUserByLoginName(loginName);

    }


}
