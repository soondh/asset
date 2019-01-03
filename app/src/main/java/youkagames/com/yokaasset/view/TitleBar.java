package youkagames.com.yokaasset.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.utils.CommonUtil;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;


/**
 * Created by songdehua on 2018/11/27.
 */


public class TitleBar extends RelativeLayout{

    protected LinearLayout leftLayout;
    protected TextView leftTv;
    protected TextView rightTv;
    protected  ImageView rightImg;
    protected RelativeLayout rightLayout;
    protected TextView titleView;
    protected RelativeLayout rootLayout;
    protected Activity activity;
    protected ProgressBar progressBar;
    protected ImageView iv_left;
    protected RelativeLayout rl_item_root;
    protected ImageView iv_image;
    protected RelativeLayout rl_middle_layout;

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs){
        activity = (Activity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_layout_bar, this);
        iv_left = view.findViewById(R.id.iv_left);
        leftLayout =  view.findViewById(R.id.left_layout);
        leftTv =  view.findViewById(R.id.left_tv);
        rightTv =  view.findViewById(R.id.right_tv);
        rightLayout =  view.findViewById(R.id.right_layout);
        rightImg = view.findViewById(R.id.iv_right_image);
        titleView =  view.findViewById(R.id.title);
        rootLayout =  view.findViewById(R.id.root);
        progressBar =  view.findViewById(R.id.pb_right);
        rl_item_root = view.findViewById(R.id.rl_item_root);
        iv_image = view.findViewById(R.id.iv_image);
        rl_middle_layout = view.findViewById(R.id.rl_middle_layout);

        parseStyle(context, attrs);
        //getRootLayout().setPadding(0 , 0 , 0 , 0);
        getRootLayout().setPadding(0 , CommonUtil.getTitleBarPadding(getContext()) , 0 , 0);
        getRootLayout().getLayoutParams().height = getRootLayout().getLayoutParams().height + CommonUtil.getTitleBarPadding(getContext());
        //getRootLayout().getLayoutParams().height = getRootLayout().getLayoutParams().height;

    }
    private void parseStyle(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EasyTitleBar);
            String title = ta.getString(R.styleable.EasyTitleBar_titleBarTitle);
            titleView.setText(title);

            String leftText = ta.getString(R.styleable.EasyTitleBar_titleBarLeftText);
            if (!TextUtils.isEmpty(leftText)) {
                leftTv.setText(leftText);
            }
            Drawable background = ta.getDrawable(R.styleable.EasyTitleBar_titleBarBackground);
            if(null != background) {
                rootLayout.setBackgroundDrawable(background);
            }

            ta.recycle();
        }
    }

    public void setMiddleImageUrl(String imgUrl){
        ImageLoaderUtils.loadRoundImg(activity, imgUrl, iv_image);
    }

    public void setMiddleImageViewVisibility(int visibility) {
        iv_image.setVisibility(visibility);
    }

    public void setTitleVisibility(int visibility) {
        titleView.setVisibility(visibility);
    }

    public void setLeftImageResource(int resId){
        iv_left.setImageResource(resId);
    }

    public void setLeftTextResource(CharSequence charSequence) {
        leftTv.setText(charSequence);
    }

    public void setRightTextResource(CharSequence charSequence) {
        rightTv.setText(charSequence);
    }

    public void setLeftTextColor(int color) {
        leftTv.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        rightTv.setTextColor(color);
    }

    public void setRightTextViewVisibility(int visibility) {
        rightTv.setVisibility(visibility);
    }

    public void setRightImageView(int imgStrId) {
        rightImg.setImageResource(imgStrId);
    }

    public void setMiddleLayoutClickListener(View.OnClickListener listener){
        rl_middle_layout.setOnClickListener(listener);
    }

    public void setRightImageViewClickListener(View.OnClickListener listener){
        rightImg.setOnClickListener(listener);
    }
    public void setLeftLayoutClickListener(View.OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(View.OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setTitle(int resid){
        titleView.setText(getResources().getString(resid));
    }

    public void setTitleColor(int color){
        titleView.setTextColor(color);
    }

    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }

    }

    public void setTitleBackgroudColor(int color){
        rootLayout.setBackgroundColor(color);
    }

    public RelativeLayout getMiddleLayout(){
        return rl_middle_layout;
    }

    public LinearLayout getLeftLayout(){
        return leftLayout;
    }

    public RelativeLayout getRightLayout(){
        return rightLayout;
    }

    public RelativeLayout getRootLayout(){
        return rootLayout;
    }

    public RelativeLayout getItemRootLayout(){
        return rl_item_root;
    }

    public ImageView getLeftImage(){
        return iv_left;
    }


    public TextView getTitleLayout(){
        return titleView;
    }


    /**
     * 设置是否需要渐变
     */
    public void setNeedTranslucent() {
        setNeedTranslucent(true, false);
    }

    /**
     * 设置是否需要渐变,并且隐藏标题
     *
     * @param translucent
     */
    public void setNeedTranslucent(boolean translucent, boolean titleInitVisibile) {
        if (translucent) {
            rootLayout.setBackgroundDrawable(null);
        }
        if (!titleInitVisibile) {
            titleView.setVisibility(View.GONE);
        }
    }

}
