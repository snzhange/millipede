package org.animeaholic.millipede.processor.base;

/**
 * 调用处理者的处理线程
 * @author snzhange
 */
public class ProcessThread extends Thread {
	
	private AbsBaseProcessor processor;

	public ProcessThread(AbsBaseProcessor processor) {
		super();
		this.processor = processor;
	}
	
	public void run() {
		while (true)
			this.processor.process();
	}
	
}
