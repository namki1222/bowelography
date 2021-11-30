package com.rightcode.bowelography.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final String ROTATED_IMAGE_DIR = "rotate";
    private static final String HTML_DIR = "html";

    public static boolean isFileExists(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }

    public static boolean saveFileToCacheDir(Context context, String fileName, byte[] data) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] loadFileFromCacheDir(Context context, String fileName) {
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            byte[] data = new byte[inputStream.available()];
            while (inputStream.read(data) != -1) {
            }
            inputStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveFileToCacheDir(Context context, String fileName, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(object);
            os.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object loadObjectFileFromCacheDir(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object object = is.readObject();
            is.close();
            fis.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File saveImageFileToExternalFilesDir(Context context, String fileName, File originalFile) {
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            FileInputStream fis = new FileInputStream(originalFile);
            FileOutputStream newfos = new FileOutputStream(file);
            int readcount = 0;
            byte[] buffer = new byte[1024];
            while ((readcount = fis.read(buffer, 0, 1024)) != -1) {
                newfos.write(buffer, 0, readcount);
            }
            newfos.close();
            fis.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteFile(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName).delete();
    }

    public static boolean deleteAllFiles(File file) {
//        Log.d(">> deleteAllFiles( filepath : %s )", file.getAbsolutePath());

        if (file.exists() && file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteAllFiles(child);
            }
        }
        return file.delete();
    }

    public static File saveBitmapToCacheDir(Context context, String fileName, Bitmap bitmap) {
        try {
            File file = new File(context.getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void clearRotatedImageTempFile(Context context) {
        deleteAllFiles(new File(context.getCacheDir() + "/" + ROTATED_IMAGE_DIR));
    }


    public static File rotateImageFileToCache(Context context, String filePath) {
        if (filePath.contains("https://") || filePath.contains("http://")) {
            return webImage(filePath);
        }
        File file = new File(filePath.trim());
        try {

            InputStream is = (InputStream) new URL(filePath).getContent();
            ExifInterface exif = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                exif = new ExifInterface(is);
            } else {
                exif = new ExifInterface(filePath);
            }

            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (rotation != 0f) {
                Matrix matrix = new Matrix();
                matrix.preRotate(exifToDegrees(rotation));

                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                File dir = new File(context.getCacheDir() + "/" + ROTATED_IMAGE_DIR);
                if (!dir.exists() || !dir.isDirectory()) {
                    dir.mkdir();
                }
                file = saveBitmapToCacheDir(context, ROTATED_IMAGE_DIR + "/" + file.getName(), bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }

    public static File webImage(final String filepath) {
        try {
            AsyncTask<String, Void, File> asyncTask = new AsyncTask<String, Void, File>() {
                @Override
                protected File doInBackground(String... url) {

                    String fileName;
                    final String SAVE_FOLDER = "/save_folder";

                    String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
                    File dir = new File(savePath);
                    File file = null;
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    //파일 이름 :날짜_시간
                    Date day = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
                    fileName = String.valueOf(sdf.format(day));

                    //웹 서버 쪽 파일이 있는 경로
                    String fileUrl = filepath;
                    //다운로드 폴더에 동일한 파일명이 존재하는지 확인
                    if (new File(savePath + "/" + fileName).exists() == false) {
                    } else {
                    }

                    String localPath = savePath + "/" + fileName + ".jpg";
                    try {
                        URL imgUrl = new URL(fileUrl);
                        //서버와 접속하는 클라이언트 객체 생성
                        HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                        int len = conn.getContentLength();
                        byte[] tmpByte = new byte[len];
                        //입력 스트림을 구한다
                        InputStream is = conn.getInputStream();
                        file = new File(localPath);
                        //파일 저장 스트림 생성
                        FileOutputStream fos = new FileOutputStream(file);
                        int read;
                        //입력 스트림을 파일로 저장
                        for (; ; ) {
                            read = is.read(tmpByte);
                            if (read <= 0) {
                                break;
                            }
                            fos.write(tmpByte, 0, read); //file 생성
                        }
                        is.close();
                        fos.close();
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return file;
                }
            };

            File file = asyncTask.execute().get();
            return file;
        } catch (Exception e) {
        }

        return new File(filepath);
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static File saveHtmlToCacheDir(Context context, String fileName, String html) {
        String path = context.getCacheDir() + "/" + HTML_DIR;

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }

        File file = null;
        try {
            fileName += ".html";
            file = new File(path + "/" + fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(html.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }

    public static void clearHtmlTempFile(Context context) {
        String path = context.getCacheDir() + "/" + HTML_DIR;
        deleteAllFiles(new File(path));
    }
}