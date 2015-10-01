package edu.ricky.mada2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;
import edu.ricky.mada2.R;
import edu.ricky.mada2.controller.EventActivity;
import edu.ricky.mada2.controller.MovieDetailController;

import static android.widget.Button.*;

public class MovieActivity extends AppCompatActivity implements ProgressDialogActivity{
    Toolbar mToolbar;
    CircleButton mSaveMovieButton;
    CircleButton mAddEventButton;
    RatingBar mRatingBar;
    MovieDetailController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        // Ensure toolbar is assigned to current activity
        mToolbar = (Toolbar) findViewById(R.id.toolbar_movie);
        mSaveMovieButton = (CircleButton) findViewById(R.id.save_movie_button);
        mAddEventButton = (CircleButton) findViewById(R.id.add_event_button);
        mRatingBar = (RatingBar) findViewById(R.id.toolbar_movie_ratingBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSaveMovieButton.setOnClickListener(saveButtonOnclickListener);
        mAddEventButton.setOnClickListener(addEventOnclickListener);
        mRatingBar.setOnRatingBarChangeListener(mOnRatingBarChangeListener);
        // Get Intent Bundle of Movie ID
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mID = extras.getString("id");
            String mTitle = extras.getString("title");
            mController = new MovieDetailController(this);
            if(mID != null) {
                mController.loadMovieByID(mID, true);
            } else if(mTitle != null) {
                mController.loadMovieByTitle(mTitle, true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private OnClickListener saveButtonOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mController.saveMovie();
        }
    };

    private OnClickListener addEventOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mController.addEvent();
        }
    };

    private RatingBar.OnRatingBarChangeListener mOnRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            mController.saveMovie();
        }
    };

    @Override
    public void showProgressdialog(String str) {

    }

    @Override
    public void dismissProgressdialog() {

    }
}
