/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.ota.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.ota.model.DeviceInfo;

/**
 * OTA升级时设备的ListView控件适配器
 * @author zeng
 * @since 2016-09-20
 */
public class DeviceLvAdapter extends BaseAdapter {

    private static final String TAG = DeviceLvAdapter.class.getSimpleName();

    private List<DeviceInfo> deviceInfos;

    private OtaActivity activity;

    public DeviceLvAdapter(OtaActivity activity) {
        super();
        this.activity = activity;
    }

    /**
     * 设置ListView中的数据
     */
    public void setData(List<DeviceInfo> deviceInfoList) {

        deviceInfos = deviceInfoList;

    }

    @Override
    public int getCount() {

        if (deviceInfos != null) {
            return deviceInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (deviceInfos != null) {
            return deviceInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if(deviceInfos != null){
            deviceInfos.clear();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.ota_item_lv_device, parent, false);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_device);
            holder.tv_old = (TextView) convertView.findViewById(R.id.text1);
            holder.tv_new = (TextView) convertView.findViewById(R.id.text2);
            holder.bt_update = (Button) convertView.findViewById(R.id.but1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取设备信息
        DeviceInfo info = deviceInfos.get(position);

        holder.tv_name.setText(info.getName());//蓝牙设备的名称
        String currentVersionName = info.getCurrentVersionName().equals("V0")?"V1.0":info.getCurrentVersionName();
        holder.tv_old.setText("当前版本:"+ currentVersionName);//蓝牙当前的版本号

        //获取到的设备的新版本号>蓝牙手柄的当前版本号
        if(info.getNewVersionCode() > info.getCurrentVersionCode()){
            holder.tv_new.setText("最新版:"+info.getNewVersionName());
            holder.bt_update.setVisibility(View.VISIBLE);
            holder.bt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDevice(position);
                }
            });
            //没有新版本
        }else {
            holder.tv_new.setText("");
            holder.bt_update.setVisibility(View.GONE);
            holder.bt_update.setOnClickListener(null);
        }

        return convertView;
    }

    //升级设备
    private void updateDevice(int position){
        if(activity != null && deviceInfos != null && deviceInfos.size() > position){
            DeviceInfo info = deviceInfos.get(position);
            activity.showUpdateDialog(info);
        }
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     * @author flan
     * @since 2015年10月28日
     */
    public static class ViewHolder {
        TextView tv_name, tv_old,tv_new;
        Button bt_update;
    }
}














