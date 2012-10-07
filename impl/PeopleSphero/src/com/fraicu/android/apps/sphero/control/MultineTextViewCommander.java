package com.fraicu.android.apps.sphero.control;

import orbotix.robot.base.Robot;
import android.widget.TextView;


public class MultineTextViewCommander {
	
	protected CmdsSpeedTimeParser mCmdParser;
	protected TextView mTViewCmd;
	protected CmdsSpeedTimeIntepreter tIntepreter;
	
	public MultineTextViewCommander(Robot robot, TextView textViewCmd) {
		mCmdParser = new CmdsSpeedTimeParser();
		mTViewCmd = textViewCmd;
		tIntepreter = new CmdsSpeedTimeIntepreter (robot);
	}
	
	public void execute() {
		String inputCmd = mTViewCmd.getText().toString();
				
		TextCommandSet cmds = mCmdParser.parse(inputCmd);
		tIntepreter.send(cmds);
	}

}
