package youkagames.com.yokaasset.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import youkagames.com.yokaasset.R;
import yokastore.youkagames.com.support.utils.LogUtil;
import youkagames.com.yokaasset.view.CustomToast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by songdehua on 2018/5/29.
 */

public class CommonUtil {

    public static String getUid(Context context) {
        String userId = PreferenceUtils.getString(context, PreferenceUtils.USER_ID, "");
        return userId;
    }

    /**
     * 清理登录的用户的缓存数据
     */
    public static void exitClearAccount(Context context){
        //不管服务端是否退出，客户端直接退出
        LogUtil.i("Dehua" , "清楚客户端缓存用户信息");
        PreferenceUtils.reset(context);
    }

    public static boolean isLogin(Context context){
        String token = PreferenceUtils.getString(context, PreferenceUtils.TOKEN, "");
        if(!TextUtils.isEmpty(token)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 检测昵称是否满足要求
     * @param nickname
     * @return
     */
    public static boolean checkNickname(Context context, String nickname){
        if(TextUtils.isEmpty(nickname)){
            // 昵称为空
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_cant_be_null), Toast.LENGTH_SHORT);
            return false;
        }

        if(nickname.length() < 2 || nickname.length() > 10){
            // 昵称长度不符合
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_length_illegal), Toast.LENGTH_SHORT);
            return false;
        }


        if(nickname.contains(" ")){
            // 昵称包含空格
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_has_space_illegal), Toast.LENGTH_SHORT);
            return false;
        }

        if(containsEmoji(nickname)){
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_emoji_illegal), Toast.LENGTH_SHORT);
            return false;
        }
//        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@①#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        if(checkNicknameHasIllegalChar(nickname)){
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_char_illegal), Toast.LENGTH_SHORT);
            return false;
        }

        if (checkNicknameHasIllegal(nickname)) {
            CustomToast.showToast(context, context.getString(R.string.toast_nickname_char_illegal), Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    public static int getStringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 检测是否包含特殊字符(包含emoji表情)
     * @param nickname
     * @return
     */
    public static boolean checkNicknameHasIllegal(String nickname){
        if(checkNicknameHasIllegalChar(nickname)){
            return false;
        }
        String limitEx = "[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^` ，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(nickname);
        return m.find();
    }

    /**
     * 检测是否包含特殊字符
     * @param nickname
     * @return
     */
    public static boolean checkNicknameHasIllegalChar(String nickname){
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@①#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(nickname);
        return m.find();
    }


    /**
     * 判断是否为整数
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static String doubleToString(double num){
        //使用0.0不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.0").format(num);
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint
     *            比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    /**
     * 验证密码为6-16位，由字母和数字的组合
     * @param password
     * @return
     */
    public static boolean checkPassword(String password){
        if(TextUtils.isEmpty(password) || password.length() < 6) {
            return false;
        }else{
            String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
            return  password.matches(regex);
        }
    }

    /**
     * 是否为默认头像链接
     * @param imgUrl
     * @return
     */
    public static boolean isDefaultAvatar(String imgUrl){
        if(imgUrl.contains("defaultUser.jpg")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 去除字符串重复字符
     * @param s
     * @return
     */
    public static String removeMethod(String s) {
        StringBuffer sb=new StringBuffer();
        int len=s.length();
        int i=0;
        boolean flag=false;
        for(i=0; i<len;i++){
            char c=s.charAt(i);
            if(s.indexOf(c)!=s.lastIndexOf(c)){
                flag=false;
            }else{
                flag=true;
            }
            if(i==s.indexOf(c))
                flag=true;
            if(flag){
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 讲字符串数组转换成字符串
     * @param strs
     * @return
     */
    public static String transformArray2String(String[] strs){
        if(strs == null || strs.length == 0){
            return null;
        }
        return transformList2String(Arrays.asList(strs));
    }

    /**
     * 将字符串集合转换成字符串
     * @param list
     * @return
     */
    public static String transformList2String(List<String> list){
        if(list == null || list.size() == 0){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String str : list){
            sb.append(str);
            sb.append(",");
        }
        sb = new StringBuilder(sb.substring(0 , sb.length() -1));
        sb.append("]");
        return sb.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale +0.5f);
    }
    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2Px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 获得应用与沉浸式标题栏上的padding值
     * @param context
     * @return
     */
    public static int getTitleBarPadding(Context context){
        int result = 0;
        if(Build.VERSION.SDK_INT > 18) {
            result = getStatusBarHeight(context);
        }
        return result;
    }


    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(Context context , View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    public static void KeyBoardCancel(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 弹出软键盘
     * @param context
     * @param view
     */
    public static void showSoftKeyboard(Context context , View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view , 0); // SHOW_IM_PICKER_MODE_AUTO
    }

    /**
     * 使PopupWindow的背景有一层遮盖
     * @param popupWindow
     */
    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }

    /**
     * 实现文本复制功能
     * @param content
     */
    public static void copy(String content, Context context){
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }


    /**
     * 用来判断服务是否运行.
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            String currentName = serviceList.get(i).service.getClassName();
            LogUtil.i("test" ,"当前运行的服务名称：" + currentName + "判断的服务名称：" + className);
            if (currentName.equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 设置ListView的高度是基于它的子view
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        LogUtil.i("test" , "commonutil中的设置listView的总高度：" + totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 根据List获取到对应的JSONArray
     * @param list
     * @return
     */
    public static JSONArray getJSONArrayByList(List<?> list){
        JSONArray jsonArray = new JSONArray();
        if (list==null ||list.isEmpty()) {
            return jsonArray;//nerver return null
        }

        for (Object object : list) {
            jsonArray.put(object);
        }
        return jsonArray;
    }

    public static Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //int fgWidth = foreground.getWidth();
        //int fgHeight = foreground.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
//        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        cv.restore();//存储
        return newbmp;
    }

    /**
     * 检查版本，并设置沉浸式全屏
     * @param activity
     */
    public static void checkVersion(Activity activity) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (null != activity.getWindow()) {
                View view = activity.getWindow().getDecorView();
                view.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
        }
    }

    private static long lastClickTime;

    /**
     * 是否过快点击
     */
    public static boolean fastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean fastClick(int dur) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < dur) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }


    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (null == layoutParams)
            return;
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 判断是否是手机号码
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber){
        // 暂时只判断是一个11位数字
        if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(phoneNumber);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 缓存的uuid
     */
    private static String myUUID;

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     *  完整的判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Java文件操作 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 保存错误日志信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public static void saveLogInfo2File(String message) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String date = "异常时间:"+format.format(new Date(System.currentTimeMillis()))+"\r\n";
        LogUtil.e("error",date+" --------------------未自动捕获的异常-------------------------");
//        StringWriter stackTrace = new StringWriter();
//        ex.printStackTrace(new PrintWriter(stackTrace));
//		LogUtil.i("error",stackTrace.toString());
//        LogUtil.e("error",stackTrace.toString());


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
            is = new ByteArrayInputStream(message.getBytes());
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

//                String crashString = CommonUtil.txt2String(logFile);


            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    result.append(System.lineSeparator() + s);
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 调用第三方浏览器打开
     * @param context
     * @param url 要浏览的资源地址
     */
    public static  void openBrowser(Context context,String url){
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
            LogUtil.i("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        long timeStamp = System.currentTimeMillis();
        Date date = new Date(timeStamp);
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return rannum + str;// 当前时间
    }
}
