package youkagames.com.yokaasset.base.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.widget.EditText;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.YokaApplication;
import youkagames.com.yokaasset.base.view.SystemBarTintManager;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.ActivityQueueManager;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.IBaseControl;
import youkagames.com.yokaasset.view.rollpageview.CustomPoupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by songdehua on 2018/11/28.
 */


public class BaseFragmentActivity extends FragmentActivity implements IBaseControl {
    private InputMethodManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initSystemBar(R.color.lib_system_bar_color);
        ActivityQueueManager.getInstance().pushActivity(this);
        EventBus.getDefault().register(this);
    }
    public void initSystemBar(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /**
     * 如果传入的context不是activity那么给返回的intent添加new_task的flag
     * @param context
     * @param classObj
     * @return
     */
    public static Intent getNewIntent(Context context , Class classObj){
        Intent intent = new Intent(context , classObj);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right,R.anim.out_from_left);
    }
    public void startActivityAnim(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    public void startActivityForResultAnim(Intent intent,int requestCode){
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomPoupWindow.getInstance().hidePopWindow();
        ActivityQueueManager.getInstance().popActivity(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        YokaApplication.getRefWatcher(this).watch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    /**
     * 空的方法，为了注册eventBus不报错
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String str) {
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
        CustomToast.showToast(this,R.string.net_error, Toast.LENGTH_SHORT);

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     * @param v
     * @param event
     * @return
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                //点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null){
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), 0);//
            }
        }
    }

}
