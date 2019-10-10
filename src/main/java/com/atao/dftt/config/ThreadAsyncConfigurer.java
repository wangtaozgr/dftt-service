package com.atao.dftt.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sun.media.jfxmedia.logging.Logger;

@Component
@EnableAsync // 开启对异步任务的支持
public class ThreadAsyncConfigurer implements AsyncConfigurer {
	/**
	 * 1、 如果此时线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态,也要创建新的线程来处理被添加的任务。 2、
	 * 如果此时线程池中的数量等于 corePoolSize，但是缓冲队列 workQueue未满，那么任务被放入缓冲队列。
	 * 3、如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。
	 * 4、 如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过
	 * handler所指定的策略来处理此任务。 也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程
	 * maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。 5、 当线程池中的线程数量大于
	 * corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。
	 * allowCoreThreadTimeout：允许核心线程超时 rejectedExecutionHandler：任务拒绝处理器
	 */
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		threadPool.setCorePoolSize(60);
		// 设置最大线程数
		threadPool.setMaxPoolSize(60);
		// 线程池所使用的缓冲队列
		threadPool.setQueueCapacity(0);
		// 等待任务在关机时完成--表明等待所有线程执行完
		threadPool.setWaitForTasksToCompleteOnShutdown(true);
		// 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
		threadPool.setAwaitTerminationSeconds(60 * 15);
		// 线程名称前缀
		threadPool.setThreadNamePrefix("atao-");
		threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		// 初始化线程
		threadPool.initialize();
		return threadPool;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		System.out.println("getAsyncUncaughtExceptionHandler");
		return new MyAsyncExceptionHandler();
	}

}
