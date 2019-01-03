package youkagames.com.yokaasset.module.Device.client;

import retrofit2.http.PUT;
import retrofit2.http.Path;
import youkagames.com.yokaasset.module.Device.model.DepartmentModel;
import youkagames.com.yokaasset.module.Device.model.DeviceInfoModel;
import youkagames.com.yokaasset.module.Device.model.DeviceTypeModel;
import youkagames.com.yokaasset.module.Device.model.DeviceComplexSearchModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by songdehua on 2018/12/20.
 */

public interface DeviceApi {
    /*
    *获取设备列表
     */
    @GET("category/")
    Observable<DeviceComplexSearchModel> getDeviceList(@QueryMap HashMap<String, String> map);

    /*
    *获取设备列表mock
     */
    @GET("/v2/5baaf0143000005900a68548")
    Observable<DeviceComplexSearchModel> getDeviceListmock(@QueryMap HashMap<String, String> map);

    /*
    * 借用/归还 设备
    * @param map
     * @return
     */
    @FormUrlEncoded
    @PUT("borrow/")
    Observable<DeviceComplexSearchModel> borrow(@FieldMap Map<String, String> map);

    /*
    *获取设备信息
    * @param device_id
     */
    @GET("qrcode/{device_id}/")
    Observable<DeviceInfoModel> qrcode(@Path("device_id") String device_id);

    /*
    *获取部门信息
    * @param device_id
     */
    @GET("deparment/")
    Observable<DepartmentModel> deparment();
}
