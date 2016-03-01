package com.fengyunwuji.help.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/**
 * 自定义popwindow
 * @com.fengyunwuji.help.base
 * @author 邹锦程
 * @date 2016-2-29下午7:41:28
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class Base_PopupWindow extends PopupWindow{
	protected View mContentView;
	protected onSubmitClickListener mOnSubmitClickListener;

	public Base_PopupWindow() {
		super();
	}

	public Base_PopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public Base_PopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Base_PopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Base_PopupWindow(Context context) {
		super(context);
	}

	public Base_PopupWindow(int width, int height) {
		super(width, height);
	}

	public Base_PopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public Base_PopupWindow(View contentView) {
		super(contentView);
	}

	@SuppressWarnings("deprecation")
	public Base_PopupWindow(View contentView, int width, int height) {
		super(contentView, width, height, true);
		mContentView = contentView;
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		initViews();
		initEvents();
		init();
	}

	public abstract void initViews();

	public abstract void initEvents();

	public abstract void init();

	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	/**
	 * 添加确认单击监听
	 * 
	 * @param l
	 */
	public void setOnSubmitClickListener(onSubmitClickListener l) {
		mOnSubmitClickListener = l;
	}

	public interface onSubmitClickListener {
		void onClick();
	}
}
