package org.animeaholic.millipede.constant;

import java.util.regex.Pattern;

/**
 * millipede中主要用到的正则
 * @author ZhangSaiBei
 * @creation 2015年8月2日, 下午5:41:33
 */
public class RegexPatternConstants {
	
	/**
	 * 匹配：(连续的数字)
	 */
	public static final Pattern KUOHAO_WITH_NUM_PATTERN = Pattern.compile("\\(\\d+\\)");
	
	/**
	 * 匹配：连续的数字
	 */
	public static final Pattern NUM_PATTERN = Pattern.compile("\\d+");
	
	/**
	 * 匹配：.xxxxxx.xxxx，取group(1)则能取到xxxxxx.xxxx<br/>
	 * 例：www.58.com，匹配到.58.com，取group(1)为58.com
	 */
	public static final Pattern HOST_PATTERN = Pattern.compile("\\.(\\w+\\.\\w+)");
	
	/**
	 * 严格匹配：xxxx.xxxxx，也就是严格意义上的host
	 */
	public static final Pattern HOST_WITHOUT_WWW_PATTERN = Pattern.compile("^\\w+\\.\\w+$");
	
	/**
	 * 用于自定义配置匹配：millipede.plan.xxxxxx.xxxx，取group(1)则取到planName，取group(2)则取到自定义配置<br/>
	 * 例：millipede.plan.wubaershouche.myconfig，匹配到millipede.plan.wubaershouche.myconfig，取group(1)为wubaershouche，取group(2)为myconfig
	 */
	public static final Pattern PLAN_OWN_CONFIG_PATTERN = Pattern.compile("^millipede\\.plan\\.(\\w+)\\.(.+)");
	
	/**
	 * 用于匹配计划的处理者
	 */
	public static final Pattern PLAN_PROCESSOR = Pattern.compile("^millipede\\.plan\\.(\\w+)\\.processor");
	
	/**
	 * 用于匹配访问频率控制：millipede.visitControl.任意域名，取group(1)则取到url，再用HOST_PATTERN进行匹配获得域名
	 */
	public static final Pattern VISIT_CONTROL_PATTERN = Pattern.compile("^millipede\\.visitControl\\.(.+)");
	
}
