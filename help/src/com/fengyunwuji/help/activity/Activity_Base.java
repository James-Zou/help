package com.fengyunwuji.help.activity;

import com.fengyunwuji.help.config.Config_Constants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
/**
 * 基类
 * @com.fengyunwuji.help.activity
 * @author 邹锦程
 * @date 2016-2-29下午5:39:20
 *
 */
public abstract class Activity_Base extends Activity{
	protected int mScreenWidth;
	protected int mScreenHeight;
	public static final String TAG = "bmob";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, Config_Constants.Bmob_APPID);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics metric=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth=metric.widthPixels;
		mScreenHeight=metric.heightPixels;
		setContentView();
		initViews();
		initListeners();
		initData();
	}
	public abstract void setContentView();
	public abstract void initViews();
	public abstract void initListeners();
	public abstract void initData();
	Toast mToast;
	public void ShowToast(String text){
		if(!TextUtils.isEmpty(text)){
			if(mToast==null){
				mToast=Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			}else{
				mToast.setText(text);
			}
			mToast.show();
		}
	}
	public int getStateBar(){
		Rect frame=new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight=frame.top;
		return statusBarHeight;
	}
	public static int dip2px(Context context,float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(scale*scale*dipValue+0.5f);
		
	}
}
