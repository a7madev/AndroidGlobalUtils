package me.a7madev.androidglobalutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class GlobalMediaUtils {

    public static final String TAG = GlobalMediaUtils.class.getSimpleName();

    /**
     * Get image or video thumbnail
     * @param file  Image
     * @return Bitmap bitmap object
     */

    public static Bitmap getMediaThumbnail(File file) {

        Bitmap thumbBitmap;

        thumbBitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);

        if(thumbBitmap == null){
            thumbBitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        }

        if(thumbBitmap == null){
            File image = new File(Uri.fromFile(file).getPath());

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(), bounds);
            if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return null;

            int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                    : bounds.outWidth;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = originalSize / 256;
            return BitmapFactory.decodeFile(image.getPath(), opts);
        }
        return thumbBitmap;
    }
}
