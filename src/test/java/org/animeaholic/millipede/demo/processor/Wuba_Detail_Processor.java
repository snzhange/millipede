package org.animeaholic.millipede.demo.processor;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.animeaholic.millipede.demo.Bootstrap;
import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.factory.PlanFactory;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Wuba_Detail_Processor extends AbsCrawlResProcessor {

	@Override
	public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws Exception {
		CrawlPlan plan = PlanFactory.getPlan(crawlResTask.getPlanName());
		Bootstrap.lock.lock();
		System.out.println("********************************************************");
		System.out.println("计划：" + crawlResTask.getPlanName() + "进入详情页页处理者\r"
						+ "当前时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE) + "\r"
						+ "当前url：" + crawlResTask.getUrl() + "\r"
						+ "当前url抓取结果：http status(" + crawlResTask.getHttpResStatus() + ")\r"
						+ "作者：" + plan.getOwnConfig("author"));
		
		String content = crawlResTask.getContent();
		Document doc = Jsoup.parse(content);
		Elements eles = doc.select("#content_sumary_right > h1");
		for (Element ele : eles)
			System.out.println("帖子标题：" + ele.text() + "\r\r");
		Bootstrap.lock.unlock();
		return null; // 返回null或者空list就不会发布新的任务了
	}

}
