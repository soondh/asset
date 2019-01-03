package youkagames.com.yokaasset.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import youkagames.com.yokaasset.YokaApplication;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.utils.MyThreadPoolExecutorHelper;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static CrashExceptionHandler ech;
    private Thread.UncaughtExceptionHandler mUncaugh;
    private Context mContext;
    public final String TAG = CrashExceptionHandler.class.getSimpleName();

    private CrashExceptionHandler(){

    }

    public static synchronized CrashExceptionHandler getInstance(){
        if(ech == null){
            ech = new CrashExceptionHandler();
        }
        return ech;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable exception) {


        LogUtil.i("Lei123","exception");
        if (!handleException(exception) && mUncaugh != null) {
            //如果自己没处理交给系统处理
            mUncaugh.uncaughtException(thread, exception);
        } else {
            //自己处理
            try {//延迟2秒杀进程
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "error : ", e);
            }

            MyThreadPoolExecutorHelper.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    ActivityQueueManager.getInstance().finishAllActivity();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    System.gc();
                }
            });
        }


    }

    /**
     * 收集错误信息
     *
     * @return 处理了该异常返回true, 否则false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //在此处处理出现异常的情况
                try{
                    // 在红米3上有可能空指针，这里发生异常捕获后不做任何处理
                    Toast.makeText(mContext, "应用程序发生异常，即将重启...", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String date = "异常时间:"+format.format(new Date(System.currentTimeMillis()))+"\r\n";
        LogUtil.e("error",date+" --------------------未自动捕获的异常-------------------------");
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        LogUtil.e("error",stackTrace.toString());


        File logFile = new File(PathUtils.getBaseSDpath() + "/errorLogFile.txt");
        InputStream is = null;
        FileOutputStream fos  = null;
        try {
            if(logFile.exists() && logFile.length() > 1024 *1024 *1024 * 20) {
                // 大于20m删除
                logFile.delete();
            }
            if(!logFile.exists()){
                logFile.createNewFile();
            }
            is = new ByteArrayInputStream(stackTrace.toString().getBytes());
            fos = new FileOutputStream(logFile , true);
            //写出异常时间
            fos.write(date.getBytes());
            byte[] buffer = new byte[1024 * 1024];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            String end =  "文件写入完毕:"+format.format(new Date(System.currentTimeMillis())) +"\r\n";
            fos.write( end.getBytes());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                is.close();
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(Context context){
        mContext = context;
        mUncaugh =   Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); //设置为系统默认的异常处理类，当出现异常时，有该类处理。
    }

}
