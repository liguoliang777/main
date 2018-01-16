package cn.ngame.store.activity.sm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;


/**
 * App设置页面
 * Created by liguoliang on 2016/12/7.
 */
public class SupportedJoypadListActivity extends BaseFgActivity {

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_singe_item_list);

        Button backBt = (Button) findViewById(R.id.left_bt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBt.setText("游戏手柄支持列表");
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setVisibility(View.INVISIBLE);

        initDatas();
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList("Ngame", "新游", "明品","小霸王","金貂","科腾","GT-COUPE机器酷博",
                "WELCOME惠康","北通"));
    }

    public class GalleryAdapter extends
            RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<String> mDatas;

        public GalleryAdapter(Context context, List<String> datats) {
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
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.item_singe_textview,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.mTxt = (TextView) view
                    .findViewById(R.id.singer_item_tv);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.mTxt.setText(mDatas.get(i));
        }

    }
}