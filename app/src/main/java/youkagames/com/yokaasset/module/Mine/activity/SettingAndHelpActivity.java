package youkagames.com.yokaasset.module.Mine.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.model.eventbus.user.LoginUserInfoUpdateNotify;
import youkagames.com.yokaasset.module.Mine.UserPresenter;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.utils.DataCleanManager;
import youkagames.com.yokaasset.utils.FileSizeUtil;
import youkagames.com.yokaasset.utils.FileUtils;
import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.view.UpdateDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
/**
 * Created by songdehua on 2018/12/11.
 */

public class SettingAndHelpActivity extends BaseActivity implements View.OnClickListener, IBaseView{
    private TitleBar titleBar;
    private TextView tv_login_out;
    private RelativeLayout rl_clear_cache;
    private TextView tv_cache_size;
    private RelativeLayout rl_version_update;
    private TextView tv_version_code;
    private UserPresenter mPresenter;
    private RelativeLayout rl_header_bar;
    public static final String URL = "url";
    public static final String VERSION = "version";
    public static final String MESSAGE = "message";
    private String url;
    private String version;
    private String message;
    private TextView tv_version_update;
    private TextView tv_tip_new;
    private Handler mhandle = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_and_help);

        titleBar = findViewById(R.id.title_bar);
        tv_login_out = findViewById(R.id.tv_login_out);
        rl_clear_cache = findViewById(R.id.rl_clear_cache);
        tv_cache_size = findViewById(R.id.tv_cache_size);

        rl_clear_cache.setOnClickListener(this);
        tv_login_out.setOnClickListener(this);

        rl_version_update = findViewById(R.id.rl_version_update);
        tv_version_code = findViewById(R.id.tv_version_code);
        tv_version_update = findViewById(R.id.tv_version_update);
        tv_tip_new = findViewById(R.id.tv_tip_new);
        rl_version_update.setOnClickListener(this);

        titleBar.setTitle(getString(R.string.setting_and_help));
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPresenter = new UserPresenter(this);
        initCacheSize();

        //url = getIntent().getStringExtra(URL);
        url = "http://www.sanguosha.com";
        version = getIntent().getStringExtra(VERSION);
        message = "";

        if(!CommonUtil.isLogin(SettingAndHelpActivity.this)){
            tv_login_out.setVisibility(View.GONE);
            return;
        }


    }

    private void initCacheSize() {
        /*获取universalImageloader缓存*/
        File universalFile =  FileUtils.getDiskCacheDir(this,"circle_bitmap");

        double universalSize = FileSizeUtil.getFileOrFilesSize(universalFile.getAbsolutePath(),FileSizeUtil.SIZETYPE_MB);
        double totalSize = universalSize;
        tv_cache_size.setText(CommonUtil.doubleToString(totalSize)+"MB");
    }

    @Override
    public void onClick(View view) {
        if (!CommonUtil.fastClick()) {
            int id = view.getId();
            if(id == R.id.tv_login_out){
                //TODO 退出登录的时候取消关联设备
                String userId = CommonUtil.getUid(this);
                mPresenter.logout();
            }else if(id == R.id.rl_clear_cache){
                showProgress();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File universalFile =  FileUtils.getDiskCacheDir(SettingAndHelpActivity.this,"circle_bitmap");
                        File glideFile = Glide.getPhotoCacheDir(SettingAndHelpActivity.this);
                        File ykdeviceFile = FileUtils.getDiskCacheDir(SettingAndHelpActivity.this,"ykdevice");

                        FileUtils.deleteDir(universalFile);
                        FileUtils.deleteDir(glideFile);
                        FileUtils.deleteDir(ykdeviceFile);
                        DataCleanManager.cleanInternalCache(SettingAndHelpActivity.this);
                        mhandle.sendEmptyMessage(1);
                    }
                }).start();

            } else if(id == R.id.rl_version_update){

                if(!TextUtils.isEmpty(version)){
                    final UpdateDialog dialog = new UpdateDialog(SettingAndHelpActivity.this,getString(R.string.have_new_version),"版本号："+version, getString(R.string.dialog_notify_positive), getString(R.string.dialog_notify_negative));
                    dialog.setListener(new UpdateDialog.OnNormalDialogClickListener() {
                        @Override
                        public void onPositive() {
                            CommonUtil.openBrowser(SettingAndHelpActivity.this, url);
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        }
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data != null) {
            CommonUtil.exitClearAccount(SettingAndHelpActivity.this);
            EventBus.getDefault().post(new LoginUserInfoUpdateNotify(LoginUserInfoUpdateNotify.QUITLOGIN));
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandle.removeCallbacksAndMessages(null);
        mhandle = null;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HideProgress();
            tv_cache_size.setText("0MB");
            CustomToast.showToast(SettingAndHelpActivity.this,"缓存清除成功", Toast.LENGTH_SHORT);
        }
    }
}
