package com.fengyunwuji.help.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fengyunwuji.help.R;
import com.fengyunwuji.help.inter.Inter_PopupItemClick;
/**
 * 长按编辑弹出
 * @com.fengyunwuji.help.base
 * @author 邹锦程
 * @date 2016-2-29下午7:48:16
 *
 */
public class Base_EditPopupWindow extends Base_PopupWindow implements OnClickListener{

	private TextView mEdit;
	private TextView mDelete;
	private Inter_PopupItemClick mOnPopupItemClickListner;

	public Base_EditPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(
				R.layout.pop_device, null), dpToPx(context,width), dpToPx(context,height));
		setAnimationStyle(R.style.PopupAnimation);
	}
	
	private static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

	@Override
	public void initViews() {
		mEdit = (TextView) findViewById(R.id.tv_pop_edit);
		mDelete = (TextView) findViewById(R.id.tv_pop_delete);
	}

	@Override
	public void initEvents() {
		mEdit.setOnClickListener(this);
		mDelete.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pop_edit:
			if (mOnPopupItemClickListner != null) {
				mOnPopupItemClickListner.onEdit(v);
			}
			break;

		case R.id.tv_pop_delete:
			if (mOnPopupItemClickListner != null) {
				mOnPopupItemClickListner.onDelete(v);
			}
			break;
		}
		dismiss();
	}

	public void setOnPopupItemClickListner(Inter_PopupItemClick l) {
		mOnPopupItemClickListner = l;
	}

}
