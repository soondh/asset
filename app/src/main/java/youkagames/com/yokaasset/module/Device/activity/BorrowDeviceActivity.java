package youkagames.com.yokaasset.module.Device.activity;


import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;


import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import youkagames.com.yokaasset.R;
import yokastore.youkagames.com.support.utils.LogUtil;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.module.Device.adapter.DepartmentSelectAdapter;
import youkagames.com.yokaasset.module.Device.model.DeviceComplexSearchModel;
import youkagames.com.yokaasset.module.Device.model.DeviceInfoModel;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.module.Device.DevicePresenter;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.model.eventbus.device.DeviceInfoListUpdateNotify;
import youkagames.com.yokaasset.utils.Globe;

/**
 * Created by songdehua on 2018/12/20.
 */

public class BorrowDeviceActivity extends BaseActivity implements IBaseView, View.OnClickListener{
    private RelativeLayout rl_header_bar;
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_NAME = "device_name";
    public static final String PRE_USER ="pre_user";
    public static final String PRE_DATE = "pre_date";
    public static final String STATUS = "status";

    private EditText mUserNameET;
    private TextView mUserNameHintTV;
    private TextView mDeviceId;
    private TextView mDeviceName;
    private TextView mDeviceDepartment;
    private TextView mPreUser;
    private TextView mPreDate;
    private TextView mStatus;
    private int mDepartmentId;

    private Button mBorrowDeviceBT;
    private TitleBar mTitleBar;
    private DevicePresenter mPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_device);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.activity_borrow_device_title_bar);
        mTitleBar.setTitle(R.string.borrow_device);

        View borrowDeviceIdView = findViewById(R.id.borrow_device_id);
        mDeviceId = borrowDeviceIdView.findViewById(R.id.borrow_device_id_tv);

        View borrowDeviceNameView = findViewById(R.id.borrow_device_name);
        mDeviceName = borrowDeviceNameView.findViewById(R.id.borrow_device_name_tv);

        View borrowPreUserView = findViewById(R.id.borrow_device_pre_user);
        mPreUser = borrowPreUserView.findViewById(R.id.borrow_device_pre_user_tv);

        View borrowPreDateView = findViewById(R.id.borrow_device_pre_date);
        mPreDate = borrowPreDateView.findViewById(R.id.borrow_device_pre_date_tv);

        View borrowStatusView = findViewById(R.id.borrow_device_status);
        mStatus = borrowStatusView.findViewById(R.id.borrow_device_status_tv);

        View borrowDepartmentView=findViewById(R.id.borrow_device_department);
        initIncludeContent(borrowDepartmentView);

        View borrowUserView = findViewById(R.id.borrow_device_user_name);
        mUserNameHintTV = borrowUserView.findViewById(R.id.borrow_device_user_hint_tv);
        mUserNameET = borrowUserView.findViewById(R.id.borrow_device_user_et);
        mUserNameHintTV.setText("申请人姓名(必填)");
        initEditTextListener(mUserNameET,mUserNameHintTV);

        InputFilter[] filtersName = {new InputFilter.LengthFilter(10)};
        mUserNameET.setFilters(filtersName);

        mBorrowDeviceBT = findViewById(R.id.borrow_device_bt);
        mBorrowDeviceBT.setOnClickListener(this);
        mPresent = new DevicePresenter(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String device_id = getIntent().getStringExtra(DEVICE_ID);
        //String device_name = getIntent().getStringExtra(DEVICE_NAME);
        //String device_status = getIntent().getStringExtra(STATUS);
        //String pre_user = getIntent().getStringExtra(PRE_USER);
        //String pre_date = getIntent().getStringExtra(PRE_DATE);
        mPresent.getDeviceInfo(device_id);
        mDeviceId.setText(device_id);
        //mDeviceName.setText(device_name);
        //mPreUser.setText(pre_user);
        //mPreDate.setText(pre_date);
        //mStatus.setText(device_status);

    }


    private void initIncludeContent(View view){
        mDeviceDepartment = view.findViewById(R.id.borrow_device_department_tv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtil.fastClick()) {
                    startDepartmentSelectActivity();
                }
            }
        });
    }

    public void startDepartmentSelectActivity(){
        Intent intent = new Intent(this,DepartmentSelectActivity.class);
        startActivityForResultAnim(intent, Globe.INTENT_REQUEST_CODE_REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Globe.INTENT_REQUEST_CODE_REFRESH && resultCode == Globe.INTENT_RESULT_DEPARTMENT_SELECT){
            String name = data.getStringExtra("name");
            mDepartmentId=data.getIntExtra("department_id",0);
            mDeviceDepartment.setText(name);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.borrow_device_bt:
                if (!CommonUtil.fastClick()) {
                    realBorrowDevice();
                }
                break;
        }
    }

    private void realBorrowDevice() {
        String deviceId = mDeviceId.getText().toString();
        String user = mUserNameET.getText().toString().trim();
        int departmentId = mDepartmentId;

        if (TextUtils.isEmpty(deviceId)) {
            showToast(getResources().getString(R.string.device_id_is_wrong));
            return;
        }

        if (TextUtils.isEmpty(user)) {
            showToast(getResources().getString(R.string.please_input_user_name));
            return;
        }

        if (departmentId <= 0) {
            showToast(getResources().getString(R.string.please_select_department));
            return;
        }

        if (CommonUtil.checkNicknameHasIllegal(user)) {
            showToast(getResources().getString(R.string.toast_user_name_char_illegal));
            return;
        }
        mPresent.borrow(deviceId,user,departmentId);
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data instanceof DeviceInfoModel) {
            if (data.cd == 0) {
                DeviceInfoModel model = (DeviceInfoModel) data;
                if (model.data.data != null) {
                    mDeviceName.setText(model.data.data.device_name);
                    mPreUser.setText(model.data.data.user);
                    mPreDate.setText(model.data.data.date);
                    mStatus.setText(model.data.data.device_status);
                    //mDeviceDepartment.setText(model.data.data.deparment);
                    mDeviceDepartment.setText(model.data.data.deparment);
                }
            }
        } else {
            if (data.cd == 0) {
                showToast(data.msg);
                finish();
                EventBus.getDefault().post(new DeviceInfoListUpdateNotify());
            } else {
                showToast(data.msg);
            }
        }
    }
    private void showToast(String content){
        CustomToast.showToast(this,content,Toast.LENGTH_SHORT);
    }

    private void initEditTextListener(EditText editText, final TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
