package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing//JPA Auditing 기능을 활성화 한다.
public class AuditConfig {
	
	@Bean
	public AuditorAware<String> auditorProvider(){
		
		return new AuditorAwareImpl();
	
	}

}
