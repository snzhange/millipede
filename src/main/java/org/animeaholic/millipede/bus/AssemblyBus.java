package org.animeaholic.millipede.bus;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.enums.TaskStatusEnum;
import org.animeaholic.millipede.factory.PlanFactory;
import org.animeaholic.millipede.factory.ProcessorFactory;
import org.animeaholic.millipede.factory.TaskPool;
import org.animeaholic.millipede.manager.IpResourceManager;
import org.animeaholic.millipede.manager.RuntimeObserver;
import org.animeaholic.millipede.processor.DefaultCrawlProcessor;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;
import org.animeaholic.millipede.processor.base.ProcessThread;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.animeaholic.millipede.constant.MillipedeConfigConstants.*;
import static org.animeaholic.millipede.constant.RegexPatternConstants.*;

/**
 * 工作总线，也是装配车间，主要工作是自动读取给定路径下的配置，自动初始化抓取所需要的一切<br/>
 * 入口方法startWork();参数millipede.properties的所在文件夹路径（可选）
 * @author ZhangSaiBei
 * @creation 2015年8月2日, 下午5:38:53
 */
@SuppressWarnings("unchecked")
public class AssemblyBus {

	private static final Logger log = LoggerFactory.getLogger(AssemblyBus.class);

	private RuntimeObserver observer = RuntimeObserver.getInstance();
	private Properties properties = new Properties();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private List<Thread> processThreadList = new ArrayList<Thread>();

	public AssemblyBus(String... propertiesAbsPath) throws Exception {
		this.observer.watch(this);
		
		log.info("****** init AssemblyBus... ******");
		InputStream millipedeConfigInputStream = null;
		if (propertiesAbsPath == null || propertiesAbsPath.length < 1)
			millipedeConfigInputStream = this.getClass().getClassLoader().getResourceAsStream("millipede.properties");
		else
			millipedeConfigInputStream = FileUtils.openInputStream(new File(propertiesAbsPath[0] + "/millipede.properties"));
		this.properties.load(millipedeConfigInputStream);
		this.initConfigConstants();
		this.initIpManager();
		this.initAllProcessors();
		this.initAllPlans();
	}
	
	public synchronized void startWork() {
		for (Thread processThread : this.processThreadList)
			this.exec.submit(processThread);
		
		for (CrawlPlan planTask : PlanFactory.getAllPlan())
			try {
				TaskPool.addTask(planTask);
			} catch (Exception e) {
				log.error("add plan error ! planName : " + planTask.getPlanName());
			}
	}
	
	public synchronized void restartWork() {
		for (CrawlPlan planTask : PlanFactory.getAllPlan())
			try {
				if (planTask.getStatus().equals(TaskStatusEnum.SUCCESS)) {
					planTask.setStatus(TaskStatusEnum.PENDING);
					TaskPool.addTask(planTask);
				}
			} catch (Exception e) {
				log.error("add plan error ! planName : " + planTask.getPlanName());
			}
	}
	
	private synchronized void initConfigConstants() {
		String defaltConnectTimeoutStr = this.properties.getProperty(KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT);
		if (!StringUtils.isBlank(defaltConnectTimeoutStr))
			VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_CONNECT_TIMEOUT = Integer.parseInt(defaltConnectTimeoutStr);
		
		String defaltReadTimeoutStr = this.properties.getProperty(KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_READ_TIMEOUT);
		if (!StringUtils.isBlank(defaltReadTimeoutStr))
			VALUE_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_READ_TIMEOUT = Integer.parseInt(defaltReadTimeoutStr);
	}

	private synchronized void initAllProcessors() throws Exception {
		try {
			this.initCustomProcessor();
			this.initDefaultProcessor();
		} catch (Exception e) {
			log.error("init failed, please check 'millipede.processors' value in your millipede.properties !");
			throw e;
		}
	}
	
