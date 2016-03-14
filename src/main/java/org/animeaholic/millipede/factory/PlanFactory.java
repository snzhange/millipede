package org.animeaholic.millipede.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 计划工厂类
 * @author snzhange
 */
public class PlanFactory {
	
	private static Map<String, CrawlPlan> planName_Plan_Map = new HashMap<String, CrawlPlan>();
	
	public static void registPlan(String planName, Class<? extends AbsBaseProcessor> call) {
		planName_Plan_Map.put(planName, new CrawlPlan(planName, call));
	}
	
	public static CrawlPlan getPlan(String planName) {
		return planName_Plan_Map.get(planName);
	}
	
	public static List<CrawlPlan> getAllPlan() {
		return new ArrayList<CrawlPlan>(planName_Plan_Map.values());
	}
	
	public static void clearPlan(String[] planNameArray) {
		List<String> planNameList = Arrays.asList(planNameArray);
		Iterator<String> it = planName_Plan_Map.keySet().iterator();
		while (it.hasNext()) { 
			String key = it.next();
			if (!planNameList.contains(key))
				it.remove();
		}
	}

}
