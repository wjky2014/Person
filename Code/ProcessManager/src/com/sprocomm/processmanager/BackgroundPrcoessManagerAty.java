package com.sprocomm.processmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sprocomm.processmanager.db.AppForbiddenDao;
import com.sprocomm.processmanager.utils.ProcessManagerUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class BackgroundPrcoessManagerAty extends Activity {

	private TextView tvProcessToast, tvAppMangagerTittle;
	private ListView lvListProcessApp;
	private List<ProcessInfo> mAppInfoList;
	private List<ProcessInfo> mAllowRunList;
	private List<ProcessInfo> mForbiddenRunList;
	private ProgressBar loadProgressBar;
	private List<String> mDBForviddenRunList;
	AppForbiddenDao mAppForbiddenDao;

	private ProcessInfo mProcessInfo;
	private MyProcessAdapter myProcessAdapter;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (myProcessAdapter == null) {
				myProcessAdapter = new MyProcessAdapter();
				lvListProcessApp.setAdapter(myProcessAdapter);
				loadProgressBar.setVisibility(View.GONE);
			} else {
				myProcessAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background_process_manager);

		InitUI();
		InitData();
	
	}

	private void InitUI() {
		// TODO Auto-generated method stub
		tvProcessToast = (TextView) findViewById(R.id.tv_process_toast);
		tvAppMangagerTittle = (TextView) findViewById(R.id.tv_appmanagers_title);
		lvListProcessApp = (ListView) findViewById(R.id.lv_list_app);
		loadProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

	}

	private void InitData() {

		getProcessData();
		lvListProcessApp.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mForbiddenRunList != null && mAllowRunList != null) {
					if (firstVisibleItem == 0) {
						tvAppMangagerTittle.setVisibility(View.GONE);
					} else {
						tvAppMangagerTittle.setVisibility(View.VISIBLE);
					}

					if (firstVisibleItem >= mAllowRunList.size() + 1) {

						tvAppMangagerTittle.setText(getResources().getString(
								R.string.forbidden)
								+ " "
								+ mForbiddenRunList.size()
								+ " "
								+ getResources().getString(
										R.string.running_background));
					} else {

						tvAppMangagerTittle.setText(getResources().getString(
								R.string.allow)
								+ " "
								+ mAllowRunList.size()
								+ " "
								+ getResources().getString(
										R.string.running_background));
					}

				}
			}
		});
		lvListProcessApp.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == mAllowRunList.size() + 1) {
					return;
				} else {

					if (position < mAllowRunList.size() + 1) {
						if (!mAllowRunList.get(position - 1).packageName
								.equals(getPackageName())) {
							mProcessInfo = mAllowRunList.get(position - 1);
						}
					} else {
						mProcessInfo = mForbiddenRunList.get(position
								- mAllowRunList.size() - 2);
					}
					if (mProcessInfo != null) {
						mProcessInfo.isCheck = !mProcessInfo.isCheck;

						CheckBox cb_box = (CheckBox) view
								.findViewById(R.id.cb_box);

						if (mProcessInfo.isCheck) {
							mAppForbiddenDao.delete(mProcessInfo.packageName);
							mAllowRunList.add(mProcessInfo);
							mForbiddenRunList.remove(mProcessInfo);
							myProcessAdapter.notifyDataSetChanged();
						} else {
							mAppForbiddenDao.insert(mProcessInfo.packageName);
							mForbiddenRunList.add(mProcessInfo);
							mAllowRunList.remove(mProcessInfo);
							myProcessAdapter.notifyDataSetChanged();
						}

					}
				}
			}
		});
	}

	public class MyProcessAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAllowRunList.size() + mForbiddenRunList.size() + 2;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == mAllowRunList.size() + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public ProcessInfo getItem(int position) {
			if (position == 0 || position == mAllowRunList.size() + 1) {
				return null;
			} else {
				if (position < mAllowRunList.size() + 1) {
					return mAllowRunList.get(position - 1);
				} else {
					return mForbiddenRunList.get(position
							- mAllowRunList.size() - 2);
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int mode = getItemViewType(position);
			if (mode == 0) {

				ViewTitleHolder holder = null;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(),
							R.layout.list_item_app_des, null);
					holder = new ViewTitleHolder();
					holder.tv_app_des = (TextView) convertView
							.findViewById(R.id.tv_app_des);
					convertView.setTag(holder);
				} else {
					holder = (ViewTitleHolder) convertView.getTag();
				}
				if (position == 0) {
					holder.tv_app_des.setText(getResources().getString(
							R.string.allow)
							+ " "
							+ mAllowRunList.size()
							+ " "
							+ getResources().getString(
									R.string.running_background));
				} else {
					holder.tv_app_des.setText(getResources().getString(
							R.string.forbidden)
							+ " "
							+ mForbiddenRunList.size()
							+ " "
							+ getResources().getString(
									R.string.running_background));
				}
				return convertView;

			} else {
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(),
							R.layout.list_item_process, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView) convertView
							.findViewById(R.id.iv_icon);
					holder.tv_process_name = (TextView) convertView
							.findViewById(R.id.tv_process_name);
					holder.tv_process_allow = (TextView) convertView
							.findViewById(R.id.tv_process_allow_toast);
					holder.cb_box = (CheckBox) convertView
							.findViewById(R.id.cb_box);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
				holder.tv_process_name.setText(getItem(position).name);

				if (getItem(position).isCheck) {
					holder.tv_process_allow.setText(R.string.allow_run_str);
				} else {
					holder.tv_process_allow.setText(R.string.forbidden_run_str);
				}

				holder.cb_box.setChecked(getItem(position).isCheck);
				return convertView;
			}
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_process_name;
		TextView tv_process_allow;
		CheckBox cb_box;
	}

	static class ViewTitleHolder {
		TextView tv_app_des;
	}

	private void getProcessData() {
		new Thread() {
			public void run() {

				mAppForbiddenDao = AppForbiddenDao
						.getInstance(BackgroundPrcoessManagerAty.this);

				mAppInfoList = ProcessManagerUtils
						.getProcessList(getApplicationContext());
				mDBForviddenRunList = mAppForbiddenDao.findAll();
				mForbiddenRunList = new ArrayList<ProcessInfo>();
				mAllowRunList = new ArrayList<ProcessInfo>();

				List<String> specappList = Arrays
						.asList(ProcessManagerUtils.apps);

				for (ProcessInfo processInfo : mAppInfoList) {

					if (mDBForviddenRunList == null
							|| mDBForviddenRunList.size() == 0) {

						if (specappList.contains(processInfo.packageName)) {
							mAllowRunList.add(processInfo);
							processInfo.isCheck = true;
						} else {
							mForbiddenRunList.add(processInfo);
							processInfo.isCheck = false;
							mAppForbiddenDao.insert(processInfo.packageName);
						}
					} else {

						if (mDBForviddenRunList
								.contains(processInfo.packageName)) {
							mForbiddenRunList.add(processInfo);
							processInfo.isCheck = false;
						} else {
							mAllowRunList.add(processInfo);
							processInfo.isCheck = true;
						}
					}

				}
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	
}
