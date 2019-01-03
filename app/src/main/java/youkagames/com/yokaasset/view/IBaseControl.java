package youkagames.com.yokaasset.view;

/**
 * Created by songdehua on 2018/11/23.
 */

public interface IBaseControl {
    void showProgress();
    void HideProgress();
    void NetWorkError();
    void RequestError(Throwable e);
}
