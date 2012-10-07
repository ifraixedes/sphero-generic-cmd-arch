package com.fraicu.android.apps.sphero.control;

public interface Command<I, V> {
	public I getCommand();
	public V getParameters();
//	public 
}
