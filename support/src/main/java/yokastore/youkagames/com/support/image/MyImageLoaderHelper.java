package yokastore.youkagames.com.support.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lei Xu on 2018/1/22.
 * 图片加载入口类，所有地方调用次方法加载图片 ， 需要更换图片框架只更换此处
 */
public class MyImageLoaderHelper {
    /**
     * 加载圆角头像图片
     * @param builder
     */
    @SuppressLint("CheckResult")
    public static void loadRoundImg(Builder builder){
        RequestBuilder requestBuilder = Glide.with(builder.mContext).load(builder.uri);
        //添加加载的监听
        requestBuilder.listener(new MyRequestListener());
        requestBuilder.load(builder.uri); //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
        requestBuilder.apply(getRoundImgRequestOptions(builder)); //使用自定义的配置
        requestBuilder.into(builder.iv);
    }

    private static void targetListener(Builder builder) {
        RequestBuilder requestBuilder = Glide.with(builder.mContext).load(builder.uri);
        requestBuilder.load(builder.uri); //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
        requestBuilder.apply(getImgRequestOptions(builder)); //使用自定义的配置
        requestBuilder.into(builder.viewTarget);
    }

    /**
     * 加载普通图片
     * @param builder
     */
    @SuppressLint("CheckResult")
    public static void loadImage(Builder builder){
        RequestBuilder requestBuilder = Glide.with(builder.mContext).load(builder.uri);
        if(builder.listener != null){
            requestBuilder.listener(builder.listener);
        }else {
            requestBuilder.listener(new MyRequestListener());
        }
        requestBuilder.load(builder.uri); //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
        requestBuilder.apply(getImgRequestOptions(builder)); //使用自定义的配置
        requestBuilder.into(builder.iv);
    }
    /**
     * 加载图片不需要缓存的
     *
     */
    public static void loadSourceImgWithNoCache(Builder builder) {
        RequestBuilder requestBuilder = Glide.with(builder.mContext).load(builder.uri);
        requestBuilder.listener(new MyRequestListener());
        requestBuilder.load(builder.uri); //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
        requestBuilder.apply(getWithNoCacheRequestOptions()); //使用自定义的配置
        requestBuilder.into(builder.iv);
    }
    /**
     * 清楚缓存
     * @return
     */
    public static boolean clearCacheDiskSelf(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class Builder{
        /**
         * 上下文
         */
        Context mContext;

        /**
         * 正在加载的占位图片
         */
        int placeHolderImage;

        /**
         * 加载失败错误图片
         */
         int errorImage;

        /**
         * 加载的uri
         */
        String uri;

        /**
         * 加载的resourceId
         */
        int drawableResoureceId;

        /**
         * 加载的资源数组
         */
        public byte[] resourceBytes;

        /**
         * 需要背加载图片的控件
         */
        ImageView iv;

        int width;
        int height;
        int corner = 1000;
        boolean isNeedCircleCrop = true;
        RequestListener listener;
        ImageViewTarget viewTarget;
        File file;

        private Builder(Context context){
            this.mContext = context;
        }

        /**
         * 创建一个配置builder
         * @param context
         * @return
         */
        public static Builder create(Context context){
            return new Builder(context);
        }

        /**
         * 加载的图片uri
         * @param uri
         * @return
         */
        public Builder load(String uri){
            this.uri = uri;
            return this;
        }

        public Builder load(int resourceId){
            this.drawableResoureceId = resourceId;
            return this;
        }

        public Builder load(byte[] resourceBytes){
            this.resourceBytes = resourceBytes;
            return this;
        }
        public Builder listener(RequestListener listener){
            this.listener = listener;
            return this;
        }
        /**
         * 设置加载中的占位图
         * @param placeHolderImage
         * @return
         */
        public Builder placeHolder(int placeHolderImage){
            this.placeHolderImage = placeHolderImage;
            return this;
        }

        /**
         * 设置错误时展示的图片
         * @param errorImage
         * @return
         */
        public Builder error(int errorImage){
            this.errorImage = errorImage;
            return this;
        }
        public Builder width(int width){
            this.width = width;
            return this;
        }
        public Builder height(int height){
            this.height = height;
            return this;
        }
        public Builder corner(int corner){
            this.corner = corner;
            return this;
        }

        public Builder isNeedCircleCrop(boolean isNeedCircleCrop){
            this.isNeedCircleCrop = isNeedCircleCrop;
            return this;
        }
        public Builder imageViewTarget(DrawableImageViewTarget viewTarget) {
            this.viewTarget = viewTarget;
            return this;
        }
        /**
         * 设置需要被加载图片的控件
         * @param iv
         * @return
         */
        public Builder target(ImageView iv){
            this.iv = iv;
            return this;
        }
        public void targetListener() {
            MyImageLoaderHelper.targetListener(this);
        }

        public void execute(){
            MyImageLoaderHelper.loadImage(this);
        }
        public void executeRoundImg(){
            MyImageLoaderHelper.loadRoundImg(this);
        }

        /**
         * 获得图片url中包含的宽高属性
         * @param url
         * @return
         */
        public static int[] getImageUrlWH(String url){
            int[] wh = new int[2];
            if(!TextUtils.isEmpty(url)){
                Matcher matcher = Pattern.compile("_(\\d)+").matcher(url);
                if(matcher.find()){
                    String width = matcher.group();
                    width = width.substring(1 , width.length());
                    wh[0] = Integer.valueOf(width);
                }

                if(matcher.find()){
                    String height = matcher.group();
                    height = height.substring(1 , height.length());
                    wh[1] = Integer.valueOf(height);
                }
            }
            return wh;
        }



    }



    private static RequestOptions getImgRequestOptions(Builder builder){

        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//全部缓存
//        options.skipMemoryCache(true);
        setBaseRequestOptions(options,builder);
        return options;
    }
    @SuppressLint("CheckResult")
    private static RequestOptions getWithNoCacheRequestOptions() {

        RequestOptions options = new RequestOptions();
        options.format(DecodeFormat.PREFER_ARGB_8888);
        options.skipMemoryCache(true); //禁用缓存,包括内存和磁盘
        return options;
    }

    private static RequestOptions getRoundImgRequestOptions(Builder builder){
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//全部缓存
        setBaseRequestOptions(options,builder);
        options.circleCrop();   //设置成圆形头像<这个是V4.0新增的>
        if(builder.corner == 1000) {
            options.transform(new GlideCircleTransform());
        }else {
            options.transform(new RoundedCorners(builder.corner)); //设置成圆角头像<这个是V4.0新增的>
        }
        return options;
    }

    private static void setBaseRequestOptions(RequestOptions options,Builder builder){
        options.format(DecodeFormat.PREFER_ARGB_8888);
        if(builder.isNeedCircleCrop){
            options.centerCrop();
        }
        if(builder.width == 0 || builder.height == 0){
        }else {
            options.override(builder.width, builder.height);//加载固定大小的图片
        }
        if(builder.errorImage != 0) {
            options.error(builder.errorImage);
        }
        if(builder.placeHolderImage != 0) {
            options.placeholder(builder.placeHolderImage);
        }
    }

    public static class MyRequestListener implements RequestListener {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
//            Log.e("ViewTimerLoopAdapter", "onException: " + e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
//            Log.e("ViewTimerLoopAdapter",  "model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }
    }
}
