package youkagames.com.yokaasset.module.Device.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Device.model.DepartmentModel;
import youkagames.com.yokaasset.module.Device.adapter.DepartmentSelectAdapter;
import youkagames.com.yokaasset.module.Device.DevicePresenter;
import youkagames.com.yokaasset.utils.Globe;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.NoContentView;
import youkagames.com.yokaasset.view.OnItemClickListener;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/20.
 */

public class DepartmentSelectActivity extends BaseActivity implements IBaseView{
    private DevicePresenter mPresenter;
    private XRecyclerView mRecyclerView;
    private ArrayList<DepartmentModel.DepartmentData> departmentDatas;

    private DepartmentSelectAdapter mAdapter;
    private NoContentView ncv;
    private TextView tv_name_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_select);
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initViewId();
        initRecycleView();
        initData();
    }

    private void initViewId() {
        mRecyclerView = findViewById(R.id.recyclerview);
        tv_name_title = findViewById(R.id.tv_name_title);
    }

    private void initRecycleView() {
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        Drawable dividerDrawable = ContextCompat.getDrawable(this,R.drawable.divider_recycle_line);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mAdapter = new DepartmentSelectAdapter(departmentDatas);
        mRecyclerView.getDefaultFootView().setLoadingHint("");
        mRecyclerView.getDefaultFootView().setNoMoreHint("");
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String name = departmentDatas.get(position).name;
                int department_id = departmentDatas.get(position).department_id;
                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("department_id", department_id);
                setResult(Globe.INTENT_RESULT_DEPARTMENT_SELECT,intent);
                finish();
            }
        });
    }

    private void initData() {
        departmentDatas = new ArrayList<>();
        mPresenter = new DevicePresenter(this);
        mPresenter.getDepartment();
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data.cd == 0) {
            if (data instanceof DepartmentModel) {
                DepartmentModel model = (DepartmentModel) data;
                if (model.data != null && model.data.size() > 0) {
                    departmentDatas = model.data;
                    mAdapter.updateData(departmentDatas);
                    mAdapter.notifyDataSetChanged();
                    if (mRecyclerView != null) {
                        mRecyclerView.refreshComplete();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRecyclerView != null){
            mRecyclerView.destroy();
            mRecyclerView = null;
        }
    }


}