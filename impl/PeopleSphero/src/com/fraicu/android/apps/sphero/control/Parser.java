package com.fraicu.android.apps.sphero.control;

public interface Parser<S, I, V> {
	public CommandSet<I, V> parse(S source);
}
