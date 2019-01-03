package youkagames.com.yokaasset;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;


//import com.bianfeng.base.BaseSdk;
//import com.devbrackets.android.exomedia.ExoMedia;
//import com.google.android.exomedia.ExoMedia;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import java.util.Locale;

import okhttp3.OkHttpClient;
import yokastore.youkagames.com.support.YokaSupport;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.CrashExceptionHandler;
import youkagames.com.yokaasset.utils.GlideImageLoader;
import youkagames.com.yokaasset.utils.SystemUtils;
import youkagames.com.yokaasset.utils.DisplayUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import java.util.Locale;

import okhttp3.OkHttpClient;
import yokastore.youkagames.com.support.YokaSupport;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.CrashExceptionHandler;
import youkagames.com.yokaasset.utils.GlideImageLoader;
import youkagames.com.yokaasset.utils.SystemUtils;
import youkagames.com.yokaasset.utils.DisplayUtil;

/**
 * Created by songdehua on 2018/11/23.
 */


public class YokaApplication extends MultiDexApplication {

    private static YokaApplication mYokaApplication;

    /*
    *获得application实例
    */
    public static YokaApplication getInstance() { return mYokaApplication;}
    private ImagePicker imagePicker;
    private Handler handler = new Handler();
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private RefWatcher refWatcher;
    public static boolean isUseIm = true;
    public static RefWatcher getRefWatcher(Context context) {
        YokaApplication application = (YokaApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        LogUtil.i("YokaApplication.onCreate start");

        super.onCreate();
        mYokaApplication = this;
        SCREEN_WIDTH = SystemUtils.getScreenWidth(this);
        SCREEN_HEIGHT = SystemUtils.getScreenHeight(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        YokaSupport.init(getApplicationContext());

                        if (BuildConfig.DEBUG) {
                            CrashExceptionHandler.getInstance().init(getApplicationContext());
                        }
                    }
                });
            }
        }).start();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher  = LeakCanary.install(this);

    }
    public void initImagePickerOption(){
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
    private void setImagePickerSelectLimit(){ imagePicker.setSelectLimit(9);}

    private void initDisplayOption() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    //低内存的时候执行
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    //程序终止的时候执行(停靠后台也会执行)
    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
    }
}
