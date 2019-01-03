package youkagames.com.yokaasset.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.utils.CommonUtil;
/**
 * Created by songdehua on 2018/12/3.
 */


public class NoContentView extends RelativeLayout {

    /**
     * 高间距
     */
    public static final int type_padding_top_high = 1;

    /**
     * 中间距
     */
    public static final int type_padding_top_medium = 2;

    /**
     * 低间距
     */
    public static final int type_padding_top_low = 3;



    /**
     * 关注的桌游列表、收藏的资讯为空的图片
     */
    public static final int type_image_love = 1;

    /**
     * 我关注的人、关注我的人、我的桌游圈列表为空的图片
     */
    public static final int type_image_circle = 2;

    /**
     * 删除提示的图片
     */
    public static final int type_image_delete = 3;


    /**
     * 用户加入俱乐部列表为空时，显示的幽灵图片
     */
    public static final int type_image_ghost = 4;

    /**
     * 积分商城为空的情况
     */
    public static final int type_image_shop = 5;

    /**
     * 提示图片的上边距
     */
    private int ivPaddingTop;

    /**
     * 提示图片的资源索引
     */
    private int ivResourceId;

    /**
     * 提示图片的宽度
     */
//    private int ivWidth;

    /**
     * 提示图片的高度
     */
//    private int ivHeight;

    /**
     * 提示的文本内容
     */
    private String tvText;

    /**
     * 图片的提示控件
     */
    private ImageView iv_no_content;

    /**
     * 文本的提示控件
     */
    private TextView tv_no_content;

    /**
     * 所有内容的线性布局
     */
    private RelativeLayout rl_all;
    private Context mContext;

    public NoContentView(Context context) {
        super(context);
        init(context , null);
    }

    public NoContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context , attrs);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context , AttributeSet attrs) {
        View.inflate(context, R.layout.view_no_content, this);
        this.mContext = context;
        iv_no_content = findViewById(R.id.iv_no_content);
        tv_no_content = findViewById(R.id.tv_no_content);
        rl_all = findViewById(R.id.rl_all);

        parseStyle(context , attrs);

        fillData();
    }

    /**
     * 解析自定义属性
     * @param context
     * @param attributeSet
     */
    private void parseStyle(Context context, AttributeSet attributeSet) {
        int imageType = 0;
        int paddingTopType = 0;
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NoContentViewAttrs);
            tvText= typedArray.getString(R.styleable.NoContentViewAttrs_text);
            imageType = typedArray.getInt(R.styleable.NoContentViewAttrs_image , 0);
            paddingTopType = typedArray.getInt(R.styleable.NoContentViewAttrs_paddingTopType , 0);
        }

        setImageType(imageType);
        setPaddingTopType(paddingTopType);
        fillData();

    }

    /**
     * 设置无内容提示
     * @param noContentText
     */
    public void setNoContentText(String noContentText){
        tvText = noContentText;
        fillData();
    }


    /**
     * 设置顶部边距
     * @param paddingTopType
     */
    public void setPaddingTopType(int paddingTopType){
        switch (paddingTopType){
            case type_padding_top_high:
                // 顶部高间距
                ivPaddingTop = CommonUtil.dip2px(getContext() , 200);
                break;
            case type_padding_top_medium:
                // 顶部中间距
                ivPaddingTop = CommonUtil.dip2px(getContext() , 75);
                break;
            case type_padding_top_low:
                // 顶部低间距
                ivPaddingTop = CommonUtil.dip2px(getContext() , 10);
                break;
            default:
                // 默认间距
                ivPaddingTop = CommonUtil.dip2px(getContext() , 100);
                break;
        }
        fillData();
    }

    /**
     * 设置无内容的图片类型
     * @param imageType
     */
    public void setImageType(int imageType){
        switch (imageType){
            case type_image_love:
                ivResourceId = R.drawable.ic_tip_no_attention;
//                ivWidth = DisplayUtil.dip2px(YokaApplication.getInstance(), 114);
//                ivHeight = DisplayUtil.dip2px(YokaApplication.getInstance() , 90);
                break;
            case type_image_circle:
                // 关注列表
                ivResourceId = R.drawable.ic_tip_no_attention_fans;
//                ivWidth = DisplayUtil.dip2px(YokaApplication.getInstance() , 116);
//                ivHeight = DisplayUtil.dip2px(YokaApplication.getInstance() , 105);
                break;

            case type_image_delete:
                //帖子被删除用的图片
//                ivResourceId = R.drawable.subject_default_pic_delete;
//                ivWidth = DisplayUtil.dip2px(YokaApplication.getInstance() , 114);
//                ivHeight = DisplayUtil.dip2px(YokaApplication.getInstance() , 106);
                break;


            case type_image_ghost:
                //用户的俱乐部列表为空
                ivResourceId = R.drawable.ic_tip_no_club_list;
//                ivWidth = DisplayUtil.dip2px(YokaApplication.getInstance() , 114);
//                ivHeight = DisplayUtil.dip2px(YokaApplication.getInstance() , 106);
                break;

            case type_image_shop:
                //用户的俱乐部列表为空
                ivResourceId = R.drawable.ic_no_shop_content;
//                ivWidth = DisplayUtil.dip2px(YokaApplication.getInstance() , 114);
//                ivHeight = DisplayUtil.dip2px(YokaApplication.getInstance() , 106);
                break;

            default:
//                ivResourceId = R.drawable.YokaApplication;
//                ivWidth = DisplayUtil.dip2px(PikApplication.getInstance() , 114);
//                ivHeight = DisplayUtil.dip2px(PikApplication.getInstance() , 90);
                break;
        }
    }

    /**
     * 设置无内容相关的数据
     * @param noContentText
     * @param imageType
     */
    public void setData(String noContentText, int imageType, int paddingTopType){
        tvText = noContentText;
        setImageType(imageType);
        setPaddingTopType(paddingTopType);
        fillData();
    }

    public void setData(String noContentText, int imageType){
        tvText = noContentText;
        setImageType(imageType);
        fillData();
    }

    private void fillData(){
        tv_no_content.setText(tvText);
        iv_no_content.setImageResource(ivResourceId);
        rl_all.setPadding(0 , ivPaddingTop , 0 , 0);
    }

}
