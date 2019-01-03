package youkagames.com.yokaasset.module.AssetList;

import android.content.Context;
import android.util.Log;


import rx.Scheduler;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.model.DataBooleanModel;
import youkagames.com.yokaasset.model.DataIntModel;
import youkagames.com.yokaasset.module.AssetList.client.AssetListApi;
import youkagames.com.yokaasset.module.AssetList.client.AssetListClient;
import youkagames.com.yokaasset.module.AssetList.model.AssetListModel;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.NetWorkUtils;
import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.IBaseControl;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by songdehua on 2018/11/28.
 */

public class AssetListPresenter {
    private IBaseView iBaseView;
    private IBaseControl iBaseControl;

    private AssetListApi mAssetListApi;
    private WeakReference<Context> mContext;

    public AssetListPresenter(Context context, IBaseView iBaseView, IBaseControl iBaseControl) {
        this.iBaseControl = iBaseControl;
        this.iBaseView = iBaseView;
        mContext = new WeakReference<Context>(context);
        mAssetListApi = AssetListClient.getInstance(context).getmAssetListApi();
    }

    /*
    * 获取资产列表
     */
    public void getAssetListData(){
        if (!NetWorkUtils.isNetworkConnected(mContext.get())){
            iBaseControl.NetWorkError();
            return;
        }

        mAssetListApi.getAssetListData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AssetListModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseControl.NetWorkError();
                    }

                    @Override
                    public void onNext(AssetListModel assetListModel) {
                        iBaseView.RequestSuccess(assetListModel);
                    }
                });
    }

    /*
* 获取资产列表
 */
    public void getAssetListDataMock(){
        if (!NetWorkUtils.isNetworkConnected(mContext.get())){
            iBaseControl.NetWorkError();
            return;
        }

        mAssetListApi.getAssetListDataMock()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AssetListModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseControl.NetWorkError();
                    }

                    @Override
                    public void onNext(AssetListModel assetListModel) {
                        iBaseView.RequestSuccess(assetListModel);
                    }
                });
    }

}
