package com.fw.app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.fw.filter.AccessFilter;
import com.fw.provider.MyFallbackProvider;
import com.fw.rule.WeightedMetadataRule;
import com.netflix.loadbalancer.IRule;

@EnableZuulProxy
@SpringCloudApplication
public class Application {
	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

//	@Bean
//	public AccessFilter accessFilter() {
//		return new AccessFilter();
//	}
//
	@Bean
	public MyFallbackProvider myFallbackProvider() {
		return new MyFallbackProvider();
	}

	@Bean
	public IRule weightedMetadataRule() {
		return new WeightedMetadataRule();
	}
}
