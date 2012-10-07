package com.fraicu.android.apps.sphero.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicTextParser implements Parser<String, String, float[]> {

	// \{\s*(\w+)(?:\s*|(?:\s)+(\d*\.\d+|\d+)(?:\s)*)?\}
	protected static final String INDIVIDUAL_CMD_REG_EXP = "\\{\\s*(\\w+)(?:\\s*|(?:\\s)+(\\d*\\.\\d+|\\d+)(?:\\s)*)?\\}";

	// protected TextCommandSet mCommandSet;

	public BasicTextParser() {

	}

	public TextCommandSet parse(String commands) {

		TextCommandSet commandSet = new TextCommandSet();

		Pattern cmdPattern = Pattern.compile(INDIVIDUAL_CMD_REG_EXP,
				Pattern.CASE_INSENSITIVE);
		Matcher cmdMatcher = cmdPattern.matcher(commands);

		while (cmdMatcher.find()) {
			TextCommand cmd;

			if (cmdMatcher.groupCount() > 1) {
				String parameters[] = new String[cmdMatcher.groupCount() - 1];

				for (int it = 2; it <= cmdMatcher.groupCount(); it++) {
					parameters[it - 2] = cmdMatcher.group(it);
				}
				cmd = new TextCommand(cmdMatcher.group(1), parameters);

			} else {
				cmd = new TextCommand(cmdMatcher.group(1));
			}

			commandSet.pushCommand(cmd);
		}

		return commandSet;
	}

}
