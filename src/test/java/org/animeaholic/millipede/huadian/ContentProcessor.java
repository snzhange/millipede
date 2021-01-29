package org.animeaholic.millipede.huadian;

import com.alibaba.fastjson.JSON;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.huadian.entity.Content;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;

import java.util.ArrayList;
import java.util.List;

public class ContentProcessor extends AbsCrawlResProcessor {

    @Override
    public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) {
        System.out.println("------ content done ------");
        System.out.println(crawlResTask.getContent());
        List<Content> contents = JSON.parseArray(crawlResTask.getContent(), Content.class);
        List<AbsBaseTask<?>> list = new ArrayList<>();
        for (Content content : contents) {
            CrawlTask crawlTask = new CrawlTask(crawlResTask.getPlanName());
            crawlTask.setCallBack(SpecialanswerProcessor.class);
            crawlTask.setUrl("http://hd.cdqindao.com:8089/Specialanswer?TypeValue=" + content.getTypeName());
            crawlTask.setHttpHeader("Proxy-Connection", "keep-alive");
            crawlTask.setHttpHeader("Host", "hd.cdqindao.com:8089");
            crawlTask.setHttpHeader("Accept", "*/*");
            crawlTask.setHttpHeader("X-Requested-With", "XMLHttpRequest");
            crawlTask.setHttpHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
            crawlTask.setHttpHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            crawlTask.setHttpHeader("Origin", "http://hd.cdqindao.com:8089");
            crawlTask.setHttpHeader("Referer", "http://hd.cdqindao.com:8089/Specialanswer");
            crawlTask.setHttpHeader("Accept-Language", "zh-CN,zh;q=0.9");
            crawlTask.setHttpHeader("Cookie", (String) crawlResTask.getContextAttibute("Cookie"));
            crawlTask.setContext(crawlResTask.getContext());
            list.add(crawlTask);
        }
        return list;
    }

}
