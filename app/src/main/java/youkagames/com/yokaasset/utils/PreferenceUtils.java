package youkagames.com.yokaasset.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by songdehua on 2018/11/23.
 */

public class PreferenceUtils {
    public static final String TOKEN = "token";
    public static final String USER_ID = "user_id";
    public static final String NICKNAME = "nickname";
    public static final String IMG_URL = "img_url";
    public static final String BIG_IMG_URL = "big_img_url";
    public static final String CONTENT = "content";
    public static final String SEX = "sex";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String FANS_NUM = "fans_num";
    public static final String ATTENTION_NUM = "attention_num";
    //    public static final String NEED_UPDATE_CITY = "need_update_city";
    public static final String IM_USER_ID = "im_user_id";
    public static final String IM_PASSWD = "im_passwd";
    public static final String GUIDE="guide";//引导页
    public static final String THIRD_BIND_PHONE="third_bind_phone";
    public static final String THIRD_UUID = "third_uuid";
    public static final String LOGINPLATFORM = "login_plat_form";
    public static final String IS_FIRST_REGIST = "is_first_regist";
    /** 清空数据 */
    public static void reset(final Context ctx) {
//        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
//        edit.clear();
//        edit.commit();
        remove(ctx,TOKEN,USER_ID,NICKNAME,IMG_URL,BIG_IMG_URL,CONTENT,SEX,PROVINCE,CITY,DISTRICT,FANS_NUM,ATTENTION_NUM,IM_USER_ID,IM_PASSWD,THIRD_BIND_PHONE,THIRD_UUID,LOGINPLATFORM,IS_FIRST_REGIST);
    }

    public static String getString(Context ctx, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, defValue);
    }

    public static long getLong(Context ctx, String key, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, defValue);
    }

    public static float getFloat(Context ctx, String key, float defValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(key, defValue);
    }

    public static void put(Context ctx, String key, String value) {
        putString(ctx, key, value);
    }

    public static void put(Context ctx, String key, int value) {
        putInt(ctx, key, value);
    }

    public static void put(Context ctx, String key, float value) {
        putFloat(ctx, key, value);
    }

    public static void put(Context ctx, String key, boolean value) {
        putBoolean(ctx, key, value);
    }

    public static void putFloat(Context ctx, String key, float value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static int getInt(Context ctx, String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, defValue);
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, defValue);
    }

    public static boolean hasString(Context ctx, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.contains(key);
    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void remove(Context ctx, String... keys) {
        if (keys != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.commit();
        }
    }
}

