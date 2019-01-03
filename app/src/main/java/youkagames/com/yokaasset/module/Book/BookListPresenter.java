package youkagames.com.yokaasset.module.Book;

import android.content.Context;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Book.client.BookApi;
import youkagames.com.yokaasset.module.Book.client.BookClient;
import youkagames.com.yokaasset.module.Book.model.BookStatusModel;
import youkagames.com.yokaasset.module.Book.model.BookTypeModel;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import youkagames.com.yokaasset.module.Book.model.BookInfoModel;
import youkagames.com.yokaasset.module.Book.model.DepartmentListModel;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.IBaseControl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songdehua on 2018/12/3.
 */

public class BookListPresenter {

    private IBaseView iBaseView;
    private BookApi mBookApi;
    private WeakReference<Context> mContext;
    private IBaseControl iBaseControl;

    public BookListPresenter(Context context, IBaseView iBaseView, IBaseControl iBaseControl) {
        this.iBaseControl = iBaseControl;
        this.iBaseView = iBaseView;
        mContext = new WeakReference<>(context);
        mBookApi = BookClient.getInstance(context).getBookApi();
    }

    public BookListPresenter(Context context) {
        this.iBaseControl = (IBaseControl) context;
        iBaseView = (IBaseView) context;
        mContext = new WeakReference<>(context);
        mBookApi = BookClient.getInstance(context).getBookApi();
    }

    /*
    *获取书籍列表
     */
    public void getBookList(int type_id,int department_id, int status_id, int page) {
        HashMap<String, String>map = new HashMap<>();
        map.put("type_id",String.valueOf(type_id));
        map.put("departmentId",String.valueOf(department_id));
        map.put("status_id",String.valueOf(status_id));
        map.put("page",String.valueOf(page));
        mBookApi.getBookList(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookComplexSearchModel>() {
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
                    public void onNext(BookComplexSearchModel bookComplexSearchModel) {
                        iBaseView.RequestSuccess(bookComplexSearchModel);
                    }
                });
    }

    /*
 *获取部门信息
  */
    public void getDepartment() {
        mBookApi.deparment().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DepartmentListModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("Dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                    }

                    @Override
                    public void onNext(DepartmentListModel departmentModel) {
                        iBaseView.RequestSuccess(departmentModel);
                    }
                });
    }

    /*
    *借用书籍
     */
    public void borrow(String book_id, String user,int departmentUserId,int statu) {
        iBaseControl.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("id",book_id);
        map.put("user", user);
        map.put("departmentUserId",String.valueOf(departmentUserId));
        map.put("statu",String.valueOf(statu));
        //map.put("password", Md5Utils.encrypt(password));
        mBookApi.borrow(map)
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
    *获取书籍信息
     */
    public void getDeviceInfo(String book_id) {
        HashMap<String, String>map = new HashMap<>();
        map.put("id",String.valueOf(book_id));
        mBookApi.qrcode(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookInfoModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("Dehua","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                    }

                    @Override
                    public void onNext(BookInfoModel bookInfoModel) {
                        iBaseView.RequestSuccess(bookInfoModel);
                    }
                });
    }
}
