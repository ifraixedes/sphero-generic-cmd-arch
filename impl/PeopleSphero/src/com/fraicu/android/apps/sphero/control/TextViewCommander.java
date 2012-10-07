package com.fraicu.android.apps.sphero.control;

import orbotix.robot.base.Robot;
import android.widget.TextView;


public class TextViewCommander {
	
	protected BasicTextParser mCmdParser;
	protected TextView mTViewCmd;
	protected TextInterpreter tIntepreter;
	
	public TextViewCommander(Robot robot, TextView textViewCmd) {
		mCmdParser = new BasicTextParser();
		mTViewCmd = textViewCmd;
		tIntepreter = new TextInterpreter(robot);
	}
	
	public void execute() {
		String inputCmd = mTViewCmd.getText().toString();
		inputCmd = "{" + inputCmd + "}";
		
		TextCommandSet cmds = mCmdParser.parse(inputCmd);
		tIntepreter.send(cmds);		
	}

}
