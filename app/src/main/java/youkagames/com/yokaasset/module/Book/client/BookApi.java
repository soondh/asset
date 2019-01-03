package youkagames.com.yokaasset.module.Book.client;

import retrofit2.http.PUT;
import retrofit2.http.Path;
import youkagames.com.yokaasset.module.Book.model.BookStatusModel;
import youkagames.com.yokaasset.module.Book.model.BookTypeModel;
import youkagames.com.yokaasset.module.Book.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import youkagames.com.yokaasset.module.Book.model.BookInfoModel;

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
 * Created by songdehua on 2018/12/3.
 */

public interface BookApi {
    /*
    * 获取书籍列表
     */
    @GET("booklist")
    Observable<BookComplexSearchModel> getBookList(@QueryMap HashMap<String, String>map);

    /*
    * 借用书籍
     */
    @FormUrlEncoded
    @PUT("book/statu")
    Observable<BookComplexSearchModel> borrow(@FieldMap Map<String, String> map);

    /*
    *获取书籍信息
    * @param device_id
     */
    @GET("book/code/info")
    Observable<BookInfoModel> qrcode(@QueryMap HashMap<String, String>map);

    /*
    *获取部门信息
     */
    @GET("deparment/")
    Observable<DepartmentListModel> deparment();

    /*
    *获取状态信息
     */
    @GET("deparment/")
    Observable<BookStatusModel> status();
}
