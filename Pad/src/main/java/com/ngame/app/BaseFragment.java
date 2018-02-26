package com.ngame.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/12/2.
 */

public abstract class BaseFragment extends Fragment {
    Context m_context;

    public BaseFragment() {
        super();
    }

    protected abstract View makeView(LayoutInflater layoutInflater, ViewGroup viewGroup);

    protected abstract void initView(View view);

    public Context getContext(){
        return (m_context != null) ? m_context : super.getContext();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return makeView(LayoutInflater.from(getContext()), container);
    }
}
