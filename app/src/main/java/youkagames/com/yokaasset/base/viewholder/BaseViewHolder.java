package youkagames.com.yokaasset.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import youkagames.com.yokaasset.R;

/**
 * Created by songdehua on 2018/12/4.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setBackgroundResource(R.drawable.recycle_bg_selector);
    }
}