	private void initCustomProcessor() throws Exception {
		String proLine = this.properties.getProperty(KEY_MILLIPEDE_PROCESSORS);
		String[] proStrArray = proLine.split(";");
		for (String proStr : proStrArray) {
			String className = proStr;
			int threadNum = VALUE_DEFAULT_PROCESSOR_THREAD_NUM;
			int poolNum = VALUE_DEFAULT_PROCESSOR_POOL_SIZE;
			
			if (proStr.contains("(")) {
				className = proStr.substring(0, proStr.indexOf("("));
				Matcher kuohaoMatcher = KUOHAO_WITH_NUM_PATTERN.matcher(proStr);
				kuohaoMatcher.find();
				Matcher threadNumMatcher = NUM_PATTERN.matcher(kuohaoMatcher.group(0));
				threadNumMatcher.find();
				String threadNumStr = threadNumMatcher.group();
				if (!StringUtils.isBlank(threadNumStr))
					threadNum = Integer.parseInt(threadNumStr);
				if (kuohaoMatcher.groupCount() > 1) {
					Matcher poolNumMatcher = NUM_PATTERN.matcher(kuohaoMatcher.group(1));
					poolNumMatcher.find();
					String poolNumStr = poolNumMatcher.group();
					poolNum = Integer.parseInt(poolNumStr);
				}
			}
			
			Class<? extends AbsBaseProcessor> cls = (Class<? extends AbsBaseProcessor>) Class.forName(className);
			ProcessorFactory.registProcessor(cls);
			TaskPool.createTaskQueue(cls, poolNum);
			for (int i = 0; i < threadNum; i++)
				this.processThreadList.add(new ProcessThread(ProcessorFactory.getProcessor(cls)));
		}
	}
	
	private void initDefaultProcessor() throws Exception {
		int threadNum = VALUE_DEFAULT_PROCESSOR_THREAD_NUM;
		int poolNum = VALUE_DEFAULT_PROCESSOR_POOL_SIZE;
		
		String threadNumStr = this.properties.getProperty(KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_THREAD_NUM);
		if (!StringUtils.isBlank(threadNumStr))
			threadNum = Integer.parseInt(threadNumStr);
		String poolNumStr = this.properties.getProperty(KEY_MILLIPEDE_DEFAULT_CRAWL_PROCESSOR_POOLNUM);
		if (!StringUtils.isBlank(poolNumStr))
			poolNum = Integer.parseInt(poolNumStr);
		
		ProcessorFactory.registProcessor(DefaultCrawlProcessor.class);
		TaskPool.createTaskQueue(DefaultCrawlProcessor.class, poolNum);
		for (int j = 0; j < threadNum; j++)
			this.processThreadList.add(new ProcessThread(ProcessorFactory.getProcessor(DefaultCrawlProcessor.class)));
	}
	
	private synchronized void initAllPlans() throws Exception {
		for (Entry<Object, Object> en : this.properties.entrySet()) {
			String proKey = String.valueOf(en.getKey());
			Matcher planProcessorMatcher = PLAN_PROCESSOR.matcher(proKey);
			if (planProcessorMatcher.find()) {
				String planName = planProcessorMatcher.group(1);
				String planProcessorClsStr = String.valueOf(en.getValue());
				Class<? extends AbsBaseProcessor> planProcessorCls = (Class<? extends AbsBaseProcessor>) Class.forName(planProcessorClsStr);
				PlanFactory.registPlan(planName, planProcessorCls);
			}
		}
		
		String planLine = this.properties.getProperty(KEY_MILLIPEDE_PLAN);
		String[] planStrArray = planLine.split(";");
		PlanFactory.clearPlan(planStrArray);
		
		for (Entry<Object, Object> en : this.properties.entrySet()) {
			String proKey = String.valueOf(en.getKey());
			Matcher ownConfigMatcher = PLAN_OWN_CONFIG_PATTERN.matcher(proKey);
			if (ownConfigMatcher.find()) {
				String planName = ownConfigMatcher.group(1);
				String ownConfigKey = ownConfigMatcher.group(2);
				CrawlPlan plan = PlanFactory.getPlan(planName);
				if (plan != null)
					plan.setOwnConfig(ownConfigKey, String.valueOf(en.getValue()));
			}
		}
	}
	
	private synchronized void initIpManager() {
		for (Entry<Object, Object> en : this.properties.entrySet()) {
			String proKey = String.valueOf(en.getKey());
			Matcher visitControlMatcher = VISIT_CONTROL_PATTERN.matcher(proKey);
			if (visitControlMatcher.find()) {
				if (HOST_WITHOUT_WWW_PATTERN.matcher(visitControlMatcher.group(1)).find()) {
					String host = visitControlMatcher.group(1);
					long intervalTime = Long.parseLong(String.valueOf(en.getValue()));
					IpResourceManager.getInstance().setVisitControl(host, intervalTime);
				} else {
					Matcher hostMatcher = HOST_PATTERN.matcher(visitControlMatcher.group(1));
					if (hostMatcher.find()) {
						String host = hostMatcher.group(1);
						long intervalTime = Long.parseLong(String.valueOf(en.getValue()));
						IpResourceManager.getInstance().setVisitControl(host, intervalTime);
					}
				}
			}
		}
	}

}
