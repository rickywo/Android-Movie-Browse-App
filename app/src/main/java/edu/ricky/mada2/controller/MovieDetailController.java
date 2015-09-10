package edu.ricky.mada2.controller;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import edu.ricky.mada2.MovieActivity;
import edu.ricky.mada2.R;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;
import edu.ricky.mada2.utility.OmdbAsyncTask;

/**
 * Created by Ricky Wu on 2015/9/8.
 */


public class MovieDetailController {
    private MovieModel model;
    private MovieActivity mActivity;
    private TextView mTitle;
    private RatingBar mRatingBar;
    private ImageView mPoster;
    private TextView mPlot;
    private TextView mRated;
    private TextView mRuntime;
    private TextView mLang;
    private TextView mGenre;
    private TextView mDirector;
    private TextView mActors;
    private Movie tempMovie;
    private boolean saveState;

    public MovieDetailController(MovieActivity mActivity) {
        this.mActivity = mActivity;
        model = MovieModel.getSingleton();
        mTitle = (TextView) mActivity.findViewById(R.id.toolbar_movie_title_textview);
        mRatingBar = (RatingBar) mActivity.findViewById(R.id.toolbar_movie_ratingBar);
        mPoster = (ImageView) mActivity.findViewById(R.id.toolbar_movie_poster_imageview);
        mPlot = (TextView) mActivity.findViewById(R.id.movie_activity_plot_textview);
        mRated = (TextView) mActivity.findViewById(R.id.movie_activity_rated_textview);
        mLang = (TextView) mActivity.findViewById(R.id.movie_activity_lang_textview);
        mGenre = (TextView) mActivity.findViewById(R.id.movie_activity_genre_textview);
        mRuntime = (TextView) mActivity.findViewById(R.id.movie_activity_runtime_textview);
        mDirector = (TextView) mActivity.findViewById(R.id.movie_activity_director_textview);
        mActors = (TextView) mActivity.findViewById(R.id.movie_activity_actors_textview);
    }


    public void loadMovieByID(String movieID, boolean longPlot) {
        OmdbAsyncTask task = new OmdbAsyncTask(this, mActivity, 0);
        if(longPlot)
            task.execute("i="+movieID+"&"+"plot=full");
        else
            task.execute("i="+movieID);
    }

    public void loadMovieByTitle(String movieTitle, boolean longPlot) {
        movieTitle = movieTitle.replace(' ','+');
        OmdbAsyncTask task = new OmdbAsyncTask(this, mActivity, 1);
        if(longPlot)
            task.execute("t="+movieTitle+"&"+"plot=full");
        else
            task.execute("t="+movieTitle);
    }

    public void displayMovieDetail() {


        String title;
        title = String.format("%s (%s)", tempMovie.getTitle(), tempMovie.getYear());
        this.mTitle.setText(title);
        this.mRatingBar.setRating(((float) tempMovie.getMyRating()));
        Picasso.with(mActivity).load(tempMovie.getIconUrl()).into(mPoster);
        mRated.setText(tempMovie.getMovieRated());
        mRuntime.setText(tempMovie.getMovieRuntime());
        mLang.setText(tempMovie.getMovieLang());
        mGenre.setText(tempMovie.getGenre());
        mDirector.setText(tempMovie.getMovieDirector());
        mActors.setText(tempMovie.getMovieActors());
        mPlot.setText(tempMovie.getPlot());
    }

    public void saveMovie() {
        saveState = true;
        loadMovieByID(tempMovie.getImdbId(), false);
    }

    public boolean verifyMovieJsonObject(JSONObject jsonObject) {
        if(jsonObject == null) {
            Toast.makeText(mActivity.getApplicationContext(), "Network Problem",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(jsonObject.has("Error")) {
            try {
                Toast.makeText(mActivity.getApplicationContext(), jsonObject.getString("Error"),
                        Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public boolean onLoadingFinished(JSONObject jsonObject) {
        boolean result = true;
        if(!verifyMovieJsonObject(jsonObject)) {
            result = false;
        }

        tempMovie = new Movie(jsonObject);
        if(saveState) {
            if(result)
                model.addMovie(tempMovie);
            else
                Toast.makeText(mActivity.getApplicationContext(), "Save Movie failed",
                        Toast.LENGTH_SHORT).show();
        } else {
            if(result)
                displayMovieDetail();
            else
                mActivity.finish();
        }
        saveState = false;
        return result;
    }
}
