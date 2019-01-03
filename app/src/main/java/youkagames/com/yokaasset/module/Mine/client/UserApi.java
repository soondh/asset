package youkagames.com.yokaasset.module.Mine.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Completable;
import rx.Observable;

import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Mine.model.UserModel;
import youkagames.com.yokaasset.module.Mine.model.VersionModel;

/**
 * Created by songdehua on 2018/12/11.
 */

public interface UserApi {

    /**
     * 登录
     * @param names
     * @return
     */
    @FormUrlEncoded
    @POST("authorizations/")
    Observable<UserModel> login(@FieldMap Map<String, String> names);

    /**
     * 登录
     * @param names
     * @return
     */
    @FormUrlEncoded
    @POST("/v2/5bac75723100007100654707")
    Observable<UserModel> loginmock(@FieldMap Map<String, String> names);

    /**
     * 获取最新版本
     */
    @GET("version")
    Observable<VersionModel> getVersion(@QueryMap HashMap<String,String> map);

    /**
     * 用户注销
     * @return
     */
    @GET("logout/")
    Observable<BaseModel> logout();

    @GET("userinfo/")
    Observable<UserModel> getUserInfo();
}
