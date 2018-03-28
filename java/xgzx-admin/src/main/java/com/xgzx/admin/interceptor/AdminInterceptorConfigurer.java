package com.xgzx.admin.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AdminInterceptorConfigurer 
        extends WebMvcConfigurerAdapter {

	// 要加bean注解，否则报错@Autowired为null，因为拦截器加载时间点在springcontext之前，
	// 使用bean注解提前加载
	@Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链        
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}