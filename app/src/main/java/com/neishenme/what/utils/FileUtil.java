package com.neishenme.what.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.neishenme.what.activity.EditSelfInfoOldActivity.getDataColumn;
import static com.neishenme.what.activity.EditSelfInfoOldActivity.isDownloadsDocument;
import static com.neishenme.what.activity.EditSelfInfoOldActivity.isExternalStorageDocument;

/**
 * Created by Administrator on 2016/3/26.
 */
public class FileUtil {
    private static String SDCARDPATH = Environment.getExternalStorageDirectory() + "/";

    public static String getSDCARDPATH() {
        return SDCARDPATH;
    }

    /**
     * 针对进行图片 . 视频, 音频的保存目录
     */
    public static final String NSM_FILE = "NSM_file" + File.separator;

    public static final String NSM_PICTURE_FILE = NSM_FILE + "picture";
    public static final String NSM_VIDEO_FILE = NSM_FILE + "video";
    public static final String NSM_AUDIO_FILE = NSM_FILE + "audio";
    public static final String NSM_DOWNLOAD_FILE = NSM_FILE + "download";

    public FileUtil() {
        // 得到手机存储器目录---因为各个厂商的手机SDcard可能不一样，最好不要写死了
        //SDCARDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 在SDcard上创建文件
     *
     * @param fileName
     * @return File
     */
    public static File creatSDFile(String fileName) {
        File file = new File(SDCARDPATH + fileName);
        return file;
    }

    /**
     * 在SDcard上创建目录
     *
     * @param dirName
     */
    public static void createSDDir(String dirName) {
        File file = new File(SDCARDPATH + dirName);
        file.mkdir();
    }

    public static File getSDAudioDir() {
        File file = new File(SDCARDPATH + NSM_AUDIO_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getSDVideoDir() {
        File file = new File(SDCARDPATH + NSM_VIDEO_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getSDPictureDir() {
        File file = new File(SDCARDPATH + NSM_PICTURE_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getSDDownloadDir() {
        File file = new File(SDCARDPATH + NSM_DOWNLOAD_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return boolean
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(SDCARDPATH + fileName);

        Log.d("editSelf", "playvideo判断是否file 存在      " + file.getAbsolutePath());
        return file.exists();
    }

    /**
     * @param path     存放目录
     * @param fileName 文件名字
     * @param input    数据来源
     * @return
     */
    public File writeToSDCard(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            //创建路径
            createSDDir(path);
            //创建文件
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            //以4K为单位，每次写4k
            int i;
            byte buffer[] = new byte[4 * 1024];
            while ((i = input.read(buffer)) != -1) {
                output.write(buffer, 0, i);
            }
            // 清除缓存
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public File writeToDestinaton(String destination, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = new File(destination);
            output = new FileOutputStream(file);
            //以4K为单位，每次写4k
            int i;
            byte buffer[] = new byte[4 * 1024];
            while ((i = input.read(buffer)) != -1) {
                output.write(buffer, 0, i);
            }
            // 清除缓存
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 通过界面和 图片的uri获取图片的绝对路径
     * @param context
     * @param imageUri
     * @return
     */
    @TargetApi(19)
    public static String getRealFilePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
