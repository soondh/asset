package youkagames.com.yokaasset.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import youkagames.com.yokaasset.R;


/**
 * Created by songdehua on 2018/5/28.
 */

public class CustomProgressDialog {
    private static CustomProgressDialog instance;
    private Dialog loadingDialog;
    public static CustomProgressDialog getInstance(){
        if(instance == null){
            instance = new CustomProgressDialog();
        }
        return instance;
    }

    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public Dialog createLoadingDialog(Context context,boolean isCancel) {
        if(loadingDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.loading_animation);
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            loadingDialog.setCancelable(isCancel);
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        }
        return loadingDialog;

    }
    public void disMissDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
