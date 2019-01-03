package youkagames.com.yokaasset.view.rollpageview;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import youkagames.com.yokaasset.utils.CommonUtil;


/**
 * Created by songdehua on 2018/11/28.
 */


public class ColorPointHintView extends ShapeHintView{
    private int focusColor;
    private int normalColor;
    private int sizeValue;

    public ColorPointHintView(Context context, int focusColor, int normalColor) {
        super(context);
        this.focusColor = focusColor;
        this.normalColor = normalColor;
    }
    public ColorPointHintView(Context context,int focusColor,int normalColor,int size){
        super(context);
        this.focusColor = focusColor;
        this.normalColor = normalColor;
        this.sizeValue=size;
    }
    @Override
    public Drawable makeFocusDrawable() {
        GradientDrawable dot_focus = new GradientDrawable();
        dot_focus.setColor(focusColor);
        if(sizeValue>0){
            dot_focus.setCornerRadius(CommonUtil.dip2px(getContext(), sizeValue/2));
            dot_focus.setSize(CommonUtil.dip2px(getContext(), sizeValue), CommonUtil.dip2px(getContext(), sizeValue));
        }else{
            dot_focus.setCornerRadius(CommonUtil.dip2px(getContext(), 4));
            dot_focus.setSize(CommonUtil.dip2px(getContext(), 8), CommonUtil.dip2px(getContext(), 8));
        }
        return dot_focus;
    }

    @Override
    public Drawable makeNormalDrawable() {
        GradientDrawable dot_normal = new GradientDrawable();
        dot_normal.setColor(normalColor);
        dot_normal.setStroke(CommonUtil.dip2px(getContext(), 0.5f),focusColor);
        if(sizeValue>0){
            dot_normal.setCornerRadius(CommonUtil.dip2px(getContext(), sizeValue/2));
            dot_normal.setSize(CommonUtil.dip2px(getContext(), sizeValue), CommonUtil.dip2px(getContext(), sizeValue));
        }else{
            dot_normal.setCornerRadius(CommonUtil.dip2px(getContext(), 4));
            dot_normal.setSize(CommonUtil.dip2px(getContext(), 8), CommonUtil.dip2px(getContext(), 8));
        }
        return dot_normal;
    }
}
