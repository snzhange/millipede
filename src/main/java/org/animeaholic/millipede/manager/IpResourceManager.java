package org.animeaholic.millipede.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

import static org.animeaholic.millipede.constant.RegexPatternConstants.*;

/**
 * 根据域名访问频率控制类，防止过度抓取ip被禁
 * @author ZhangSaiBei
 * @creation 2015年9月13日, 下午3:41:53
 */
public class IpResourceManager {
	
	private static final IpResourceManager singleton = new IpResourceManager();
	
	public static IpResourceManager getInstance() {
		return singleton;
	}
	
	private Map<String, Long> host_intervalTime_Map = new HashMap<String, Long>();
	private Map<String, Long> host_lastVisitTime_Map = new HashMap<String, Long>();
	private Map<String, Lock> host_Lock_Map = new ConcurrentHashMap<String, Lock>();
	
	private IpResourceManager() {
		super();
	}
	
	public void holdIpResource(String url) throws Exception {
		Matcher hostMatcher = HOST_PATTERN.matcher(url);
		if (!hostMatcher.find())
			throw new Exception("url不正确，无法解析域名 : " + url);
		String host = hostMatcher.group(1);
		
		if (this.host_intervalTime_Map.get(host) == null)
			this.host_intervalTime_Map.put(host, 5L * 1000L);
		
		this.lockIsNULL(host);
		this.host_Lock_Map.get(host).lock();
		long intervalTime = this.host_intervalTime_Map.get(host);
		while (new Date().getTime() - (this.host_lastVisitTime_Map.get(host) == null ? 0L : this.host_lastVisitTime_Map.get(host)) < intervalTime)
			Thread.sleep(200);
	}
	
	public void releaseIpResource(String url) {
		Matcher hostMatcher = HOST_PATTERN.matcher(url);
		hostMatcher.find();
		String host = hostMatcher.group(1);
		
		this.host_lastVisitTime_Map.put(host, new Date().getTime());
		this.host_Lock_Map.get(host).unlock();
	}
	
	public void setVisitControl(String host, long intervalTime) {
		this.host_intervalTime_Map.put(host, intervalTime);
		this.host_Lock_Map.put(host, new ReentrantLock());
	}
	
	private synchronized void lockIsNULL(String host) {
		if (this.host_Lock_Map.get(host) == null)
			this.host_Lock_Map.put(host, new ReentrantLock());
	}
}
