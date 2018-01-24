
package cn.ngame.store.adapter;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.activity.manager.NecessaryFragment;
import cn.ngame.store.bean.NecessaryItemData;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class NeccssaryFragmentAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Timer timer = new Timer();
    private List<TimerTask> timerTasks = new ArrayList<>();
    private List<NecessaryItemData> mPlanDetails;
    private Context context;
    private FragmentManager fm;
    private Handler uiHandler = new Handler();
    private String mPositionGameId = "";

    public NeccssaryFragmentAdapter(Context context, FragmentManager fm, List<TimerTask>
            timerTasks) {
        super();
        this.context = context;
        this.fm = fm;
        this.timerTasks = timerTasks;

    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<NecessaryItemData> fileInfoList) {
        //uiHandler = new Handler();
        this.mPlanDetails = fileInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mPlanDetails != null) {
            return mPlanDetails.size();
        }
        return 0;
    }

    public String getItemGameId() {
        return mPositionGameId;
    }

    @Override
    public Object getItem(int position) {
        if (mPlanDetails != null) {
            return mPlanDetails.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (mPlanDetails != null) {
            mPlanDetails.clear();
            uiHandler = null;
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_necessary, parent, false);
            holder.toolLogo = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.toolNameTv = (TextView) convertView.findViewById(R.id.necessary_tv_title);
            holder.versionTv = (TextView) convertView.findViewById(R.id.tv_version_time);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.becessary_item_desc_tv);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.show_more_disc_bt = (ImageView) convertView.findViewById(R.id.show_more_disc_bt);
            holder.neccessary_content_ll = (LinearLayout) convertView.findViewById(R.id.neccessary_content_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NecessaryItemData planDetail = (mPlanDetails == null) ? null : mPlanDetails.get(position);
        if (planDetail != null) {
            holder.update(planDetail);
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.necessary_list_item_header, parent, false);
            holder.itemParentTv = (TextView) convertView.findViewById(R.id.necessary_list_header_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set proj_plans_header itemParentTv as first char in name
        String parentText = this.mPlanDetails.get(position).getParentName();
        holder.itemParentTv.setText(parentText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        // getHeaderId决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
        return Long.parseLong(this.mPlanDetails.get(position).getParentId());
    }


    class HeaderViewHolder {
        TextView itemParentTv;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    protected final static String TAG = NecessaryFragment.class.getSimpleName();

    public class ViewHolder {

        private Context context;
        private FragmentManager fm;
        private NecessaryItemData toolInfo;
        private ImageView show_more_disc_bt;
        private SimpleDraweeView toolLogo;
        private TextView toolNameTv, tv_size, versionTv, tv_desc;
        private GameLoadProgressBar progressBar;    //下载进度条
        private IFileLoad fileLoad;
        private LinearLayout neccessary_content_ll;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            init();
        }

        private void init() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (uiHandler == null) {
                        this.cancel();
                        return;
                    }// "org.getlantern.lantern\r\n",
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(toolInfo.getFileName(), toolInfo
                                    .getToolURL(), toolInfo.getPackages(), ConvUtil.NI(toolInfo.getToolVersion())));
                            //progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            };
            timerTasks.add(task);
            timer.schedule(task, 0, 300);
        }

        public void update(final NecessaryItemData toolInfo) {
            this.toolInfo = toolInfo;
            toolNameTv.setText(toolInfo.getToolName());
            String toolVersion = toolInfo.getToolVersion();
            versionTv.setText(toolVersion==null?"":"V" +toolVersion);
            tv_size.setText(toolInfo.getToolSize());
            tv_desc.setText(toolInfo.getToolDesc());
      /*      Uri uri = Uri.parse("res:///" + R.drawable.ic_google_neccessary_logo);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setTapToRetryEnabled(true)
                    .build();
            holder.toolLogo.setController(controller);*/
            toolLogo.setImageURI(toolInfo.getToolLogo());
            neccessary_content_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lineCount = tv_desc.getLineCount();
                    if (1 == lineCount) {
                        tv_desc.setSingleLine(false);
                        show_more_disc_bt.setImageResource(R.drawable.ic_bottom_hide_more);
                    } else {
                        tv_desc.setMaxLines(1);
                        show_more_disc_bt.setImageResource(R.drawable.ic_bottom_show_more);
                    }
                }
            });
            //progressBar.setVisibility(View.INVISIBLE);
            //设置进度条状态
            int version = ConvUtil.NI(toolVersion);
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(toolInfo.getFileName(), toolInfo
                    .getToolURL(), toolInfo.getPackages(), version));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(toolInfo.getFileName(), toolInfo.getToolURL(), toolInfo
                    .getMd5(), version, toolInfo.getToolName(), toolInfo.getToolLogo(), toolInfo
                    .getToolId(), FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(toolInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });
        }

    }

}














