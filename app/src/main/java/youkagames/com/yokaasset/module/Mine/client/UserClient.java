package youkagames.com.yokaasset.module.Mine.client;

import android.content.Context;

import youkagames.com.yokaasset.client.BaseClient;
import youkagames.com.yokaasset.module.Mine.client.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
/**
 * Created by songdehua on 2018/12/11.
 */

public class UserClient extends BaseClient{

    private static UserClient userClient;
    private UserApi mUserApiInterface;
    private Retrofit mUserRetrofit;
    public static UserClient getInstance(Context context) {
        if (userClient == null) {
            synchronized (UserClient.class) {
                if (userClient == null)
                    userClient = new UserClient(context);
            }
        }
        return userClient;
    }
    private UserClient(Context context) {
        OkHttpClient httpClient = getHttpClientBuilder(context).build();
        mUserRetrofit = getOkhttpRetrofit(httpClient);
    }
    public UserApi getUserApi() {
        if (mUserApiInterface == null) {
            mUserApiInterface = mUserRetrofit.create(UserApi.class);
        }
        return mUserApiInterface;
    }

}
