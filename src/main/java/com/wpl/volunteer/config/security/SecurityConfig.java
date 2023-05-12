package com.wpl.volunteer.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier("adminServiceImpl")
//    private UserDetailsService adminServiceImpl;
//
//    @Autowired
//    @Qualifier("userServiceImpl")
//    private UserDetailsService userServiceImpl;

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//    /**
//     * 使用userDetailsService来进行账号密码验证
//     * @param auth
//     * @throws Exception
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(adminServiceImpl);
//        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
//        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
//        auth.authenticationProvider(daoAuthenticationProvider);
////        auth.userDetailsService(userDetailsService);
//    }

    // 放行和认证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

                .authorizeRequests()
                .antMatchers("/api/v1.0/out/**").permitAll()//所有人都可以访问
                .anyRequest().authenticated()//需要登陆，即需要认证

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//没有认证
                .accessDeniedHandler(jwtAccessDeniedHandler)//没有权限

                //配置自定义的过滤器=》前置过滤器
                .and()
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class);
                //.addFilterAt(a,b)默认会将a设置在b之前。
    }

    /**
     * 跨域拦截设置
     * @author 文
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        //开放哪些ip、端口、域名的访问权限，*表示开放所有域
        configuration.addAllowedOriginPattern("*");
        //允许HTTP请求中的携带哪些Header信息,*放行全部原始头信息
        configuration.addAllowedHeader("*");
        //开放哪些Http方法，允许跨域访问,*允许所有请求方法跨域调用
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        configuration.setExposedHeaders(Collections.singletonList(HttpHeaders.ACCEPT));
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);
        //使用默认跨域配置
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**");
    }
}
