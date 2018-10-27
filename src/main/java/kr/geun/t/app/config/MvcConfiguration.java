package kr.geun.t.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * MVC 관련 설정
 *
 * @author akageun
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}

	/**
	 * 정적 자원에 대한 설정
	 *
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//@formatter:off
		VersionResourceResolver versionResourceResolver = new VersionResourceResolver();
		versionResourceResolver.addFixedVersionStrategy("0.0.1", "/**");

		registry
			.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/")
					.setCachePeriod(60 * 60)
					.resourceChain(true)
					.addResolver(versionResourceResolver);

		registry
			.addResourceHandler("/favicon.ico")
				.addResourceLocations("classpath:/static/")
					.setCachePeriod(60 * 60)
					.resourceChain(true)
					.addResolver(new PathResourceResolver());

		registry
			.addResourceHandler("/robots.txt")
				.addResourceLocations("classpath:/static/");
		//@formatter:on
	}
}
