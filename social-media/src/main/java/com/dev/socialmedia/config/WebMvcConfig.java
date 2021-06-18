package com.dev.socialmedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{ // web 설정 파일
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			WebMvcConfigurer.super.addResourceHandlers(registry);
			
			// C:/dev/workspace/springbootwork/upload/
			registry
				.addResourceHandler("/upload/**") // jsp페이지에서 /upload/** 주소 페이지 나오면 아래 경로로 연결
				.addResourceLocations("file:///"+uploadFolder)
				.setCachePeriod(60*10*6) // 한시간
				.resourceChain(true)
				.addResolver(new PathResourceResolver());
		}
	
}
