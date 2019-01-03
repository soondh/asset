package youkagames.com.yokaasset.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseFragment;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.AssetList.AssetListPresenter;
import youkagames.com.yokaasset.module.AssetList.adapter.AssetListAdapter;
import youkagames.com.yokaasset.module.AssetList.model.AssetListModel;
import youkagames.com.yokaasset.module.Book.activity.BookActivity;
import youkagames.com.yokaasset.module.Device.activity.DeviceActivity;
import youkagames.com.yokaasset.module.Game.activity.GameActivity;
import youkagames.com.yokaasset.module.Device.activity.BorrowDeviceActivity;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.OnItemClickListener;
import youkagames.com.yokaasset.view.WrapContentLinearLayoutManager;
import youkagames.com.yokaasset.view.rollpageview.RollPagerView;
import youkagames.com.yokaasset.view.CustomToast;

/**
 * Created by songdehua on 2018/11/28.
 */

public class AssetFragment extends BaseFragment implements IBaseView {
    private AssetListPresenter mPresenter;
    private XRecyclerView mRecycleView;
    private ArrayList<AssetListModel.AssetListData> mAssetListData;
    private ViewGroup container;
    private boolean isInRefresh = false;
    private AssetListAdapter mAssetListAdapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_asset,null);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        mRecycleView = view.findViewById(R.id.recyclerview);
        TextView tv_title_text = view.findViewById(R.id.tv_title_text);

        tv_title_text.setText("Yoka Asset");
        initRecycleView();
    }

    @Override
    public void initDataFragment() {
        mAssetListAdapter = new AssetListAdapter(mAssetListData, this.getActivity());
        mAssetListAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!CommonUtil.fastClick()){
                    switch(position) {
                        case 0:
                            startDeviceActivity();
                            break;
                        case 1:
                            startBookActivity();
                            break;
                        case 2:
                            startGameActivity();
                            break;
                    }
                }
            }
        });
        mRecycleView.setAdapter(mAssetListAdapter);
        mPresenter.getAssetListData();
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        isInRefresh = false;
        if (data.cd == 0) {
            if (data instanceof AssetListModel) {
                AssetListModel model = (AssetListModel)data;
                mAssetListData.clear();
                mAssetListData.addAll(model.data);
                mAssetListAdapter.setData(mAssetListData);
                mRecycleView.refreshComplete();
            }
        }
    }

    private void initRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecycleView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecycleView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecycleView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);

        mPresenter = new AssetListPresenter(getActivity(),this,this);
        mAssetListData = new ArrayList<>();
        mRecycleView.getDefaultFootView().setLoadingHint("");
        mRecycleView.getDefaultFootView().setNoMoreHint("") ;
        mRecycleView.setLoadingListener(new AssetFragment.RecycleLoadingListener());
    }

    public void refreshDataState() {
        if (mRecycleView != null && !isInRefresh){
            isInRefresh = true;
            mRecycleView.scrollToPosition(0);
            mRecycleView.refresh();
        }
    }

    public class RecycleLoadingListener implements XRecyclerView.LoadingListener {
        @Override
        public void onRefresh() {
            mPresenter.getAssetListData();
        }

        @Override
        public void onLoadMore() {
            mPresenter.getAssetListData();
        }
    }

    @Override
    public void NetWorkError() {
        isInRefresh = false;
        if(mRecycleView != null) {
            mRecycleView.refreshComplete();
        }
        CustomToast.showToast(getActivity(),R.string.net_error, Toast.LENGTH_SHORT);
    }

    @Override
    public void RequestError(Throwable e) {
        super.RequestError(e);
        isInRefresh = false;
        if (mRecycleView != null) {
            mRecycleView.refreshComplete();
        }
    }

    public void startBookActivity(){
        Intent intent = new Intent(getActivity() , BookActivity.class);
        startActivityAnim(intent);
    }

    public void startGameActivity(){
        Intent intent = new Intent(getActivity() , GameActivity.class);
        startActivityAnim(intent);
    }

    public void startDeviceActivity(){
        Intent intent = new Intent(getActivity() , DeviceActivity.class);
        startActivityAnim(intent);
    }
}

