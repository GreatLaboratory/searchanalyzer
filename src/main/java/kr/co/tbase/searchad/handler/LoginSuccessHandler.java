package kr.co.tbase.searchad.handler;

import kr.co.tbase.searchad.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication) throws IOException, ServletException {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        authorities.forEach(authority -> {
            if(authority.getAuthority().equals(Role.USER.getKey())) { // 1-1. 로그인 성공한 유저의 권한이 USER이면,
                try {
                    redirectStrategy.sendRedirect(arg0, arg1, "/search"); // 1-2. /search 페이지로 이동
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equals(Role.ADMIN.getKey())) { // 2-1. 로그인 성공한 유저의 권한이 ADMIN이면,
                try {
                    redirectStrategy.sendRedirect(arg0, arg1, "/admin/users"); // 2-2. /admin/users 페이지로 이동
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalStateException();
            }
        });

    }
}
