package org.animeaholic.millipede.huadian;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PracticeProcessor extends AbsCrawlResProcessor {

    @Override
    public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws UnsupportedEncodingException {
        System.out.println("-------- PracticeProcessor done --------");
        List<AbsBaseTask<?>> list = new ArrayList<>();
        Document doc = Jsoup.parse(crawlResTask.getContent());
        Elements eles = doc.select("#datadiv > div > div:nth-child(1) > div > div > div");
        for (Element ele : eles) {
            String questionId = ele.attr("question");
            CrawlTask crawlTask = new CrawlTask(crawlResTask.getPlanName());
            crawlTask.setHttpMethod(HttpMethod.POST);
            crawlTask.setCallBack(AnswerProcessor.class);
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("type", "ckda"));
            parameters.add(new BasicNameValuePair("question", questionId));
            System.out.println(questionId);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, CharEncoding.UTF_8);
            crawlTask.setHttpEntity(urlEncodedFormEntity);
            crawlTask.setUrl("http://hd.cdqindao.com:8089/Daily");
            crawlTask.setHttpHeader("Proxy-Connection", "keep-alive");
            crawlTask.setHttpHeader("Host", "hd.cdqindao.com:8089");
            crawlTask.setHttpHeader("Accept", "*/*");
            crawlTask.setHttpHeader("X-Requested-With", "XMLHttpRequest");
            crawlTask.setHttpHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
            crawlTask.setHttpHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            crawlTask.setHttpHeader("Origin", "http://hd.cdqindao.com:8089");
            crawlTask.setHttpHeader("Referer", crawlResTask.getUrl());
            crawlTask.setHttpHeader("Accept-Language", "zh-CN,zh;q=0.9");
            crawlTask.setHttpHeader("Cookie", (String) crawlResTask.getContextAttibute("Cookie"));
            crawlTask.setContext(crawlResTask.getContext());
            list.add(crawlTask);
        }
        return list;
    }

}
