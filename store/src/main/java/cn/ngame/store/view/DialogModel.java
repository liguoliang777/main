package cn.ngame.store.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.ngame.store.R;


public class DialogModel extends Dialog {
    private Context context;
    public SimpleDraweeView sdv_img;
    public ImageView iv_delete;
    String imgUrl;

    public DialogModel(Context context, String imgUrl) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.imgUrl = imgUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_model);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        sdv_img = (SimpleDraweeView) findViewById(R.id.sdv_img);

        if (imgUrl.contains("gif")) {
            Uri uri = Uri.parse(imgUrl);
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                    .setUri(uri)
                    .build();
            sdv_img.setController(draweeController);
        } else {
            sdv_img.setImageURI(imgUrl);
        }
    }
}
