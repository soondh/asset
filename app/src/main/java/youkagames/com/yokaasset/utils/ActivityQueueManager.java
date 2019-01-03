package youkagames.com.yokaasset.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;

import yokastore.youkagames.com.support.utils.LogUtil;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by songdehua on 2018/11/23.
 */

public class ActivityQueueManager {
    private static ActivityQueueManager mInstance = null;

    private final static String TAG = ActivityQueueManager.class.getSimpleName();
    private  LinkedList<Activity> mQueue;
    public static ActivityQueueManager getInstance() {
        synchronized (ActivityQueueManager.class) {
            if(mInstance == null){
                mInstance = new ActivityQueueManager();
            }
            return mInstance;
        }
    }

    private ActivityQueueManager() {
        mQueue = new LinkedList<Activity>();
    }

    /**
     * push activity to queue
     *
     * @param activity
     * @return void
     * @throws
     */
    public void pushActivity(Activity activity) {
        mInstance.doPushActivity(activity);
        for (Activity activity1:mQueue){
            LogUtil.d("yunli_stack","activity1 = " + activity1);
        }
    }

    /**
     * pop activity from queue
     *
     * @param activity
     * @return void
     * @throws
     */
    public void popActivity(Activity activity) {
        mInstance.doPopActivity(activity);
        for (Activity activity1:mQueue){
            LogUtil.d("yunli_stack","activity1 = " + activity1);
        }
    }

    /**
     * pop the stack top activity
     *
     * @return Activity
     * @throws
     */
    public Activity pop() {
        if (mQueue != null && mQueue.size() > 0) {
            return mQueue.peek();
        } else {
            return null;
        }
    }
    /**
     * pop the stack last activity
     *
     * @return Activity
     * @throws
     */
    public Activity last() {
        if (mQueue != null && mQueue.size() > 0) {
            return mQueue.getLast();
        } else {
            return null;
        }
    }

    /**
     * pop the postion activity
     *
     * @return Activity
     * @throws
     */
    public Activity popIndex(int postion) {
        if (mQueue != null && mQueue.size() > postion) {
            return mQueue.get(postion);
        } else {
            return null;
        }
    }

    /**
     * finish all activity from queue
     *
     * @return void
     * @throws
     */
    public void finishAllActivity() {
        mInstance.doFinishAll();
    }

    @SuppressLint("NewApi")
    public void doPushActivity(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            mQueue.push(activity);
        } else {
            mQueue.addFirst(activity);
        }
    }

    public void doPopActivity(Activity activity) {
        if (mQueue.contains(activity)) {
            mQueue.remove(activity);
        }
    }

    public void doFinishAll() {
        Iterator<Activity> it = mQueue.iterator();
        while (it.hasNext()) {
            Activity a = it.next();
            it.remove();
            a.finish();
        }
    }


    public LinkedList<Activity> getActivityLinkQueue() {
        return mQueue;
    }

    public int getSize() {
        return mQueue.size();
    }


    /**
     * 关闭N个activities
     * @param closeNumberActivities 关闭activity的个数
     */
    public void closeNumberActivities(int closeNumberActivities) {
        if (closeNumberActivities <= 0) {
            return;
        }
        LinkedList<Activity> mActivities = mQueue;
        if (mActivities != null && mActivities.size() <= 1) {
            return;
        }

        try {
            int countTemp = 0;
            for (int i = mActivities.size() - 1; i >= 0; i--) {
                Activity mActivity = mActivities.get(i);
                if (mActivity != null ) {
                    mActivity.finish();
                    mActivities.remove(mActivity);
                }
                else {
                    if (mActivities.size() > 1 ) {
                        mActivity.finish();
                        mActivities.remove(mActivity);
                        countTemp ++;
                    } else {
                        i = -1;
                    }
                }
                if (countTemp == closeNumberActivities) {
                    i = -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
