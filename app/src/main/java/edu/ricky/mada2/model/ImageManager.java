package edu.ricky.mada2.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Map;

import edu.ricky.mada2.ProgressDialogActivity;
import edu.ricky.mada2.utility.BoundedLruCache;

/**
 * Created by Ricky Wu on 2015/10/1.
 */
public class ImageManager {
    private Map<String, Bitmap> imageMap;

    public ImageManager() {
        this.imageMap = new BoundedLruCache<>(10);
    }

    // Model Access
    public Bitmap getImgByUrl(String imdbId, ProgressDialogActivity activity)
    {
        return null;
    }

    public void insertImg(String url, Bitmap bmp) {
        imageMap.put(url, bmp);
    }
}
