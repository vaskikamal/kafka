package com.example.executor;

import java.util.concurrent.Callable;

public abstract class ParameterizedMultiInputCallable<I, J, O> implements Callable<ExecutorRespone<O>> {
	
	private String name;
	private I inputParam1;
	private J inputParam2;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public I getInputParam1() {
		return inputParam1;
	}

	public void setInputParam1(I inputParam1) {
		this.inputParam1 = inputParam1;
	}

	public J getInputParam2() {
		return inputParam2;
	}

	public void setInputParam2(J inputParam2) {
		this.inputParam2 = inputParam2;
	}

	public abstract ExecutorRespone<O> process();
	
	public  ExecutorRespone<O> call() throws Exception {
		return process();
	}

}
