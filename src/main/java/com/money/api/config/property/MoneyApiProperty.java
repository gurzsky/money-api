package com.money.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("money")
public class MoneyApiProperty {
	
	private String originAllowed = "http://localhost:8000";
	
	private final Security security = new Security();
	
	public Security getSecurity() {
		return security;
	}	

	public String getOriginAllowed() {
		return originAllowed;
	}
	
	public static class Security {
		
		private boolean enableHttps;
		
		public boolean isEnableHttps() {
			return enableHttps;
		}
		
		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
}

