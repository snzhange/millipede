package org.animeaholic.millipede.demo.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.animeaholic.millipede.demo.Bootstrap;
import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.factory.PlanFactory;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Wuba_List_Processor extends AbsCrawlResProcessor {
	
	private static Pattern wubaCommonURLPattern = Pattern.compile("^http://bj.58.com/ershouche/.+$");
	
	// 如果不抛出异常，那么传进来的这个crawlResTask在该方法结束后就被认为是执行成功了
	@Override
	public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws Exception  {
		List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();// 要返回的任务列表
		
		// 每个任务都传递了这个任务所属的计划名，我们可以根据计划名拿到计划的引用，随时随地读取里面的自定义配置
		CrawlPlan plan = PlanFactory.getPlan(crawlResTask.getPlanName());
		Bootstrap.lock.lock();
		System.out.println("********************************************************");
		System.out.println("计划：" + crawlResTask.getPlanName() + "进入列表页处理者\r"
						+ "当前时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE) + "\r"
						+ "当前url：" + crawlResTask.getUrl() + "\r"
						+ "当前url抓取结果：http status(" + crawlResTask.getHttpResStatus() + ")\r"
						+ "作者：" + plan.getOwnConfig("author"));
		
		String content = crawlResTask.getContent();
		Document doc = Jsoup.parse(content);
		Elements eles = doc.select("#infolist > table > tbody > tr > td.t > a.t");
		for (Element ele : eles) {
			String url = ele.attr("href");
			// 因为58的帖子分精准、置顶、推广好几种类型，解析方式不一样，有的还是跳转链接，我这里只抓一般帖，不是的跳过
			if (!wubaCommonURLPattern.matcher(url).find())
				continue;
			nextTaskList.add(this.buildCommonURLBaseTask(url, crawlResTask.getPlanName()));
		}
		System.out.println("即将抓取的详情页数量：" + nextTaskList.size() + "\r\r");
		Bootstrap.lock.unlock();
		return nextTaskList;
	}
	
	private AbsBaseTask<?> buildCommonURLBaseTask(String url, String planName) {
		CrawlTask crawlTask = new CrawlTask(planName);
		crawlTask.setCallBack(Wuba_Detail_Processor.class)
				 .setUrl(url);
		return crawlTask;
	}
	
}
