package com.fraicu.android.apps.sphero.control;

import java.util.ArrayList;

import android.util.Log;

public class TextCommand implements Command<String, float[]>{

	protected static final String _LOGTAG = "TEXT COMMAND"; 
	
	protected String mCommand;
	protected float[] mParameters; 
		
	
	public TextCommand(String command) {
		mCommand = command;
		mParameters = new float[0];
	}
	
	public TextCommand(String command, String[] parameters) {
		mCommand = command;
		parseParametersValue(parameters);
	}
	
	protected void parseParametersValue(String[] parameters) {
		
		//mParameters = new float[parameters.length];
		ArrayList<Float> list = new ArrayList<Float>();
		int it = 0;
		
		for(it = 0; it < parameters.length; it++) {
//			mParameters[it] = Float.valueOf(parameters[it]);
			if (parameters[it] != null) {
				try {
					list.add(Float.valueOf(parameters[it]));
				} catch (NumberFormatException nbEx) {
					Log.e(_LOGTAG, "No possible to convert the parameter to a valid float value");
				}
			}
		}
		
		mParameters = new float[list.size()];
		it = 0;
		
		for(Float parameter : list) {
			mParameters[it++] = parameter.floatValue();			
		}
	}
	
	public String getCommand() {
		return mCommand;
	}


	public float[] getParameters() {
		return mParameters;
	}

}
