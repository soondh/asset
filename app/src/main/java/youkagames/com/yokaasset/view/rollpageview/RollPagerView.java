package youkagames.com.yokaasset.view.rollpageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.view.OnItemClickListener;

/**
 * Created by songdehua on 2018/11/28.
 */


public class RollPagerView extends RelativeLayout implements ViewPager.OnPageChangeListener {
    public ViewPager mViewPager;
    public View mHintView;
    public int gravity;
    public int color;
    public int alpha;
    public int paddingLeft;
    public int paddingTop;
    public int paddingRight;
    public int paddingBottom;
    public PagerAdapter mAdapter;
    private OnItemClickListener itemClickListener;
    //播放延迟
    public int delay;
    public RollPagerView(Context context) {
        this(context,null);
    }

    public RollPagerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RollPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RollPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }
    public void initHint(HintView hintview){
        if(mHintView!=null){
            removeView(mHintView);
        }

        if(hintview == null||!(hintview instanceof HintView)){
            return;
        }

        mHintView = (View) hintview;
        loadHintView();
    }
    public void hideHintView(){
        if(mHintView != null){
            mHintView.setVisibility(View.GONE);
        }
    }
    public void showHintView(){
        if(mHintView != null){
            mHintView.setVisibility(View.VISIBLE);
        }
    }
    private void initView(AttributeSet attrs){
        if(mViewPager!=null){
            removeView(mViewPager);
            mViewPager = null;
        }
        TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.RollViewPager);
        gravity = type.getInteger(R.styleable.RollViewPager_rollviewpager_hint_gravity, 2);
        color = type.getColor(R.styleable.RollViewPager_rollviewpager_hint_color, Color.BLACK);
        delay = type.getInt(R.styleable.RollViewPager_rollviewpager_play_delay, 0);
        alpha = type.getInt(R.styleable.RollViewPager_rollviewpager_hint_alpha, 0);
        paddingLeft = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingLeft, 0);
        paddingRight = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingRight, CommonUtil.dip2px(getContext(),15));
        paddingTop = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingTop, 0);
        paddingBottom = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingBottom, CommonUtil.dip2px(getContext(),11));

        mViewPager = new ViewPager(getContext());
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mViewPager);
        type.recycle();
        initHint(new ColorPointHintView(getContext(), Color.parseColor("#5bc4be"),Color.parseColor("#88ffffff")));
    }
    /**
     * 加载hintview的容器
     */
    private void loadHintView(){
        addView(mHintView);
        mHintView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((View) mHintView).setLayoutParams(lp);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setAlpha(alpha);
        mHintView.setBackgroundDrawable(gd);

        mHintViewDelegate.initView(mAdapter == null ? 0 : mAdapter.getCount(), gravity, (HintView) mHintView);
    }
    /**
     * 支持自定义hintview
     * 只需new一个实现HintView的View传进来
     * 会自动将你的view添加到本View里面。重新设置LayoutParams。
     * @param hintview
     */
    public void setHintView(HintView hintview){

        if (mHintView != null) {
            removeView(mHintView);
        }
        this.mHintView = (View) hintview;
        if (hintview!=null&&hintview instanceof View){
            initHint(hintview);
        }
    }

    /**
     * 设置位置
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
    /**
     * 取真正的Viewpager
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setAdapter(PagerAdapter adapter){
        adapter.registerDataSetObserver(new JPagerObserver());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mAdapter = adapter;
        dataSetChanged();
    }

    /**
     * 用来实现adapter的notifyDataSetChanged通知HintView变化
     */
    private class JPagerObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            dataSetChanged();
        }

        @Override
        public void onInvalidated() {
            dataSetChanged();
        }
    }
    private void dataSetChanged(){
        if(mHintView!=null) {
            mHintViewDelegate.initView(mAdapter.getCount(), gravity, (HintView) mHintView);
            mHintViewDelegate.setCurrentPosition(mViewPager.getCurrentItem(), (HintView) mHintView);
        }
    }
    public void setHintViewDelegate(HintViewDelegate delegate){
        this.mHintViewDelegate = delegate;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onPageSelected(int position) {
        mHintViewDelegate.setCurrentPosition(position, (HintView) mHintView);
        if(itemClickListener != null) {
            itemClickListener.onItemClick(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public interface HintViewDelegate{
        void setCurrentPosition(int position,HintView hintView);
        void initView(int length, int gravity,HintView hintView);
    }

    public HintViewDelegate mHintViewDelegate = new HintViewDelegate() {
        @Override
        public void setCurrentPosition(int position,HintView hintView) {
            if(hintView!=null)
                hintView.setCurrent(position);
        }

        @Override
        public void initView(int length, int gravity,HintView hintView) {
            if (hintView!=null)
                hintView.initView(length,gravity);
        }
    };
}