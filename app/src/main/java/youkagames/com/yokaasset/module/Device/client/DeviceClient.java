package youkagames.com.yokaasset.module.Device.client;


import android.content.Context;

import youkagames.com.yokaasset.client.BaseClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by songdehua on 2018/12/20.
 */

public class DeviceClient extends BaseClient{
    private static DeviceClient deviceClient;
    private DeviceApi mDeviceApiInterface;
    private Retrofit mDeviceRetrofit;
    public static DeviceClient getInstance(Context context) {
        if (deviceClient == null ){
            synchronized (DeviceClient.class) {
                if (deviceClient == null){
                    deviceClient = new DeviceClient(context);
                }
            }
        }
        return deviceClient;
    }

    private DeviceClient(Context context) {
        OkHttpClient httpClient = getHttpClientBuilder(context).build();
        mDeviceRetrofit = getOkhttpRetrofit(httpClient);
    }

    public DeviceApi getDeviceApi() {
        if (mDeviceApiInterface == null) {
            mDeviceApiInterface = mDeviceRetrofit.create(DeviceApi.class);
        }
        return mDeviceApiInterface;
    }
}
