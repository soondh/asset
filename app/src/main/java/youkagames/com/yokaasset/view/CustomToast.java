package youkagames.com.yokaasset.view;

import youkagames.com.yokaasset.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by songdehua on 2018/5/25.
 */

public class CustomToast {
    public static void showToast(Context context, int txtid, int duration){
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mTextView.setText(txtid);
        Toast toastStart = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        toastStart.setGravity(Gravity.TOP, 0,height/2);
        toastStart.setDuration(duration);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void showToast(Context context, String name, int duration){
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.custom_toast,null);
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mTextView.setText(name);
        Toast toastStart = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        toastStart.setGravity(Gravity.TOP, 0, height / 2);
        toastStart.setDuration(duration);
        toastStart.setView(toastRoot);
        toastStart.show();

    }
}