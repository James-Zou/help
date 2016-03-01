package com.fengyunwuji.help.activity;

import static com.fengyunwuji.help.R.id.tv_describe;
import static com.fengyunwuji.help.R.id.tv_time;
import static com.fengyunwuji.help.R.id.tv_title;
import static com.fengyunwuji.help.R.id.tv_photo;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.fengyunwuji.help.R;
import com.fengyunwuji.help.adapter.BaseAdapterHelper;
import com.fengyunwuji.help.adapter.QuickAdapter;
import com.fengyunwuji.help.base.Base_EditPopupWindow;
import com.fengyunwuji.help.been.Can_Help;
import com.fengyunwuji.help.been.For_Help;
import com.fengyunwuji.help.config.Config_Constants;
import com.fengyunwuji.help.inter.Inter_PopupItemClick;

public class MainActivity extends Activity_Base implements OnClickListener,
Inter_PopupItemClick, OnItemLongClickListener,OnItemClickListener {
	RelativeLayout layout_action;
	LinearLayout layout_all;
	TextView tv_lost;
	ListView listview;
	Button btn_add;
	protected QuickAdapter<For_Help> LostAdapter;

	protected QuickAdapter<Can_Help> FoundAdapter;

	private Button layout_found;
	private Button layout_lost;
	PopupWindow morePop;

	RelativeLayout progress;
	LinearLayout layout_no;
	TextView tv_no;
	
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		progress = (RelativeLayout) findViewById(R.id.progress);
		layout_no = (LinearLayout) findViewById(R.id.layout_no);
		tv_no = (TextView) findViewById(R.id.tv_no);

		layout_action = (RelativeLayout) findViewById(R.id.layout_action);
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		// 默认
		tv_lost = (TextView) findViewById(R.id.tv_lost);
		tv_lost.setTag("For_Help");
		listview = (ListView) findViewById(R.id.list_lost);
		btn_add = (Button) findViewById(R.id.btn_add);
		
		// 初始化长按弹窗
		initEditPop();
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		listview.setOnItemLongClickListener(this);
		listview.setOnItemClickListener(this);
		btn_add.setOnClickListener(this);
		layout_all.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == layout_all) {
			showListPop();
		} else if (v == btn_add) {
			Intent intent = new Intent(this, Activity_Add.class);
			intent.putExtra("from", tv_lost.getTag().toString());
			startActivityForResult(intent, Config_Constants.REQUESTCODE_ADD);
		} else if (v == layout_found) {
			changeTextView(v);
			morePop.dismiss();
			queryFounds();
		} else if (v == layout_lost) {
			changeTextView(v);
			morePop.dismiss();
			queryLosts();
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (LostAdapter == null) {
			LostAdapter = new QuickAdapter<For_Help>(this, R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, For_Help lost) {
					helper.setText(tv_title, lost.getTitle())
							.setText(tv_describe, lost.getDescribe())
							.setText(tv_time, lost.getCreatedAt())
							.setText(tv_photo, lost.getPhone());
				}

				
			};
		}

		if (FoundAdapter == null) {
			FoundAdapter = new QuickAdapter<Can_Help>(this, R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, Can_Help found) {
					helper.setText(tv_title, found.getTitle())
							.setText(tv_describe, found.getDescribe())
							.setText(tv_time, found.getCreatedAt())
							.setText(tv_photo, found.getPhone());
				}
			};
		}
		listview.setAdapter(LostAdapter);
		
		queryLosts();
	}

	private void changeTextView(View v) {
		if (v == layout_found) {
			tv_lost.setTag("Can_Help");
			tv_lost.setText("助攻");
		} else {
			tv_lost.setTag("For_Help");
			tv_lost.setText("求助");
		}
	}

	@SuppressWarnings("deprecation")
	private void showListPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_lost, null);
		// 注入
		layout_found = (Button) view.findViewById(R.id.layout_found);
		layout_lost = (Button) view.findViewById(R.id.layout_lost);
		layout_found.setOnClickListener(this);
		layout_lost.setOnClickListener(this);
		morePop = new PopupWindow(view, mScreenWidth, 600);

		morePop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					morePop.dismiss();
					return true;
				}
				return false;
			}
		});

		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setOutsideTouchable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从顶部弹下
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(layout_action, 0, -dip2px(this, 2.0F));
	}

	private void initEditPop() {
		mPopupWindow = new Base_EditPopupWindow(this, 200, 48);
		mPopupWindow.setOnPopupItemClickListner(this);
	}

	Base_EditPopupWindow mPopupWindow;
	int position;

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		position = arg2;
		int[] location = new int[2];
		arg1.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(arg1, Gravity.RIGHT | Gravity.TOP,
				location[0], getStateBar() + location[1]);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Config_Constants.REQUESTCODE_ADD:// 添加成功之后的回调
			String tag = tv_lost.getTag().toString();
			if (tag.equals("For_Help")) {
				queryLosts();
			} else {
				queryFounds();
			}
			break;
		}
	}

	/**
	 * 查询全部 queryLosts
	 * 
	 * @return void
	 * @throws
	 */
	private void queryLosts() {
		showView();
		BmobQuery<For_Help> query = new BmobQuery<For_Help>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(this, new FindListener<For_Help>() {

			@Override
			public void onSuccess(List<For_Help> losts) {
				// TODO Auto-generated method stub
				LostAdapter.clear();
				FoundAdapter.clear();
				if (losts == null || losts.size() == 0) {
					showErrorView(0);
					LostAdapter.notifyDataSetChanged();
					return;
				}
				progress.setVisibility(View.GONE);
				LostAdapter.addAll(losts);
				listview.setAdapter(LostAdapter);
			}

			@Override
			public void onError(int code, String arg0) {
				// TODO Auto-generated method stub
				showErrorView(0);
			}
		});
	}

	public void queryFounds() {
		showView();
		BmobQuery<Can_Help> query = new BmobQuery<Can_Help>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(this, new FindListener<Can_Help>() {

			@Override
			public void onSuccess(List<Can_Help> arg0) {
				// TODO Auto-generated method stub
				LostAdapter.clear();
				FoundAdapter.clear();
				if (arg0 == null || arg0.size() == 0) {
					showErrorView(1);
					FoundAdapter.notifyDataSetChanged();
					return;
				}
				FoundAdapter.addAll(arg0);
				listview.setAdapter(FoundAdapter);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onError(int code, String arg0) {
				// TODO Auto-generated method stub
				showErrorView(1);
			}
		});
	}

	/**
	 * 请求出错或者无数据时候显示的界面 showErrorView
	 * 
	 * @return void
	 * @throws
	 */
	private void showErrorView(int tag) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
		if (tag == 0) {
			tv_no.setText(getResources().getText(R.string.list_no_data_lost));
		} else {
			tv_no.setText(getResources().getText(R.string.list_no_data_found));
		}
	}

	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
	}

	@Override
	public void onEdit(View v) {
		// TODO Auto-generated method stub
		String tag = tv_lost.getTag().toString();
		Intent intent = new Intent(this, Activity_Add.class);
		String title = "";
		String describe = "";
		String phone = "";
		if (tag.equals("For_Help")) {
			title = LostAdapter.getItem(position).getTitle();
			describe = LostAdapter.getItem(position).getDescribe();
			phone = LostAdapter.getItem(position).getPhone();
		} else {
			title = FoundAdapter.getItem(position).getTitle();
			describe = FoundAdapter.getItem(position).getDescribe();
			phone = FoundAdapter.getItem(position).getPhone();
		}
		intent.putExtra("describe", describe);
		intent.putExtra("phone", phone);
		intent.putExtra("title", title);
		intent.putExtra("from", tag);
		startActivityForResult(intent, Config_Constants.REQUESTCODE_ADD);
	}

	@Override
	public void onDelete(View v) {
		// TODO Auto-generated method stub
		String tag = tv_lost.getTag().toString();
		if (tag.equals("For_Help")) {
			deleteLost();
		} else {
			deleteFound();
		}
	}

	private void deleteLost() {
		For_Help lost = new For_Help();
		lost.setObjectId(LostAdapter.getItem(position).getObjectId());
		lost.delete(this, new DeleteListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LostAdapter.remove(position);
			}

			@Override
			public void onFailure(int code, String arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void deleteFound() {
		Can_Help found = new Can_Help();
		found.setObjectId(FoundAdapter.getItem(position).getObjectId());
		found.delete(this, new DeleteListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				FoundAdapter.remove(position);
			}

			@Override
			public void onFailure(int code, String arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		dailPhone(arg1);
		
	}
	public void dailPhone(View view){
		
		TextView tv=(TextView)view.findViewById(R.id.tv_photo);
		String number=tv.getText().toString();
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number)); 
		startActivity(intent);
		
	}
	
}
