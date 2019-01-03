package youkagames.com.yokaasset.module.Game.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.viewholder.BaseViewHolder;
import youkagames.com.yokaasset.module.Game.model.GameComplexSearchModel;
import youkagames.com.yokaasset.view.OnItemClickListener;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
/**
 * Created by songdehua on 2018/12/6.
 * 桌游搜索adapter
 */

public class GameSearchListAdapter extends RecyclerView.Adapter<GameSearchListAdapter.ViewHolder>{
    public static final int status_available = 1;
    public static final int status_unavailable = 2;

    private ArrayList<GameComplexSearchModel.DataBeanX.DataBean> mListData;
    private Context mContext;
    private OnItemClickListener clickCallBack;

    public GameSearchListAdapter(ArrayList<GameComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void updateGameData(ArrayList<GameComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void setClickCallBack(OnItemClickListener clickCallBack){
        this.clickCallBack = clickCallBack;
    }

    @Override
    public GameSearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.game_adapter_item,parent,false);
        return new GameSearchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameSearchListAdapter.ViewHolder holder, final int position){
        GameComplexSearchModel.DataBeanX.DataBean data = mListData.get(position);
        holder.tv_game_name.setText(data.eName);
        holder.tv_game_cName.setText(mContext.getResources().getString(R.string.game_cName) + data.cName);
        holder.tv_game_jName.setText(mContext.getResources().getString(R.string.game_jName) + data.jName);
        holder.tv_game_location.setText(mContext.getResources().getString(R.string.game_adress) + data.address);
        holder.tv_game_user.setText(data.user+"/"+data.borrowDate);
        holder.tv_game_publisher.setText(mContext.getResources().getString(R.string.game_publisher) + String.valueOf(data.publisher));
        holder.tv_game_code.setText(mContext.getResources().getString(R.string.game_code) + String.valueOf(data.code));
        holder.tv_game_department_own.setText(mContext.getResources().getString(R.string.game_department_own) + data.department);
        holder.tv_game_departmentUse.setText(mContext.getResources().getString(R.string.game_departmentUse) + data.departmentUser);
        holder.tv_game_label.setText(mContext.getResources().getString(R.string.game_label) + data.label);
        holder.tv_sort_number.setText(String.valueOf(position+1));
        if (data.statu == status_unavailable) {
            holder.tv_game_status.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tv_game_status.setText(mContext.getResources().getString(R.string.status_unavailable));
        } else {
            holder.tv_game_status.setTextColor(mContext.getResources().getColor(R.color.main_color));
            holder.tv_game_status.setText(mContext.getResources().getString(R.string.status_available));
        }

        if (data.opened){
            holder.ll_hide_layout.setVisibility(View.VISIBLE);
            holder.ll_hide_line.setVisibility(View.GONE);
        } else {
            holder.ll_hide_layout.setVisibility(View.GONE);
            holder.ll_hide_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return  mListData != null ? mListData.size() : 0;
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener{
        public TextView tv_game_name;
        public TextView tv_game_cName;
        public TextView tv_game_jName;
        public TextView tv_game_location;
        public TextView tv_game_user;
        public TextView tv_game_status;
        private TextView tv_sort_number;
        private LinearLayout ll_hide_layout;
        private LinearLayout ll_hide_line;
        public TextView tv_game_publisher;
        public TextView tv_game_code;
        public TextView tv_game_department_own;
        public TextView tv_game_departmentUse;
        public TextView tv_game_label;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            tv_game_name = view.findViewById(R.id.tv_game_name);
            tv_game_cName = view.findViewById(R.id.tv_game_cName);
            tv_game_jName = view.findViewById(R.id.tv_game_jName);
            tv_game_location = view.findViewById(R.id.tv_game_location);
            tv_game_user = view.findViewById(R.id.tv_game_user);
            tv_game_status = view.findViewById(R.id.tv_game_status);
            tv_sort_number = view.findViewById(R.id.tv_sort_number);
            tv_sort_number.setVisibility(View.VISIBLE);
            ll_hide_layout = view.findViewById(R.id.ll_hide_layout);
            ll_hide_line = view.findViewById(R.id.ll_hide_line);
            tv_game_publisher = view.findViewById(R.id.tv_game_publisher);
            tv_game_code = view.findViewById(R.id.tv_game_code);
            tv_game_department_own = view.findViewById(R.id.tv_game_department_own);
            tv_game_departmentUse = view.findViewById(R.id.tv_game_departmentUse);
            tv_game_label = view.findViewById(R.id.tv_game_label);

        }

        @Override
        public void onClick(View v){
            if (clickCallBack != null ){
                if (getAdapterPosition() == NO_POSITION || getAdapterPosition() == 0){
                    return;
                }
                clickCallBack.onItemClick(getAdapterPosition()-1);
            }
        }
    }
}
