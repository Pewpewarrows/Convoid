package com.pewpewarrows.util;

/**
 * AsyncTaskListener
 * 
 * Inspired by: http://ajeyasharma.com/2010/04/returning-values-from-asynctask.html
 */
public interface AsyncTaskListener<T> {
	public void onTaskComplete(T result);
	
	public void onTaskCancel();
}
