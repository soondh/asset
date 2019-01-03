package youkagames.com.yokaasset.module.AssetList.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.YokaApplication;
import youkagames.com.yokaasset.module.AssetList.AssetListPresenter;
import youkagames.com.yokaasset.module.AssetList.model.AssetListModel;
import youkagames.com.yokaasset.utils.CommonUtil;
import youkagames.com.yokaasset.view.OnItemClickListener;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.utils.Base64ImgUtil;
import java.util.ArrayList;

/**
 * Created by songdehua on 2018/11/29.
 */

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder> {
    public ArrayList<AssetListModel.AssetListData> mDatas;
    private Context mContext;
    private OnItemClickListener clickCallBack;

    public void setClickCallBack(OnItemClickListener clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }

    public AssetListAdapter(ArrayList<AssetListModel.AssetListData> datas, Context context) {
        mContext = context;
        mDatas = datas;
    }

    public void setData(ArrayList<AssetListModel.AssetListData> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    //创建View,被LayouManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assetlistitem, viewGroup,false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.nameText.setText(mDatas.get(position).name);
        if (position == 0) {
            viewHolder.coverView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.device_icon));
        } else if (position == 1) {
            viewHolder.coverView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.book_icon));
        } else if (position == 2) {
            viewHolder.coverView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.game_icon));
        }

        //Base64ImgUtil imgUtil = new Base64ImgUtil();
        //icon传的base64图
        //Bitmap icon_bitmap = imgUtil.stringToBitmap(mDatas.get(position).icon);

        //ImageLoaderUtils.loadImgWithCorner(mContext,mDatas.get(position).icon,viewHolder.coverView,17,R.drawable.ic_img_default);
        //viewHolder.coverView.setImageBitmap(icon_bitmap);
        if (position % 7 == 0){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_one));
        } else if (position % 7 == 1){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_two));
        } else if (position % 7 == 2){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_three));
        } else if (position % 7 == 3){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_four));
        } else if (position % 7 == 4){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_five));
        }else if (position % 7 == 5){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_six));
        }else if (position % 7 == 6){
            viewHolder.rl_grid_item_cover.setBackgroundColor(mContext.getResources().getColor(R.color.assert_list_item_color_seven));
        }

        viewHolder.cardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null){
                            clickCallBack.onItemClick(position);
                        }
                    }
                }
        );
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_grid_item_cover;
        public ImageView coverView;
        public TextView nameText;
        public View colorView;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            rl_grid_item_cover = (RelativeLayout) v.findViewById(R.id.rl_grid_item_cover);
            cardView = (CardView) v.findViewById(R.id.grid_item_card_view);
            coverView = (ImageView) v.findViewById(R.id.grid_item_cover);
            nameText = (TextView) v.findViewById(R.id.text);
            //colorView = v.findViewById(R.id.grid_item_color);
        }
    }
}
