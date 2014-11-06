package com.tdclighthouse.prototype.utils;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

	private ThreadGroup threadGroup;
	private int threadInitNumber = 0;

	public DaemonThreadFactory() {
		this("daemon-thread-factory");
	}

	public DaemonThreadFactory(String name) {
		threadGroup = new ThreadGroup(name);
	}

	private synchronized int nextThreadNum() {
		return threadInitNumber++;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(threadGroup, r, threadGroup.getName() + nextThreadNum());
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	}
}
