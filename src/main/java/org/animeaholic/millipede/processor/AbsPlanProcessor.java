package org.animeaholic.millipede.processor;

import java.util.List;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计划处理者基类
 * @author snzhange
 */
public abstract class AbsPlanProcessor extends AbsBaseProcessor {
	
	private static Logger log = LoggerFactory.getLogger(AbsPlanProcessor.class);

	@Override
	protected List<AbsBaseTask<?>> process(AbsBaseTask<?> baseTask) throws Exception {
		if (!(baseTask instanceof CrawlPlan)) {
			log.error("计划处理者不能处理计划以外的类型！请检查调用，涉及计划：" + baseTask.getPlanName());
			return null;
		}
		CrawlPlan plan = (CrawlPlan) baseTask;
		return this.process(plan);
	}
	
	public abstract List<AbsBaseTask<?>> process(CrawlPlan plan) throws Exception;

}
