package youkagames.com.yokaasset.module.Game;

import android.content.Context;
import android.util.Log;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import youkagames.com.yokaasset.module.Game.client.GameApi;
import youkagames.com.yokaasset.module.Game.client.GameClient;
import youkagames.com.yokaasset.module.Game.model.GameLanguageModel;
import youkagames.com.yokaasset.module.Game.model.GameTypeModel;
import youkagames.com.yokaasset.module.Game.model.GameInfoModel;
import youkagames.com.yokaasset.module.Game.model.GameComplexSearchModel;
import youkagames.com.yokaasset.module.Game.model.GameStatusModel;
import youkagames.com.yokaasset.module.Game.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Game.model.GameLocationModel;
import youkagames.com.yokaasset.module.Game.model.GameLanguageModel;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.IBaseControl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by songdehua on 2018/12/6.
 */

public class GameListPresenter {

    private IBaseView iBaseView;
    private GameApi mGameApi;
    private WeakReference<Context> mContext;
    private IBaseControl iBaseControl;

    public GameListPresenter(Context context, IBaseView iBaseView, IBaseControl iBaseControl) {
        this.iBaseControl = iBaseControl;
        this.iBaseView = iBaseView;
        mContext = new WeakReference<Context>(context);
        mGameApi = GameClient.getInstance(context).getGameApi();

    }

    public GameListPresenter(Context context){
        this.iBaseControl = (IBaseControl) context;
        iBaseView = (IBaseView) context;
        mContext = new WeakReference<Context>(context);
        mGameApi = GameClient.getInstance(context).getGameApi();

    }

    /*
    *获取桌游列表
     */
    public void getGameList(int language_id, int location_id, int status_id, int page){
        HashMap<String, String>map = new HashMap<>();
        map.put("languageId",String.valueOf(language_id));
        map.put("address",String.valueOf(location_id));
        map.put("status_id",String.valueOf(status_id));
        map.put("page",String.valueOf(page));
        mGameApi.getGameList(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameComplexSearchModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("dehua","onError");
                        LogUtil.i("error",e.toString());
                        if (iBaseControl != null){
                            iBaseControl.RequestError(e);
                        }
                    }

                    @Override
                    public void onNext(GameComplexSearchModel gameComplexSearchModel) {
                        iBaseView.RequestSuccess(gameComplexSearchModel);
                    }
                });
    }

    /*
    *获取桌游归属地数据
     */
    public void getLocation() {
        mGameApi.location().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameLocationModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("dehua","onError");
                    }

                    @Override
                    public void onNext(GameLocationModel gameLocationModel) {
                        iBaseView.RequestSuccess(gameLocationModel);
                    }
                });
    }

    /*
    * 获取桌游语言数据
    */
    public void getLanguage() {
        mGameApi.language().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameLanguageModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("dehua","onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("dehua","onError");
                    }

                    @Override
                    public void onNext(GameLanguageModel gameLanguageModel) {
                        iBaseView.RequestSuccess(gameLanguageModel);
                    }
                });
    }


    /*
 *获取部门信息
  */
    public void getDepartment() {
        mGameApi.deparment().subscribeOn(Schedulers.io())
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
    *获取桌游信息
     */
    public void getGameInfo(String book_id) {
        HashMap<String, String>map = new HashMap<>();
        map.put("id",String.valueOf(book_id));
        mGameApi.qrcode(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameInfoModel>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("Dehua","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("Dehua","onError");
                    }

                    @Override
                    public void onNext(GameInfoModel gameInfoModel) {
                        iBaseView.RequestSuccess(gameInfoModel);
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
        mGameApi.borrow(map)
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
}