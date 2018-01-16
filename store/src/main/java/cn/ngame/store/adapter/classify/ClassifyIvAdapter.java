
package cn.ngame.store.adapter.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * @author liguoliang
 */
public class ClassifyIvAdapter extends RecyclerView.Adapter<ClassifyIvAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<YunduanBean.DataBean> list;
    private Intent singeTopicsDetailIntent = new Intent();
    ;


    public ClassifyIvAdapter(Context context, List<YunduanBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
    }

    public void setList(List<YunduanBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_classify_iv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIV.setImageURI(list.get(position).getLogoUrl());
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YunduanBean.DataBean topicsInfo = list.get(position);
                singeTopicsDetailIntent.putExtra(KeyConstant.category_Id, topicsInfo.getId());
                singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, topicsInfo.getTypeName());
                singeTopicsDetailIntent.putExtra(KeyConstant.DESC, topicsInfo.getTypeDesc());
                singeTopicsDetailIntent.putExtra(KeyConstant.URL, topicsInfo.getLogoUrl());
                context.startActivity(singeTopicsDetailIntent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView mIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mIV = (SimpleDraweeView) itemView.findViewById(R.id.iv_item_iv);
        }
    }
}














