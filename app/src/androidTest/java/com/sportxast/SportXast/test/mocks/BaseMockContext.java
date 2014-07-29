package com.sportxast.SportXast.test.mocks;

import android.content.Context;
import android.test.mock.MockContext;

public class BaseMockContext extends MockContext {
	
	private Context context;

	public BaseMockContext(Context context) {
		this.context = context;
	}
	
	@Override
	public String getPackageName() {
		return context.getPackageName();
	}
}
