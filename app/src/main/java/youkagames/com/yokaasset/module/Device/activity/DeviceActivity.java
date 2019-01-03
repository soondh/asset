package youkagames.com.yokaasset.module.Device.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;


import com.znq.zbarcode.CaptureActivity;
import com.znq.zbarcode.camera.CameraManager;
import com.znq.zbarcode.decode.MainHandler;
import com.znq.zbarcode.utils.BeepManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.model.eventbus.device.DeviceInfoListUpdateNotify;
import youkagames.com.yokaasset.module.Mine.activity.LoginActivity;
import youkagames.com.yokaasset.module.Device.adapter.DeviceInfoListAdapter;
import youkagames.com.yokaasset.module.Device.adapter.DeviceTypeAdapter;
import youkagames.com.yokaasset.module.Device.client.DeviceClient;
import youkagames.com.yokaasset.view.UpdateDialog;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseFragmentActivity;
import youkagames.com.yokaasset.client.Contants;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Device.DevicePresenter;
import youkagames.com.yokaasset.module.Device.model.DepartmentModel;
import youkagames.com.yokaasset.module.Device.model.DeviceComplexSearchModel;
import youkagames.com.yokaasset.module.Device.model.DeviceInfoModel;
import youkagames.com.yokaasset.module.Device.model.DeviceTypeModel;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.NoContentView;
import youkagames.com.yokaasset.view.OnItemClickListener;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.utils.DialogFragmentDataCallback;
import youkagames.com.yokaasset.view.IBaseView;
import yokastore.youkagames.com.support.utils.LogUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by songdehua on 2018/12/20.
 */

public class DeviceActivity extends BaseActivity implements IBaseView,View.OnClickListener{
    private TitleBar titleBar;
    private int openIndex = -1;
    private NoContentView ncv;
    private XRecyclerView mRecyclerView;
    private int m_Page = 1;
    private DevicePresenter mPresenter;
    private DeviceInfoListAdapter mDeviceInfoListAdapter;
    private ArrayList<DeviceComplexSearchModel.DataBeanX.DataBean> mDeviceInfoListData = new ArrayList<>();
    private XRecyclerView mSelectTypeRecyclerView;

    private TextView tv_all_os;
    private TextView tv_all_type;
    private TextView tv_all_status;

    private RelativeLayout rl_all_os;
    private RelativeLayout rl_all_type;
    private RelativeLayout rl_all_status;

    private ImageView iv_all_os;
    private ImageView iv_all_type;
    private ImageView iv_all_status;
    private static final int REQUEST_CODE_SCAN = 0x0000;// 扫描二维码

    /*
    * 操作系统类型集合
     */
    private ArrayList<DeviceTypeModel.DeviceTypeData> mDeviceTypeOsListData = new ArrayList<>();

    /*
    *设备类型集合
     */
    private ArrayList<DeviceTypeModel.DeviceTypeData> mDeviceTypePtListData = new ArrayList<>();

    /*
    *设备状态集合
     */
    private ArrayList<DeviceTypeModel.DeviceTypeData> mDeviceTypeStListData = new ArrayList<>();
    private DeviceTypeAdapter deviceTypeAdapter;
    private RelativeLayout rl_select_type;
    private DeviceTypeModel.DeviceTypeData deviceTypeData;

    /*
    * 0 按操作系统类型
    * 1 按设备类型
    * 2 按设备借出状态
     */
    private int selectType = 0;
    private int deviceTypeOsPosition = 0;
    private int deviceTypePtPosition = 0;
    private int deviceTypeStPosition = 0;

    private int lastOs = 0;
    private int lastPt = 0;
    private int lastSt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        mPresenter = new DevicePresenter(this);
        ncv = findViewById(R.id.ncv);
        titleBar = findViewById(R.id.title_bar);

        tv_all_os = findViewById(R.id.tv_all_os);
        tv_all_type = findViewById(R.id.tv_all_type);
        tv_all_status = findViewById(R.id.tv_all_status);

        rl_all_os = findViewById(R.id.rl_all_os);
        rl_all_type = findViewById(R.id.rl_all_type);
        rl_all_status = findViewById(R.id.rl_all_status);

        iv_all_os = findViewById(R.id.iv_all_os);
        iv_all_type = findViewById(R.id.iv_all_type);
        iv_all_status = findViewById(R.id.iv_all_status);

        rl_select_type = findViewById(R.id.rl_select_type);

        titleBar.setTitle(R.string.books);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        initRecycleView();

        titleBar.setRightImageView(R.drawable.qr_code);
        titleBar.getRightLayout().setOnClickListener(this);
        rl_select_type.setOnClickListener(this);

        rl_all_os.setOnClickListener(this);
        rl_all_type.setOnClickListener(this);
        rl_all_status.setOnClickListener(this);

