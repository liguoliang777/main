package cn.ngame.store.game.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.GameCategorys;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ToastUtil;

/**
 * Created by zeng on 2016/8/24.
 */
public class LabelsActivity extends BaseFgActivity {

    private RecyclerView mRecyclerView;
    private LabelsItemAdapter mAdapter;
    private List<GameCategorys> mDatas = new ArrayList<>();
    private LabelsActivity content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_singe_item_list);
        content = this;
        Intent intent = getIntent();
        mDatas = (List<GameCategorys>) intent.getSerializableExtra(KeyConstant.GAME_LABELS);
        Button backBt = (Button) findViewById(R.id.left_bt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBt.setText(intent.getStringExtra(KeyConstant.game_Name) + "标签");
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setVisibility(View.INVISIBLE);

        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new LabelsItemAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        if (mDatas == null || mDatas.size() == 0) {
            ToastUtil.show(content, "暂无标签");
        }
    }

    public class LabelsItemAdapter extends RecyclerView.Adapter<LabelsItemAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<GameCategorys> mDatas;

        public LabelsItemAdapter(Context context, List<GameCategorys> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            TextView mTxt;
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.item_singe_tv_iv_next,
                    viewGroup, false);
            LabelsItemAdapter.ViewHolder viewHolder = new LabelsItemAdapter.ViewHolder(view);

            viewHolder.mTxt = (TextView) view.findViewById(R.id.singer_item_tv);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            if (mDatas == null) {
                return;
            }
            final GameCategorys gameLabels = mDatas.get(i);
            final String itemLabelName = gameLabels.cName;
            viewHolder.mTxt.setText(itemLabelName);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setClass(content, SeeMoreActivity.class);
                    i.putExtra(KeyConstant.category_Id, String.valueOf(gameLabels.id));// 动作游戏精选 getId()==369
                    i.putExtra(KeyConstant.TITLE, itemLabelName);//mStickyLV.get(position).getTypeName()
                    startActivity(i);
                }
            });
        }

    }
}
