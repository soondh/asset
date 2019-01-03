package youkagames.com.yokaasset.view.rollpageview;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by songdehua on 2018/11/28.
 */

public abstract class LoopPagerAdapter extends PagerAdapter {
    private RollPagerView mViewPager;

    private ArrayList<View> mViewList = new ArrayList<>();

    private class LoopHintViewDelegate implements RollPagerView.HintViewDelegate{
        @Override
        public void setCurrentPosition(int position, HintView hintView) {
            if (hintView!=null&&getRealCount()>0)
                hintView.setCurrent(position%getRealCount());
        }

        @Override
        public void initView(int length, int gravity, HintView hintView) {
            if (hintView!=null)
                hintView.initView(getRealCount(),gravity);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mViewList.clear();
        initPosition();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initPosition();
    }

    private void initPosition(){
    }


    public LoopPagerAdapter(RollPagerView viewPager){
        this.mViewPager = viewPager;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container, int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container, int position);

    @Deprecated
    @Override
    public final int getCount() {
        return getRealCount() == 0 ? 0:getRealCount();
    }

    public abstract int getRealCount();
}

