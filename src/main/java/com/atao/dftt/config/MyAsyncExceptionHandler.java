package com.atao.dftt.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("-------------》》》捕获线程异常信息");
		logger.error("Exception message - " + ex.getMessage());
		logger.error("Method name - " + method.getName());
		for (Object param : params) {
			logger.error("Parameter value - " + param);
		}
	}

}
