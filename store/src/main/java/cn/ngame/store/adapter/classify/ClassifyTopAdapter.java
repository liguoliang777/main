
package cn.ngame.store.adapter.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.ClassifyTopBean;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.SeeMoreActivity;

/**
 * @author gp
 */
public class ClassifyTopAdapter extends RecyclerView.Adapter<ClassifyTopAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<ClassifyTopBean> list;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, int text);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public ClassifyTopAdapter(Context context, List<ClassifyTopBean> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<ClassifyTopBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_classify_top_item,
                parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list == null) {
            return;
        }
        final ClassifyTopBean categroyBean = list.get(position);
        final String name = categroyBean.getName();

        holder.tv_content.setText(name);
        holder.iconIv.setImageResource(categroyBean.getIcon());
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, SeeMoreActivity.class);
                intent.putExtra(KeyConstant.category_Id, categroyBean.getId() + "");//原生手柄 id 367
                intent.putExtra(KeyConstant.TITLE, name);
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
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconIv;
        private TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.singer_item_tv);
            iconIv = itemView.findViewById(R.id.classify_item_iv);
        }
    }
}














