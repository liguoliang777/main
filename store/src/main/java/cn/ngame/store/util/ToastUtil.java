package cn.ngame.store.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    //分别对应Toast.LENGTH_LONG（3.5秒）和Toast.LENGTH_SHORT（2秒）的值
    //分别对应Toast.LENGTH_LONG（3.5秒）和Toast.LENGTH_SHORT（2秒）的值
    public static void show(Context context , String message){
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else{
            toast.setText(message);
        }
        toast.show();
    }
    public static void longShow(Context context , String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void CenterToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 带图片的toast
    public static void ImageToast(Context context, int imgid, String msg) {
        Toast toast = Toast.makeText(context,
                msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(imgid);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }
}
