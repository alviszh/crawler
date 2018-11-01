package app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import app.entity.security.SUser;
import app.entity.security.SecurityUser;




/**
 * Spring Security 用户信息Service
 * @author rongshengxu
 *
 */
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SUserService suserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SUser user = suserService.findUserByLoginName(userName); 
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        }
        SecurityUser securityUser = new SecurityUser(user);
        return securityUser;

    }

}
