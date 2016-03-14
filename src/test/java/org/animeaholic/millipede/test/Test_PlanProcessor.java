package org.animeaholic.millipede.test;

import java.util.List;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsPlanProcessor;

public class Test_PlanProcessor extends AbsPlanProcessor {

	@Override
	public List<AbsBaseTask<?>> process(CrawlPlan plan) throws Exception {
		System.out.println(plan.getOwnConfig("bigbang"));
		return null;
	}

}
