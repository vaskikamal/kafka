package com.example.executor;

public class ExecutorRespone<T> {
	public enum Status { SUCCESS, FAILED }
	
	private String callableName;
	private T data;
	private Status status;
	private String errorMessage;
	public String getCallableName() {
		return callableName;
	}
	public void setCallableName(String callableName) {
		this.callableName = callableName;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		return "ExecutorRespone [callableName=" + callableName + ", data="
				+ data + ", status=" + status + ", errorMessage="
				+ errorMessage + "]";
	}
	
	
}
