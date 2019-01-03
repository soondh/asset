package youkagames.com.yokaasset.module.Game.activity;

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
import android.widget.LinearLayout;
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
import youkagames.com.yokaasset.model.eventbus.game.GameListUpdateNotify;
import youkagames.com.yokaasset.module.Game.model.GameComplexSearchModel;
import youkagames.com.yokaasset.module.Game.model.GameInfoModel;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.module.Game.GameListPresenter;
import youkagames.com.yokaasset.view.IBaseControl;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.utils.Globe;

/**
 * Created by songdehua on 2018/12/12.
 */

public class BorrowGameActivity extends BaseActivity implements IBaseView, View.OnClickListener {
    private RelativeLayout rl_header_bar;
    public static final String GAME_ID = "game_id";
    private final int VALID = 1;
    private final int USED = 2;

    private View borrowUserView;
    private View borrowPreUserView;
    private View borrowPreDateView;
    private EditText mUserNameET;
    private TextView mUserNameHintTV;
    private TextView mGameId;
    private TextView mGameName;
    private TextView mGameDepartment;
    private TextView mPreUser;
    private TextView mPreDate;
    private TextView mStatus;
    private int mDepartmentId;

    private Button mBorrowGameBT;
    private TitleBar mTitleBar;
    private GameListPresenter mPresent;

    private int borrowStatus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_game);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.activity_borrow_game_title_bar);
        mTitleBar.setTitle(R.string.borrow_game);

        View borrowBookIdView = findViewById(R.id.borrow_game_id);
        mGameId = borrowBookIdView.findViewById(R.id.borrow_game_id_tv);

        View borrowBookNameView = findViewById(R.id.borrow_game_name);
        mGameName = borrowBookNameView.findViewById(R.id.borrow_game_name_tv);

        borrowPreUserView = findViewById(R.id.borrow_game_pre_user);
        mPreUser = borrowPreUserView.findViewById(R.id.borrow_game_pre_user_tv);

        borrowPreDateView = findViewById(R.id.borrow_game_pre_date);
        mPreDate = borrowPreDateView.findViewById(R.id.borrow_game_pre_date_tv);

        View borrowStatusView = findViewById(R.id.borrow_game_status);
        mStatus = borrowStatusView.findViewById(R.id.borrow_game_status_tv);

        View borrowDepartmentView = findViewById(R.id.borrow_game_department);
        initIncludeContent(borrowDepartmentView);

        borrowUserView = findViewById(R.id.borrow_game_user_name);
        mUserNameHintTV = borrowUserView.findViewById(R.id.borrow_game_user_hint_tv);
        mUserNameET = borrowUserView.findViewById(R.id.borrow_game_user_et);
        mUserNameHintTV.setText("申请人姓名(必填)");
        initEditTextListener(mUserNameET, mUserNameHintTV);

        InputFilter[] filtersName = {new InputFilter.LengthFilter(10)};
        mUserNameET.setFilters(filtersName);

        mBorrowGameBT = findViewById(R.id.borrow_game_bt);
        mBorrowGameBT.setOnClickListener(this);
        mPresent = new GameListPresenter(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String game_id = getIntent().getStringExtra(GAME_ID);
        mPresent.getGameInfo(game_id);
        mGameId.setText(game_id);
    }

    private void initIncludeContent(View view) {
        mGameDepartment = view.findViewById(R.id.borrow_book_department_tv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtil.fastClick()) {
                    if (borrowStatus != USED) {
                        startDepartmentSelectActivity();
                    }
                }
            }
        });
    }

    public void startDepartmentSelectActivity() {
        Intent intent = new Intent(this, DepartmentSelectActivity.class);
        startActivityForResultAnim(intent, Globe.INTENT_REQUEST_CODE_REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Globe.INTENT_REQUEST_CODE_REFRESH && resultCode == Globe.INTENT_RESULT_DEPARTMENT_SELECT) {
            String name = data.getStringExtra("name");
            mDepartmentId = data.getIntExtra("department_id", 0);
            mGameDepartment.setText(name);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.borrow_game_bt:
                if (!CommonUtil.fastClick()) {
                    realBorrowGame();
                }
                break;
        }
    }

    private void realBorrowGame() {
        String bookId = mGameId.getText().toString();
        String user = mUserNameET.getText().toString().trim();
        int departmentId = mDepartmentId;

        if (TextUtils.isEmpty(bookId)) {
            showToast(getResources().getString(R.string.game_id_is_wrong));
            return;
        }

        if (borrowStatus == 1) {
            if (TextUtils.isEmpty(user)) {
                showToast(getResources().getString(R.string.please_input_user_name));
                return;
            }
        }
        if (departmentId < 0) {
            showToast(getResources().getString(R.string.please_select_department));
            return;
        }

        if (CommonUtil.checkNicknameHasIllegal(user)) {
            showToast(getResources().getString(R.string.toast_user_name_char_illegal));
            return;
        }
        if (borrowStatus == 1) {
            mPresent.borrow(bookId, user, departmentId, 2);
        } else {
            mPresent.borrow(bookId, user, departmentId, 1);
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

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data instanceof GameInfoModel) {
            if (data.cd == 0) {
                GameInfoModel model = (GameInfoModel) data;
                if (model.data.gamelist != null) {
                    borrowStatus = model.data.gamelist.get(0).statu;
                    mGameName.setText(model.data.gamelist.get(0).eName);
                    mPreUser.setText(model.data.gamelist.get(0).user);
                    mPreDate.setText(model.data.gamelist.get(0).borrowDate);
                    mStatus.setText(model.data.gamelist.get(0).statuName);
                    //mDeviceDepartment.setText(model.data.data.deparment);
                    mGameDepartment.setText(model.data.gamelist.get(0).departmentUser);
                    if (model.data.gamelist.get(0).statuName.equals("占用")){
                        borrowUserView.setVisibility(View.GONE);
                        mBorrowGameBT.setText(R.string.borrow_game_sure_return);
                    }else{
                        borrowPreUserView.setVisibility(View.GONE);
                        borrowPreDateView.setVisibility(View.GONE);
                        mBorrowGameBT.setText(R.string.borrow_game_sure_borrow);
                    }
                }
            }
        } else {
            if (data.cd == 0) {
                showToast(data.msg);
                finish();
                EventBus.getDefault().post(new GameListUpdateNotify());
            } else {
                showToast(data.msg);
            }
        }
    }
}