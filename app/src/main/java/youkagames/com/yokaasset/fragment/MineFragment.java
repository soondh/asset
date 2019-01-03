package youkagames.com.yokaasset.fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;

import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.BuildConfig;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseFragment;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.module.Mine.UserPresenter;
import youkagames.com.yokaasset.utils.CommonUtil;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Mine.client.UserApi;
import youkagames.com.yokaasset.module.Mine.client.UserClient;
import youkagames.com.yokaasset.module.Mine.model.UserModel;
import youkagames.com.yokaasset.module.Mine.model.VersionModel;
import youkagames.com.yokaasset.module.Mine.model.LoginToastModel;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.model.eventbus.user.LoginUserInfoUpdateNotify;
import youkagames.com.yokaasset.module.Mine.activity.LoginActivity;
import youkagames.com.yokaasset.module.Mine.activity.SettingAndHelpActivity;


/**
 * Created by songdehua on 2018/11/28.
 */

public class MineFragment extends BaseFragment implements IBaseView,View.OnClickListener {
    private RelativeLayout rl_my_layout_me;
    private RelativeLayout rl_setting_help;
    private ImageView iv_header;
    private TextView tv_nickname;
    private UserPresenter mPresenter;
    private TextView tv_new_tip;
    private String url;
    private String version;
    private String message;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mine,null);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        rl_my_layout_me = view.findViewById(R.id.rl_my_layout_me);
        rl_setting_help = view.findViewById(R.id.rl_setting_help);
        iv_header = view.findViewById(R.id.iv_header);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_new_tip = view.findViewById(R.id.tv_new_tip);

        mPresenter = new UserPresenter(getActivity(),this,this);
        rl_my_layout_me.setOnClickListener(this);
        rl_setting_help.setOnClickListener(this);
    }

    @Override
    public void initDataFragment() {
        if(!CommonUtil.isLogin(getActivity())){
            tv_nickname.setText(R.string.not_login);
            String imgUrl = PreferenceUtils.getString(getActivity(), PreferenceUtils.IMG_URL, "");
            ImageLoaderUtils.loadRoundImg(getActivity(), imgUrl,iv_header);
        }else {
            String nickName = PreferenceUtils.getString(getActivity(), PreferenceUtils.NICKNAME, "");
            tv_nickname.setText(nickName);
            String imgUrl = PreferenceUtils.getString(getActivity(), PreferenceUtils.IMG_URL, "");
            ImageLoaderUtils.loadRoundImg(getActivity(), imgUrl,iv_header);
            mPresenter.getUserInfo();
        }
        mPresenter.getVersion(BuildConfig.VERSION_NAME);
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data.cd == 0) {
            if (data instanceof UserModel) {
                UserModel model = (UserModel) data;

                tv_nickname.setText(model.data.nickname);
                ImageLoaderUtils.loadRoundImg(getActivity(), model.data.img_url, iv_header);
            } else if (data instanceof VersionModel) {
                VersionModel model = (VersionModel) data;
                url = model.data.url;
                version = model.data.name;
                message = model.data.message;
                tv_new_tip.setVisibility(View.VISIBLE);
            } else if (data instanceof LoginToastModel) {
                LoginToastModel model = (LoginToastModel) data;
                if (model.data.type == 1) {
                    CustomToast.showToast(getActivity(), "登录成功", Toast.LENGTH_SHORT);
                }
            }
        } else {
            if (data instanceof VersionModel) {
                VersionModel model = (VersionModel) data;
                if (model.cd == 2) {
                    LogUtil.i("dehua","已经是最新版本");
                    tv_new_tip.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (!CommonUtil.fastClick()) {
            switch (id){
                case R.id.rl_my_layout_me:
                    if (!CommonUtil.isLogin(getActivity())){
                        startLoginActivity();
                    }
                    break;
                case R.id.rl_setting_help:
                    startSettingAndHelpActivity();
                    break;
            }
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity() , LoginActivity.class);
        startActivityAnim(intent);
    }

    /**
     * 跳转到设置与帮助页面
     */
    public void startSettingAndHelpActivity(){
        Intent intent = new Intent(getActivity() , SettingAndHelpActivity.class);
        intent.putExtra(SettingAndHelpActivity.URL, url);
        intent.putExtra(SettingAndHelpActivity.VERSION, version);
        startActivityAnim(intent);
    }

    /**
     * 登录成功，通知需要刷新的页面刷新
     * @param loginUserInfoUpdateNotify
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginUserInfoUpdateNotify loginUserInfoUpdateNotify) {
        if(loginUserInfoUpdateNotify.getLoginStatus() == LoginUserInfoUpdateNotify.LOGINSUCCESS) {
            mPresenter.getUserInfo();
            //mPresenter.getLoginToast();
        }else if(loginUserInfoUpdateNotify.getLoginStatus() == LoginUserInfoUpdateNotify.QUITLOGIN){
            initDataFragment();
        }
    }
}
