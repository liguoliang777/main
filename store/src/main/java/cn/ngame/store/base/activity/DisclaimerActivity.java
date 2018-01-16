package cn.ngame.store.base.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 显示 免责声明的 界面
 * Created by zeng on 2016/7/7.
 */
public class DisclaimerActivity extends BaseFgActivity {

    private static final String TAG = DisclaimerActivity.class.getSimpleName();

    private SimpleTitleBar title_bar;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_disclaimer);
        title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_content = (TextView) findViewById(R.id.text1);

        String s = "<html>" +
                "<p><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一切资源仅为学习交流娱乐所用，请在下载后24小时内删除，未经版权许可，\n" +
                "   任何单位或个人不得将本APP内容或服务用于商业目的。</strong></p>\n" +
                "<p><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;任何单位或个人认为通过Ngame提供的内容可能涉嫌侵犯其信息网络传播权，应该及时向Ngame提出书面权利通知，\n" +
                "   并提供身份证明、权属证明及详细侵权情况证明。\n" +
                "   Ngame在收到上述法律文件后，将会依法尽快断开相关链接内容。</strong></p>\n" +
                "</html>";
        tv_content.setText(Html.fromHtml(s));
    }

}
