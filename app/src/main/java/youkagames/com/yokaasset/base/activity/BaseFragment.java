package youkagames.com.yokaasset.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.YokaApplication;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.IBaseControl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by songdehua on 2018/11/28.
 */


public abstract class BaseFragment extends Fragment implements IBaseControl{
    protected boolean isInit = false;
    private boolean isFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*
     * 返回一个需要展示的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInit = true;
        View view = initView(inflater, container);
        initFindViewById(view);
        isCanLoadData();
        return view;
    }

    /*
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void isCanLoadData() {
        if(!isInit){
            return;
        }
        if (getUserVisibleHint() && isFirst) {
            isFirst = false;
            initDataFragment();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 子类实现此抽象方法返回View进行展示
     *
     * @return
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化控件
     */
    protected abstract void initFindViewById(View view);

    /**
     * 子类在此方法中实现数据的初始化
     */
    public abstract void initDataFragment();


    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
//        MobclickAgent.onResume(getActivity());
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//        MobclickAgent.onPause(getActivity());

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }
    }

    //分享 SSO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        isInit = false;
        isFirst = true;
        RefWatcher refWatcher = YokaApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void HideProgress() {

    }

    @Override
    public void NetWorkError() {

    }

    @Override
    public void RequestError(Throwable e) {
        CustomToast.showToast(getActivity(), R.string.net_error, Toast.LENGTH_SHORT);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(Object object) {
//
//    }

    /**
     * 空的方法，为了注册eventBus不报错
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String str) {
    }
    public void startActivityAnim(Intent intent){
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

    public void startActivityForResultAnim(Intent intent,int requestCode){
        getActivity().startActivityForResult(intent,requestCode);
        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

}
