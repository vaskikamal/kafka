package com.example.executor;

import java.util.concurrent.Callable;

public abstract class ParameterizedOneInputCallable<I, O> implements Callable<ExecutorRespone<O>> {
	
	private String name;
	private I input;
	
	public ParameterizedOneInputCallable(String name, I input) {
		super();
		this.name = name;
		this.input = input;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public I getInput() {
		return input;
	}

	public void setInput(I input) {
		this.input = input;
	}
	
	public abstract ExecutorRespone<O> process();
	
	public  ExecutorRespone<O> call() throws Exception {
		return process();
	}

}
