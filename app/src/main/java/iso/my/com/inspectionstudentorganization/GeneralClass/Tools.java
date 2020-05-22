package iso.my.com.inspectionstudentorganization.GeneralClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {

    public static File generatePicturePath(Context context) {
        try {
            File storageDir = getAlbumDir(context);
            Date date = new Date();
            date.setTime(Long.parseLong(System.currentTimeMillis() + randomInteger(4)));
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File getAlbumDir(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getCacheDir(context);
        }
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
        }

        return storageDir;
    }

    public static String randomInteger(int len) {
        Random random = new Random();
        double doublenumber = random.nextDouble();
        double tavan = Math.pow(10, len);
        int randomnum = (int) (doublenumber * tavan);
        return "" + randomnum;
    }

    public static File getCacheDir(Context context) {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            try {
                File file = context.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File file = context.getCacheDir();
            if (file != null) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File("");
    }

    public static boolean isempty(String string) {
        return string == null || string.equals("") || string.equalsIgnoreCase("null");
    }

    public static void setFullImageFromFilePath(ImageView imageview, String path) {
        Bitmap myBitmap = BitmapFactory.decodeFile(path);

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            imageview.setImageBitmap(myBitmap);
        } catch (Exception e) {

        }

    }


}
