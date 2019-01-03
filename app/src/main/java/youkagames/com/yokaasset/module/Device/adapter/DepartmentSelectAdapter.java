package youkagames.com.yokaasset.module.Device.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.viewholder.BaseViewHolder;
import youkagames.com.yokaasset.module.Device.model.DepartmentModel;
import youkagames.com.yokaasset.view.OnItemClickListener;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/20.
 */

public class DepartmentSelectAdapter extends RecyclerView.Adapter<DepartmentSelectAdapter.ViewHolder>{

    private ArrayList<DepartmentModel.DepartmentData> mListData;
    private Context mContext;
    private OnItemClickListener clickCallBack;
    public DepartmentSelectAdapter(ArrayList<DepartmentModel.DepartmentData> listData) { mListData = listData;}
    public void updateData(ArrayList<DepartmentModel.DepartmentData> listData) { mListData = listData; }

    public void setClickCallBack(OnItemClickListener clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.department_select_adapter_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DepartmentModel.DepartmentData data = mListData.get(position);
        holder.tv_name.setText(data.name);
        holder.ll_container.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (clickCallBack != null){
                    clickCallBack.onItemClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }

    public class ViewHolder extends BaseViewHolder {
        public TextView tv_name;
        public LinearLayout ll_container;
        public ViewHolder(View view){
            super(view);
            ll_container = view.findViewById(R.id.ll_container);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
