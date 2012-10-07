package com.fraicu.android.apps.sphero.control;

import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RollCommand;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;

public class TextInterpreter implements Intepreter<String, float[]> {

	public static final String[] COMMANDS = { "forward", "righ", "backward",
			"left" };
	protected static final float[] COMMANDS_DEGREES = { 0.0f, 90.0f, 180.0f,
			270.0f };
	protected static final float[] DEFAULT_SPEED = { 1.0f, 0.40f, 0.25f, 0.40f };
	protected static final long[] DEFAULT_STOP_TIME = { 2000l, 1000l, 500l,
			1000l };

	/*
	 * [0] = Before to start and after to finish [1] = Executing command [2] =
	 * Change to next command [3] = Command error
	 */
	protected static final int[][] STATE_COLORS = { { 0, 0, 0 }, { 0, 255, 0 },
			{ 0, 0, 255 }, { 255, 0, 0 } };

	protected Robot mRobot = null;
	protected Handler stopHandler = new Handler();

	public TextInterpreter(Robot robot) {
		mRobot = robot;
		RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[0][0],
				STATE_COLORS[0][1], STATE_COLORS[0][2]);
	}

	public void send(CommandSet<String, float[]> commandSet) {

		commandSet.start();

		while (commandSet.hasNextCommand()) {
			send(commandSet.nextCommand());
//			RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[2][0],
//					STATE_COLORS[2][1], STATE_COLORS[2][2]);
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
			return;
		}

		RGBLEDOutputCommand.sendCommand(mRobot, STATE_COLORS[1][0],
				STATE_COLORS[1][1], STATE_COLORS[1][2]);

		float velocity = (params.length > 0) ? normalizeFloat(params[0])
				: DEFAULT_SPEED[cmdIndex];

		RollCommand.sendCommand(mRobot, degrees, velocity);

		long stopTime = (params.length > 1) ? normalizeInt((int) params[1])
				: DEFAULT_STOP_TIME[cmdIndex];

		stopHandler.postDelayed(new StopRobot(), stopTime);

		Log.e("BALL COMMAND",
				"Command: " + cmd + " // velocity: " + Float.toString(velocity)
						+ " // stop time: " + Long.toString(stopTime));

	}

	protected int normalizeInt(int dValue) {
		return (dValue > 255) ? (int) (dValue / dValue * 255) * dValue : dValue;
	}

	protected float normalizeFloat(float dValue) {
		return (dValue > 1.0f) ? (float) FloatMath.ceil(dValue) - dValue
				: dValue;
	}

	protected class StopRobot implements Runnable {
		public void run() {
			RollCommand.sendStop(mRobot);
		}
	}
}
