package youkagames.com.yokaasset.module.Book.activity;

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
import youkagames.com.yokaasset.model.eventbus.book.BookListUpdateNotify;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import youkagames.com.yokaasset.module.Book.model.BookInfoModel;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.module.Book.BookListPresenter;
import youkagames.com.yokaasset.view.IBaseControl;
import youkagames.com.yokaasset.view.IBaseView;
import youkagames.com.yokaasset.view.CustomToast;
import youkagames.com.yokaasset.view.TitleBar;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.utils.Globe;


/**
 * Created by songdehua on 2018/12/10.
 */

public class BorrowBookActivity extends  BaseActivity implements IBaseView, View.OnClickListener{

    private RelativeLayout rl_header_bar;
    public static final String BOOK_ID = "book_id";
    private final int VALID = 1;
    private final int USED = 2;

    private View borrowUserView;
    private View borrowPreUserView;
    private View borrowPreDateView;
    private EditText mUserNameET;
    private TextView mUserNameHintTV;
    private TextView mBookId;
    private TextView mBookName;
    private TextView mBookDepartment;
    private TextView mPreUser;
    private TextView mPreDate;
    private TextView mStatus;
    private int mDepartmentId;

    private Button mBorrowBookBT;
    private TitleBar mTitleBar;
    private BookListPresenter mPresent;

    private int borrowStatus = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.activity_borrow_book_title_bar);
        mTitleBar.setTitle(R.string.borrow_book);

        View borrowBookIdView = findViewById(R.id.borrow_book_id);
        mBookId = borrowBookIdView.findViewById(R.id.borrow_book_id_tv);

        View borrowBookNameView = findViewById(R.id.borrow_book_name);
        mBookName = borrowBookNameView.findViewById(R.id.borrow_book_name_tv);

        borrowPreUserView = findViewById(R.id.borrow_book_pre_user);
        mPreUser = borrowPreUserView.findViewById(R.id.borrow_book_pre_user_tv);

        borrowPreDateView = findViewById(R.id.borrow_book_pre_date);
        mPreDate = borrowPreDateView.findViewById(R.id.borrow_book_pre_date_tv);

        View borrowStatusView = findViewById(R.id.borrow_book_status);
        mStatus = borrowStatusView.findViewById(R.id.borrow_book_status_tv);

        View borrowDepartmentView=findViewById(R.id.borrow_book_department);
        initIncludeContent(borrowDepartmentView);

        borrowUserView = findViewById(R.id.borrow_book_user_name);
        mUserNameHintTV = borrowUserView.findViewById(R.id.borrow_book_user_hint_tv);
        mUserNameET = borrowUserView.findViewById(R.id.borrow_book_user_et);
        mUserNameHintTV.setText("申请人姓名(必填)");
        initEditTextListener(mUserNameET,mUserNameHintTV);

        InputFilter[] filtersName = {new InputFilter.LengthFilter(10)};
        mUserNameET.setFilters(filtersName);

        mBorrowBookBT = findViewById(R.id.borrow_book_bt);
        mBorrowBookBT.setOnClickListener(this);
        mPresent = new BookListPresenter(this);
        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        String book_id = getIntent().getStringExtra(BOOK_ID);
        mPresent.getDeviceInfo(book_id);
        mBookId.setText(book_id);
}


    private void initIncludeContent(View view){
        mBookDepartment = view.findViewById(R.id.borrow_book_department_tv);
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
            mBookDepartment.setText(name);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.borrow_book_bt:
                if (!CommonUtil.fastClick()) {
                    realBorrowBook();
                }
                break;
        }
    }

    private void realBorrowBook() {
        String bookId = mBookId.getText().toString();
        String user = mUserNameET.getText().toString().trim();
        int departmentId = mDepartmentId;

        if (TextUtils.isEmpty(bookId)) {
            showToast(getResources().getString(R.string.book_id_is_wrong));
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
        if (data instanceof BookInfoModel) {
            if (data.cd == 0) {
                BookInfoModel model = (BookInfoModel) data;
                if (model.data.booklist != null) {
                    borrowStatus = model.data.booklist.get(0).statu;
                    mBookName.setText(model.data.booklist.get(0).name);
                    mPreUser.setText(model.data.booklist.get(0).user);
                    mPreDate.setText(model.data.booklist.get(0).borrowDate);
                    mStatus.setText(model.data.booklist.get(0).statuName);
                    //mDeviceDepartment.setText(model.data.data.deparment);
                    mBookDepartment.setText(model.data.booklist.get(0).departmentUser);
                    if (model.data.booklist.get(0).statuName.equals("占用")){
                        borrowUserView.setVisibility(View.GONE);
                        mBorrowBookBT.setText(R.string.borrow_book_sure_return);
                    }else{
                        borrowPreUserView.setVisibility(View.GONE);
                        borrowPreDateView.setVisibility(View.GONE);
                        mBorrowBookBT.setText(R.string.borrow_book_sure_borrow);
                    }
                }
            }
        } else {
            if (data.cd == 0) {
                showToast(data.msg);
                finish();
                EventBus.getDefault().post(new BookListUpdateNotify());
            } else {
                showToast(data.msg);
            }
        }
    }
}
