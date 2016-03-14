package org.animeaholic.millipede.processor;

import static org.animeaholic.millipede.constant.MillipedeConfigConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.manager.IpResourceManager;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 默认的抓取处理者
 * @author snzhange
 */
public class DefaultCrawlProcessor extends AbsBaseProcessor {
	
	private static Logger log = LoggerFactory.getLogger(DefaultCrawlProcessor.class);
	private static HttpClient httpClient = HttpClients.custom()
			.setDefaultRequestConfig(RequestConfig.custom()
					.setConnectionRequestTimeout(VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT)
					.setConnectTimeout(VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT)
					.setSocketTimeout(VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_READ_TIMEOUT)
					.build())
			.build();
	
	private IpResourceManager ipResourceManager = IpResourceManager.getInstance();
	
	@Override
	protected List<AbsBaseTask<?>> process(AbsBaseTask<?> baseTask) throws Exception {
		List<AbsBaseTask<?>> crawlResTaskList = new ArrayList<AbsBaseTask<?>>(1);
		CrawlTask crawlTask = (CrawlTask) baseTask;
		try {
			ipResourceManager.holdIpResource(crawlTask.getUrl());
			HttpUriRequest get = this.assembleRequest(crawlTask);
			
			HttpResponse res = null;
			for (int i = 0; i < 3; i++) {
				try {
					res = httpClient.execute(get);
					if (res != null)
						break;
				} catch (Exception e) {
					log.warn("try time " + (i+1) + StringUtils.SPACE + crawlTask.getUrl() + StringUtils.SPACE + e.getMessage());
				}
			}
			
			crawlResTaskList.add(this.assembleCrawlResTask(res, crawlTask));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			ipResourceManager.releaseIpResource(crawlTask.getUrl());
		}
		return crawlResTaskList;
	}
	
	private HttpUriRequest assembleRequest(CrawlTask crawlTask) throws Exception {
		HttpUriRequest request = null;
		switch (crawlTask.getHttpMethod()) {
		case GET:
			request = new HttpGet(crawlTask.getUrl());
			break;
		case POST:
			request = new HttpPost(crawlTask.getUrl());
			break;
		case HEAD:
			// TODO : 加入head方法的支持
			break;
		case OPTIONS:
			// TODO : 加入options方法的支持
			break;
		default:
			new Exception("crawlTask " + JSON.toJSONString(crawlTask) + " httpMethod is invalid!");
		}
		if (request == null)
			new Exception("crawlTask " + JSON.toJSONString(crawlTask) + " canot be transfer a request!");
		
		for (Entry<String, String> en : crawlTask.getHttpHeadersMap().entrySet())
			request.setHeader(en.getKey(), en.getValue());
		return request;
	}
	
	private CrawlResTask assembleCrawlResTask(HttpResponse res, CrawlTask crawlTask) throws Exception {
		CrawlResTask crawlResTask = new CrawlResTask(crawlTask.getPlanName(), crawlTask.getCallBack());
		crawlResTask.setUrl(crawlTask.getUrl())
					.setHttpResStatus(res.getStatusLine().getStatusCode())
					.setContent(EntityUtils.toString(res.getEntity()))
					.setContext(crawlTask.getContext());
		for (Header header : res.getAllHeaders())
			crawlResTask.setHttpHeader(header.getName(), header.getValue());
		return crawlResTask;
	}

}
