package youkagames.com.yokaasset.module.Mine.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.MainActivity;
import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.db.MyDatabase;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.model.eventbus.user.LoginUserInfoUpdateNotify;
import youkagames.com.yokaasset.module.Mine.model.UserModel;
import youkagames.com.yokaasset.module.Mine.UserPresenter;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.view.CustomProgressDialog;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.TitleBar;

/**
 * Created by songdehua on 2018/12/11.
 */

public class LoginActivity extends BaseActivity implements IBaseView{
    public static final String IS_SET_PWD = "is_set_pwd";
    private EditText username;
    private EditText password;
    private UserPresenter mPresenter;
    private LinearLayout ll_layout;
    private RelativeLayout rl_header_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (CommonUtil.isLogin(this)) {
            CommonUtil.exitClearAccount(this);
            EventBus.getDefault().post(new LoginUserInfoUpdateNotify(LoginUserInfoUpdateNotify.QUITLOGIN));
        }
        Button login = findViewById(R.id.login);
        ll_layout = findViewById(R.id.ll_layout);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        mPresenter = new UserPresenter(this);

        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.login);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.fastClick()) {
                    String number = username.getText().toString().trim();
                    if (TextUtils.isEmpty(number)) {
                        CustomToast.showToast(LoginActivity.this, R.string.please_input_username, Toast.LENGTH_SHORT);
                        return;
                    }
                    String pass = password.getText().toString().trim();
                    if (TextUtils.isEmpty(pass)) {
                        CustomToast.showToast(LoginActivity.this, R.string.please_input_password, Toast.LENGTH_SHORT);
                        return;
                    }
                    mPresenter.login(number, pass);
                }
            }
        });

        ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.hideSoftKeyboard(LoginActivity.this, ll_layout);
            }
        });
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomProgressDialog.getInstance().disMissDialog();
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data.cd == 0) {
            if (data != null && data instanceof UserModel) {
                UserModel model = (UserModel) data;
                if (!TextUtils.isEmpty(model.data.token)) {
                    PreferenceUtils.put(this, PreferenceUtils.TOKEN, model.data.token);
                    PreferenceUtils.put(this, PreferenceUtils.USER_ID, model.data.user_id);
                    PreferenceUtils.put(this, PreferenceUtils.IMG_URL, model.data.img_url);
                    PreferenceUtils.put(this, PreferenceUtils.NICKNAME, model.data.nickname);
                    //TODO 当用户使用自有账号登录时，可以这样统计：
                    MyDatabase.createInstance(this);
                    EventBus.getDefault().post(new LoginUserInfoUpdateNotify(LoginUserInfoUpdateNotify.LOGINSUCCESS));
                    finish();
                }
            } else {
                CustomToast.showToast(this, data.msg, Toast.LENGTH_SHORT);
            }
        }
        else if (data.cd == 1){
            CustomToast.showToast(this, data.msg, Toast.LENGTH_SHORT);
        }
    }
}
