package org.animeaholic.millipede.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.exception.JoinEachOtherException;
import org.animeaholic.millipede.processor.base.AbsBaseProcessor;

/**
 * 任务工厂
 * @author snzhange
 */
public class TaskPool {

	/* 按照“需要被不同类型处理者处理”而进行划分的任务池 */
	private static Map<Class<? extends AbsBaseProcessor>, LinkedBlockingQueue<AbsBaseTask<?>>> taskPool = new HashMap<Class<? extends AbsBaseProcessor>,LinkedBlockingQueue<AbsBaseTask<?>>>();

	/**
	 * 从任务池中取得需要被处理的任务
	 * @param cls
	 * @return
	 * @throws JoinEachOtherException 
	 */
	public static AbsBaseTask<?> getTask(Class<? extends AbsBaseProcessor> cls) throws Exception {
		LinkedBlockingQueue<AbsBaseTask<?>> taskQueue = taskPool.get(cls);
		if (taskQueue == null)
			throw new JoinEachOtherException("taskQueue of group [" + cls.getSimpleName() + "] has not been creat !");
		return taskQueue.take();
	}
	
	/**
	 * 向任务池中增加任务
	 * @param task
	 * @throws Exception
	 */
	public static void addTask(AbsBaseTask<?> task) throws Exception {
		LinkedBlockingQueue<AbsBaseTask<?>> taskQueue = taskPool.get(task.getCall());
		if (taskQueue == null)
			throw new JoinEachOtherException("taskQueue of group [" + task.getCall().getSimpleName() + "] has not been creat !");
		taskQueue.put(task);
	}
	
	/**
	 * 初始化任务池中的任务队列
	 * @param cls
	 * @param poolSize
	 */
	public static void createTaskQueue(Class<? extends AbsBaseProcessor> cls, int poolSize) {
		taskPool.put(cls, new LinkedBlockingQueue<AbsBaseTask<?>>(poolSize));
	}
	
}
