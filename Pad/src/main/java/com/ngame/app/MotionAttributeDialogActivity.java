package com.ngame.app;

import android.content.Context;
import android.content.Intent;

import com.lx.pad.R;
import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MotionAttributeDialogActivity extends JoystickAttributeDialogActivity {
    protected final int getLayoutId(){
        return R.layout.dialog_motion_attributes_edit;
    }

    public static Intent makeIntent(Context context, KeyEnum keyEnum){
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(MotionAttributeDialogActivity.class.getName());
        intent.putExtra("btn", keyEnum.name());
        return intent;
    }
}
