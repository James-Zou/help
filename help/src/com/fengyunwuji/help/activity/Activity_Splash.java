package com.fengyunwuji.help.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.fengyunwuji.help.R;

/**
 * ½éÉÜÒ³Ãæ
 * @com.fengyunwuji.help.activity
 * @author ×Þ½õ³Ì
 * @date 2016-2-29ÏÂÎç6:17:38
 *
 */
@SuppressLint("HandlerLeak")
public class Activity_Splash extends Activity_Base{

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void initViews() {
		
	}

	@Override
	public void initListeners() {
		
	}

	@Override
	public void initData() {
		mHandler.sendEmptyMessageDelayed(GO_HOME, 3000);
	}
	public void goHome(){
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		this.finish();
	}
	private static final int GO_HOME=100;
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			}
		}
	};

}
