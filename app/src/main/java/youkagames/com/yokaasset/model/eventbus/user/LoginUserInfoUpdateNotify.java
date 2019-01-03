package youkagames.com.yokaasset.model.eventbus.user;

/**
 * Created by songdehua on 2018/11/28.
 */

public class LoginUserInfoUpdateNotify {
    public static final int LOGINSUCCESS = 0;
    public static final int QUITLOGIN = 1;
    private int loginStatus;
    public LoginUserInfoUpdateNotify(int status){
        loginStatus = status;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
