package com.lx.pad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.pad.ItemType.AppInfo;
import com.lx.pad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppInfoAdapter extends BaseAdapter {
    private List<AppInfo> appInfoList = null;
    private Context context = null;

    class ViewHolder{
        private ImageView icon = null;
        private TextView name = null;

        public ViewHolder(){
            super();
        }
    }

    public AppInfoAdapter(List<AppInfo> aryList, Context context){
        super();
        this.appInfoList = aryList != null ? aryList : new ArrayList<AppInfo>();
        this.context = context;
    }

    public void initArrayList(List<AppInfo> appList){
        appInfoList = appList != null ? appList : new ArrayList<AppInfo>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_game_view, null);
            holder.icon = (ImageView)convertView.findViewById(R.id.icon);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = (AppInfo)getItem(position);
        holder.name.setText(appInfo.getAppName());
        holder.icon.setTag(appInfo.getPackageName());
        holder.icon.setImageDrawable(appInfo.getAppIcon());

        return convertView;
    }
}
