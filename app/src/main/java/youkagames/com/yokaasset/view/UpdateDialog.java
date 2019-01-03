package youkagames.com.yokaasset.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import yokastore.youkagames.com.support.utils.LogUtil;


/**
 * Created by songdehua on 2018/11/27.
 */


public class UpdateDialog extends AlertDialog {

    private OnNormalDialogClickListener listener;

    private Activity mActivity;

    /**
     * 标题控件
     */
    private TextView tv_title;

    private TextView tv_version;

    /**
     * 确定按钮
     */
    private TextView tv_positive;

    /**
     * 取消按钮
     */
    private TextView tv_negative;

    /**
     * 确定取消之间的竖线
     */
    private View view_vertical_line;

    /**
     * 标题内容
     */
    private CharSequence strTitle;

    private CharSequence strVersion;


    /**
     * 确定按钮的名称
     */
    private String strPositive;

    /**
     * 取消按钮的名称
     */
    private String strNegative;


    /**
     * 右边是确定，左边是取消
     * @param activity
     * @param title
     * @param strPositive
     * @param strNegative
     */
    public UpdateDialog(@NonNull Activity activity , CharSequence title , CharSequence strVersion,  String strPositive , String strNegative) {
        super(activity);
        this.mActivity = activity;
        this.strTitle = title;
        this.strVersion = strVersion;
        this.strPositive = strPositive;
        this.strNegative = strNegative;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        setContentView(R.layout.dialog_notify);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_negative = (TextView) findViewById(R.id.tv_negative);
        view_vertical_line = findViewById(R.id.view_vertical_line);
    }

    /**
     * 设置确定取消按钮的监听器 ， 右边是确定，左边是取消
     * @param listener
     */
    public void setListener(OnNormalDialogClickListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化数据
     */
    private void initData(){
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i("test" , "通用对话框被取消了");
//                if(listener != null){
//                    listener.onNegative();
//                }
            }
        });
        if(TextUtils.isEmpty(strTitle)){
            tv_title.setVisibility(View.GONE);
        }else{
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(strTitle);
        }

        if(TextUtils.isEmpty(strVersion)){   //版本信息
            tv_version.setVisibility(View.GONE);
        }else{
            tv_version.setVisibility(View.VISIBLE);
            tv_version.setText(strVersion);
        }



        if(!TextUtils.isEmpty(strPositive)){
            tv_positive.setText(strPositive);
        }else{
            tv_positive.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(strNegative)){
            tv_negative.setText(strNegative);
        }else{
            tv_negative.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(strNegative) || TextUtils.isEmpty(strPositive)){
            view_vertical_line.setVisibility(View.GONE);
        }

        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定
                LogUtil.i("test" , "通用对话框确定被点击了");
                dismiss();
                if(listener != null){
                    listener.onPositive();
                }
            }
        });

        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                LogUtil.i("test" , "通用对话框取消被点击了");
                dismiss();
                if(listener != null){
                    listener.onNegative();
                }
            }
        });
    }

    /**
     * 通用对话框的确定取消按钮点击事件
     */
    public static interface OnNormalDialogClickListener{

        /**
         * 确定
         */
        void onPositive();

        /**
         * 取消
         */
        void onNegative();
    }


    /**
     * 返回标题的控件
     * @return
     */
    public TextView getTitleTextView(){
        return tv_title;
    }


}
