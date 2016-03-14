package org.animeaholic.millipede.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.animeaholic.millipede.bus.AssemblyBus;

public class Bootstrap {
	
	// 因为我后面打印不是一行system.out.println输出的输出的，所以多线程打印到控制台会不按照我的顺序打，所以这个地方new一个静态的可重入锁，打印之前lock一下
	public static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
		try {
			new AssemblyBus().startWork();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
