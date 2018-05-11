
package cn.ngame.store.adapter.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverTopBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.GameDetailActivity;

/**
 * @author gp
 */
public class DiscoverTop2Adapter extends RecyclerView.Adapter<DiscoverTop2Adapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<DiscoverTopBean> mList = new ArrayList<>();

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String tag);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public DiscoverTop2Adapter(Context context, List<DiscoverTopBean> list) {
        super();
        this.context = context;
        mList.clear();
        if (list.size() > 3) {
            this.mList.add(list.get(0));
            this.mList.add(list.get(1));
            this.mList.add(list.get(2));
            this.mList.add(list.get(3));
        } else {
            this.mList = list;

        }
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<DiscoverTopBean> list) {
        mList.clear();
        if (list.size() > 3) {
            this.mList.add(list.get(0));
            this.mList.add(list.get(1));
            this.mList.add(list.get(2));
            this.mList.add(list.get(3));
        } else {
            this.mList = list;

        }
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_discover_top2, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiscoverTopBean discoverTopBean = mList.get(position);
        holder.mTV.setText(discoverTopBean.getGameName());
        holder.mIV.setImageURI(discoverTopBean.getImgLink());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra(KeyConstant.ID, discoverTopBean.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTV;
        private SimpleDraweeView mIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mTV = (TextView) itemView.findViewById(R.id.discover_top2_tv);
            mIV = (SimpleDraweeView) itemView.findViewById(R.id.discover_top2_sdv);
        }
    }
}














