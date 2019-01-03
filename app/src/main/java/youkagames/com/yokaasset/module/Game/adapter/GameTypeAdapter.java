package youkagames.com.yokaasset.module.Game.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.viewholder.BaseViewHolder;
import youkagames.com.yokaasset.module.Game.model.DepartmentListModel;
import youkagames.com.yokaasset.module.Game.model.GameTypeModel;
import youkagames.com.yokaasset.view.OnItemClickListener;
import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by songdehua on 2018/12/6.
 */

public class GameTypeAdapter extends RecyclerView.Adapter<GameTypeAdapter.ViewHolder>{

    private ArrayList<GameTypeModel.GameTypeData> mListData;
    private OnItemClickListener clickCallBack;
    private Context mContext;
    private int mShowPosition;

    public void setClickCallBack(OnItemClickListener clickCallBack){
        this.clickCallBack = clickCallBack;
    }

    public GameTypeAdapter(ArrayList<GameTypeModel.GameTypeData> listData){
        mListData = listData;
    }

    public void updateGameTypeData(ArrayList<GameTypeModel.GameTypeData> listData){
        mListData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.game_type_adapter_item,parent,false);
        return new GameTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        GameTypeModel.GameTypeData data = mListData.get(position);
        holder.tv_title.setText(data.name);
        if (position == mShowPosition){
            holder.iv_choose_type.setVisibility(View.VISIBLE);
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.choose_type_select_color));
        } else {
            holder.iv_choose_type.setVisibility(View.GONE);
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener{

        public TextView tv_title;
        public ImageView iv_choose_type;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            tv_title = view.findViewById(R.id.tv_title);
            iv_choose_type = view.findViewById(R.id.iv_choose_type);
        }

        @Override
        public void onClick(View v){
            if (clickCallBack != null){
                if (getAdapterPosition() == NO_POSITION || getAdapterPosition() == 0){
                    return;
                }
                clickCallBack.onItemClick(getAdapterPosition() - 1);
                iv_choose_type.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showWhichChooseTypeItemIcon(int position){
        mShowPosition = position;
        notifyDataSetChanged();
    }
}
