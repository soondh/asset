package youkagames.com.yokaasset.module.Book.activity;

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
import youkagames.com.yokaasset.model.eventbus.book.BookListUpdateNotify;
import youkagames.com.yokaasset.module.Mine.activity.LoginActivity;
import youkagames.com.yokaasset.module.Book.adapter.BookSearchListAdapter;
import youkagames.com.yokaasset.module.Book.adapter.BookTypeAdapter;
import youkagames.com.yokaasset.module.Book.client.BookClient;
import youkagames.com.yokaasset.view.UpdateDialog;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseFragmentActivity;
import youkagames.com.yokaasset.client.Contants;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Book.BookListPresenter;
import youkagames.com.yokaasset.module.Book.model.BookStatusModel;
import youkagames.com.yokaasset.module.Book.model.BookTypeModel;
import youkagames.com.yokaasset.module.Book.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import youkagames.com.yokaasset.module.Book.model.BookInfoModel;
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
 * Created by songdehua on 2018/12/3.
 */

public class BookActivity extends BaseActivity implements IBaseView,View.OnClickListener{

    private TitleBar titleBar;
    private int openIndex = -1;
    private NoContentView ncv;
    private XRecyclerView mRecyclerView;
    private int m_Page = 1;
    private BookListPresenter mPresenter;
    private BookSearchListAdapter bookSearchListAdapter;
    private ArrayList<BookComplexSearchModel.DataBeanX.DataBean> mBookListData = new ArrayList<>();
    private TextView tv_all_type;
    private TextView tv_all_department;
    private TextView tv_all_status;
    private XRecyclerView mSelectTypeRecyclerView;
    private RelativeLayout rl_all_type;
    private RelativeLayout rl_all_department;
    private RelativeLayout rl_all_status;
    private ImageView iv_all_type;
    private ImageView iv_all_department;
    private ImageView iv_all_status;

    /**
     * 书籍类型数据集合
     */
    private ArrayList<DepartmentListModel.DepartmentData> mBookTypeListData = new ArrayList<>();
    /**
     * 书籍所属部门数据集合
     */
    private ArrayList<DepartmentListModel.DepartmentData> mBookDepartmentListData = new ArrayList<>();
    public DepartmentListModel.DepartmentData departmentData = new DepartmentListModel.DepartmentData();

    /**
     * 书籍状态数据集合
     */
    private ArrayList<DepartmentListModel.DepartmentData> mBookStatusListData = new ArrayList<>();

    private BookTypeAdapter bookTypeAdapter;
    private RelativeLayout rl_select_type;
    //此处三个type的model统一用department的model做处理，因为只用name，其他两个tab可以忽略department_id
    private DepartmentListModel.DepartmentData bookTypeData;

    /*
    * 0 按书籍类型
    * 1 按所属部门
    * 2 按书籍状态
     */
    private int selectType = -1;
    private int bookTypePosition = 0;
    private int bookDepartmentPosition = 0;
    private int bookStatusPosition = 0;

    private int lastType = 0;
    private int lastDepartment = 0;
    private int lastStatus = 0;

    private static final int REQUEST_CODE_SCAN = 0x0000;// 扫描二维码

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mPresenter = new BookListPresenter(this);
        ncv = findViewById(R.id.ncv);
        tv_all_type = findViewById(R.id.tv_all_type);
        tv_all_department = findViewById(R.id.tv_all_department);
        tv_all_status=findViewById(R.id.tv_all_status);

        rl_all_type = findViewById(R.id.rl_all_type);
        rl_all_department = findViewById(R.id.rl_all_department);
        rl_all_status = findViewById(R.id.rl_all_status);

        iv_all_type = findViewById(R.id.iv_all_type);
        iv_all_department = findViewById(R.id.iv_all_department);
        iv_all_status = findViewById(R.id.iv_all_status);

        rl_select_type = findViewById(R.id.rl_select_type);

        titleBar = findViewById(R.id.title_bar);
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

        rl_all_type.setOnClickListener(this);
        rl_all_department.setOnClickListener(this);
        rl_all_status.setOnClickListener(this);

        mPresenter.getBookList(0,0,0,m_Page);
        departmentData.department_id = 0;
        mBookDepartmentListData.add(departmentData);
        //mPresenter.getDepartment();
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
        bookSearchListAdapter = new BookSearchListAdapter(mBookListData);

        bookSearchListAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (openIndex >= 0 && position != openIndex){
                    mBookListData.get(openIndex).opened=false;
                }
                if (!mBookListData.get(position).opened) {
                    mBookListData.get(position).opened = true;
                } else {
                    mBookListData.get(position).opened = false;
                }
                bookSearchListAdapter.notifyDataSetChanged();
                openIndex = position;
            }
        });

        mRecyclerView.setAdapter(bookSearchListAdapter);

        mSelectTypeRecyclerView = findViewById(R.id.recyclerview_select_type);
        LinearLayoutManager selectLayoutManager = new LinearLayoutManager(this);
        selectLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSelectTypeRecyclerView.setLayoutManager(selectLayoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this,R.drawable.divider_recycle_line);
        mSelectTypeRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mSelectTypeRecyclerView.setNoMore(true);
        mSelectTypeRecyclerView.setPullRefreshEnabled(false);
        bookTypeAdapter = new BookTypeAdapter(mBookDepartmentListData);
        bookTypeAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                m_Page = 1;
                if (selectType == 0){
                    clickSelectTypeByBookType(position);
                }else if (selectType == 1){
                    clickSelectTypeByBookDepartment(position);
                }else if (selectType ==2) {
                    clickSelectTypeByBookStatus(position);
                }
                rl_select_type.setVisibility(View.GONE);
            }
        });

        mSelectTypeRecyclerView.setAdapter(bookTypeAdapter);
        bookTypeAdapter.showWhichChooseTypeItemIcon(0);

    }

    @Override
    public void RequestSuccess(BaseModel data){
        if (data.cd == 0){
            if (data instanceof BookComplexSearchModel){
                BookComplexSearchModel model = (BookComplexSearchModel) data;
                if (model.data.booklist != null && model.data.booklist.size() >= 0) {
                    if (m_Page == 1) {
                        ncv.setVisibility(View.GONE);
                        mBookListData.clear();
                        mBookListData = model.data.booklist;
                        bookSearchListAdapter.updateBookData(mBookListData);
                        bookSearchListAdapter.notifyDataSetChanged();
                        if (mRecyclerView != null) {
                            mRecyclerView.refreshComplete();
                        }
                    } else {
                        mBookListData.addAll(model.data.booklist);
                        if (mRecyclerView != null ) {
                            mRecyclerView.loadMoreComplete();
                            bookSearchListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (m_Page == 1){
                        mBookListData.clear();
                        bookSearchListAdapter.notifyDataSetChanged();
                        ncv.setVisibility(View.VISIBLE);
                        ncv.setData(getString(R.string.tip_no_result),NoContentView.type_image_circle);
                    }
                    if (mRecyclerView != null ){
                        mRecyclerView.setNoMore(true);
                        if (m_Page == 1){
                            mRecyclerView.refreshComplete();
                        }
                    }
                }
            } else if (data instanceof DepartmentListModel){
                mBookDepartmentListData.clear();
                DepartmentListModel model = (DepartmentListModel) data;
                if (model.data != null && model.data.size() >= 0 ){
                    DepartmentListModel.DepartmentData departmentData = new DepartmentListModel.DepartmentData();
                    departmentData.name = getString(R.string.book_all_department);
                    departmentData.department_id = 0;
                    mBookDepartmentListData.add(departmentData);
                    mBookDepartmentListData.addAll(model.data);
                    bookTypeAdapter.updateBookTypeData(mBookDepartmentListData);
                    if (mSelectTypeRecyclerView != null ){
                        mSelectTypeRecyclerView.refreshComplete();
                        bookTypeAdapter.notifyDataSetChanged();
                        bookTypeAdapter.showWhichChooseTypeItemIcon(bookDepartmentPosition);
                        showTopAnimOut();
                        rl_select_type.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            CustomToast.showToast(this,data.msg,Toast.LENGTH_SHORT);
        }
    }


    //部门信息每次要重新加载，其他信息写死
    @Override
    public void onClick(View view) {
        int id = view.getId();
         switch (id) {
             case R.id.rl_all_type:
                 if (selectType == 0 && rl_select_type.getVisibility() == View.VISIBLE){
                     showTopAnimIn();
                     return;
                 }
                 iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                 iv_all_department.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 tv_all_type.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                 tv_all_department.setTextColor(getResources().getColor(R.color.main_color));
                 tv_all_status.setTextColor(getResources().getColor(R.color.main_color));

                 rl_select_type.setVisibility(View.VISIBLE);
                 showTopAnimOut();
                 mBookTypeListData.clear();
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_all_type);
                 mBookTypeListData.add(bookTypeData);
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_select_type_one);
                 mBookTypeListData.add(bookTypeData);
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_select_type_two);
                 mBookTypeListData.add(bookTypeData);

                 bookTypeAdapter.updateBookTypeData(mBookTypeListData);
                 if (mSelectTypeRecyclerView != null) {
                     mSelectTypeRecyclerView.refreshComplete();
                     bookTypeAdapter.notifyDataSetChanged();
                 }
                 selectType = 0;
                 bookTypeAdapter.showWhichChooseTypeItemIcon(bookTypePosition);
                 break;
             case R.id.rl_all_department:
                 if (selectType == 1){
                     if (rl_select_type.getVisibility() == View.VISIBLE) {
                         showTopAnimIn();
                         return;
                     }
                 }

                 iv_all_department.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                 iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 tv_all_department.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                 tv_all_type.setTextColor(getResources().getColor(R.color.main_color));
                 tv_all_status.setTextColor(getResources().getColor(R.color.main_color));

                 if (selectType == 1){
                     rl_select_type.setVisibility(View.VISIBLE);
                     bookTypeAdapter.showWhichChooseTypeItemIcon(bookDepartmentPosition);
                     showTopAnimOut();
                 } else {
                     mPresenter.getDepartment();
                     //rl_select_type.setVisibility(View.VISIBLE);
                 }

                 selectType = 1;
                 break;
             case R.id.rl_all_status:
                 if (selectType == 2 && rl_select_type.getVisibility() == View.VISIBLE){
                     showTopAnimIn();
                     return;
                 }
                 iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                 iv_all_department.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                 tv_all_type.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                 tv_all_department.setTextColor(getResources().getColor(R.color.main_color));
                 tv_all_status.setTextColor(getResources().getColor(R.color.main_color));

                 rl_select_type.setVisibility(View.VISIBLE);
                 showTopAnimOut();
                 mBookStatusListData.clear();
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_all_status);
                 mBookStatusListData.add(bookTypeData);
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_select_status_one);
                 mBookStatusListData.add(bookTypeData);
                 bookTypeData = new DepartmentListModel.DepartmentData();
                 bookTypeData.name = getString(R.string.book_select_status_two);
                 mBookStatusListData.add(bookTypeData);

                 bookTypeAdapter.updateBookTypeData(mBookStatusListData);
                 if (mSelectTypeRecyclerView != null) {
                     mSelectTypeRecyclerView.refreshComplete();
                     bookTypeAdapter.notifyDataSetChanged();
                 }
                 selectType = 2;
                 bookTypeAdapter.showWhichChooseTypeItemIcon(bookStatusPosition);
                 break;
             case R.id.rl_select_type:
                 showTopAnimIn();
                 break;
             case R.id.right_layout:
                if (!CommonUtil.fastClick()){
                    if (!CommonUtil.isLogin(this)){
                        startLoginActivity();
                    } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                        } else {
                            goScan();
                        }
                }
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

    private void showTopAnimOut(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_from_top);
        if (mSelectTypeRecyclerView != null){
            //        mSelectTypeRecyclerView.clearAnimation();
            mSelectTypeRecyclerView.startAnimation(animation);
        }



    }
    public class RecycleLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {
            m_Page = 1;
            mPresenter.getBookList(lastType,lastDepartment,lastStatus,m_Page);
        }

        @Override
        public void onLoadMore() {
            m_Page ++;
            mPresenter.getBookList(lastType,lastDepartment,lastStatus,m_Page);
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


    /*
    *通过点击书籍类型搜索
     */
    private void clickSelectTypeByBookType(int pos){

        bookTypePosition = pos;
        int departmentId = mBookDepartmentListData.get(bookDepartmentPosition).department_id;

        switch (pos){
            case 0:
                tv_all_type.setText(getString(R.string.book_all_type));
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(0,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(0,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(0,departmentId,2,m_Page);
                        break;
                }
                break;
            case 1:
                tv_all_type.setText(getString(R.string.book_select_type_one));
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(1,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(1,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(1,departmentId,2,m_Page);
                        break;
                }
                break;
            case 2:
                tv_all_type.setText(getString(R.string.book_select_type_two));
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(2,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(2,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(2,departmentId,2,m_Page);
                        break;
                }
                break;
        }
    }

    /*
    *通过所属部门搜索
     */
    private void clickSelectTypeByBookDepartment(int pos){

        bookDepartmentPosition = pos;
        int departmentId = mBookDepartmentListData.get(pos).department_id;
        lastDepartment = departmentId;
        tv_all_department.setText(mBookDepartmentListData.get(pos).name);

        switch (bookTypePosition){
            case 0:
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(0,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(0,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(0,departmentId,2,m_Page);
                        break;
                }
            case 1:
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(1,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(1,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(1,departmentId,2,m_Page);
                        break;
                }
            case 2:
                switch (bookStatusPosition){
                    case 0:
                        mPresenter.getBookList(2,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(2,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(2,departmentId,2,m_Page);
                        break;
                }
        }
    }

    /*
    *通过点击状态搜索
     */
    private void clickSelectTypeByBookStatus(int pos){

        bookStatusPosition = pos;
        int departmentId = mBookDepartmentListData.get(bookDepartmentPosition).department_id;

        switch (pos){
            case 0:
                tv_all_status.setText(getString(R.string.book_all_status));
                switch (bookTypePosition){
                    case 0:
                        mPresenter.getBookList(0,departmentId,0,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(1,departmentId,0,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(2,departmentId,0,m_Page);
                        break;
                }
                break;
            case 1:
                tv_all_status.setText(getString(R.string.book_select_status_one));
                switch (bookTypePosition){
                    case 0:
                        mPresenter.getBookList(0,departmentId,1,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(1,departmentId,1,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(2,departmentId,1,m_Page);
                        break;
                }
                break;
            case 2:
                tv_all_status.setText(getString(R.string.book_select_status_two));
                switch (bookTypePosition){
                    case 0:
                        mPresenter.getBookList(0,departmentId,2,m_Page);
                        break;
                    case 1:
                        mPresenter.getBookList(1,departmentId,2,m_Page);
                        break;
                    case 2:
                        mPresenter.getBookList(2,departmentId,2,m_Page);
                        break;
                }
                break;

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
                iv_all_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_department.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
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
                        JSONObject bookdata;
                        //JSONArray devicedata;
                        String id = "";
                        try {
                            jsonObj = new JSONObject(result);
                            bookdata = jsonObj.getJSONObject("data");
                            id = bookdata.getString("id");
                            //devicedata = jsonObj.getJSONArray("data");
                            //device_id = devicedata.getJSONObject(0).getString("device_id");
                            //device_name = devicedata.getJSONObject(0).getString("device_name");
                            //device_status = devicedata.getJSONObject(0).getString("device_status");
                            //pre_user = devicedata.getJSONObject(0).getString("user");
                            //pre_date = devicedata.getJSONObject(0).getString("date");
                        }catch (JSONException e)
                        {
                            LogUtil.i("error",e.toString());
                        }
                        //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, BorrowBookActivity.class);
                        intent.putExtra("book_id",id);
                        //intent.putExtra("device_name",device_name);
                        //intent.putExtra("pre_user",pre_user);
                        //intent.putExtra("pre_date",pre_date);
                        //intent.putExtra("status",device_status);
                        startActivity(intent);
/*
                        String uriStr = result;
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                ( Uri.parse(uriStr))
                        ).addCategory(Intent.CATEGORY_BROWSABLE)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
*/
                    }
                }
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BookListUpdateNotify deviceInfoListUpdateNotify) {
        mPresenter.getBookList(lastType,lastDepartment,lastStatus,m_Page);
    }

    /**
     * 跳转到登录页面
     */
    public void startLoginActivity(){
        Intent intent = new Intent(this , LoginActivity.class);
        startActivityAnim(intent);
    }

}
