package org.animeaholic.millipede.entity;

import java.util.Properties;

import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 抓取计划实体类
 * @author snzhange
 */
public class CrawlPlan extends AbsBaseTask<CrawlPlan> {
	
	private Properties planOwnConfigs = new Properties();
	
	public CrawlPlan(String planName, Class<? extends AbsBaseProcessor> call) {
		super(planName, call);
	}
	
	public String getOwnConfig(String key) {
		return this.planOwnConfigs.getProperty(key);
	}
	
	public CrawlPlan setOwnConfig(String key, String value) {
		this.planOwnConfigs.setProperty(key, value);
		return this;
	}

}
