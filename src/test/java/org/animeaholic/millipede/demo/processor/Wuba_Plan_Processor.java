package org.animeaholic.millipede.demo.processor;

import java.util.ArrayList;
import java.util.List;

import org.animeaholic.millipede.demo.Bootstrap;
import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsPlanProcessor;

public class Wuba_Plan_Processor extends AbsPlanProcessor {

	@Override
	public List<AbsBaseTask<?>> process(CrawlPlan plan) throws Exception {
		List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();// 要返回的任务列表
		
		Bootstrap.lock.lock();
		System.out.println("********************************************************");
		System.out.println("计划：" + plan.getPlanName() + " 进入计划处理者, 作者：" + plan.getOwnConfig("author") + "\r\r");
		Bootstrap.lock.unlock();
		
		// 创建一个抓取任务
		CrawlTask crawlTask = new CrawlTask(plan.getPlanName());
		crawlTask.setCallBack(Wuba_List_Processor.class) // 设置抓取任务抓完以后由谁处理
				 .setUrl(plan.getOwnConfig("seedURL")); // 抓取哪个链接
		
		nextTaskList.add(crawlTask);
		return nextTaskList;
	}

}
