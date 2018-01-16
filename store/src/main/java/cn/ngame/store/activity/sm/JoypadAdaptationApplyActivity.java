package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.util.ToastUtil;


/**
 * App设置页面
 * Created by liguoliang on 2016/12/7.
 */
public class JoypadAdaptationApplyActivity extends BaseFgActivity {

    private TextView typeNameTv;
    private TextView typeNumTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_joypad_adaptation_apply);

        Button backBt = (Button) findViewById(R.id.left_bt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBt.setText("手柄适配申请");
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setVisibility(View.INVISIBLE);

        typeNameTv = (TextView) findViewById(R.id.joypad_type_name_tv);
        typeNumTv = (TextView) findViewById(R.id.joypad_type_num_tv);

    }

    public void onJoypadApplyClick(View view) {
        String typeNameStr = typeNameTv.getText().toString().trim();
        String typeNumStr = typeNumTv.getText().toString().trim();
        if (TextUtil.isEmpty(typeNameStr)) {
            ToastUtil.show(this, "品牌名称不能为空哦");
            return;
        }
        if (TextUtil.isEmpty(typeNumStr)) {
            ToastUtil.show(this, "手柄型号不能为空哦");
            return;
        }
        ToastUtil.show(this, "提交成功");
        finish();
    }
}