        mPresenter.getDeviceList(0,0,0);
    }

    private void initRecycleView(){
        mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        mRecyclerView.getDefaultFootView().setLoadingHint("");
        mRecyclerView.getDefaultFootView().setNoMoreHint("");
        mRecyclerView.setLoadingListener(new RecycleLoadingListener());
        mDeviceInfoListAdapter = new DeviceInfoListAdapter(mDeviceInfoListData);

        mDeviceInfoListAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        mRecyclerView.setAdapter(mDeviceInfoListAdapter);

        mSelectTypeRecyclerView = findViewById(R.id.recyclerview_select_type);
        LinearLayoutManager selectLayoutManager = new LinearLayoutManager(this);
        selectLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSelectTypeRecyclerView.setLayoutManager(selectLayoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this,R.drawable.divider_recycle_line);
        mSelectTypeRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mSelectTypeRecyclerView.setNoMore(true);
        mSelectTypeRecyclerView.setPullRefreshEnabled(false);
        deviceTypeAdapter = new DeviceTypeAdapter(mDeviceTypeOsListData);
        deviceTypeAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(selectType == 0){
                    clickSelectTypeByOs(position);
                }else  if(selectType == 1){
                    clickSelectTypeByType(position);
                }else  if(selectType == 2){
                    clickSelectTypeByStatus(position);
                }
                rl_select_type.setVisibility(View.GONE);

            }
        });

        mSelectTypeRecyclerView.setAdapter(deviceTypeAdapter);
        deviceTypeAdapter.showWhichChooseTypeItemIcon(0);

    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if(data.cd == 0) {
            if(data instanceof DeviceComplexSearchModel){
                DeviceComplexSearchModel model = (DeviceComplexSearchModel) data;
                if(model.data.data != null && model.data.data.size() > 0) {
                    ncv.setVisibility(View.GONE);
                    mDeviceInfoListData.clear();
                    mDeviceInfoListData = model.data.data;
                    mDeviceInfoListAdapter.updateDeviceInfoListData(mDeviceInfoListData);
                    mDeviceInfoListAdapter.notifyDataSetChanged();
                    if(mRecyclerView != null) {
                        mRecyclerView.refreshComplete();
                    }
                }else{
                    mDeviceInfoListData.clear();
                    mDeviceInfoListAdapter.notifyDataSetChanged();
                    ncv.setVisibility(View.VISIBLE);
                    ncv.setData(getString(R.string.tip_no_result), NoContentView.type_image_circle);
                    if(mRecyclerView != null) {
                        mRecyclerView.setNoMore(true);
                        mRecyclerView.refreshComplete();
                    }
                }
            }
        }else{
            CustomToast.showToast(this,data.msg, Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.rl_all_os:
                if(selectType == 0 && rl_select_type.getVisibility() == View.VISIBLE){
                    return;
                }
                iv_all_os.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_os.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_type.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_status.setTextColor(getResources().getColor(R.color.main_color));
                rl_select_type.setVisibility(View.VISIBLE);
                deviceTypeAdapter.showWhichChooseTypeItemIcon(deviceTypeOsPosition);
                showTopAnimOut();
                mDeviceTypeOsListData.clear();
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.all_os);
                mDeviceTypeOsListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_os_iOS);
                mDeviceTypeOsListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_os_Android);
                mDeviceTypeOsListData.add(deviceTypeData);

                deviceTypeAdapter.updateDeviceTypeData(mDeviceTypeOsListData);
                if(mSelectTypeRecyclerView != null){
                    mSelectTypeRecyclerView.refreshComplete();
                    deviceTypeAdapter.notifyDataSetChanged();
                }
                selectType = 0;
                deviceTypeAdapter.showWhichChooseTypeItemIcon(deviceTypeOsPosition);
                break;

            case R.id.rl_all_type:
                if(selectType == 1 && rl_select_type.getVisibility() == View.VISIBLE){
                    return;
                }
                iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_os.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_type.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_os.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_status.setTextColor(getResources().getColor(R.color.main_color));

                rl_select_type.setVisibility(View.VISIBLE);
                showTopAnimOut();
                mDeviceTypePtListData.clear();
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.any_device_type);
                mDeviceTypePtListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_device_phone);
                mDeviceTypePtListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_device_tablet);
                mDeviceTypePtListData.add(deviceTypeData);

                deviceTypeAdapter.updateDeviceTypeData(mDeviceTypePtListData);
                if(mSelectTypeRecyclerView != null){
                    mSelectTypeRecyclerView.refreshComplete();
                    deviceTypeAdapter.notifyDataSetChanged();
                }
                selectType = 1;
                deviceTypeAdapter.showWhichChooseTypeItemIcon(deviceTypePtPosition);
                break;
            case R.id.rl_all_status:
                if(selectType == 2 && rl_select_type.getVisibility() == View.VISIBLE){
                    return;
                }
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_os.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_status.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_type.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_os.setTextColor(getResources().getColor(R.color.main_color));

                rl_select_type.setVisibility(View.VISIBLE);
                showTopAnimOut();
                mDeviceTypeStListData.clear();
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.all_status);
                mDeviceTypeStListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_status_avaliable);
                mDeviceTypeStListData.add(deviceTypeData);
                deviceTypeData = new DeviceTypeModel.DeviceTypeData();
                deviceTypeData.type = getString(R.string.select_status_unavailable);
                mDeviceTypeStListData.add(deviceTypeData);


                deviceTypeAdapter.updateDeviceTypeData(mDeviceTypeStListData);
                if(mSelectTypeRecyclerView != null){
                    mSelectTypeRecyclerView.refreshComplete();
                    deviceTypeAdapter.notifyDataSetChanged();
                }
                selectType = 2;
                deviceTypeAdapter.showWhichChooseTypeItemIcon(deviceTypeStPosition);
                break;
            case R.id.rl_select_type:
                showTopAnimIn();
                break;
            case R.id.right_layout:
                if (!CommonUtil.fastClick()) {
                    if(!CommonUtil.isLogin(this)) {
                        startLoginActivity();
                    }
                    else {
                        //动态权限申请
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                        } else {
                            goScan();
                        }
                    }
                }
