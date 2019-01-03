package youkagames.com.yokaasset.utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by songdehua on 2018/11/23.
 */

public class MyThreadPoolExecutorHelper {
    /**
     * 单例实体
     */
    public static MyThreadPoolExecutorHelper mHelper;

    /**
     * 当前使用的线程池服务
     */
    private ExecutorService mExecutorService;

    /**
     * 当前使用的线程池
     */
    private MyThreadPoolExecutorHelper() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    /**
     * 获得单例对象
     *
     * @return
     */
    public static MyThreadPoolExecutorHelper getInstance() {
        if (mHelper == null) {
            mHelper = new MyThreadPoolExecutorHelper();
        }
        return mHelper;
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (mExecutorService != null) {
            mExecutorService.execute(runnable);
        }
    }
}
