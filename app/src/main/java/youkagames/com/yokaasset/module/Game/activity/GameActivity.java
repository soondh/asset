package youkagames.com.yokaasset.module.Game.activity;

import android.Manifest;
import android.content.Context;
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
import com.znq.zbarcode.CaptureActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


import youkagames.com.yokaasset.base.activity.BaseActivity;
import youkagames.com.yokaasset.model.eventbus.game.GameListUpdateNotify;
import youkagames.com.yokaasset.module.Game.activity.BorrowGameActivity;
import youkagames.com.yokaasset.module.Game.adapter.GameSearchListAdapter;
import youkagames.com.yokaasset.module.Game.adapter.GameTypeAdapter;
import youkagames.com.yokaasset.module.Game.client.GameClient;
import youkagames.com.yokaasset.module.Game.model.GameLanguageModel;
import youkagames.com.yokaasset.module.Game.model.GameLocationModel;
import youkagames.com.yokaasset.module.Mine.activity.LoginActivity;
import youkagames.com.yokaasset.view.UpdateDialog;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.activity.BaseFragmentActivity;
import youkagames.com.yokaasset.client.Contants;
import youkagames.com.yokaasset.model.BaseModel;
import youkagames.com.yokaasset.module.Game.GameListPresenter;
import youkagames.com.yokaasset.module.Game.model.GameStatusModel;
import youkagames.com.yokaasset.module.Game.model.GameTypeModel;
import youkagames.com.yokaasset.module.Game.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Game.model.GameComplexSearchModel;
import youkagames.com.yokaasset.module.Game.model.GameInfoModel;
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
import java.util.IllegalFormatCodePointException;

/**
 * Created by songdehua on 2018/12/6.
 */

public class GameActivity extends BaseActivity implements  IBaseView,View.OnClickListener{

    private TitleBar titleBar;
    private int openIndex = -1;
    private NoContentView ncv;
    private XRecyclerView mRecyclerView;
    private int m_Page = 1;
    private GameListPresenter mPresenter;
    private GameSearchListAdapter gameSearchListAdapter;
    private ArrayList<GameComplexSearchModel.DataBeanX.DataBean> mGameListData = new ArrayList<>();
    private TextView tv_all_language;
    private TextView tv_all_location;
    private TextView tv_all_status;
    private XRecyclerView mSelectTypeRecyclerView;
    private RelativeLayout rl_all_language;
    private RelativeLayout rl_all_location;
    private RelativeLayout rl_all_status;
    private ImageView iv_all_language;
    private ImageView iv_all_location;
    private ImageView iv_all_status;

    /*
    *桌游语言数据集合
     */
    private ArrayList<GameTypeModel.GameTypeData> mGameLanguageListData = new ArrayList<>();
    private ArrayList<GameTypeModel.GameTypeData> mGameTypeLanguageListData = new ArrayList<>();
    /*
    *桌游归属地数据集合
     */
    private ArrayList<GameLocationModel.DataBeanX.DataBean> mGameLocationListData = new ArrayList<>();
    private ArrayList<GameTypeModel.GameTypeData> mGameTypeLocationListData = new ArrayList<>();

    /*
    *桌游状态数据集合
     */
    private ArrayList<GameTypeModel.GameTypeData> mGameStatusListData = new ArrayList<>();

    private GameTypeAdapter gameTypeAdapter;
    private RelativeLayout rl_select_type;
    //三个tab用同一个model
    private GameTypeModel.GameTypeData gameTypeData;

    /*
    * 0 按桌游语言
    * 1 按桌游位置
    * 2 按桌游状态
     */
    private int selectType = -1;
    private int gameLanguagePosition = 0;
    private int gameLocationPosition = 0;
    private int gameStatusPosition = 0;

    private int lastLanguage = 0;
    private int lastLocation = 0;
    private int lastStatus = 0;

    private static final int REQUEST_CODE_SCAN = 0x0000;// 扫描二维码

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPresenter = new GameListPresenter(this);
        ncv = findViewById(R.id.ncv);
        tv_all_language = findViewById(R.id.tv_all_language);
        tv_all_location = findViewById(R.id.tv_all_location);
        tv_all_status = findViewById(R.id.tv_all_status);

        rl_all_language = findViewById(R.id.rl_all_language);
        rl_all_location = findViewById(R.id.rl_all_location);
        rl_all_status = findViewById(R.id.rl_all_status);

