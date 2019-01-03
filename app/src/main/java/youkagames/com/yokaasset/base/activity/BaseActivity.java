package youkagames.com.yokaasset.base.activity;


import android.content.res.TypedArray;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.YokaApplication;
import youkagames.com.yokaasset.base.view.SystemBarTintManager;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.ActivityQueueManager;
import youkagames.com.yokaasset.view.CustomProgressDialog;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.IBaseControl;
import youkagames.com.yokaasset.view.rollpageview.CustomPoupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by songdehua on 2018/11/27.
 */


public abstract class BaseActivity extends Activity implements IBaseControl {
    public boolean isUseTranslucent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            LogUtil.i("dehua", "onCreate fixOrientation when Oreo, result = " + result);
        }


        super.onCreate(savedInstanceState);
        if (isUseTranslucent){

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
                boolean result = fixOrientation();
                LogUtil.i("dehua", "onCreate fixOrientation when Oreo, result = " + result);
            }

            //设置竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.lib_system_bar_color));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.lib_system_bar_color);
            }
        }
        ActivityQueueManager.getInstance().pushActivity(this);

        EventBus.getDefault().register(this);
    }

    public void initSystemBar(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        CustomPoupWindow.getInstance().hidePopWindow();
        ActivityQueueManager.getInstance().popActivity(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        YokaApplication.getRefWatcher(this).watch(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showProgress() {
        CustomProgressDialog.getInstance().createLoadingDialog(this,false).show();
    }

    @Override
    public void RequestError(Throwable e) {
        CustomToast.showToast(this,"网络异常",Toast.LENGTH_SHORT);
    }

    @Override
    public void HideProgress() {
        CustomProgressDialog.getInstance().disMissDialog();
    }

    @Override
    public void NetWorkError() {
        CustomToast.showToast(this,R.string.net_not_connect,Toast.LENGTH_SHORT);
    }
    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 是否在前台
     * @return
     */
    protected boolean isFrontground(){
        boolean isFrontground = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if(runningTasks != null && !runningTasks.isEmpty()){
            String topActivityName = runningTasks.get(0).topActivity.getClassName();
            String currentActivityName = getClass().getName();
            LogUtil.i("pik" , "topActivityName : " + topActivityName + "----currentActivityName : " + currentActivityName);
            isFrontground = topActivityName.equals(currentActivityName);
        }
        return isFrontground;
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


    /**
     * 获得activity的系统级根布局
     *
     * @return
     */
    public ViewGroup getActivityBaseViewGroup() {
        return (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
    }

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
        overridePendingTransition(R.anim.in_from_right,R.anim.out_from_left);
    }

    /**
     * 空的方法，为了注册eventBus不报错
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String str) {
    }

    public void startActivityAnim(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    public void startActivityForResultAnim(Intent intent,int requestCode){
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            LogUtil.i("dehua", "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }
}