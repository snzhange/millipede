package org.animeaholic.millipede.constant;

/**
 * millipede.properties主要参数名
 * @author ZhangSaiBei
 * @creation 2015年8月2日, 下午5:30:42
 */
public class MillipedeConfigConstants {
	
	public static final String KEY_MILLIPEDE_PLAN = "millipede.plan";
	
	public static final String KEY_MILLIPEDE_PROCESSORS = "millipede.processors";
	
	public static final String KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_THREAD_NUM = "millipede.DefaultCrawlProcessor.threadNum";
	
	public static final String KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_POOLNUM = "millipede.DefaultCrawlProcessor.poolSize";
	
	public static final String KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT = "millipede.DefaultCrawlProcessor.connectTimeout";
	public static int VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT = 10 * 1000;
	
	public static final String KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_READ_TIMEOUT = "millipede.DefaultCrawlProcessor.readTimeout";
	public static int VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_READ_TIMEOUT = 10 * 1000;
	
	public static final int VALUE_DEFAULT_PROCESSOR_THREAD_NUM = 10;
	public static final int VALUE_DEFAULT_PROCESSOR_POOL_SIZE = 100;

}
