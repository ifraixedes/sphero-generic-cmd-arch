package com.fraicu.android.apps.sphero.control;

import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RollCommand;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;

public class CmdsSpeedTimeIntepreter implements Intepreter<String, float[]> {

	public static final String[] COMMANDS = { "forward", "righ", "backward",
			"left" };
	protected static final float[] COMMANDS_DEGREES = { 0.0f, 90.0f, 180.0f,
			270.0f };
	protected static final long[] DEFAULT_STOP_TIME = { 2000l, 1000l, 500l,
			1000l };

	protected static final long CMD_TIME_STEP = 2000l;

	/*
	 * [0] = On the beginning [1] = Before execute command [2] = Executing
	 * command [3] = Command error
	 */
	protected static final int[][] STATE_COLORS = { { 0, 0, 0 }, { 0, 255, 0 },
			{ 0, 0, 255 }, { 255, 0, 0 } };

	protected Robot mRobot = null;
	protected Handler sendHandler = new Handler();
	protected Handler stopHandler = new Handler();

	protected CommandSet<String, float[]> currentCmds;
	protected Command<String, float[]> currentCmd;

	public CmdsSpeedTimeIntepreter(Robot robot) {
		mRobot = robot;
		RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[0][0],
				STATE_COLORS[0][1], STATE_COLORS[0][2]);
	}

	public void send(CommandSet<String, float[]> commandSet) {

		currentCmds = commandSet;
		currentCmds.start();

		if (currentCmds.hasNextCommand()) {
			RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[1][0],
					STATE_COLORS[1][1], STATE_COLORS[1][2]);

			currentCmd = currentCmds.nextCommand();
			sendHandler.postDelayed(new SendCommand(), CMD_TIME_STEP);
			

		}
	}

	public void setRobot(Robot robot) {
		mRobot = robot;
	}

	public Robot getRobot() {
		return mRobot;
	}

	public void send(Command<String, float[]> command) {

		String cmd = command.getCommand();
		float params[] = command.getParameters();
		int cmdIndex = -1;
		float degrees = 0;
		long stopTime = CMD_TIME_STEP;

		RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[2][0],
				STATE_COLORS[2][1], STATE_COLORS[2][2]);

		for (int it = 0; it < COMMANDS.length; it++) {
			if (cmd.equalsIgnoreCase(COMMANDS[it])) {
				cmdIndex = it;
				degrees = COMMANDS_DEGREES[it];
			}
		}

		if (cmdIndex < 0) {
			RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[3][0],
					STATE_COLORS[3][1], STATE_COLORS[3][2]);
			Log.e("COMMAND LOG", "INCORRECT COMMAND !!!!!!11");

		} else {

			float velocity = normalizeFloat(params[0]);

			RollCommand.sendCommand(mRobot, degrees, velocity);

			stopTime = (params.length > 1) ? (int) params[1]
					: DEFAULT_STOP_TIME[cmdIndex];

			Log.e("BALL COMMAND",
					"Command: " + cmd + " // velocity: "
							+ Float.toString(velocity) + " // stop time: "
							+ Long.toString(stopTime));
		}

		stopHandler.postDelayed(new StopRobot(), stopTime);
	}


	protected float normalizeFloat(float dValue) {
		return (dValue > 1.0f) ? (float) FloatMath.ceil(dValue) - dValue
				: dValue;
	}

	protected class SendCommand implements Runnable {

		public void run() {
			send(currentCmd);
		}

	}

	protected class StopRobot implements Runnable {
		public void run() {
			RollCommand.sendStop(mRobot);
			RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[1][0],
					STATE_COLORS[1][1], STATE_COLORS[1][2]);

			Log.e("BALL COMMAND", "STOP!!!!!!!!!!!!");
			
			if (currentCmds.hasNextCommand()) {
				currentCmd = currentCmds.nextCommand();
				sendHandler.postDelayed(new SendCommand(), CMD_TIME_STEP);
			}

		}
	}
}
