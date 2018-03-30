package cn.ngame.store.game.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameCategorys;
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.fragment.ImageDialogFragment;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.gamehub.view.ShowViewActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.AutoHeightViewPager;

import static cn.ngame.store.R.id.tv_show_all2;

/**
 * 显示游戏详情的Fragment
 *
 * @author flan
 * @since 2016/5/17
 */
@SuppressLint("ValidFragment")
public class GameDetailFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GameDetailFragment.class.getSimpleName();
    private AutoHeightViewPager vp;
    private Activity context;
    private LinearLayout ll_detail;
    private GameInfo gameInfo;
    private TextView tv_summary, updateMsgTv, tv_version, tv_time, tv_company, tv_show_all,
            tv_show_all_2;
    private LinearLayout img_container;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<TextView> tagTvList = new ArrayList<>();
    private boolean flag1 = true;
    private boolean flag2 = true;
    private HomeFragmentChangeLayoutListener listener;
    private TextView tv_download_count;
    private TextView tv_game_size;
    private Button itemAllNext;
    private TextView labelTv0, labelTv1, labelTv2, labelTv3;
    private List<GameCategorys> gameLabels;
    private SimpleDraweeView picassoImageView;

    public GameDetailFragment(AutoHeightViewPager viewpager, GameInfo gameInfo) {
        this.vp = viewpager;
        this.gameInfo = gameInfo;
    }

    public GameDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_game_detail, null);
        tv_summary = (TextView) view.findViewById(R.id.tv_summary);
        updateMsgTv = (TextView) view.findViewById(R.id.tv_update_content);
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        tv_download_count = (TextView) view.findViewById(R.id.download_count_bottom_tv);
        tv_game_size = (TextView) view.findViewById(R.id.game_size_tv);
        tv_time = (TextView) view.findViewById(R.id.text);
        tv_company = (TextView) view.findViewById(R.id.tv_company);

        tv_show_all = (TextView) view.findViewById(R.id.tv_show_all);
        tv_show_all_2 = (TextView) view.findViewById(tv_show_all2);
        itemAllNext = (Button) view.findViewById(R.id.game_detail_next_bt);
        img_container = (LinearLayout) view.findViewById(R.id.img_container);
        ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);

        labelTv0 = (TextView) view.findViewById(R.id.tag_tv_01);
        labelTv1 = (TextView) view.findViewById(R.id.tag_tv_02);
        labelTv2 = (TextView) view.findViewById(R.id.tag_tv_03);
        labelTv3 = (TextView) view.findViewById(R.id.tag_tv_04);
        tagTvList.add(labelTv0);
        tagTvList.add(labelTv1);
        tagTvList.add(labelTv2);
        tagTvList.add(labelTv3);

        itemAllNext.setOnClickListener(this);
        tv_show_all.setOnClickListener(this);
        tv_show_all_2.setOnClickListener(this);

        if (gameInfo != null && gameInfo.gameDetailsImages != null && gameInfo.gameDetailsImages
                .size() > 0) {
            img_container.removeAllViews();
            imgs.clear();
            img_container.setBackgroundResource(R.color.transparent);
            List<GameImage> imagesList = gameInfo.gameDetailsImages;
            int dp250 = CommonUtil.dip2px(context, 250);
            int dp160 = CommonUtil.dip2px(context, 160);
            int dp10 = CommonUtil.dip2px(context, 10);
            int size = imagesList.size();
            for (int i = 0; i < size; i++) {
                imgs.add(imagesList.get(i).imageLink);

            }

            for (int i = 0; i < size; i++) {
                GameImage img = imagesList.get(i);
                if (1 != img.type) {
                    continue;
                }
                picassoImageView = new SimpleDraweeView(context);
                picassoImageView.setScaleType(ImageView.ScaleType.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams.WRAP_CONTENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT);
                params.width = dp250;
                params.height = dp160;
                if (size - 1 == i) {
                    params.setMargins(0, 0, 0, 0);
                } else {
                    params.setMargins(0, 0, dp10, 0);
                }
                picassoImageView.setLayoutParams(params);
                picassoImageView.setImageURI(img.imageLink);
                img_container.addView(picassoImageView);
                final int finalI = i;
                picassoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent gv = new Intent();
                        gv.setClass(getActivity(), ShowViewActivity.class);
                        gv.putStringArrayListExtra("viewImages", imgs);
                        gv.putExtra("selectPosition", finalI);
                        startActivity(gv);
                    }
                });

            }
        }
        img_container.setOnClickListener(this);
        if (vp != null) {
            vp.setObjectForPosition(view, 0);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (gameInfo != null) {
            //标签
            gameLabels = gameInfo.gameCategorys;
            if (gameLabels != null) {
                int labelSize = gameLabels.size();
                int tvSize = tagTvList.size();
                int size = labelSize > tvSize ? tvSize : labelSize;
                final Intent intent = new Intent();
                intent.setClass(context, SeeMoreActivity.class);
                for (int i = 0; i < size; i++) {
                    TextView labelTv = tagTvList.get(i);
                    final GameCategorys gameLabel = this.gameLabels.get(i);
                    final String labelName = gameLabel.cName;
                    labelTv.setText(labelName);
                    labelTv.setVisibility(View.VISIBLE);
                    labelTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra(KeyConstant.category_Id, String.valueOf(gameLabel.id));
                            intent.putExtra(KeyConstant.TITLE, labelName);
                            startActivity(intent);
                        }
                    });

                }
            }
            tv_summary.setText(gameInfo.gameDesc);
            String gameUpdateInfoStr = this.gameInfo.gameInfo;
            updateMsgTv.setText(gameUpdateInfoStr == null ? "" : gameUpdateInfoStr);//更新内容
            updateMsgTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                    .OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    updateMsgTv.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (updateMsgTv.getLineCount() > 5) {
                        tv_show_all_2.setVisibility(View.VISIBLE);
                        updateMsgTv.setMaxLines(5);
                    }
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                    return false;
                }
            });
            tv_summary.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                    .OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    tv_summary.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (tv_summary.getLineCount() > 5) {
                        tv_show_all.setVisibility(View.VISIBLE);
                        tv_summary.setMaxLines(5);
                    }
