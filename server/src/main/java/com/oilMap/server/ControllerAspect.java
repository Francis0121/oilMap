package com.oilMap.server;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {

	private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Around("bean(*Controller)")
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
		String signatureString = joinPoint.getSignature().toShortString();
		logger.info("Start time " + signatureString );
		int i = 0;
		for(Object argObj : joinPoint.getArgs()){
			logger.info("[Access Args_"+ i++ +" : "+ argObj.getClass()+"] "+ argObj.toString());
		}
		long start = System.currentTimeMillis();

		try {

			logger.info("Aspect Test");

			Object returnObj = joinPoint.proceed();
			logger.info("[Access Returns : "+ returnObj.getClass()+"] "+ returnObj.toString());
			return returnObj;
		} finally {
			long finish = System.currentTimeMillis();
			logger.info("End time " + signatureString + " " + (finish - start) + "ms\n");
		}
	}

}
