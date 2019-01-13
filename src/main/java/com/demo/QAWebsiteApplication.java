package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SolrAutoConfiguration.class})  // 禁用solr自动配置
public class QAWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(QAWebsiteApplication.class, args);
	}
}
