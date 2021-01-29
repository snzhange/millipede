package org.animeaholic.millipede.huadian;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginProcessor extends AbsCrawlResProcessor {

    @Override
    public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws UnsupportedEncodingException {
        System.out.println("------ login done ------");

        CrawlTask crawlTask = new CrawlTask(crawlResTask.getPlanName());
        crawlTask.setCallBack(ContentProcessor.class);
        crawlTask.setHttpHeader("Host", "hd.cdqindao.com:8089");
        crawlTask.setHttpHeader("Accept", "*/*");
        crawlTask.setHttpHeader("X-Requested-With", "XMLHttpRequest");
        crawlTask.setHttpHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        crawlTask.setHttpHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        crawlTask.setHttpHeader("Origin", "http://hd.cdqindao.com:8089");
        crawlTask.setHttpHeader("Referer", "http://hd.cdqindao.com:8089/Specialanswer");
        crawlTask.setHttpHeader("Accept-Language", "zh-CN,zh;q=0.9");
        crawlTask.setHttpHeader("Cookie", crawlResTask.getHttpHeader("Set-Cookie"));
        crawlTask.setContextAttribute("Cookie", crawlResTask.getHttpHeader("Set-Cookie"));
        crawlTask.setHttpMethod(HttpMethod.POST);
        crawlTask.setUrl("http://hd.cdqindao.com:8089/Daily");
        BasicNameValuePair basicNameValuePair = new BasicNameValuePair("type", "GetQuestionType");
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(Collections.singletonList(basicNameValuePair), CharEncoding.UTF_8);
        crawlTask.setHttpEntity(urlEncodedFormEntity);

        List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();
        nextTaskList.add(crawlTask);
        return nextTaskList;
    }

}
