package self.spring.cloud.sample.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;

import self.spring.cloud.sample.interceptor.ShadowDataBaseInterceptor;

@Configuration
@AutoConfigureAfter(PageHelperAutoConfiguration.class)
public class ShadowDataBaseConfig {

	@Bean
	ShadowDataBaseInterceptor addImgDataBaseInterceptor(){
		return new ShadowDataBaseInterceptor();
	}
}
