package youkagames.com.yokaasset.module.Device.adapter;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.viewholder.BaseViewHolder;
import youkagames.com.yokaasset.module.Device.model.DeviceComplexSearchModel;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.view.OnItemClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
/**
 * Created by songdehua on 2018/12/20.
 */

public class DeviceInfoListAdapter extends RecyclerView.Adapter<DeviceInfoListAdapter.ViewHolder> {
    private ArrayList<DeviceComplexSearchModel.DataBeanX.DataBean> mListData;
    private Context mContext;
    private OnItemClickListener clickCallBack;

    public DeviceInfoListAdapter(ArrayList<DeviceComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void updateDeviceInfoListData(ArrayList<DeviceComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void setClickCallBack(OnItemClickListener clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    @Override
    public DeviceInfoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.attention_adapter_item,parent,false);
        return new DeviceInfoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceInfoListAdapter.ViewHolder holder, final int position) {
        DeviceComplexSearchModel.DataBeanX.DataBean data = mListData.get(position);
        holder.tv_title.setText(data.device_name);
        holder.tv_osversion.setText(data.os);
        if (data.device_status.equals("占用")) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }
        holder.tv_status.setText(data.device_status);
        holder.tv_usrdate.setText(data.user+"/"+data.date);
        holder.tv_sort_number.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener{
        public TextView tv_title;
        public TextView tv_osversion;
        public TextView tv_usrdate;
        public TextView tv_status;
        private TextView tv_sort_number;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            tv_title = view.findViewById(R.id.tv_title);
            tv_osversion = view.findViewById(R.id.tv_osversion);
            tv_usrdate = view.findViewById(R.id.tv_usrdate);
            tv_status = view.findViewById(R.id.tv_status);
            tv_sort_number = view.findViewById(R.id.tv_sort_number);
            tv_sort_number.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if(clickCallBack != null){
                if(getAdapterPosition() == NO_POSITION || getAdapterPosition() == 0){
                    return;
                }
                clickCallBack.onItemClick(getAdapterPosition()-1);
            }
        }
    }
}
