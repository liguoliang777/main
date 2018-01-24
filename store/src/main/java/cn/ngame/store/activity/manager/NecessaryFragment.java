package cn.ngame.store.activity.manager;

import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.sm.NecessaryOrLikeActivity;
import cn.ngame.store.adapter.NeccssaryFragmentAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.NecessaryItemData;
import cn.ngame.store.bean.NecessaryListInfo;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.UrlConstant;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * 必备
 * Created by gp on 2017/3/3 0003.
 */

public class NecessaryFragment extends BaseSearchFragment {

    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    protected QuickAction mItemClickQuickAction;
    private GameRankListBean gameInfoBean;
    private FragmentActivity content;
    private NecessaryListInfo.AuxiliaryToolsBean mToolInfo;
    private TextView mEmptyTV;

    public NecessaryFragment(NecessaryOrLikeActivity necessaryActivity) {
        content = necessaryActivity;
    }

    /*  public static NecessaryFragment newInstance(String type, int bean, FragmentActivity
    context) {
          NecessaryFragment fragment = new NecessaryFragment();
          Bundle bundle = new Bundle();
          content = context;
          bundle.putString("type", type);
          bundle.putSerializable("typeValue", bean);
          fragment.setArguments(bundle);
          return fragment;
      }
  */
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_necessary;
    }

    private List<TimerTask> timerTasks = new ArrayList<>();
    public StickyListHeadersListView mStickyLV;

    private NeccssaryFragmentAdapter mNecessaryAdapter;

    private List<NecessaryItemData> mNecessaryList = new ArrayList<>();

    @Override
    protected void initViewsAndEvents(View view) {
        mStickyLV = (StickyListHeadersListView) view.findViewById(R.id.sticky_list_view);
        mEmptyTV = (TextView) view.findViewById(R.id.necessary_empty_tv);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        mStickyLV.setOnItemClickListener(new OnItemClick());
        mStickyLV.setOnItemLongClickListener(new OnPlanItemLongClick());
        mStickyLV.setDividerHeight(0);
        mNecessaryAdapter = new NeccssaryFragmentAdapter(getActivity(), getSupportFragmentManager
                (), timerTasks);
        mStickyLV.setAdapter(mNecessaryAdapter);
        getData();
        //initPop();
    }

    private void getData() {
        String url = Constant.WEB_SITE + UrlConstant.URL_QUERY_NECESSARY;
        Response.Listener<JsonResult<List<NecessaryListInfo>>> successListener = new Response
                .Listener<JsonResult<List<NecessaryListInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<NecessaryListInfo>> result) {

                if (result == null || result.code != 0) {
                    //"服务端异常"
                    mEmptyTV.setVisibility(View.VISIBLE);
                    mEmptyTV.setText("服务器开小差了~");
                    return;
                }

                List<NecessaryListInfo> necessaryListInfoList = result.data;
                if (necessaryListInfoList != null && necessaryListInfoList.size() > 0) {
                    mEmptyTV.setVisibility(View.GONE);
                    setData(necessaryListInfoList);
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    mEmptyTV.setVisibility(View.VISIBLE);
                    mEmptyTV.setText("数据为空~");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                cn.ngame.store.core.utils.Log.d(TAG, "HTTP请求失败：网络连接错误！");
                mEmptyTV.setVisibility(View.VISIBLE);
                mEmptyTV.setText("服务器开小差了~");
            }
        };

        Request<JsonResult<List<NecessaryListInfo>>> request = new
                GsonRequest<JsonResult<List<NecessaryListInfo>>>(Request
                        .Method.POST, url,
                        successListener, errorListener, new
                        TypeToken<JsonResult<List<NecessaryListInfo>>>() {
                        }.getType()) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                        params.put(KeyConstant.parentId, Constant.APP_TYPE_ID_0_ANDROID);
                        return params;
                    }
                };
        StoreApplication.requestQueue.add(request);
    }


    private class OnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long itemPosition = mNecessaryList.get(position).getToolId();
            String getItemTitle = mNecessaryList.get(position).getToolName();
            Log.d(TAG, getItemTitle + ",点击" + itemPosition);
        }
    }

    private class OnPlanItemLongClick implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //长按删除一行
           /* NecessaryItemData oLangyaSimple = mNecessaryList.get(position);
            mNecessaryList.remove(oLangyaSimple);
            if (mNecessaryAdapter != null && mNecessaryList != null) {
                mNecessaryAdapter.setDate(mNecessaryList);
                mNecessaryAdapter.notifyDataSetChanged();
            }*/
            return true;
        }
    }

    private void setData(List<NecessaryListInfo> necessaryListInfoList) {
        mNecessaryList.clear();
        mNecessaryAdapter.setDate(mNecessaryList);
        for (int i = 0; i < necessaryListInfoList.size(); i++) {
            NecessaryListInfo necessaryListInfo = necessaryListInfoList.get(i);
            int id = necessaryListInfo.getId();
            String toolName = necessaryListInfo.getToolName();
            List<NecessaryListInfo.AuxiliaryToolsBean> singeToolList = necessaryListInfo
                    .getAuxiliaryTools();
            Log.d(TAG, "数据: " + singeToolList.size());
            if (singeToolList != null) {
                for (int j = 0; j < singeToolList.size(); j++) {
                    mToolInfo = singeToolList.get(j);
                    mNecessaryList.add(new NecessaryItemData(id + "", toolName, mToolInfo.getId()
                            , mToolInfo.getToolName()
                            , mToolInfo.getInstallDesc(), Formatter.formatFileSize(content,
                            mToolInfo.getFileSize()),
                            mToolInfo.getToolLogo(), mToolInfo.getToolURL(), mToolInfo.getMd5(),
                            mToolInfo.getFileName(),
                            mToolInfo
                                    .getPackages(), mToolInfo.getToolVersion()));
                }
            }
        }
        mNecessaryAdapter.setDate(mNecessaryList);
    }

    /**
     * 初始化数据时，数据必须proj_id必须按分组排列，
     * 即，不要将proj_id不同的数据      参差着放在集合中，
     * 否则容易造成列表显示多组相同组名的数据。
     */
