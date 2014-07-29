package com.sportxast.SportXast.test.mocks;

import android.content.Context;
import android.test.RenamingDelegatingContext;

public class SportXMockContext extends RenamingDelegatingContext {

	private static final String PREFIX = "sportx_test.";
	
	public SportXMockContext(Context context) {
		super(new BaseMockContext(context), PREFIX);
	}
}
