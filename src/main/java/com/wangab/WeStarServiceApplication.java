package com.wangab;

import com.wangab.filter.TokenFilter;
import com.wangab.utils.BlowfishUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class WeStarServiceApplication {
	private static final Logger log = LoggerFactory.getLogger(WeStarServiceApplication.class);

	@Value("${application.blow.key}")
	private String blowKey;

	@Bean
	public Docket testApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("users")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.pathMapping("/")// base，最终调用接口后会和paths拼接在一起
				.select()
				.paths(regex("/service.*"))//过滤的接口
				.build()
				.apiInfo(new ApiInfo(
						"WeStar新版服务接口测试",
						"WeStar新版服务接口测试",
						"1.0",
						"NO terms of service",
						new Contact("王安邦","", ""),
						"westar API project",
						"https://github.com/Wangab/WeStarService"));
	}

	@Bean
	public FilterRegistrationBean registrationFilterBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("accessTokenFilter");
		registrationBean.setFilter(new TokenFilter());
		registrationBean.setOrder(1);
		registrationBean.addUrlPatterns(
				"/service/changepwd",
				"/service/*/*/delact",
				"/service/updateact"
		);
		return registrationBean;
	}

	@Bean
	public BlowfishUtil blowfishUtil(){
		return new BlowfishUtil(blowKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeStarServiceApplication.class, args);
	}

	public String getBlowKey() {
		return blowKey;
	}

	public void setBlowKey(String blowKey) {
		this.blowKey = blowKey;
	}

	@Bean(name = "jdbcTXManager")
	public DataSourceTransactionManager txManager(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
