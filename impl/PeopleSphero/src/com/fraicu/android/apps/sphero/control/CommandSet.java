package com.fraicu.android.apps.sphero.control;



public interface CommandSet<I, V>  {
	
	public void pushCommands(Command<I,V> commands[]);
	public void pushCommand(Command<I, V> command);	
	public void addCommands(int index, Command<I,V> commands[]);
	public void addCommand(int index, Command<I, V> command);	
	public boolean hasNextCommand();
	public Command<I, V> nextCommand();
	public void start();

}
