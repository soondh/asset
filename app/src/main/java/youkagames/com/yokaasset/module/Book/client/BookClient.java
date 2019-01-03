package youkagames.com.yokaasset.module.Book.client;

import android.content.Context;

import youkagames.com.yokaasset.client.BaseClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by songdehua on 2018/12/3.
 */

public class BookClient extends BaseClient{
    private static BookClient bookClient;
    private BookApi mBookApiInterface;
    private Retrofit mBookRetrofit;
    public static BookClient getInstance(Context context) {
        if (bookClient == null ){
            synchronized (BookClient.class) {
                if (bookClient == null){
                    bookClient = new BookClient(context);
                }
            }
        }
        return bookClient;
    }

    private BookClient(Context context) {
        OkHttpClient httpClient = getHttpClientBuilder(context).build();
        mBookRetrofit = getOkhttpRetrofit(httpClient);
    }

    public BookApi getBookApi() {
        if (mBookApiInterface == null) {
            mBookApiInterface = mBookRetrofit.create(BookApi.class);
        }
        return mBookApiInterface;
    }
}
