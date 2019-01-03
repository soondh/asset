package yokastore.youkagames.com.support.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import yokastore.youkagames.com.support.R;

/**
 * Created by liyun on 2018/1/26.
 *
 */

public class ImageLoaderUtils {

    /**
     * 加载矩形网络图片
     * @param url
     * @param imageView
     * @param isNeedCircleCrop
     */
    public static void loadImg(Context context, String url, ImageView imageView, boolean isNeedCircleCrop){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .isNeedCircleCrop(isNeedCircleCrop)
                .target(imageView)
                .execute();
    }

    /**
     * 加载矩形网络图片（默认是要加CircleCrop）
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .target(imageView)
                .execute();
    }
    public static void loadImgWithListener(Context context, String url, boolean isNeedCircleCrop, DrawableImageViewTarget viewTarget){
        MyImageLoaderHelper.Builder.create(context)
                .imageViewTarget(viewTarget)
                .isNeedCircleCrop(isNeedCircleCrop)
                .load(url)
                .placeHolder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .targetListener();
    }

    /*
    *加载矩形网络图片
    */
    public static void loadImg(Context context, String url, ImageView imageView,int defaultId){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(defaultId)
                .error(defaultId)
                .target(imageView)
                .execute();
    }
    /*
    *加载圆形头像网络图片
    */
    public static void loadRoundImg(Context context, String url, ImageView imageView){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.shape_game_type_bg)
                .error(R.drawable.ic_default_head)
                .target(imageView)
                .executeRoundImg();

//        Glide.with(YokaApplication.getInstance()).load("https://www.baidu.com/img/bdlogo.png").transform(new GlideCircleTransform(YokaApplication.getInstance())).into(imageView);
    }
    /*
    *加载圆形头像网络图片
    */
    public static void loadImgWithCorner(Context context, String url, ImageView imageView,int corner){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.shape_game_type_bg)
                .error(R.drawable.ic_default_head)
                .corner(corner)
                .target(imageView)
                .executeRoundImg();
    }


    /*
    *加载圆形普通网络图片
    */
    public static void loadImgWithCorner(Context context, String url, ImageView imageView, int corner, int drawableID){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(drawableID)
                .error(drawableID)
                .corner(corner)
                .target(imageView)
                .executeRoundImg();
    }
    /*
    *加载矩形网络图片，指定宽高
    */
    public static void loadImgWithSize(Context context, String url, ImageView imageView,int width, int height){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .width(width)
                .height(height)
                .target(imageView)
                .execute();
    }
    /*
    *加载圆形网络图片，指定宽高
    */
    public static void loadRoundImgWithSize(Context context, String url, ImageView imageView,int width, int height){
        MyImageLoaderHelper.Builder.create(context)
                .load(url)
                .placeHolder(R.drawable.shape_game_type_bg)
                .error(R.drawable.ic_default_head)
                .width(width)
                .height(height)
                .target(imageView)
                .executeRoundImg();
    }

}
