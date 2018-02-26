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

public class ChoiceAppInfoAdapter extends BaseAdapter {
    private List<AppInfo> arrayListAppInfo;
    private Context context;
    private boolean[] addFlagsTable;

    public class ViewHolderChoice{
        public ImageView icon;
        public ImageView iconChoice;
        public TextView name;
    }

    public ChoiceAppInfoAdapter(List<AppInfo> arrayList, Context context) {
        super();
        arrayListAppInfo = arrayList != null ? arrayList : new ArrayList<AppInfo>();
        addFlagsTable = new boolean[arrayList.size()];
        for(int nIndex = 0; nIndex < arrayList.size(); nIndex++){
            addFlagsTable[nIndex] = false;
        }

        this.context = context;
    }

    public void initAddFlagsTable(){
        for(int nIndex = 0; nIndex < addFlagsTable.length; nIndex++){
            if(addFlagsTable[nIndex]){
                addFlagsTable[nIndex] = false;
            }
        }
    }

    public void initArrayListAppInfo(List<AppInfo> arrayList){
        arrayListAppInfo = arrayList != null ? arrayList : new ArrayList<AppInfo>();
        addFlagsTable = new boolean[arrayList.size()];
        for(int nIndex = 0; nIndex < arrayList.size(); nIndex++){
            addFlagsTable[nIndex] = false;
        }
        notifyDataSetChanged();
    }

    public boolean addGameByPosition(int position){
        boolean bAdd = true;
        boolean[] flagsTable = addFlagsTable;
        if(addFlagsTable[position]){
            bAdd = false;
        }
        flagsTable[position] = bAdd;
        notifyDataSetChanged();
        return addFlagsTable[position];
    }

    @Override
    public int getCount() {
        return arrayListAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderChoice holder;
        if(convertView == null){
            holder = new ViewHolderChoice();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_game_view, null);
            holder.icon = (ImageView)convertView.findViewById(R.id.icon);
            holder.iconChoice = (ImageView)convertView.findViewById(R.id.icon_choice);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolderChoice)convertView.getTag();
        }

        AppInfo appInfo = (AppInfo)getItem(position);
        holder.name.setText(appInfo.getAppName());
        holder.icon.setImageDrawable(appInfo.getAppIcon());
        if(addFlagsTable[position]){
            holder.iconChoice.setVisibility(View.VISIBLE);
        }else{
            holder.iconChoice.setVisibility(View.GONE);
        }

        holder.icon.setTag(appInfo.getPackageName());

        return convertView;
    }
}
