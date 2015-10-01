package edu.ricky.mada2.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ricky.mada2.ProgressDialogActivity;
import edu.ricky.mada2.utility.BoundedLruCache;

/**
 * Created by Ricky Wu on 2015/8/26.
 */
public class MovieModel {
    // Singleton
    private static MovieModel singleton = null;

    // Model
    private Map<String, Movie> movieMap;
    private Context context;
    private DbModel db;

    public static MovieModel getSingleton(Context context)
    {
        if(singleton == null)
            singleton = new MovieModel(context);
        return singleton;
    }



    // Construction
    private MovieModel(Context context)
    {
        this.movieMap = new BoundedLruCache<>(10);
        this.context = context;
        this.db = DbModel.getSingleton(context);
        loadMovies();
    }

    // Model Access
    public Movie getMovieById(String imdbId, Activity activity)
    {
        if(contains(imdbId)) {
            return movieMap.get(imdbId);

        } else if(getMovieFromDBbyID(imdbId)) {
            return movieMap.get(imdbId);
        } else {
            OmdbAsyncTask task = new OmdbAsyncTask(activity);
            task.execute("i=" + imdbId + "&" + "plot=full");
        }
        return null;
    }

    public boolean contains(String imdbId) {
        return this.movieMap.containsKey(imdbId);
    }

    public Map<String, Movie> getMovieMap() {
        return movieMap;
    }

    public List<Movie> getAllMovies()
    {
        ArrayList<Movie> arrayList = new ArrayList(movieMap.values());
        ArrayList<Movie> reverseOrder = new ArrayList<>();
        for(int i = arrayList.size() -1 ; i >= 0 ; i --) {
            reverseOrder.add(arrayList.get(i));
        }
        return reverseOrder;
    }

    public boolean addMovie(JSONObject j) {
        Movie m = new Movie(j);
        db.insertMovie(m);
        this.movieMap.put(m.getImdbId(), m);
        return true;
    }

    public boolean addMovie(Movie movie) {
        if(contains(movie.getImdbId())) {
            return false;
        }
        Log.e("addMovie", movie.toString());
        this.movieMap.put(movie.getImdbId(), movie);
        return true;
    }

    public boolean updateMovie(Movie movie) {
        if(contains(movie.getImdbId())) {
            this.movieMap.remove(movie.getImdbId());
            this.movieMap.put(movie.getImdbId(), movie);
            return true;
        }
        return false;
    }

    public boolean removeMovie(Movie m) {
        boolean result = true;
        if(!movieMap.containsKey(m.getImdbId())) {
            result = false;
        } else {
            movieMap.remove(m.getImdbId());
        }
        return result;
    }

    public int getMovieCount() {
        return movieMap.size();
    }

    private boolean getMovieFromDBbyID(String id) {
        Map<String, String> map = db.getMovieByID(id);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                JSONObject jo = new JSONObject(entry.getValue());
                if(addMovie(jo))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    class OmdbAsyncTask extends AsyncTask<String, Void, Void> {
        final String OMDB_URL = "http://www.omdbapi.com/?";
        private JSONObject jsonObject;
        private ProgressDialogActivity pActivity;


        public OmdbAsyncTask(Activity act) {
            pActivity = (ProgressDialogActivity) act;
        }

        @Override
        protected void onPreExecute() {
            Log.d("preExcute", "Open dialog");
            pActivity.showProgressdialog("Loading details, Please wait...");
        }

        @Override
        protected void onPostExecute(Void result) {
            addMovie(jsonObject);
            pActivity.dismissProgressdialog();
        }

        @Override
        protected Void doInBackground(String... params) {
            jsonObject = loadMovieJson(params[0]);

            return null;
        }

        public JSONObject loadMovieJson(String query) {
            // Making HTTP request
            try {
                URL newurl = new URL(OMDB_URL + query);
                InputStream urlInputStream = newurl.openConnection().getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(urlInputStream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                return new JSONObject(responseStrBuilder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void loadMovies() {
        //TODO: Load movies from Sqlite to Map
        Map<String, String> map = db.getAllMovies();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                JSONObject jo = new JSONObject(entry.getValue());
                Movie m = new Movie(jo);
                addMovie(m);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public void updateMovieOrder() {
        db.saveAllMovies((BoundedLruCache)this.movieMap);
    }

    public void close() {
        updateMovieOrder();
    }
}
