package kr.co.tbase.searchad.config.auth;

import kr.co.tbase.searchad.entity.Role;
import kr.co.tbase.searchad.handler.LoginSuccessHandler;
import kr.co.tbase.searchad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // h2-console 화면을 사용하기 위한 비활성화 옵션
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    // url별 권한 관리를 설정하는 옵션의 시작점이 authorizeRequests
                    .authorizeRequests()
                    .antMatchers("/search/**", "/host/**", "/relatedwords/**", "/login", "/join", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 해당 url엔 인증되지 않은 사용자 포함해서 전체에게 허용
                    .antMatchers("/admin/users").hasRole(Role.ADMIN.name()) // ADMIN의 ROLE을 가지고 있는 인증된 사용자만 해당 url 접근 허용
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/user/login")
                    .usernameParameter("uid")
                    .defaultSuccessUrl("/search")
                    .successHandler(new LoginSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
