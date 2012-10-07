package com.fraicu.android.apps.sphero.control;

import orbotix.robot.base.Robot;

public interface Intepreter<I, V> {

	public void setRobot(Robot robot);
	public Robot getRobot();
	public void send(CommandSet<I, V> commandSet);
	public void send(Command<I, V> command);
	
}
