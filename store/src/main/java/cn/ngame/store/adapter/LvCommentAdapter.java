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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.Comment;

/**
 * 视评论的Listview控件适配器
 *
 * @author zeng
 * @since 2016-05-30
 */
public class LvCommentAdapter extends BaseAdapter {

    private static final String TAG = LvCommentAdapter.class.getSimpleName();

    private List<Comment> comments;

    private Context context;

    public LvCommentAdapter(Context context, List<Comment> comments) {
        super();
        this.context = context;
        this.comments = comments;
    }

    /**
     * 设置ListView中的数据
     */
    public void setData(List<Comment> commentList) {

        if(comments == null){
            comments = commentList;
        }else {
            comments.addAll(commentList);
        }
    }

    @Override
    public int getCount() {

        if (comments != null) {
            return comments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (comments != null) {
            return comments.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if(comments != null){
            comments.clear();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context);

            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_comment, parent, false);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = comments.get(position);
        holder.update(comment);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     * @author flan
     * @since 2015年10月28日
     */
    public static class ViewHolder {

        private DateFormat df = new SimpleDateFormat("MM月dd日");
        private Context context;

        public RatingBar ratingBar;
        public ImageView img;
        public TextView tv_nickname, tv_summary,tv_time;

        public ViewHolder(Context context) {
            this.context = context;

        }

        public void update(Comment comment) {

            ratingBar.setRating(comment.chooseItemValue);
            tv_nickname.setText(comment.nickName);
            tv_summary.setText(comment.content);
            Date date = new Date();
            date.setTime(comment.createTime);
            tv_time.setText("发表于"+df.format(date));

            Picasso.with(context)
                    .load(comment.headPhoto)
                    .placeholder(R.drawable.login_photo_big)
                    .error(R.drawable.login_photo_big)
                    .fit()
                    .tag(context)
                    .into(img);
        }
    }

}














