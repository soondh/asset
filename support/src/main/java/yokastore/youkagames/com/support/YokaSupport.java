package yokastore.youkagames.com.support;

import android.content.Context;

/**
 * Created by Lei Xu on 2018/1/23.
 * 项目基础支持类
 */

public class YokaSupport {




    public static void init(Context context) {
        initImageLoader(context);
        //阿里云初始化
//        OSSManager.getInstance().init(context);
    }

    private static void initImageLoader(Context context) {

    }

}
