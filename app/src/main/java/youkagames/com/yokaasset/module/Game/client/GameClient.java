package youkagames.com.yokaasset.module.Game.client;

import android.content.Context;

import youkagames.com.yokaasset.client.BaseClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
/**
 * Created by songdehua on 2018/12/6.
 */

public class GameClient extends BaseClient {

    private static GameClient gameClient;
    private GameApi mGameApiInterface;
    private Retrofit mGameRetrofit;
    public static GameClient getInstance(Context context) {
        if (gameClient == null ){
            synchronized (GameClient.class) {
                if (gameClient == null){
                    gameClient = new GameClient(context);
                }
            }
        }
        return gameClient;
    }

    private GameClient(Context context) {
        OkHttpClient httpClient = getHttpClientBuilder(context).build();
        mGameRetrofit = getOkhttpRetrofit(httpClient);
    }

    public GameApi getGameApi() {
        if (mGameApiInterface == null) {
            mGameApiInterface = mGameRetrofit.create(GameApi.class);
        }
        return mGameApiInterface;
    }
}
