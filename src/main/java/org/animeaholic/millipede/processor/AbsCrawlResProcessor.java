package org.animeaholic.millipede.processor;

import java.util.List;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 页面解析处理者基类
 * @author snzhange
 */
public abstract class AbsCrawlResProcessor extends AbsBaseProcessor {

	@Override
	protected List<AbsBaseTask<?>> process(AbsBaseTask<?> baseTask) throws Exception {
		return this.process((CrawlResTask) baseTask);
	}
	
	public abstract List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws Exception;
	
}
