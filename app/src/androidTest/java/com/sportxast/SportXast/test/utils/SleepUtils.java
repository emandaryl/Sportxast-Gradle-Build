package com.sportxast.SportXast.test.utils;

import android.app.Instrumentation;

public class SleepUtils {

	public static void freeze(Instrumentation instr, int millis) {
		instr.waitForIdleSync();
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
