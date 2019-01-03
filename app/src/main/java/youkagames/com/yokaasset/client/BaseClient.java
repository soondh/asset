package youkagames.com.yokaasset.client;


import youkagames.com.yokaasset.utils.PreferenceUtils;
import youkagames.com.yokaasset.utils.SystemConfig;
import youkagames.com.yokaasset.utils.SystemUtils;
import android.content.Context;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by songdehua on 2018/11/29.
 */

public class BaseClient {

    private static final int DEFAULT_TIMEOUT = 10;

    public OkHttpClient.Builder getHttpClientBuilder(final Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                .followSslRedirects(false)//https的重定向也自己处理
                .addInterceptor(getInterceptorLog())
                .sslSocketFactory(getSSLFactory());
        /**
         * 在实现的 HostnameVerifier 子类中，需要使用 verify 函数效验服务器主机
         名的合法性，否则会导致恶意程序利用中间人攻击绕过主机名效验。
         说明：
         在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，则验证机制可以
         回调此接口的实现程序来确定是否应该允许此连接。如果回调内实现不恰当，默认
         接受所有域名，则有安全风险。
         */
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if(Contants.Host.equals(hostname)){
                    return true;
                } else {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                }
            }
        });
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("DEVICE-ID", SystemUtils.getUUID(context))
                        .addHeader("APP-VERSION",SystemUtils.getVersionName(context))
                        .addHeader("APP-SYSTEM","Android")
                        .addHeader("token", PreferenceUtils.getString(context,PreferenceUtils.TOKEN,""))
                        .build();
                return chain.proceed(newRequest);
            }
        });
        return builder;
    }
    public Retrofit getOkhttpRetrofit(OkHttpClient httpClient){
        Retrofit retrofit_user = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Contants.Host)
                .client(httpClient)
                .build();
        return retrofit_user;
    }

    public HttpLoggingInterceptor getInterceptorLog() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                LogUtil.d("ttt", "OkHttp====Message:" + message);

            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    public SSLSocketFactory getSSLFactory() {
        SSLContext sslContext;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

//            sslContext = SSLContext.getInstance("SSL");
            sslContext = SSLContext.getInstance("TLSv1.2");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslContext.init(null, null, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

}
