package youkagames.com.yokaasset.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import yokastore.youkagames.com.support.utils.LogUtil;

/**
 * Created by songdehua on 2018/11/23.
 */


public class FileUtils {

    public static String SDPATH = PathUtils.getBaseSDpath();

    public static File getDiskCacheDir(Context context, String uniqueName) {
        File file = null;
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if(context.getExternalCacheDir() != null){
                cachePath = context.getExternalCacheDir().getPath(); //路径为:/mnt/sdcard//Android/data/< package name >/cach/…
            }else{
                try {
                    cachePath  = Environment.getExternalStorageDirectory().getPath();   //SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
                }catch (Exception e){
                    cachePath = context.getCacheDir().getPath();  	//路径是:/data/data/< package name >/cach/…
                }
            }
        } else {
            cachePath = context.getCacheDir().getPath();  	//路径是:/data/data/< package name >/cach/…
        }
        file = new File(cachePath + File.separator + uniqueName);
        if(file.exists()){
            return file;
        }else {
            if(file.mkdir()){
                return file;
            }
        }
        return file;
    }


    public static String saveBitmap(Bitmap bm, String picName) {
        File tempf = null;
        LogUtil.e("youkagames", "保存图片");
        try {
            if (!isFileExist("")) {
                File f  = createSDDir("");
            }
            tempf = new File(SDPATH, picName + ".JPEG");
            if (tempf.exists()) {
                tempf.delete();
            }
            FileOutputStream out = new FileOutputStream(tempf);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            LogUtil.e("youkagames", "已经保存:" +tempf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempf.toString();
    }

    public static void copyFile(File source, File target) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (source.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(source); //读入原文件
                FileOutputStream fs = new FileOutputStream(target);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    /**
     * 复制文件夹
     * @param sourceDir
     * @param targetDir
     */
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile=file[i];
                // 目标文件
                File targetFile=new
                        File(new File(targetDir).getAbsolutePath()
                        + File.separator+file[i].getName());
                copyFile(sourceFile,targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1=sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2=targetDir + "/"+ file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }


    public static void deleteDir(File folder) {
        if (folder == null || !folder.exists() || !folder.isDirectory())
            return;

        for (File file : folder.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file); // 递规的方式删除文件夹
        }
//        folder.delete();// 删除目录本身
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 默认不裁剪一半的方法
     * @param zipFile
     * @param folderPath
     * @throws IOException
     */
    public static void unZipFile(File zipFile, String folderPath) throws IOException {
        unZipFile(zipFile , folderPath , false);
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile 压缩文件
     * @param folderPath 解压缩的目标目录
     * @param cutHalf 是否裁剪掉一半内容
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void unZipFile(File zipFile, String folderPath , boolean cutHalf) throws IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        LogUtil.w("youkagames", zipFile.getPath());
        ZipFile zf = new ZipFile(zipFile);
        int i = 0;
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); i ++) {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            if(cutHalf && i % 2 == 1){
                // 裁剪一半
                continue ;
            }
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }

    public static File zipFiles(Context context, List<String> files, boolean digitalize, File dir) {
        File output = null;
        try {
            if(dir != null && dir.exists()) {
                output = new File(dir , getRandomFileName());
            }else{
                output = new File(context.getCacheDir(), getRandomFileName());
            }

            if(output.exists()) {
                output.delete();
            }
            output.createNewFile();
            ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(output), 1024));
            int len = files.size();
            for(int i=0; i < len; ++i) {
                File file = new File(files.get(i));
                String entryName = digitalize ? String.valueOf(i) : "";
                zipFile(file, zipout, "", entryName);
            }
            zipout.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 压缩文件
     *
     * @param resFile 需要压缩的文件（夹）
     * @param zipout 压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFile(File resFile, ZipOutputStream zipout, String rootpath, String entryName)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + (entryName == "" ? resFile.getName() : entryName);
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath, "");
            }
        } else {
            byte buffer[] = new byte[1024];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    1024);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }

    public static String getRandomFileName() {
        String n = Md5Utils.encrypt(String.valueOf(Math.random()));
        return n;
    }

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     * @param fileName 文件名称
     * @param bitmap 图片
     * @return
     */
    public static String saveJpegFile(String fileName, Bitmap bitmap) {
        return saveJpegFile("", fileName, bitmap);
    }

    public static String saveJpegFile(String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToJpegBytes(bitmap);
        return saveFile(filePath, fileName, bytes);
    }

    public static String savePngFile(String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToPngBytes(bitmap);
        return saveFile(filePath, fileName, bytes);
    }

    public static byte[] bitmapToPngBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 80, baos);
        return baos.toByteArray();
    }

    public static byte[] bitmapToJpegBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    public static String saveFile(String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/JiaXT/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName);
            fileFullName = fullFile.getPath();
            if(fullFile.exists() == false) {
                fullFile.createNewFile();
            }
            fos = new FileOutputStream(fullFile);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 移动一个文件
     * @param sourceFilePath
     * @param targetFilePath
     * @return
     */
    public static boolean moveFile(String sourceFilePath , String targetFilePath){
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        if(!sourceFile.exists()){
            return false;
        }
        targetFile.delete();
        try {
            targetFile.createNewFile();
            return moveFile(new FileInputStream(sourceFile) , new FileOutputStream(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移动文件
     * @param is
     * @param os
     * @return
     */
    public static boolean moveFile(InputStream is , OutputStream os){
        try {
//            FileInputStream fis = (FileInputStream) is;
//            FileOutputStream fos = (FileOutputStream) os;
//            FileChannel fisChannel = fis.getChannel();
//            FileChannel fosChannel = fos.getChannel();
//            fisChannel.transferTo(0 , fisChannel.size() , fosChannel);
            byte[] buffer = new byte[1024];
            int size = 0;
            while(true){
                size = is.read(buffer);
                if(size > 0){
                    os.write(buffer , 0 , size);
                }else{
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static final String VIDEO_PATH_NAME = "videorecord";


    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }
}
