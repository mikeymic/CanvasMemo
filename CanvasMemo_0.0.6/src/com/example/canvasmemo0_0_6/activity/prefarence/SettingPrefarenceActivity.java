package com.example.canvasmemo0_0_6.activity.prefarence;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingPrefarenceActivity extends PreferenceActivity {

	PreferenceManager preferenceManager;


	/* (非 Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
	}

	/* (非 Javadoc)
	 * @see android.preference.PreferenceActivity#onBuildHeaders(java.util.List)
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		// TODO 自動生成されたメソッド・スタブ
		super.onBuildHeaders(target);
	}

	/* (非 Javadoc)
	 * @see android.preference.PreferenceActivity#isValidFragment(java.lang.String)
	 */
	@Override
	protected boolean isValidFragment(String fragmentName) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}



	/* (非 Javadoc)
	 * @see android.preference.PreferenceActivity#getPreferenceManager()
	 */
	@Override
	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	/* (非 Javadoc)
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}

}
