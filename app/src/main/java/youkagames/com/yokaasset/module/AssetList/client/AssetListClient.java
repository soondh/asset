package youkagames.com.yokaasset.module.AssetList.client;

import android.content.Context;

import youkagames.com.yokaasset.client.BaseClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by songdehua on 2018/11/29.
 */

public class AssetListClient extends BaseClient{
    private static AssetListClient assetListClient;
    private AssetListApi mAssetListApiInterface;
    private Retrofit mAssetListRetrofit;

    public static AssetListClient getInstance(Context context){
        if (assetListClient == null){
            synchronized (AssetListClient.class) {
                if (assetListClient == null)
                    assetListClient = new AssetListClient(context);
            }
        }
        return assetListClient;
    }

    private AssetListClient(Context context){
        OkHttpClient httpClient = getHttpClientBuilder(context).build();
        mAssetListRetrofit = getOkhttpRetrofit(httpClient);
    }
    public AssetListApi getmAssetListApi(){
        if (mAssetListApiInterface == null){
            mAssetListApiInterface = mAssetListRetrofit.create(AssetListApi.class);
        }
        return mAssetListApiInterface;
    }
}
