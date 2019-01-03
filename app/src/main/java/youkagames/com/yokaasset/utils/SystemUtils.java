package youkagames.com.yokaasset.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.UUID;

/**
 * Created by songdehua on 2018/5/24.
 */

public class SystemUtils {
    /**
     * 返回设备的唯一标识。
     */
    public static String getUUID(Context context) {
        String uuid = null;
        // 设备ID，一般为IMEI
        if (isPermissionGranted(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return uuid;
            }
            final String imei = phoneManager.getDeviceId();
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (imei != null && androidId != null) {
                uuid = new UUID(androidId.hashCode(), ((long) imei.hashCode() << 32)).toString();
            } else if (imei == null) {
                uuid = imei;
            } else if (androidId != null) {
                uuid = androidId;
            }
        }
        if (uuid == null || uuid.isEmpty()) {
            if (isPermissionGranted(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                if (info != null) {
                    uuid = info.getMacAddress();
                }
            }
        }
        return uuid;
    }
    public static String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    public static boolean isPermissionGranted(Context context, String permission) {
        return context != null && !TextUtils.isEmpty(permission)
                && (context.getPackageManager().checkPermission(permission,
                context.getPackageName()) == PackageManager.PERMISSION_GRANTED);
    }

    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

