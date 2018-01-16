package cn.ngame.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.ngame.store.core.utils.CommonUtil;

/**
 * 自定义 自动加载图片控件
 * Created by zeng on 2016/7/28.
 */
public class PicassoImageView extends ImageView {

    private Context context;

    public PicassoImageView(Context context) {
        super(context);
        this.context = context;
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    /**
     * 指定需要加载图片的地方
     * @param url               图片的URL地址
     * @param width             图片宽度 单位dp
     * @param height            图片高度 单位dp
     * @param defaultImageId    图片加载失败后的默认图片
     * @return
     */
    public void setImageUrl(String url,float width,float height,int defaultImageId){

        if(url != null && url.trim().equals("")){
            url = null;
        }

        Picasso.with(context)
                .load(url)
                .placeholder(defaultImageId)
                .error(defaultImageId)
                .resize(CommonUtil.dip2px(context,width),CommonUtil.dip2px(context,height))
                .centerCrop()
                .tag(context)
                .into(this);
    }

    /**
     * 指定需要加载图片的地方
     * @param url               图片的URL地址
     * @param width             图片宽度 单位px
     * @param height            图片高度 单位px
     * @param defaultImageId    图片加载失败后的默认图片
     * @return
     */
    public void setImageUrl(String url,int width,int height,int defaultImageId){

        if(url != null && url.trim().equals("")){
            url = null;
        }

        Picasso.with(context)
                .load(url)
                .placeholder(defaultImageId)
                .error(defaultImageId)
                .resize(width,height)
                .centerCrop()
                .tag(context)
                .into(this);
    }

    /**
     * 指定需要加载图片的地方
     * @param url               图片的URL地址
     * @param defaultImageId    图片加载失败后的默认图片
     * @return
     */
    public void setImageUrl(String url,int defaultImageId){

        if(url != null && url.trim().equals("")){
            url = null;
        }
        Picasso.with(context)
                .load(url)
                .placeholder(defaultImageId)
                .error(defaultImageId)
                .fit()
                .tag(context)
                .into(this);
    }

    /**
     * 指定需要加载图片的地方
     * @param url               图片的URL地址
     * @return
     */
    public void setImageUrl(String url){

        Picasso.with(context)
                .load(url)
                .fit()
                .into(this);
    }

}
