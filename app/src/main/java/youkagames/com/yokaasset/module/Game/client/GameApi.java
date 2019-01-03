package youkagames.com.yokaasset.module.Game.client;

import retrofit2.http.PUT;
import retrofit2.http.Path;
import youkagames.com.yokaasset.module.Game.model.GameLanguageModel;
import youkagames.com.yokaasset.module.Game.model.GameLocationModel;
import youkagames.com.yokaasset.module.Game.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Game.model.GameStatusModel;
import youkagames.com.yokaasset.module.Game.model.GameComplexSearchModel;
import youkagames.com.yokaasset.module.Game.model.GameInfoModel;
import youkagames.com.yokaasset.module.Game.model.GameTypeModel;

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
 * Created by songdehua on 2018/12/6.
 */

public interface GameApi {
    /*
    * 获取桌游列表
     */
    @GET("gamelist")
    Observable<GameComplexSearchModel> getGameList(@QueryMap HashMap<String, String>map);

    /*
    *借用桌游
     */
    @FormUrlEncoded
    @PUT("game/statu")
    Observable<GameComplexSearchModel> borrow(@FieldMap Map<String, String> map);

    /*
    *获取语言信息
     */
    @GET("game/language")
    Observable<GameLanguageModel> language();

    /*
    *获取归属地信息
     */
    @GET("addresslist")
    Observable<GameLocationModel> location();

    /*
    *获取部门信息
     */
    @GET("deparment/")
    Observable<DepartmentListModel> deparment();

    /*
    *获取状态信息
     */
    @GET("gaem/gstatus/")
    Observable<GameStatusModel> status();

    /*
    *获取书籍信息
    * @param game_id
     */
    @GET("game/code/info")
    Observable<GameInfoModel> qrcode(@QueryMap HashMap<String, String>map);
}
