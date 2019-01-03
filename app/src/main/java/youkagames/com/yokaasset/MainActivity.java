package youkagames.com.yokaasset;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;


import youkagames.com.yokaasset.base.activity.BaseFragment;
import youkagames.com.yokaasset.base.activity.BaseFragmentActivity;
import youkagames.com.yokaasset.fragment.AssetFragment;
import youkagames.com.yokaasset.fragment.MineFragment;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.utils.ActivityQueueManager;
import youkagames.com.yokaasset.view.IBaseControl;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.CommonUtil;

//import com.igexin.sdk.PushManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseFragmentActivity implements IBaseControl {
    private Fragment[] mFragments;
    private ViewPager mViewPager;
    private String[] mMainTitle;
    private TypedArray mTypedArray;

    /**
     * 权限检查器
     */
    RxPermissions rxPermissions;
    //private CustomChooseDialog mChooseDialog;
    private static long oldSystemTime;
    private TabLayout tablayout;
    //private UserPresenter mPresenter;
    private static final int REQUEST_PERMISSION = 0;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("MainActivity onCreate");
        setContentView(R.layout.activity_main);

        initData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //initIMData();
                        PackageManager pkgManager = getPackageManager();
                        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
                        boolean sdCardWritePermission =
                                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

                        // read phone state用于获取 imei 设备信息
                        boolean phoneSatePermission =
                                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

                        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
                            requestPermission();
                        } else {
                            //PushManager.getInstance().initialize(getApplicationContext(), GeTuiPushService.class);
                        }

                        //PushManager.getInstance().registerPushIntentService(getApplicationContext(), GeTuiIntentService.class);
                    }
                });
            }
        }).start();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @SuppressLint("CheckResult")
    private void initData() {
        mMainTitle = getResources().getStringArray(R.array.main);
        mTypedArray =getResources().obtainTypedArray(R.array.main_icon);
        mFragments = new Fragment[2];
        tablayout = findViewById(R.id.tabLayout);
        mViewPager=  findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(mMainTitle.length);
        tablayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
            tab.getCustomView();
        }
    }

    public View getTabView(final int position){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_view, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(mMainTitle[position]);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(mTypedArray.getResourceId(position,R.drawable.tab_mine_selector));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem()!=position){
                    mViewPager.setCurrentItem(position);
                    return;
                }
                if(mFragments == null || mFragments[position] == null){
                    return;
                }

                if (!CommonUtil.fastClick()) {
                    switch (position) {
                        case 0:
                            ((AssetFragment) mFragments[position]).refreshDataState();
                            break;
                        //case 1:
                        //    ((GameFragment) mFragments[position]).refreshDataState();
                        //    break;
                    }

                }
            }
        });
        return view;
    }

    class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    mFragments[0] = new AssetFragment();
                    break;
                case 1:
                    mFragments[1] = new MineFragment();
                    //mFragments[1] = new AssetFragment();
                    break;
            }
            return mFragments[position];
        }
        @Override
        public int getCount() {
            return mMainTitle.length;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //点击返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            exitBy2Click();
            return true;
        }
        return false;
    }


    protected static Boolean isExit = false;

    /**
     * 双击退出
     */
    protected void exitBy2Click() {
        Timer tExit;
        if (isExit == false) {
            isExit = true; // 准备退出
            CustomToast.showToast(this,R.string.press_again_quit,Toast.LENGTH_SHORT);
            tExit = new Timer();

            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            //MyDatabase.getInstance(this).getCommentDraftDao().clearTable();
            ActivityQueueManager.getInstance().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
