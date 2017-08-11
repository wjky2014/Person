package com.android.settings.smartawake;

import com.android.settings.smartawake.MyListAdapter.RowType;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import com.android.settings.R;

public class AnimationItem implements Item {
    
    private Context mContext;
    private int animNum;
    private String[] tips;
    private String[] tipsSummary;
    private AnimationDrawable frameAnim;

    public AnimationItem(Context context, int animNum) {
        this.mContext = context;
        this.animNum = animNum;
        tips = mContext.getResources().getStringArray(R.array.gesture_anim_tips);
        tipsSummary = mContext.getResources().getStringArray(R.array.gesture_anim_tips_summary);
    }
    
    @Override
    public int getViewType() {
        // TODO Auto-generated method stub
        return RowType.ANIM_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, int position) {
        // TODO Auto-generated method stub
        final viewHolder viewH;
        if (convertView == null) {
            viewH= new viewHolder();
            convertView = inflater.inflate(R.layout.anim_item, null);
            viewH.tips = (TextView) convertView.findViewById(R.id.tips);
            viewH.tipsSummary = (TextView) convertView.findViewById(R.id.tips_summary);
            viewH.animImage = (ImageView) convertView.findViewById(R.id.anim_image);
            convertView.setTag(viewH);
        } else {
            viewH = (viewHolder) convertView.getTag();
        }
        switch (animNum) {
        case 0:
            frameAnim=(AnimationDrawable) mContext.getResources().getDrawable(R.anim.two_points_up);
            break;
        case 1:
            frameAnim=(AnimationDrawable) mContext.getResources().getDrawable(R.anim.two_points_down);
            break;
        case 2:
            frameAnim=(AnimationDrawable) mContext.getResources().getDrawable(R.anim.three_points_up);
            break;
        case 3:
            frameAnim=(AnimationDrawable) mContext.getResources().getDrawable(R.anim.three_points_down);
            break;
        }
        viewH.tips.setText(tips[animNum]);
        viewH.tipsSummary.setText(tipsSummary[animNum]);
        viewH.animImage.setBackground(frameAnim);
        frameAnim.start();
                
        return convertView;
    }
    
    static class viewHolder {
        public TextView tips;
        public TextView tipsSummary;
        public ImageView animImage;
    }
}