/*    private void initDatas() {
        mNecessaryList.add(new NecessaryItemData("1", "谷歌", "4", "谷歌安装器", "未知", getString(R.string
                .necessary_content_desc)));
        mNecessaryList.add(new NecessaryItemData("1", "谷歌", "5", "谷歌安装器", "未知", getString(R.string
                .necessary_content_desc)));
        mNecessaryList.add(new NecessaryItemData("2", "腾讯", "54", "腾讯助手", "腾讯助手腾讯助手腾讯助手腾讯助手",
        getString(R.string
                .necessary_content_desc)));
        mNecessaryList.add(new NecessaryItemData("2", "腾讯", "10", "腾讯助手", "腾讯助手腾讯助手腾讯助手腾讯助手",
        getString(R.string
                .necessary_content_desc)));


        mNecessaryList.add(new NecessaryItemData("5", "百度", "11", "百度助手", "百度助手百度助手百度助手百度助手百度助手",
         getString(R.string
                .necessary_content_desc)));
        mNecessaryList.add(new NecessaryItemData("5", "百度", "16", "百度助手", "百度助手百度助手百度助手百度助手百度助手",
         getString(R.string
                .necessary_content_desc)));

    }*/
    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(1, "不再喜欢", null);
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction
                .OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //获取gameId  传给服务器 不再喜欢
                    String currentGameId = mNecessaryAdapter.getItemGameId();
                }
                //取消弹出框
                mItemClickQuickAction.dismiss();
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
      /*  if (null != mNecessaryAdapter) {
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }*/
    }

    @Override
    protected void onFirstUserVisible() {
        //getLikeList();
    }

    protected final static String TAG = NecessaryFragment.class.getSimpleName();

    @Override
    protected void onUserVisible() {
        //getLikeList();
    }


    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}