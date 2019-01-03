package youkagames.com.yokaasset.module.Book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import youkagames.com.yokaasset.R;
import youkagames.com.yokaasset.base.viewholder.BaseViewHolder;
import youkagames.com.yokaasset.module.Book.model.BookComplexSearchModel;
import yokastore.youkagames.com.support.image.ImageLoaderUtils;
import youkagames.com.yokaasset.view.OnItemClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by songdehua on 2018/12/4.
 * 书籍搜索adapter
 */

public class BookSearchListAdapter extends RecyclerView.Adapter<BookSearchListAdapter.ViewHolder>{
    public static final int status_available = 1;
    public static final int status_unavailable = 2;

    private ArrayList<BookComplexSearchModel.DataBeanX.DataBean> mListData;
    private Context mContext;
    private OnItemClickListener clickCallBack;

    public BookSearchListAdapter(ArrayList<BookComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void updateBookData(ArrayList<BookComplexSearchModel.DataBeanX.DataBean> listData){
        mListData = listData;
    }

    public void setClickCallBack(OnItemClickListener clickCallBack){
        this.clickCallBack = clickCallBack;
    }

    @Override
    public BookSearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_adapter_item,parent,false);
        return new BookSearchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookSearchListAdapter.ViewHolder holder, final int position) {
        BookComplexSearchModel.DataBeanX.DataBean data = mListData.get(position);
        holder.tv_book_name.setText(data.name);
        holder.tv_book_department.setText(data.departmentUser);
        if (data.statu == status_unavailable) {
            holder.tv_book_status.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tv_book_status.setText(mContext.getResources().getString(R.string.status_unavailable));
        } else {
            holder.tv_book_status.setTextColor(mContext.getResources().getColor(R.color.main_color));
            holder.tv_book_status.setText(mContext.getResources().getString(R.string.status_available));
        }
        holder.tv_book_user.setText(data.user+"/"+data.borrowDate);
        holder.tv_book_isbn.setText(mContext.getResources().getString(R.string.book_isbn) + String.valueOf(data.isbn));
        holder.tv_book_code.setText(mContext.getResources().getString(R.string.book_code) + String.valueOf(data.code));
        holder.tv_book_department_own.setText(mContext.getResources().getString(R.string.book_department_own)+data.department);
        holder.tv_sort_number.setText(String.valueOf(position + 1));

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
        return mListData != null ? mListData.size() : 0;
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener{
        public TextView tv_book_name;
        public TextView tv_book_department;
        public TextView tv_book_user;
        public TextView tv_book_status;
        private TextView tv_sort_number;
        private LinearLayout ll_hide_layout;
        private LinearLayout ll_hide_line;
        public TextView tv_book_isbn;
        public TextView tv_book_code;
        public TextView tv_book_department_own;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            tv_book_name = view.findViewById(R.id.tv_book_name);
            tv_book_department = view.findViewById(R.id.tv_book_department);
            tv_book_user = view.findViewById(R.id.tv_book_user);
            tv_book_status = view.findViewById(R.id.tv_book_status);
            tv_sort_number = view.findViewById(R.id.tv_sort_number);
            tv_sort_number.setVisibility(View.VISIBLE);
            ll_hide_layout = view.findViewById(R.id.ll_hide_layout);
            ll_hide_line = view.findViewById(R.id.ll_hide_line);
            tv_book_isbn = view.findViewById(R.id.tv_book_isbn);
            tv_book_code = view.findViewById(R.id.tv_book_code);
            tv_book_department_own = view.findViewById(R.id.tv_book_department_own);
        }

        @Override
        public void onClick(View v) {
            if (clickCallBack != null){
                if (getAdapterPosition() == NO_POSITION || getAdapterPosition() ==0){
                    return;
                }
                clickCallBack.onItemClick(getAdapterPosition()-1);
            }
        }
    }



    /**
     *
     */
    public void updateMyLikeUiItemDataBack(RecyclerView recyclerView, int index){
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(index);
        if (viewHolder != null && viewHolder instanceof ViewHolder) {
            ViewHolder itemHolder = (ViewHolder) viewHolder;
            //itemHolder.ll_hide_layout.setVisibility();

        }
    }



}
