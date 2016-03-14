package org.animeaholic.millipede.entity;

import java.util.HashMap;
import java.util.Map;

import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 用于抓取完解析页面的实体类
 * @author snzhange
 */
public class CrawlResTask extends AbsBaseTask<CrawlResTask> {
	
	private String url; // 抓的url
	private int httpResStatus; // 请求这个url的结果
	private Map<String, String> httpHeadersMap = new HashMap<String, String>(); // 返回的http headers
	private String content; // 抓到的http body

	public CrawlResTask(String planName, Class<? extends AbsBaseProcessor> call) {
		super(planName, call);
	}

	public String getUrl() {
		return url;
	}

	public CrawlResTask setUrl(String url) {
		this.url = url;
		return this;
	}

	public int getHttpResStatus() {
		return httpResStatus;
	}

	public CrawlResTask setHttpResStatus(int httpResStatus) {
		this.httpResStatus = httpResStatus;
		return this;
	}
	
	public String getHttpHeader(String headerKey) {
		return this.httpHeadersMap.get(headerKey);
	}
	
	public CrawlResTask setHttpHeader(String headerKey, String headerValue) {
		this.httpHeadersMap.put(headerKey, headerValue);
		return this;
	}

	public Map<String, String> getHttpHeadersMap() {
		return httpHeadersMap;
	}

	public String getContent() {
		return content;
	}

	public CrawlResTask setContent(String content) {
		this.content = content;
		return this;
	}

}