//                    listener.changeLayoutHeight(ll_detail.getMeasuredHeight());
                    return false;
                }
            });
            tv_version.setText(this.gameInfo.versionName);
            tv_game_size.setText(Formatter.formatFileSize(context, this.gameInfo.gameSize));
            tv_download_count.setText(this.gameInfo.downloadCount + "");
            tv_time.setText(df.format(new Date(this.gameInfo.updateTime)));
            String gameAgent = gameInfo.gameAgent;
            tv_company.setText(gameAgent);
        }
        vp.setObjectForPosition(view, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载大图片
     */
    private void imageEnlarge() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("imageDialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);

        final ImageDialogFragment dialogFragment = ImageDialogFragment.newInstance();
        dialogFragment.show(transaction, "imageDialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_all:
                if (flag1) {
                    flag1 = false;
                    tv_show_all.setText("收起");
                    tv_summary.setSingleLine(false);
                } else {
                    flag1 = true;
                    tv_show_all.setText("显示全部");
                    tv_summary.setMaxLines(5);
                }
                break;
            case R.id.tv_show_all2:
                if (flag2) {
                    flag2 = false;
                    tv_show_all_2.setText("收起");
                    updateMsgTv.setSingleLine(false);
                } else {
                    flag2 = true;
                    tv_show_all_2.setText("显示全部");
                    updateMsgTv.setMaxLines(5);
                }
                break;
            //所有标签页面
            case R.id.game_detail_next_bt:
                if (null != gameInfo) {
                    Intent intent = new Intent(context, LabelsActivity.class);
                    intent.putExtra(KeyConstant.game_Name, gameInfo.gameName);
                    intent.putExtra(KeyConstant.GAME_LABELS, (Serializable) gameLabels);
                    startActivity(intent);
                } else {
                    ToastUtil.show(context, "无标签数据");
                }
                break;
        }
    }
}
