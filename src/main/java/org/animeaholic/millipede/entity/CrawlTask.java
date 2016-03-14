package org.animeaholic.millipede.entity;

import java.util.HashMap;
import java.util.Map;

import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.DefaultCrawlProcessor;

import com.gargoylesoftware.htmlunit.HttpMethod;

/**
 * 抓取实体类，等同于一个request
 * @author snzhange
 */
public class CrawlTask extends AbsBaseTask<CrawlTask> {
	
	private String url; // 要抓的url
	private HttpMethod httpMethod = HttpMethod.GET;
	private Map<String, String> httpHeadersMap = new HashMap<String, String>(); // 请求时可能需要的http header
	
	public CrawlTask(String planName) {
		super(planName, DefaultCrawlProcessor.class);
	}

	public String getUrl() {
		return url;
	}

	public CrawlTask setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public CrawlTask setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}
	
	public String getHttpHeader(String headerKey) {
		return this.httpHeadersMap.get(headerKey);
	}
	
	public CrawlTask setHttpHeader(String headerKey, String headerValue) {
		this.httpHeadersMap.put(headerKey, headerValue);
		return this;
	}

	public Map<String, String> getHttpHeadersMap() {
		return httpHeadersMap;
	}

}
