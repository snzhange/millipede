package org.animeaholic.millipede.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.animeaholic.millipede.bus.AssemblyBus;
import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.enums.TaskStatusEnum;
import org.animeaholic.millipede.factory.PlanFactory;

import com.alibaba.fastjson.JSON;

/**
 * 计划及任务监控类
 * @author ZhangSaiBei
 * @creation 2015年9月18日, 下午8:22:04
 */
@SuppressWarnings("unused")
public class RuntimeObserver {
	
	private static final RuntimeObserver singleton = new RuntimeObserver();
	
	public static RuntimeObserver getInstance() {
		return singleton;
	}
	
	private long pendingPlanNum;
	private long finishedPlanNum;
	private long pendingTaskNum;
	private long successTaskNum;
	private long failedTaskNum;
	private AssemblyBus assemblyBus;
	private Map<String, PlanRuntimeStatics> planName_Statics = new HashMap<String, RuntimeObserver.PlanRuntimeStatics>();
	
	private RuntimeObserver() {
		super();
	}
	
	public void watch(AssemblyBus assemblyBus) {
		this.assemblyBus = assemblyBus;
	}
	
	public synchronized void update(AbsBaseTask<?> baseTask) {
		this.staticsGlobal(baseTask);
		this.staticsForPlan(baseTask);
		
		PlanRuntimeStatics planStatics = this.planName_Statics.get(baseTask.getPlanName());
		if (planStatics.isPlanFinish()) {
			PlanFactory.getPlan(baseTask.getPlanName()).setStatusWithoutNotify(TaskStatusEnum.SUCCESS);
			planStatics.flush();
			this.pendingPlanNum--;
			this.assemblyBus.restartWork();
		}
	}
	
	private void staticsGlobal(AbsBaseTask<?> baseTask) {
		if (!this.planName_Statics.containsKey(baseTask.getPlanName()))
			this.planName_Statics.put(baseTask.getPlanName(), this.new PlanRuntimeStatics());
		switch (baseTask.getStatus()) {
		case PENDING:
			this.pendingTaskNum++;
			if (baseTask instanceof CrawlPlan) {
				this.pendingPlanNum++;
			}
			break;
		case SUCCESS:
			this.successTaskNum++;
			this.pendingTaskNum--;
			break;
		case FAILED:
			this.failedTaskNum++;
			this.pendingTaskNum--;
			break;
		}
	}
	
	private void staticsForPlan(AbsBaseTask<?> baseTask) {
		if (!(baseTask instanceof CrawlPlan))
			this.planName_Statics.get(baseTask.getPlanName()).update(baseTask.getStatus());
	}
	
	private class PlanRuntimeStatics {
		private long pendingTaskNum;
		private long successTaskNum;
		private long failedTaskNum;
		private long lastBeginTime;
		private long thisBeginTime = new Date().getTime();
		
		void update(TaskStatusEnum taskStatusEnum) {
			switch (taskStatusEnum) {
			case PENDING:
				this.pendingTaskNum++;
				break;
			case SUCCESS:
				this.successTaskNum++;
				this.pendingTaskNum--;
				break;
			case FAILED:
				this.failedTaskNum++;
				this.pendingTaskNum--;
				break;
			}
		}
		boolean isPlanFinish() {
			if (this.pendingTaskNum == 0L && this.successTaskNum != 0L)
				return true;
			else
				return false;
		}
		void flush() {
			this.pendingTaskNum = 0L;
			this.successTaskNum = 0L;
			this.failedTaskNum = 0L;
			this.lastBeginTime = this.thisBeginTime;
			this.thisBeginTime = new Date().getTime();
		}
		public long getPendingTaskNum() {
			return pendingTaskNum;
		}
		public long getSuccessTaskNum() {
			return successTaskNum;
		}
		public long getFailedTaskNum() {
			return failedTaskNum;
		}
		public long getLastBeginTime() {
			return lastBeginTime;
		}
		public long getThisBeginTime() {
			return thisBeginTime;
		}
	}

	public long getPendingPlanNum() {
		return pendingPlanNum;
	}

	public long getFinishedPlanNum() {
		return finishedPlanNum;
	}

	public long getPendingTaskNum() {
		return pendingTaskNum;
	}

	public long getSuccessTaskNum() {
		return successTaskNum;
	}

	public long getFailedTaskNum() {
		return failedTaskNum;
	}

	public AssemblyBus getAssemblyBus() {
		return assemblyBus;
	}

	public Map<String, PlanRuntimeStatics> getPlanName_Statics() {
		return planName_Statics;
	}
	
}
