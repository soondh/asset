package youkagames.com.yokaasset.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by songdehua on 2018/11/23.
 */


public class PathUtils {

    private static final String APP_DIRECTORY = "yokastore";

    /**
     * 获得内部的文件目录
     *
     * @return
     */
    public static String getFilesInsidePath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 获得内部的缓存目录
     *
     * @return
     */
    public static String getCacheInsidePath(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获得sd卡中自己的应用的缓存文件目录
     * @return
     */
    public static String getCacheOutsidePath(){
        File file = new File(getBaseSDpath() + "/cache");
        if(!file.exists()){
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获得自己的应用文件夹在内部存储设备的根目录
     *
     * @return
     */
    public static String getBaseSDpath() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + APP_DIRECTORY);
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    /**
     * 获得保存的gif图片路径
     *
     * @return
     */
    public static String getSaveGifPath() {
        File file = new File(getBaseSDpath() + "/gif");
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    /**
     * 获得保存的条漫长图的路径
     *
     * @return
     */
    public static String getSaveLongPicPath() {
        File file = new File(getBaseSDpath() + "/comicLongPic");
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    /**
     * 获得保存的视频路径
     *
     * @return
     */
    public static String getSaveVideoPath() {
        File file = new File(getBaseSDpath() + "/video");
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    /**
     * 根据时间和传入的后缀生成一个文件名
     *
     * @param suffix
     * @return
     */
    public static String getFileNameForTime(String suffix) {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()) + suffix;
        return fileName;
    }

    /**
     * 获得assets目录的路径
     *
     * @return
     */
    public static String getAssetsPath() {
        return "file://android_asset/";
    }

    /***********************   动画生成视频的相关缓存文件目录     *******************************/


    /**
     * 获得动画生成视频的相关缓存文件的目录
     *
     * @return
     */
    public static String getAnimation2VideoCachePath(Context context) {
        String anim2VideoCache = getCacheInsidePath(context) + "/anim2video";
        File cacheFile = new File(anim2VideoCache);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile.getAbsolutePath();
    }

    /**
     * 获得制作动画生成的视频存储的文件夹
     *
     * @return
     */
    public static String getAnimation2VideoFinalPath(Context context) {
        String videoFinalPath = getFilesInsidePath(context) + "/finalVideo";
        File videoFinalFile = new File(videoFinalPath);
        if (!videoFinalFile.exists()) {
            videoFinalFile.mkdirs();
        }
        return videoFinalFile.getAbsolutePath();
    }

    /**
     * 获得七牛上传文件的缓存目录
     * @return
     */
    public static String getUploadFileCachePath(Context context){
        String cachePath = getCacheInsidePath(context) + "/qiniuUploadCache";
        File cacheFile = new File(cachePath);
        if(!cacheFile.exists()){
            cacheFile.mkdirs();
        }
        return cacheFile.getAbsolutePath();
    }
}

