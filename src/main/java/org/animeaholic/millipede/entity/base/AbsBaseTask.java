package org.animeaholic.millipede.entity.base;

import java.util.HashMap;
import java.util.Map;

import org.animeaholic.millipede.enums.TaskStatusEnum;
import org.animeaholic.millipede.manager.RuntimeObserver;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 通用的任务实体基类
 * @author snzhange
 */
@SuppressWarnings("unchecked")
public abstract class AbsBaseTask<T> {
	
	private String planName; // 所属计划的名称
	private TaskStatusEnum status = TaskStatusEnum.PENDING; // 任务的状态
	private Class<? extends AbsBaseProcessor> call; // 这个任务由谁来执行
	private Class<? extends AbsBaseProcessor> callBack; // 这个任务执行完之后要让谁来执行
	private Map<String, Object> context = new HashMap<String, Object>(); // 上一个任务可以传递给下一个任务一些信息，简称上下文 
	private RuntimeObserver observer = RuntimeObserver.getInstance();
	
	public AbsBaseTask(String planName, Class<? extends AbsBaseProcessor> call) {
		super(); // 大括号里面如果不写点什么我混森难叟啊
		this.planName = planName;
		this.call = call;
		
		observer.update(this);
	}
	
	public T setContextAttribute(String key, Object value) {
		this.context.put(key, value);
		return (T) this;
	}
	
	public Object getContextAttibute(String key) {
		return this.context.get(key);
	}
	
	public String getPlanName() {
		return planName;
	}

	public T setPlanName(String planName) {
		this.planName = planName;
		return (T) this;
	}

	public TaskStatusEnum getStatus() {
		return status;
	}

	public T setStatus(TaskStatusEnum status) {
		this.status = status;
		
		this.observer.update(this);
		return (T) this;
	}
	
	public T setStatusWithoutNotify(TaskStatusEnum status) {
		this.status = status;
		return (T) this;
	}

	public Class<? extends AbsBaseProcessor> getCall() {
		return call;
	}

	public T setCall(Class<? extends AbsBaseProcessor> call) {
		this.call = call;
		return (T) this;
	}

	public Class<? extends AbsBaseProcessor> getCallBack() {
		return callBack;
	}

	public T setCallBack(Class<? extends AbsBaseProcessor> callBack) {
		this.callBack = callBack;
		return (T) this;
	}

	public Map<String, Object> getContext() {
		return context;
	}
	
	public T setContext(Map<String, Object> context) {
		this.context = context;
		return (T) this;
	}

}
