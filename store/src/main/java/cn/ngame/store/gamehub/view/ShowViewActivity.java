package cn.ngame.store.gamehub.view;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.widget.TouchImageView;

/**
 * 帖子详情-查看大图
 */
public class ShowViewActivity extends FragmentActivity {
    // 用于装载4个图片布局View
    private ArrayList<View> imgViews;

    // 声明main布局对象,对应main.xml
    private View main;

    // 声明滑动图片显示区域ViewPager
    private ViewPager viewPager;

    // 声明导航显示区域,因为要装载多个小圆点,所以声明为ViewGroup
    private ViewGroup pointGroup;

    // 声明ImageView[],有多少图片,就应该有对应的导航小圆点view,这些view都放在这个数组中
    private ImageView[] pointViews;

    // 具体一个小圆点
    private boolean flag = false; // 等待框 是否显示
    private int initLocal = 0; // viewpager初始位置

    // 模拟图片网络路径,正常情况下,这些路径可以通过远程加载方式获取.现在固定路径简单测试
    private String[] paths = {"http://www.foreveross.com/foreveross/images/banner05.jpg",
            "http://www.foreveross.com/foreveross/images/banner07.jpg",
            "http://www.foreveross.com/foreveross/images/banner03.jpg",
            "http://www.foreveross.com/foreveross/images/banner04.jpg"};
    private ArrayList<String> imgs = new ArrayList<>();
    private TouchImageView iv;
    private ShowViewActivity content;
    protected static final String TAG = "ShowViewActivity";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        main = getLayoutInflater().inflate(R.layout.activity_show_view, null);
        content = this;
        imgs = getIntent().getStringArrayListExtra("viewImages");
        //7. 通过main layout对象获取图片区域ViewPager
        viewPager = (ViewPager) main.findViewById(R.id.imagePages);
        //8. 通过main layout对象获取导航指示区域
        pointGroup = (ViewGroup) main.findViewById(R.id.pointGroup);
        if (imgs == null || imgs.size() == 0) {
            Log.d(TAG, "图片为空: ");
            ImageView imageView = (ImageView) main.findViewById(R.id.show_view_iv);
            imageView.setVisibility(View.VISIBLE);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    content.finish();
                }
            });
            setContentView(main);
            return;
        }
        initLocal = getIntent().getIntExtra("selectPosition", 0);

        DialogHelper.showWaiting(getSupportFragmentManager(), "加载中...");
        // 2. 启动线程用于加载远程图片
        new LoadImageThread().start();
    }

    class LoadImageThread extends Thread {
        @Override
        public void run() {
            //3. 获取LayoutInflater,目的是方便后面得到xml布局文件进行装配
            //4. 创建图片view存储集合
            imgViews = new ArrayList<>();

            Looper.prepare();
            //5. 通过网络方式下载图片,并最终放在集合中
            int size = imgs.size();
            for (int i = 0; i < size; i++) {
                iv = new TouchImageView(content);
                //从网上取图片
                iv.setImageBitmap(getHttpBitmap(imgs.get(i)));
                imgViews.add(iv);

                iv.setOnImageClickListener(new TouchImageView.OnClickListener() {
                    @Override
                    public void onClick() {
                        content.finish();
                    }
                });
            }
            //6. 获取main.xml layout对象,他是装配其他图片布局的中心点
            //   要记得,它里面声明了一个图片区域ViewPager,以及一个导航指示区域
            // main = getLayoutInflater().inflate(R.layout.activity_show_view, null);
            //9. 下面开始控制导航小圆点,有多少张img,就要做多大的小圆点数组
            pointViews = new ImageView[imgViews.size()];

            if (content == null && content.isFinishing()) {
                return;
            }
            ImageView iv;
            //10. 根据图片集合的长度决定创建多少小圆点ImageView
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
            int size1 = imgViews.size();
            for (int i = 0; i < size1; i++) {
//                imageView = new ImageView(ShowViewActivity.this);
//                imageView.setLayoutParams(new LayoutParams(15, 15));
//                imageView.setPadding(20, 0, 20, 0);
                if (i < 1) {
                    pointParams.setMargins(0, 0, 0, 0);
                } else {
                    pointParams.setMargins(10, 0, 0, 0);
                }
                iv = new ImageView(content);
                iv.setLayoutParams(pointParams);
                pointViews[i] = iv;
                if (i == 0) {
                    //默认选中第一张图片,加入焦点
                    pointViews[i].setBackgroundResource(R.drawable.choosen_radius);
                } else {
                    pointViews[i].setBackgroundResource(R.drawable.white_radius);
                }
                // 把每一个导航小圆点都加入到ViewGroup中
                pointGroup.addView(pointViews[i]);
            }
            handler.sendEmptyMessage(0); //表示下载完毕.
            Looper.loop();
        }
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                this.finish();
            } else {
                if (content != null && !content.isFinishing()) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
            }
        }
        return false;
    }*/

    public Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL
                    .openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // adapter内部类
    // 指引页面数据适配器
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 通过集合的size告诉Adapter共有多少张图片
            return imgViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            // 移除旧的View
            ((ViewPager) arg0).removeView(imgViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            // 获取新的view
            ((ViewPager) arg0).addView(imgViews.get(arg1));
            return imgViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub
        }
    }

    // 监听器内部类
    // 指引页面更改事件监听器
    class PointChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            pointViews[arg0]
                    .setBackgroundResource(R.drawable.choosen_radius);
            for (int i = 0; i < pointViews.length; i++) {
                if (arg0 != i) {
                    pointViews[i]
                            .setBackgroundResource(R.drawable.white_radius);
                }
            }
            // 简单测试:
            if (arg0 == pointViews.length - 1) {
                Log.i("show", "last page");
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {

                    if (content != null && !content.isFinishing()) {
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                    }
                    //11. 设置main布局为当前Activity内容
                    setContentView(main);
                    //12. 设置viewPager 图片切换Adapter,图片最终能够切换就是在Adapter中实现的
                    viewPager.setAdapter(new ViewPagerAdapter());
                    //13. 设置viewPager 页面改变监听器,利用监听器改变小圆点焦点状态
                    viewPager.setOnPageChangeListener(new PointChangeListener());
                    //14. 根据需求 初始位置
                    viewPager.setCurrentItem(initLocal);
                    break;
                }
            }
        }
    };

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.show_view, menu);
//		return true;
//	}
}
