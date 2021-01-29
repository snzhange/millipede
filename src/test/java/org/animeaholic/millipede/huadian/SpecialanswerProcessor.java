package org.animeaholic.millipede.huadian;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.lang3.CharEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SpecialanswerProcessor extends AbsCrawlResProcessor {

    private static String base_url = "http://hd.cdqindao.com:8089/";

    @Override
    public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws UnsupportedEncodingException {
        System.out.println("------- Specialanswer done --------");
        List<AbsBaseTask<?>> list = new ArrayList<>();
        Document doc = Jsoup.parse(crawlResTask.getContent());
        Elements eles = doc.select("#dom > div > div > div > div > div > div > a");
        for (Element ele : eles) {
            String href = ele.attr("href");
            CrawlTask crawlTask = new CrawlTask(crawlResTask.getPlanName());
            crawlTask.setCallBack(PracticeProcessor.class);
            String Question_ZJName = href.substring(href.indexOf("Question_ZJName=") + "Question_ZJName=".length());
            String encode = URLEncoder.encode(Question_ZJName, CharEncoding.UTF_8);
            System.out.println(Question_ZJName);
            crawlTask.setUrl(base_url + "Practice?type=zxdt&&Question_ZJName=" + encode);
            System.out.println(crawlTask.getUrl());
            crawlTask.setHttpHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
            crawlTask.setHttpHeader("Cookie", (String) crawlResTask.getContextAttibute("Cookie"));
            crawlTask.setContext(crawlResTask.getContext());
            list.add(crawlTask);
        }
        return list;
    }

}
