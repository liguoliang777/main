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

package cn.ngame.store.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.view.QuickAction;

/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class InstalledGameAdapter extends BaseAdapter {
    private List<PackageInfo> fileInfoList;
    private Context context;
    private FragmentManager fm;
    private QuickAction mItemClickQuickAction;
    private int mPosition;
    private ViewHolder holder;
    private PackageManager packageManager;
    private String TAG = InstalledGameAdapter.class.getSimpleName();
    public InstalledGameAdapter(Context context, FragmentManager fm, QuickAction mItemClickQuickAction) {
        super();
        packageManager = context.getPackageManager();
        this.mItemClickQuickAction = mItemClickQuickAction;
        this.context = context;
        this.fm = fm;
    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<PackageInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fileInfoList != null) {
            return fileInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (fileInfoList != null) {
            return fileInfoList.get(position);
        }
        return null;
    }

    public PackageInfo getItemInfo() {
        if (fileInfoList != null) {
            return fileInfoList.get(mPosition);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void clean() {
        if (fileInfoList != null) {
            fileInfoList.clear();
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final PackageInfo fileInfo = (fileInfoList == null) ? null : fileInfoList.get(position);
        if (convertView == null) {
            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_load_finished, parent, false);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_install_title);
            holder.more_bt = (ImageView) convertView.findViewById(R.id.manager_installed_more_bt);
            holder.openBt = (TextView) convertView.findViewById(R.id.installed_open_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            holder.update(fileInfo);
            holder.more_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    if (null != mItemClickQuickAction) {
                        mItemClickQuickAction.show(v);
                    }
                }
            });
        }
        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    public class ViewHolder {

        private FragmentManager fm;
        private FileLoadInfo fileInfo;

        private SimpleDraweeView img;
        private ImageView more_bt;
        private TextView tv_title, openBt, tv_size;
        private IFileLoad fileLoad;

        public ViewHolder(Context context, FragmentManager fm) {
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            //init();
        }

        public void update(final PackageInfo packageInfo) {
            //this.fileInfo = fileInfo;
            final ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (null == applicationInfo) {
                return;
            }

            final String appName = applicationInfo.loadLabel(packageManager).toString();
            Drawable drawable = getIconFromPackageName(packageInfo.packageName,context);
            tv_title.setText(null != appName ? appName : "");
           //加载图片
     /*       Picasso.with(context)
                    .load("...")
                    .placeholder(drawable)
                    .resizeDimen(R.dimen.dm100, R.dimen.dm100)
                    .into(img);*/
            GenericDraweeHierarchy hierarchy = img.getHierarchy();
            hierarchy.setPlaceholderImage(drawable, ScalingUtils.ScaleType.FIT_XY);
            openBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppInstallHelper.openApp(context, applicationInfo.packageName);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KeyConstant.game_Name, appName);
                    MobclickAgent.onEvent(context, UMEventNameConstant.gameOpenButton, map);
                }
            });
        }
        public  Drawable getIconFromPackageName(String packageName, Context context)
        {
            PackageManager pm = context.getPackageManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            {
                try
                {
                    PackageInfo pi = pm.getPackageInfo(packageName, 0);
                    Context otherAppCtx = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
                    int displayMetrics[] = {DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_HIGH, DisplayMetrics.DENSITY_TV};
                    for (int displayMetric : displayMetrics)
                    {
                        try
                        {
                            Drawable d = otherAppCtx.getResources().getDrawableForDensity(pi.applicationInfo.icon, displayMetric);
                            if (d != null)
                            {
                                return d;
                            }
                        }
                        catch (Resources.NotFoundException e)
                        {
                            continue;
                        }
                    }
                }
                catch (Exception e)
                {
                    // Handle Error here
                }
            }
            ApplicationInfo appInfo = null;
            try
            {
                appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                return null;
            }
            return appInfo.loadIcon(pm);
        }
        /*private void init() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                                    fileInfo.getPackageName(), fileInfo.getVersionCode());
                            if (fileStatus == null) {
                                return;
                            }
                            int status = fileStatus.getStatus();
                            if (status == GameFileStatus.STATE_HAS_INSTALL) {
                                tv_size.setVisibility(View.INVISIBLE);
                                tv_state.setText("已安装");
                                progressBar.setLoadState(fileStatus);
                                progressBar.setVisibility(View.VISIBLE);
                            } else if (status == GameFileStatus.STATE_HAS_DOWNLOAD) {//安装
                                tv_size.setVisibility(View.VISIBLE);
                                tv_state.setText("下载完成");
                                progressBar.setLoadState(fileStatus);
                                progressBar.setVisibility(View.VISIBLE);
                            } else {
                                tv_size.setVisibility(View.INVISIBLE);
                                tv_state.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }, 0, 300);
        }*/
    }
}














