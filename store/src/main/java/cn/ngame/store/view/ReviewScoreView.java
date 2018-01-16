package cn.ngame.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.QuestionResult;

/**
 * 显示总评分的控件
 * Created by zeng on 2016/6/13.
 */
public class ReviewScoreView extends LinearLayout {

    private TextView percentTv;
    private RatingBar ratingBar;
    private ProgressBar pb1, pb2, pb3, pb4, pb5;

    private List<QuestionResult> resultList;
    private Context context;


    public ReviewScoreView(Context context) {
        this(context, null);
    }

    public ReviewScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ReviewScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflate(context, R.layout.layout_review_score, this);
        percentTv = (TextView) findViewById(R.id.tv_percent);
        ratingBar = (RatingBar) findViewById(R.id.sv_rating_bar);

        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        pb3 = (ProgressBar) findViewById(R.id.progressBar3);
        pb4 = (ProgressBar) findViewById(R.id.progressBar4);
        pb5 = (ProgressBar) findViewById(R.id.progressBar5);
    }

    public void setData(GameInfo gameInfo) {
        percentTv.setText("5.0");
        if (null == gameInfo) {
            return;
        }
        this.resultList = gameInfo.questionResults;
        int totalPeople = gameInfo.commentPeople;//总人数
        if (null == resultList) {
            return;
        }
        int starType;
        for (QuestionResult starItem : resultList) {
            starType = starItem.itemId;//对应分数值 1/2/3/4/5
            double starPeopleNum = starItem.commentPeople;//对应的人数
            int process = (int) (starPeopleNum / totalPeople * 100);
            switch (starType) {
                case 1:
                    pb1.setProgress(process);
                    break;
                case 2:
                    pb2.setProgress(process);
                    break;
                case 3:
                    pb3.setProgress(process);
                    break;
                case 4:
                    pb4.setProgress(process);
                    break;
                case 5:  //5星
                    pb5.setProgress(process);
                    break;

            }


            //总人数:totalPeople
        }

        ratingBar.setRating(gameInfo.percentage);
        //percentTv.setText(gameInfo.percentage + "");
    }


}
