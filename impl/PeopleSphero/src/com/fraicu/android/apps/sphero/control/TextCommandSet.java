package com.fraicu.android.apps.sphero.control;

import java.util.ArrayList;
import java.util.Iterator;

public class TextCommandSet implements CommandSet<String, float[]> {

	protected ArrayList<TextCommand> mCommands;
	protected Iterator<TextCommand> mItCommands = null;
	
	public TextCommandSet() {
		mCommands = new ArrayList<TextCommand>();		
	}


	public void pushCommands(Command<String, float[]>[] commands) {
		for (int it = 0; it < commands.length; it++) {
			mCommands.add((TextCommand) commands[it]);
		}
		
		mItCommands = mCommands.iterator();
	}


	public void pushCommand(Command<String, float[]> command) {
		mCommands.add((TextCommand) command);
		mItCommands = mCommands.iterator();
		
	}


	public void addCommands(int index, Command<String, float[]>[] commands) {
		for (int it = 0; it < commands.length; it++) {
			mCommands.add(index + it, (TextCommand) commands[it]);
		}
		
		mItCommands = mCommands.iterator();		
	}


	public void addCommand(int index, Command<String, float[]> command) {
		mCommands.add(index, (TextCommand) command);
		mItCommands = mCommands.iterator();
		
	}

	public boolean hasNextCommand() {
		mItCommands = (mItCommands == null) ? mCommands.iterator() : mItCommands;
		
		return mItCommands.hasNext();
	}

	public Command<String, float[]> nextCommand() {
		mItCommands = (mItCommands == null) ? mCommands.iterator() : mItCommands;
		
		return mItCommands.next();
	}
	
	public void start() {
		mItCommands = mCommands.iterator();
	}

}
