package edu.ricky.mada2.utility;

import android.app.ProgressDialog;
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

import edu.ricky.mada2.MovieActivity;
import edu.ricky.mada2.controller.MovieDetailController;

/**
 * Created by Ricky Wu on 2015/9/9.
 */
public class OmdbAsyncTask extends AsyncTask<String, Void, Void> {
    static String OMDB_URL = "http://www.omdbapi.com/?";
    static String ID_PARA = "i=";
    static String TITLE_PARA = "t=";
    static String SHORT_PLOT = "plot=short";
    static String LONG_PLOT = "plot=full";
    private int mode; // Indicater for retrive movie by Id/ Title
    private ProgressDialog dialog;
    private JSONObject jsonObject;
    private MovieDetailController detailController;


    public OmdbAsyncTask(MovieDetailController mDetailController,MovieActivity activity, int mode) {
        dialog = new ProgressDialog(activity);
        detailController = mDetailController;
        this.mode = mode;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading Movie details, please wait.");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        detailController.onLoadingFinished(jsonObject);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        jsonObject = loadMovieJson(params[0]);
        /*switch(mode) {
            case 0:
                jsonObject = loadMovieJsonByID(params[0]);
                break;
            case 1:
                jsonObject = loadMovieJsonByTitle(params[0]);
                break;
            default:
                break;
        }*/


           // detailController.mPlot.setText(loadMovieJson(""));

        return null;
    }

    public static JSONObject loadMovieJson(String query) {
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