/*
                //动态权限申请
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    goScan();
                }
                */
                break;
        }

    }

    /**
     * 跳转到扫码界面扫码
     */
    private void goScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    private void showTopAnimOut() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_from_top);
        if (mSelectTypeRecyclerView != null) {
            //        mSelectTypeRecyclerView.clearAnimation();
            mSelectTypeRecyclerView.startAnimation(animation);
        }
    }

    private void showTopAnimIn(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_from_top);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_select_type.setVisibility(View.GONE);
                iv_all_os.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (mSelectTypeRecyclerView != null){
            mSelectTypeRecyclerView.startAnimation(animation);
        }
    }
    public class RecycleLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {
            m_Page = 1;
            mPresenter.getDeviceList(lastOs,lastPt,lastSt);
        }

        @Override
        public void onLoadMore() {
            m_Page ++;
            mPresenter.getDeviceList(lastOs,lastPt,lastSt);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRecyclerView != null){
            mRecyclerView.destroy();
            mRecyclerView = null;
        }

        if(mSelectTypeRecyclerView != null){
            mSelectTypeRecyclerView.destroy();
            mSelectTypeRecyclerView = null;
        }

    }

    /**
     * 通过点击操作系统搜索
     */
    private void clickSelectTypeByOs(int pos){

        deviceTypeOsPosition = pos;

        switch (pos){
            case 0:
                tv_all_os.setText(getString(R.string.all_os));
                break;
            case 1:
                tv_all_os.setText(getString(R.string.select_os_iOS));
                break;
            case 2:
                tv_all_os.setText(getString(R.string.select_os_Android));
                break;
        }
        lastOs = deviceTypeOsPosition;
        mPresenter.getDeviceList(deviceTypeOsPosition,deviceTypePtPosition,deviceTypeStPosition);
    }

    /**
     * 通过点击设备类型搜索
     */
    private void clickSelectTypeByType(int pos){

        deviceTypePtPosition = pos;

        switch (pos){
            case 0:
                tv_all_type.setText(getString(R.string.any_device_type));
                break;
            case 1:
                tv_all_type.setText(getString(R.string.select_device_phone));
                break;
            case 2:
                tv_all_type.setText(getString(R.string.select_device_tablet));
                break;
        }
        lastPt = deviceTypePtPosition;
        mPresenter.getDeviceList(deviceTypeOsPosition,deviceTypePtPosition,deviceTypeStPosition);
    }

    /**
     * 通过点击设备状态搜索
     */
    private void clickSelectTypeByStatus(int pos){

        deviceTypeStPosition = pos;

        switch (pos){
            case 0:
                tv_all_status.setText(getString(R.string.all_status));
                break;
            case 1:
                tv_all_status.setText(getString(R.string.select_status_avaliable));
                break;
            case 2:
                tv_all_status.setText(getString(R.string.select_status_unavailable));
                break;
        }
        lastSt = deviceTypeStPosition;
        mPresenter.getDeviceList(deviceTypeOsPosition,deviceTypePtPosition,deviceTypeStPosition);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，可能无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN:// 二维码
                // 扫描二维码回传
                if (resultCode == -1) {
                    if (data != null) {
                        //获取扫描结果
                        Bundle bundle = data.getExtras();
                        String result = bundle.getString(CaptureActivity.EXTRA_STRING);
                        JSONObject jsonObj;
                        JSONObject devicedata;
                        String device_id = "";
                        try {
                            jsonObj = new JSONObject(result);
                            devicedata = jsonObj.getJSONObject("data");
                            device_id = devicedata.getString("device_id");
                        }catch (JSONException e)
                        {
                            LogUtil.i("error",e.toString());
                        }
                        Intent intent = new Intent(this, BorrowDeviceActivity.class);
                        intent.putExtra("device_id",device_id);

                        startActivity(intent);

                    }
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeviceInfoListUpdateNotify deviceInfoListUpdateNotify) {
        mPresenter.getDeviceList(lastOs,lastPt,lastSt);
    }

    /**
     * 跳转到登录页面
     */
    public void startLoginActivity(){
        Intent intent = new Intent(this , LoginActivity.class);
        startActivityAnim(intent);
    }

}
