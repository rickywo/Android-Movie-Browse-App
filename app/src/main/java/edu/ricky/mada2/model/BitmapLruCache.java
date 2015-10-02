package edu.ricky.mada2.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ricky.mada2.R;

/**
 * Created by Ricky Wu on 2015/10/2.
 */
public class BitmapLruCache {
    private static BitmapLruCache singleton = null;

    public static BitmapLruCache getSingleton()
    {
        if(singleton == null)
            singleton = new BitmapLruCache();
        return singleton;
    }
    private LruCache<String, Bitmap> mMemoryCache;
    private DbModel db;



    private BitmapLruCache() {
        db = DbModel.getSingleton();
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String url, ImageView mImageView) {
        final String imageKey = url;

        Bitmap bitmap ;
        if ((bitmap= getBitmapFromMemCache(imageKey)) != null) {
            Log.d("Load image", "Memory");
            mImageView.setImageBitmap(bitmap);
        } else if ((bitmap = db.getImage(imageKey)) != null) {
            Log.d("Load image", "Sqlite");
            mImageView.setImageBitmap(bitmap);
        } else {
            Log.d("Load image", "Remote");
            mImageView.setImageResource(R.drawable.placeholder);
            ImageDownloaderTask task = new ImageDownloaderTask(mImageView);
            task.execute(imageKey);
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String url;
        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        addBitmapToMemoryCache(url, bitmap);
                        db.insertImage(url, bitmap, 15);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
