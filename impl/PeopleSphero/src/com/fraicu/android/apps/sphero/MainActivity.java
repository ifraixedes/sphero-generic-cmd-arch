package com.fraicu.android.apps.sphero;

import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fraicu.android.apps.sphero.control.MultineTextViewCommander;
import com.fraicu.android.apps.sphero.control.TextViewCommander;

public class MainActivity extends Activity {

	/**
	 * ID for launching the StartupActivity for result to connect to the robot
	 */
	private final static int STARTUP_ACTIVITY = 0;

	/**
	 * The Sphero Robot
	 */
	private Robot mRobot;

	private TextViewCommander mOneCommander;
	private MultineTextViewCommander mMultiCommander;

	private TextView mInputCmd;
	private Button mExecBtn;
	private TextView mInputCmds;
	private Button mExecCmdsBtn;

	private static final String DEFAULT_CMDS_TO_TEST = "" +
			"{forward 1.0 5000}" +
			"{righ 0.5 3000}" +
			"{forward 0.8 1000}" +
			"{wrong 0.8 1000}" +
			"{left 1.0 2000}" +
			"{backward 0.3 7000}";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mInputCmd = (TextView) findViewById(R.id.inputCmd);
		mExecBtn = (Button) findViewById(R.id.execBtn);

		mInputCmds = (TextView) findViewById(R.id.inputCmds);
		mInputCmds.setText(DEFAULT_CMDS_TO_TEST);
		mExecCmdsBtn = (Button) findViewById(R.id.execCmdsButon);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();

		// Launch the StartupActivity to connect to the robot
		Intent i = new Intent(this, StartupActivity.class);
		startActivityForResult(i, STARTUP_ACTIVITY);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == STARTUP_ACTIVITY && resultCode == RESULT_OK) {

			// Get the connected Robot
			final String robot_id = data
					.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
			if (robot_id != null && !robot_id.equals("")) {
				mRobot = RobotProvider.getDefaultProvider().findRobot(robot_id);
			}

			mOneCommander = new TextViewCommander(mRobot, mInputCmd);
			mMultiCommander = new MultineTextViewCommander(mRobot, mInputCmds);

			mExecBtn.setEnabled(true);
			mExecCmdsBtn.setEnabled(true);

		}

		// TODO show message to the user to restart the app or anything else
	}

	@Override
	protected void onStop() {
		super.onStop();

		mRobot = null;

		// Disconnect Robot
		RobotProvider.getDefaultProvider().removeAllControls();
	}

	public void execCommand(View execBtn) {
		if (execBtn.getId() == R.id.execBtn) {
			if (mOneCommander != null) {
				mOneCommander.execute();
			}
		}
	}

	public void execCommands(View execBtn) {
		if (execBtn.getId() == R.id.execCmdsButon) {
			if (mMultiCommander != null) {
				mMultiCommander.execute();
			}
		}
	}
}
