package org.animeaholic.millipede.test;

import java.util.ArrayList;
import java.util.List;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsPlanProcessor;

public class VOA_PlanProcessor extends AbsPlanProcessor {

	@Override
	public List<AbsBaseTask<?>> process(CrawlPlan plan) throws Exception {
		List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();
		String seedUrl = plan.getOwnConfig("seedUrl");
		CrawlTask task = new CrawlTask(plan.getPlanName());
		task.setCallBack(VOA_HomeProcessor.class);
		task.setUrl(seedUrl);
		nextTaskList.add(task);
		return nextTaskList;
	}

}
