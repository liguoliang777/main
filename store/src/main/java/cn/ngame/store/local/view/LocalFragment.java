package cn.ngame.store.local.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.ngame.store.R;

/**
 * 显示本地管理主页面
 * Created by zeng on 2016/10/11.
 */
public class LocalFragment extends Fragment implements View.OnClickListener{

    private static LocalFragment localFragment = null;

    public static LocalFragment getInstance(){
        if(localFragment == null){
            return localFragment = new LocalFragment();
        }
        return localFragment;
    }

    private Context context;
    private RelativeLayout rl_game,rl_video,rl_history;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.local_fragment_local,container,false);
        rl_game = (RelativeLayout) view.findViewById(R.id.rl_game);
        rl_game.setOnClickListener(this);
        rl_video = (RelativeLayout) view.findViewById(R.id.rl_video);
        rl_video.setOnClickListener(this);
        rl_history = (RelativeLayout) view.findViewById(R.id.rl_history);
        rl_history.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_game:

                Intent gameIntent = new Intent(context,MyGameActivity.class);
                context.startActivity(gameIntent);

                break;
            case R.id.rl_video:
                break;
            case R.id.rl_history:

                Intent historyIntent = new Intent(context,WatchHistoryActivity.class);
                context.startActivity(historyIntent);

                break;
        }
    }
}
