package youkagames.com.yokaasset.module.Device;

import android.content.Context;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Device.client.DeviceApi;
import youkagames.com.yokaasset.module.Device.client.DeviceClient;
import youkagames.com.yokaasset.module.Device.model.DepartmentModel;
import youkagames.com.yokaasset.module.Device.model.DeviceComplexSearchModel;
import youkagames.com.yokaasset.module.Device.model.DeviceInfoModel;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.IBaseControl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songdehua on 2018/12/20.
 */

public class DevicePresenter {
    private IBaseView iBaseView;
    private DeviceApi mDeviceApi;
    private WeakReference<Context> mContext;
    private IBaseControl iBaseControl;

    public DevicePresenter(Context context, IBaseView iBaseView, IBaseControl iBaseControl) {
        this.iBaseControl = iBaseControl;
        this.iBaseView = iBaseView;
        mContext = new WeakReference<>(context);
        mDeviceApi = DeviceClient.getInstance(context).getDeviceApi();
    }

    public DevicePresenter(Context context) {
        this.iBaseControl = (IBaseControl) context;
        iBaseView = (IBaseView) context;
        mContext = new WeakReference<>(context);
        mDeviceApi = DeviceClient.getInstance(context).getDeviceApi();
    }

    /*
    *获取设备列表
     */
    //mDeviceApi.getDeviceList(map).subscribeOn(Schedulers.io())
    public void getDeviceList(int os_id, int type_id, int status_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("system_id",String.valueOf(os_id));
        map.put("device_class_id",String.valueOf(type_id));
        map.put("status",String.valueOf(status_id));
        mDeviceApi.getDeviceList(map).subscribeOn(Schedulers.io())
                //mDeviceApi.getDeviceListmock(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceComplexSearchModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                        if (iBaseControl != null){
                            iBaseControl.RequestError(e);
                        }
                    }

                    @Override
                    public void onNext(DeviceComplexSearchModel deviceComplexSearchModel) {
                        iBaseView.RequestSuccess(deviceComplexSearchModel);
                    }
                });
    }


    /**
     * 借用/归还设备
     * @param device_id
     * @param username
     */
    public void borrow(String device_id, String username,int departmentId) {
        iBaseControl.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("device_id",device_id);
        map.put("username", username);
        map.put("department_id",String.valueOf(departmentId));
        //map.put("password", Md5Utils.encrypt(password));
        mDeviceApi.borrow(map)
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


    /*
     *获取设备信息
      */
    //mDeviceApi.getDeviceList(map).subscribeOn(Schedulers.io())
    public void getDeviceInfo(String device_id) {
        mDeviceApi.qrcode(device_id).subscribeOn(Schedulers.io())
                //mDeviceApi.getDeviceListmock(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceInfoModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                        if (iBaseControl != null){
                            iBaseControl.RequestError(e);
                        }
                    }

                    @Override
                    public void onNext(DeviceInfoModel deviceComplexSearchModel) {
                        iBaseView.RequestSuccess(deviceComplexSearchModel);
                    }
                });
    }

    /*
     *获取部门信息
      */
    public void getDepartment() {
        mDeviceApi.deparment().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DepartmentModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("Dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                    }

                    @Override
                    public void onNext(DepartmentModel departmentModel) {
                        iBaseView.RequestSuccess(departmentModel);
                    }
                });
    }
}
