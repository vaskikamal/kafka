package com.example.kafkademo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.example.executor.ExecutorRespone;
import com.example.executor.ExecutorUtil;
import com.example.executor.ParameterizedOneInputCallable;

public class ExecutorDemo {

	public static void main(String[] args) {
		ExecutorService service = Executors.newSingleThreadExecutor(
				new NamedThreadFactory("ExecutorDemo"));
		
		List<ExecutorRespone<String>> executorRespones = 
				ExecutorUtil.executeOneInputCallable(service, createSingleInputCallables());
		
		for(ExecutorRespone<String> response: executorRespones) {
			System.out.println(response);
		}
		ExecutorUtil.shutdown(service);
	}
	
	private static List<ParameterizedOneInputCallable<String, String>> createSingleInputCallables() {
		List<ParameterizedOneInputCallable<String, String>> inputs = 
				new ArrayList<ParameterizedOneInputCallable<String, String>>();
		for(int i=0; i < 5; i++) {
			
			inputs.add(new SingleInputCallable("Callable instance:"+i, "Say Hello"));
		}
		return inputs;
	}

	static class NamedThreadFactory implements ThreadFactory {
		private String threadName;
		public NamedThreadFactory(String threadName) {
			this.threadName = threadName;
		}

		public Thread newThread(Runnable runnable) {
			return new Thread(runnable, threadName);
		}
	}
	
	static class SingleInputCallable extends 
			ParameterizedOneInputCallable<String, String> {
		
		public SingleInputCallable(String name, String input) {
			super(name, input);
		}
		
		@Override
		public ExecutorRespone<String> process() {
			try {
				Thread.sleep(1000);
				
				ExecutorRespone<String> response = new ExecutorRespone<String>();
				response.setCallableName(this.getName());
				response.setStatus(ExecutorRespone.Status.SUCCESS);
				response.setData(
						"Input Excuted by Thread: "+ Thread.currentThread().getName()+
						" O/P result: "+this.getInput());
				return response;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
}
