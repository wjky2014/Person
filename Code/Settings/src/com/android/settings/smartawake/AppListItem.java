package com.android.settings.smartawake;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.android.settings.smartawake.MyListAdapter.RowType;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.graphics.drawable.Drawable;
import com.android.settings.R;
import android.graphics.Color;
import android.content.res.ColorStateList;

public class AppListItem implements Item {
    
    public Context mContext;
    private ResolveInfo appInfo;
    private String mGesture;
	private ColorStateList colorStateList;//save texiview default color

    @Override
    public int getViewType() {
        // TODO Auto-generated method stub
        return RowType.APP_LIST_ITEM.ordinal();
    }
    
    public AppListItem(Context context, ResolveInfo info) {
        this.mContext = context;
        this.appInfo = info;
		
		colorStateList = new TextView(context).getTextColors();
    }
    
    public AppListItem(Context context, String gusture) {
        this.mContext = context;
        this.mGesture = gusture;
        this.appInfo = null;
		
		colorStateList = new TextView(context).getTextColors();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, int position) {
        // TODO Auto-generated method stub
        final viewHolder viewH ;
        if (convertView == null) {
            viewH = new viewHolder();
            convertView = inflater.inflate(R.layout.select_app_item, null);
            viewH.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            viewH.appName = (TextView) convertView.findViewById(R.id.app_name);
            convertView.setTag(viewH);
			
        } else {
            viewH = (viewHolder) convertView.getTag();
        }
		
        Drawable iconDrawable = appInfo != null ? appInfo.loadIcon(mContext.getPackageManager()) : null;
        final String nameString = appInfo != null ? (String) appInfo.loadLabel(mContext.getPackageManager()) : mGesture;
        viewH.appIcon.setBackground(iconDrawable);
		viewH.appName.setText(nameString);
		if(appInfo==null){
			//bold
			viewH.appName.getPaint().setFakeBoldText(true);
			viewH.appName.setTextColor(Color.parseColor("#000000"));
		}else{
			//default
			viewH.appName.getPaint().setFakeBoldText(false);
			viewH.appName.setTextColor(colorStateList);
		} 
		
        
        final String packageName = appInfo != null ? appInfo.activityInfo.packageName : "";
        final String className = appInfo != null ? appInfo.activityInfo.name : "";
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                listener.onItemClickListener(nameString, packageName, className);
            }
        });

        return convertView;
    }
    
    static class viewHolder {
        public ImageView appIcon;
        public TextView appName;
    }
    
    private onAppListItemClickListenter listener;
    
    public interface onAppListItemClickListenter {
        public void onItemClickListener(String name, String name2, String name3);
    }
    
    public AppListItem setAppListItemClickListener(onAppListItemClickListenter listener) {
        this.listener = listener;
        return this;
    }

}
