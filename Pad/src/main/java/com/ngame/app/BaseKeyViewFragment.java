package com.ngame.app;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/2.
 */

public abstract class BaseKeyViewFragment extends BaseFragment {
    protected ViewDragInterface viewDragInterface;
    private boolean m_isPress = true;

    public BaseKeyViewFragment() {
        super();
    }

    public final void setViewDragInterface(ViewDragInterface viewDragInterface){
        this.viewDragInterface = viewDragInterface;
    }

    public abstract void setImgPressState(ImageView view);

    public final boolean viewMove(View view, MotionEvent motionEvent){
        LLog.d("BaseKeyViewFragment->viewMove action:" + motionEvent.getAction());
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                m_isPress = true;
                setImgPressState((ImageView)view);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if(!m_isPress){
                    return true;
                }
                m_isPress = false;
                if(viewDragInterface != null){
                    int id = view.getId();
                    if (id == R.id.iv_key_a) {
                        view.setTag(KeyEnum.A);
                    } else if (id == R.id.iv_key_b) {
                        view.setTag(KeyEnum.B);
                    } else if (id == R.id.iv_key_x) {
                        view.setTag(KeyEnum.X);
                    } else if (id == R.id.iv_key_y) {
                        view.setTag(KeyEnum.Y);
                    } else if (id == R.id.iv_key_up) {
                        view.setTag(KeyEnum.UP);
                    } else if (id == R.id.iv_key_down) {
                        view.setTag(KeyEnum.DOWN);
                    } else if (id == R.id.iv_key_left) {
                        view.setTag(KeyEnum.LEFT);
                    } else if (id == R.id.iv_key_right) {
                        view.setTag(KeyEnum.RIGHT);
                    } else if (id == R.id.iv_key_r1) {
                        view.setTag(KeyEnum.R1);
                    } else if (id == R.id.iv_key_r2) {
                        view.setTag(KeyEnum.R2);
                    } else if (id == R.id.iv_key_l1) {
                        view.setTag(KeyEnum.L1);
                    } else if (id == R.id.iv_key_l2) {
                        view.setTag(KeyEnum.L2);
                    } else if (id == R.id.iv_key_start) {
                        view.setTag(KeyEnum.START);
                    } else if (id == R.id.iv_key_back) {
                        view.setTag(KeyEnum.BACK);
                    } else if (id == R.id.iv_key_r) {
                        view.setTag(KeyEnum.R);
                    } else if (id == R.id.iv_key_direction) {
                        view.setTag(KeyEnum.F);
                    } else {
                        LLog.d("BaseKeyViewFragment->viewMove ACTION_MOVE default");
                    }

                    viewDragInterface.setCurDragView((ImageView)view);
                    if(viewDragInterface.isViewDraged((ImageView)view)){
                        view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                    }

                    sSetKeyUpImgView((ImageView)view);
                    return true;
                }
                view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                sSetKeyUpImgView((ImageView)view);
                break;
            }
        }
        return true;
    }

    public static void setPressImgView(ImageView view){
        int vId = view.getId();
        if (vId == R.id.iv_key_a) {
            view.setImageResource(R.mipmap.ic_key_a_press);
        } else if (vId == R.id.iv_key_b) {
            view.setImageResource(R.mipmap.ic_key_b_press);
        } else if (vId == R.id.iv_key_x) {
            view.setImageResource(R.mipmap.ic_key_x_press);
        } else if (vId == R.id.iv_key_y) {
            view.setImageResource(R.mipmap.ic_key_y_press);
        } else if (vId == R.id.iv_key_up) {
            view.setImageResource(R.mipmap.ic_key_up_press);
        } else if (vId == R.id.iv_key_down) {
            view.setImageResource(R.mipmap.ic_key_down_press);
        } else if (vId == R.id.iv_key_left) {
            view.setImageResource(R.mipmap.ic_key_left_press);
        } else if (vId == R.id.iv_key_right) {
            view.setImageResource(R.mipmap.ic_key_right_press);
        } else if (vId == R.id.iv_key_r1) {
            view.setImageResource(R.mipmap.ic_key_r1_press);
        } else if (vId == R.id.iv_key_r2) {
            view.setImageResource(R.mipmap.ic_key_r2_press);
        } else if (vId == R.id.iv_key_l1) {
            view.setImageResource(R.mipmap.ic_key_l1_press);
        } else if (vId == R.id.iv_key_l2) {
            view.setImageResource(R.mipmap.ic_key_l2_press);
        } else if (vId == R.id.iv_key_start) {
            view.setImageResource(R.mipmap.ic_key_start_press);
        } else if (vId == R.id.iv_key_back) {
            view.setImageResource(R.mipmap.ic_key_back_press);
        } else if (vId == R.id.iv_key_r) {
            view.setImageResource(R.mipmap.ic_key_r_press);
        } else if (vId == R.id.iv_key_direction) {
            view.setImageResource(R.mipmap.ic_key_direction_press);
        } else {
            LLog.d("BaseKeyViewFragment->setPressImgView default");
        }
    }

    public static void sSetKeyUpImgView(ImageView view){
        int vId = view.getId();
        if (vId == R.id.iv_key_a) {
            view.setImageResource(R.mipmap.ic_key_a);
        } else if (vId == R.id.iv_key_b) {
            view.setImageResource(R.mipmap.ic_key_b);
        } else if (vId == R.id.iv_key_x) {
            view.setImageResource(R.mipmap.ic_key_x);
        } else if (vId == R.id.iv_key_y) {
            view.setImageResource(R.mipmap.ic_key_y);
        } else if (vId == R.id.iv_key_up) {
            view.setImageResource(R.mipmap.ic_key_up);
        } else if (vId == R.id.iv_key_down) {
            view.setImageResource(R.mipmap.ic_key_down);
        } else if (vId == R.id.iv_key_left) {
            view.setImageResource(R.mipmap.ic_key_left);
        } else if (vId == R.id.iv_key_right) {
            view.setImageResource(R.mipmap.ic_key_right);
        } else if (vId == R.id.iv_key_r1) {
            view.setImageResource(R.mipmap.ic_key_r1);
        } else if (vId == R.id.iv_key_r2) {
            view.setImageResource(R.mipmap.ic_key_r2);
        } else if (vId == R.id.iv_key_l1) {
            view.setImageResource(R.mipmap.ic_key_l1);
        } else if (vId == R.id.iv_key_l2) {
            view.setImageResource(R.mipmap.ic_key_l2);
        } else if (vId == R.id.iv_key_start) {
            view.setImageResource(R.mipmap.ic_key_start);
        } else if (vId == R.id.iv_key_back) {
            view.setImageResource(R.mipmap.ic_key_back);
        } else if (vId == R.id.iv_key_r) {
            view.setImageResource(R.mipmap.ic_key_r);
        } else if (vId == R.id.iv_key_direction) {
            view.setImageResource(R.mipmap.ic_key_direction);
        } else {
            LLog.d("BaseKeyViewFragment->sSetKeyUpImgView default");
        }
    }
}
