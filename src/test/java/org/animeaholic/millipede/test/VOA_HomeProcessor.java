package org.animeaholic.millipede.test;

import java.util.ArrayList;
import java.util.List;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VOA_HomeProcessor extends AbsCrawlResProcessor {
	
	private static String voa_base_url = "http://www.51voa.com/";

	@Override
	public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws Exception {
		List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();
		String html = crawlResTask.getContent();
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("#list > ul > li > a");
		for (Element ele : eles) {
			String href = ele.attr("href");
			if (!href.contains("VOA"))
				continue;
			String detailUrl = voa_base_url + href;
			CrawlTask task = new CrawlTask(crawlResTask.getPlanName());
			task.setCallBack(VOA_DetailProcessor.class);
			task.setUrl(detailUrl);
			nextTaskList.add(task);
		}
		return nextTaskList;
	}

}
