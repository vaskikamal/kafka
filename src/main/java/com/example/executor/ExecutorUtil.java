package com.example.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ExecutorUtil {
	private static final Logger logger = Logger.getLogger(ExecutorUtil.class);
	
	public static <I, O> List<ExecutorRespone<O>> executeOneInputCallable(
			ExecutorService executorService, 
			List<ParameterizedOneInputCallable<I, O>> callable) {
		
		List<ExecutorRespone<O>> executorRespones = new ArrayList<ExecutorRespone<O>>();
		
		if(callable == null || callable.isEmpty()) return executorRespones;
		
		for(ParameterizedOneInputCallable<I, O> executable: callable) {
			Future<ExecutorRespone<O>> future = executorService.submit(executable);
			try {
				ExecutorRespone<O> response =  future.get();
				executorRespones.add(response);
			} catch(Exception ex) {
				ExecutorRespone<String> response = new ExecutorRespone<String>();
				response.setCallableName(executable.getName());
				response.setErrorMessage(ex.getMessage());
				response.setStatus(ExecutorRespone.Status.FAILED);
			}			
		}
		
		return executorRespones;
	}
	
	public static <I, J, O> List<ExecutorRespone<O>> executeMultiInputCallable(
			ExecutorService executorService, 
			List<ParameterizedMultiInputCallable<I, J, O>> callable) {
		
		List<ExecutorRespone<O>> executorRespones = new ArrayList<ExecutorRespone<O>>();
		
		if(callable == null || callable.isEmpty()) return executorRespones;
		
		for(ParameterizedMultiInputCallable<I, J, O> executable: callable) {
			Future<ExecutorRespone<O>> future = executorService.submit(executable);
			try {
				ExecutorRespone<O> response =  future.get();
				executorRespones.add(response);
			} catch(Exception ex) {
				ExecutorRespone<String> response = new ExecutorRespone<String>();
				response.setCallableName(executable.getName());
				response.setErrorMessage(ex.getMessage());
				response.setStatus(ExecutorRespone.Status.FAILED);
			}			
		}
		
		return executorRespones;
	}
	
	public static void shutdown(ExecutorService executorService) {
		logger.info("About to shutdown executor-service");
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow(); // Cancel currently executing tasks
	            // Wait a while for tasks to respond to being cancelled
	            if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
	                logger.error("Executor Pool did not terminate");
	        }
		} catch(InterruptedException ex) {
			// (Re-)Cancel if current thread also interrupted
			executorService.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
