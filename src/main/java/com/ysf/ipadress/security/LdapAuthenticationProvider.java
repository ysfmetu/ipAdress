package com.ysf.ipadress.security;



import com.ysf.ipadress.model.Kullanici;
import com.ysf.ipadress.repo.KullaniciRepostories;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {
    private final MessageSourceAccessor message;
    private final KullaniciRepostories repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username;
        String password = "";
        com.ysf.ipadress.security.LoggedUserModel userModel;
        boolean fromToken = false;
        if (authentication.getPrincipal() instanceof  com.ysf.ipadress.security.LoggedUserModel) {
            username = (( com.ysf.ipadress.security.LoggedUserModel) authentication.getPrincipal()).getUserName();
            fromToken = true;
        } else {
            username = (String) authentication.getPrincipal();
            password = (String) authentication.getCredentials();
        }
        Optional<Kullanici> entity = repository.findByUserName(username);
            if(entity == null){
                throw new UsernameNotFoundException(message.getMessage("login.failed"));
            }


        if (!fromToken) {
            try {
                com.ysf.ipadress.security.LdapAuthenticator authenticator = com.ysf.ipadress.security.LdapAuthenticator.getInstance();
                String displayName = authenticator.authenticate(username, password);
                Long userId = repository.findByUserName(username).get().getId();
                userModel = new com.ysf.ipadress.security.LoggedUserModel(userId,username, displayName);
            } catch (NamingException e) {
                throw new UsernameNotFoundException(message.getMessage("login.failed"));
            }
        } else {
            userModel = (com.ysf.ipadress.security.LoggedUserModel) authentication.getPrincipal();
        }
        return new UsernamePasswordAuthenticationToken(userModel, password);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
