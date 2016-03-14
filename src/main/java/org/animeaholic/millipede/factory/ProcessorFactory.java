package org.animeaholic.millipede.factory;

import java.util.HashMap;
import java.util.Map;

import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 处理者工厂
 * @author snzhange
 */
public class ProcessorFactory {

	private static Map<Class<? extends AbsBaseProcessor>, AbsBaseProcessor> processorMap = new HashMap<Class<? extends AbsBaseProcessor>, AbsBaseProcessor>();
	
	public static void registProcessor(Class<? extends AbsBaseProcessor> cls) throws Exception {
		processorMap.put(cls, cls.newInstance());
	}
	
	public static AbsBaseProcessor getProcessor(Class<? extends AbsBaseProcessor> cls) {
		return processorMap.get(cls);
	}
	
}
