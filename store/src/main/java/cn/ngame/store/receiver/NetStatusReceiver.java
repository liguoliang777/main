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

package cn.ngame.store.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.utils.Constant;

/**
 * 网络状态监听器
 * @author flan
 * @date   2015年12月23日
 */
public class NetStatusReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//NetworkInfo activeInfo = manager.getActiveNetworkInfo();
		
		//Toast.makeText(context, "mobile:"+mobileInfo.isConnected()+" wifi:"+wifiInfo.isConnected(), Toast.LENGTH_SHORT).show();

		if(wifiInfo.isConnected()){

			FileLoadManager.getInstance(context).loadAllPauseTemp();
			StoreApplication.net_status = Constant.NET_STATUS_WIFI;

		}else if(mobileInfo.isConnected()){

			if(StoreApplication.allowAnyNet){
				FileLoadManager.getInstance(context).loadAllPauseTemp();
			}else {
				FileLoadManager.getInstance(context).pauseAllTemp();
			}
			StoreApplication.net_status = Constant.NET_STATUS_4G;

		}else {
			FileLoadManager.getInstance(context).pauseAllTemp();
			StoreApplication.net_status = Constant.NET_STATUS_DISCONNECT;
		}
	}
	
}
