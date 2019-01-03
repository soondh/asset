package youkagames.com.yokaasset.module.Mine;

import android.content.Context;

import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.utils.Md5Utils;
import youkagames.com.yokaasset.utils.NetWorkUtils;
import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.utils.SystemUtils;
import youkagames.com.yokaasset.view.IBaseControl;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.module.Mine.model.UserModel;
import youkagames.com.yokaasset.module.Mine.model.VersionModel;
import youkagames.com.yokaasset.module.Mine.client.UserApi;
import youkagames.com.yokaasset.module.Mine.client.UserClient;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Mine.model.LoginToastModel;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by songdehua on 2018/12/11.
 */

public class UserPresenter {

    private IBaseView iBaseView;
    private IBaseControl iBaseControl;
    private UserApi mUserApi;
    private WeakReference<Context> mContext;

    public UserPresenter(Context context, IBaseView iBaseView, IBaseControl iBaseControl){
        this.iBaseView = iBaseView;
        this.iBaseControl = iBaseControl;
        mContext = new WeakReference<>(context);
        mUserApi = UserClient.getInstance(mContext.get()).getUserApi();
    }
    public UserPresenter(Context context){
        this.iBaseView = (IBaseView)context;
        this.iBaseControl = (IBaseControl)context;
        mContext = new WeakReference<>(context);
        mUserApi = UserClient.getInstance(mContext.get()).getUserApi();
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        iBaseControl.showProgress();
        HashMap<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password", password);
        //map.put("password", Md5Utils.encrypt(password));
        mUserApi.login(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserModel>() {
                    @Override
                    public void onCompleted() {
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseControl.RequestError(e);
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onNext(UserModel baseModel) {
                        iBaseView.RequestSuccess(baseModel);
                    }
                });
    }

    /**
     * 用户注销
     */
    public void logout() {
        iBaseControl.showProgress();
        mUserApi.logout()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseModel>() {
                    @Override
                    public void onCompleted() {
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseControl.RequestError(e);
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onNext(BaseModel baseModel) {
                        iBaseView.RequestSuccess(baseModel);
                    }
                });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(){
        if(!NetWorkUtils.isNetworkConnected(mContext.get())){
            iBaseControl.NetWorkError();
            return;
        }
        iBaseControl.showProgress();
        mUserApi.getUserInfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserModel>() {
                    @Override
                    public void onCompleted() {
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseControl.RequestError(e);
                        iBaseControl.HideProgress();
                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        iBaseView.RequestSuccess(userModel);
                    }
                });

    }

    /**
     * 获取最新版本
     */
    public void getVersion(String version){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("name", version);
        mUserApi.getVersion(hashMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VersionModel versionModel) {
                        iBaseView.RequestSuccess(versionModel);
                    }
                });
    }


}