        iv_all_language = findViewById(R.id.iv_all_language);
        iv_all_location = findViewById(R.id.iv_all_location);
        iv_all_status = findViewById(R.id.iv_all_status);

        rl_select_type = findViewById(R.id.rl_select_type);

        titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(R.string.games);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        initRecycleViw();

        titleBar.setRightImageView(R.drawable.qr_code);
        titleBar.setRightLayoutClickListener(this);
        rl_select_type.setOnClickListener(this);

        rl_all_language.setOnClickListener(this);
        rl_all_location.setOnClickListener(this);
        rl_all_status.setOnClickListener(this);

        mPresenter.getGameList(0,0,0,m_Page);

        GameTypeModel.GameTypeData languageData = new GameTypeModel.GameTypeData();
        GameTypeModel.GameTypeData locationData = new GameTypeModel.GameTypeData();
        languageData.type_id = 0;
        locationData.type_id = 0;
        mGameTypeLanguageListData.add(languageData);
        mGameTypeLocationListData.add(locationData);
        //mPresenter.getLanguage();
        //mPresenter.getLocation();
    }

    private void initRecycleViw() {
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
        gameSearchListAdapter = new GameSearchListAdapter(mGameListData);
        gameSearchListAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (openIndex >= 0 && position != openIndex){
                    mGameListData.get(openIndex).opened=false;
                }
                if (!mGameListData.get(position).opened) {
                    mGameListData.get(position).opened = true;
                } else {
                    mGameListData.get(position).opened = false;
                }
                gameSearchListAdapter.notifyDataSetChanged();
                openIndex = position;
            }
        });
        mRecyclerView.setAdapter(gameSearchListAdapter);

        mSelectTypeRecyclerView = findViewById(R.id.recyclerview_select_type);
        LinearLayoutManager selectLayoutManager = new LinearLayoutManager(this);
        selectLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSelectTypeRecyclerView.setLayoutManager(selectLayoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this,R.drawable.divider_recycle_line);
        mSelectTypeRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mSelectTypeRecyclerView.setNoMore(true);
        mSelectTypeRecyclerView.setPullRefreshEnabled(false);
        for (int i = 0; i <mGameLanguageListData.size(); i++){
            GameTypeModel.GameTypeData gameTypeData = new GameTypeModel.GameTypeData();
            gameTypeData.type_id = mGameLanguageListData.get(i).type_id;
            gameTypeData.name = mGameLanguageListData.get(i).name;
            mGameTypeLanguageListData.add(gameTypeData);
        }
        gameTypeAdapter = new GameTypeAdapter(mGameTypeLanguageListData);
        gameTypeAdapter.setClickCallBack(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                m_Page = 1;
                if (selectType == 0){
                    clickSelectTypeByGameLanguage(position);
                } else if (selectType == 1){
                    clickSelectTypeByGameLocation(position);
                } else if (selectType == 2){
                    clickSelectTypeByGameStatus(position);
                }
                rl_select_type.setVisibility(View.GONE);
            }
        });
        mSelectTypeRecyclerView.setAdapter(gameTypeAdapter);
        gameTypeAdapter.showWhichChooseTypeItemIcon(0);
    }

    @Override
    public void RequestSuccess(BaseModel data) {
        if (data.cd == 0){
            if (data instanceof GameComplexSearchModel){
                GameComplexSearchModel model = (GameComplexSearchModel)data;
                if (model.data.gamelist != null && model.data.gamelist.size() >= 0){
                    if (m_Page == 1){
                        ncv.setVisibility(View.GONE);
                        mGameListData.clear();
                        mGameListData = model.data.gamelist;
                        gameSearchListAdapter.updateGameData(mGameListData);
                        gameSearchListAdapter.notifyDataSetChanged();
                        if (mRecyclerView != null){
                            mRecyclerView.refreshComplete();
                        }
                    } else {
                        mGameListData.addAll(model.data.gamelist);
                        if (mRecyclerView != null ){
                            mRecyclerView.loadMoreComplete();
                            gameSearchListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } else if (data instanceof GameLanguageModel){
                mGameLanguageListData.clear();
                mGameTypeLanguageListData.clear();
                GameLanguageModel model = (GameLanguageModel) data;
                if (model.data != null && model.data.languagelist.size() >= 0){
                    GameTypeModel.GameTypeData languageTypeData = new GameTypeModel.GameTypeData();
                    languageTypeData.type_id = 0;
                    languageTypeData.name = getString(R.string.game_all_language);
                    mGameTypeLanguageListData.add(languageTypeData);
                    for (int i = 0; i< model.data.languagelist.size(); i++){
                        GameTypeModel.GameTypeData gameTypeData = new GameTypeModel.GameTypeData();
                        gameTypeData.type_id = model.data.languagelist.get(i).id;
                        gameTypeData.name = model.data.languagelist.get(i).language;
                        mGameTypeLanguageListData.add(gameTypeData);
                    }
                    gameTypeAdapter.updateGameTypeData(mGameTypeLanguageListData);
                    if (mSelectTypeRecyclerView != null){
                        mSelectTypeRecyclerView.refreshComplete();
                        gameTypeAdapter.notifyDataSetChanged();
                        gameTypeAdapter.showWhichChooseTypeItemIcon(gameLanguagePosition);
                        showTopAnimOut();
                        rl_select_type.setVisibility(View.VISIBLE);
                    }
                }
            } else if (data instanceof GameLocationModel){
                mGameLocationListData.clear();
                mGameTypeLocationListData.clear();
                GameLocationModel model = (GameLocationModel) data;
                if (model.data != null && model.data.addresslist.size() >= 0){
                    GameTypeModel.GameTypeData locationTypeData = new GameTypeModel.GameTypeData();
                    locationTypeData.type_id = 0;
                    locationTypeData.name = getString(R.string.game_all_location);
                    mGameTypeLocationListData.add(locationTypeData);
                    for (int i = 0; i< model.data.addresslist.size(); i++){
                        GameTypeModel.GameTypeData gameTypeData = new GameTypeModel.GameTypeData();
                        gameTypeData.type_id = model.data.addresslist.get(i).id;
                        gameTypeData.name = model.data.addresslist.get(i).address;
                        mGameTypeLocationListData.add(gameTypeData);
                    }
                    gameTypeAdapter.updateGameTypeData(mGameTypeLocationListData);
                    if (mSelectTypeRecyclerView != null){
                        mSelectTypeRecyclerView.refreshComplete();
                        gameTypeAdapter.notifyDataSetChanged();
                        gameTypeAdapter.showWhichChooseTypeItemIcon(gameLocationPosition);
                        showTopAnimOut();
                        rl_select_type.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            CustomToast.showToast(this,data.msg,Toast.LENGTH_SHORT);
        }
    }

    //桌游语言和桌游归属地每次重新加载，状态信息写死
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_all_language:
                if (selectType == 0 && rl_select_type.getVisibility() == View.VISIBLE){
                    showTopAnimIn();
                    return;
                }

                iv_all_language.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_location.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_language.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_location.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_status.setTextColor(getResources().getColor(R.color.choose_type_normal_color));

                if (selectType == 0){
                    rl_select_type.setVisibility(View.VISIBLE);
                    gameTypeAdapter.showWhichChooseTypeItemIcon(gameLanguagePosition);
                    showTopAnimOut();
                } else {
                    mPresenter.getLanguage();
                    //rl_select_type.setVisibility(View.VISIBLE);
                }
                selectType = 0;
                break;
            case R.id.rl_all_location:
                if (selectType == 1 && rl_select_type.getVisibility() == View.VISIBLE){
                    showTopAnimIn();
                    return;
                }

                iv_all_language.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_location.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_language.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_location.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_status.setTextColor(getResources().getColor(R.color.choose_type_normal_color));

                if (selectType == 1){
                    rl_select_type.setVisibility(View.VISIBLE);
                    gameTypeAdapter.showWhichChooseTypeItemIcon(gameLocationPosition);
                    showTopAnimOut();
                } else {
                    mPresenter.getLocation();
                    //rl_select_type.setVisibility(View.VISIBLE);
                }
                selectType = 1;
                break;
            case R.id.rl_all_status:
                if (selectType == 2 && rl_select_type.getVisibility() == View.VISIBLE){
                    showTopAnimIn();
                    return;
                }
                iv_all_language.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_location.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_select));
                iv_all_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                tv_all_language.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_location.setTextColor(getResources().getColor(R.color.choose_type_normal_color));
                tv_all_status.setTextColor(getResources().getColor(R.color.main_color));

                rl_select_type.setVisibility(View.VISIBLE);
                showTopAnimOut();
                mGameStatusListData.clear();
                gameTypeData = new GameTypeModel.GameTypeData();
                gameTypeData.name = getString(R.string.game_all_status);
                mGameStatusListData.add(gameTypeData);
                gameTypeData = new GameTypeModel.GameTypeData();
                gameTypeData.name = getString(R.string.game_select_status_one);
                mGameStatusListData.add(gameTypeData);
                gameTypeData = new GameTypeModel.GameTypeData();
                gameTypeData.name = getString(R.string.game_select_status_two);
                mGameStatusListData.add(gameTypeData);

                gameTypeAdapter.updateGameTypeData(mGameStatusListData);
                if (mSelectTypeRecyclerView != null) {
                    mSelectTypeRecyclerView.refreshComplete();
                    gameTypeAdapter.notifyDataSetChanged();
                }
                selectType = 2;
                gameTypeAdapter.showWhichChooseTypeItemIcon(gameStatusPosition);
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
                        JSONObject gamedata;
                        //JSONArray devicedata;
                        String id = "";
                        try {
                            jsonObj = new JSONObject(result);
                            gamedata = jsonObj.getJSONObject("data");
                            id = gamedata.getString("id");
                        }catch (JSONException e)
                        {
                            LogUtil.i("error",e.toString());
                        }
                        //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, BorrowGameActivity.class);
                        intent.putExtra("game_id",id);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }
    }
    /*
    * 通过桌游语言搜索
     */
    private void clickSelectTypeByGameLanguage(int pos) {
        gameLanguagePosition = pos;
        int languageId = mGameTypeLanguageListData.get(pos).type_id;
        int locationId = mGameTypeLocationListData.get(gameLocationPosition).type_id;
        tv_all_language.setText(mGameTypeLanguageListData.get(pos).name);

        switch (gameStatusPosition){
            case 0:
                mPresenter.getGameList(languageId,locationId,0,m_Page);
                break;
            case 1:
                mPresenter.getGameList(languageId,locationId,1,m_Page);
                break;
            case 2:
                mPresenter.getGameList(languageId,locationId,2,m_Page);
                break;
        }
    }

    /*
    *通过桌游归属地搜索
     */
    private void clickSelectTypeByGameLocation(int pos) {
        gameLocationPosition = pos;
        int languageId = mGameTypeLanguageListData.get(gameLanguagePosition).type_id;
        int locationId = mGameTypeLocationListData.get(pos).type_id;
        tv_all_location.setText(mGameTypeLocationListData.get(pos).name);

        switch (gameStatusPosition){
            case 0:
                mPresenter.getGameList(languageId,locationId,0,m_Page);
                break;
            case 1:
                mPresenter.getGameList(languageId,locationId,1,m_Page);
                break;
            case 2:
                mPresenter.getGameList(languageId,locationId,2,m_Page);
                break;
        }
    }

    /*
    *通过桌游状态搜索
     */
    private void clickSelectTypeByGameStatus(int pos){
        gameStatusPosition = pos;
        int languageId = mGameTypeLanguageListData.get(gameLanguagePosition).type_id;
        int locationId = mGameTypeLocationListData.get(gameLocationPosition).type_id;

        switch(pos){
            case 0:
                tv_all_status.setText(getString(R.string.game_all_status));
                mPresenter.getGameList(languageId,locationId,0,m_Page);
                break;
            case 1:
                tv_all_status.setText(getString(R.string.game_select_status_one));
                mPresenter.getGameList(languageId,locationId,1,m_Page);
                break;
            case 2:
                tv_all_status.setText(getString(R.string.game_select_status_two));
                mPresenter.getGameList(languageId,locationId,2,m_Page);
                break;
        }
    }




    public class RecycleLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {
            m_Page = 1;
            mPresenter.getGameList(lastLanguage,lastLocation,lastStatus,m_Page);
        }

        @Override
        public void onLoadMore() {
            m_Page ++;
            mPresenter.getGameList(lastLanguage,lastLocation,lastStatus,m_Page);
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
                iv_all_language.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
                iv_all_location.setImageDrawable(getResources().getDrawable(R.drawable.ic_choose_type_arrow_normal));
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
    private void showTopAnimOut(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_from_top);
        if (mSelectTypeRecyclerView != null){
            //        mSelectTypeRecyclerView.clearAnimation();
            mSelectTypeRecyclerView.startAnimation(animation);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GameListUpdateNotify gameListUpdateNotify) {
        mPresenter.getGameList(lastLanguage,lastLocation,lastStatus,m_Page);
    }
    /**
     * 跳转到登录页面
     */
    public void startLoginActivity(){
        Intent intent = new Intent(this , LoginActivity.class);
        startActivityAnim(intent);
    }
}
