package org.animeaholic.millipede.processor.base;

import java.util.List;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.enums.TaskStatusEnum;
import org.animeaholic.millipede.factory.TaskPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbsBaseProcessor {
	
	private static Logger log = LoggerFactory.getLogger(AbsBaseProcessor.class);
	
	void process() {
		AbsBaseTask<?> task = null;
		try {
			task = TaskPool.getTask(this.getClass());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (task != null) {
			try {
				List<AbsBaseTask<?>> newTaskList = this.process(task);
				task.setStatus(TaskStatusEnum.SUCCESS);
				if (newTaskList != null)
					for (AbsBaseTask<?> newTask : newTaskList)
						TaskPool.addTask(newTask);
			} catch (Exception e) {
				if (task instanceof CrawlResTask) {
					log.error("--[CrawResTask FAILURE]--URL : {}",((CrawlResTask)task).getUrl());
				}
				log.error(e.getMessage(), e);
				task.setStatus(TaskStatusEnum.FAILED);
			}
		}
	}
	
	protected abstract List<AbsBaseTask<?>> process(AbsBaseTask<?> baseTask) throws Exception;

}
