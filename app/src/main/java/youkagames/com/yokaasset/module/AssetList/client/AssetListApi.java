package youkagames.com.yokaasset.module.AssetList.client;


import youkagames.com.yokaasset.module.AssetList.model.AssetListModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Path;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by songdehua on 2018/11/29.
 */

public interface AssetListApi {
    /*
    * 获取资产类别列表
     */
    @GET("plat")
    Observable<AssetListModel> getAssetListData();

    /*
    * 获取资产类别列表
     */
    @GET("v2/5c00a87e3200005100b28819")
    Observable<AssetListModel> getAssetListDataMock();
}
