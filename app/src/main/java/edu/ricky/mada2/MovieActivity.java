package edu.ricky.mada2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.markushi.ui.CircleButton;
import edu.ricky.mada2.R;
import edu.ricky.mada2.controller.MovieDetailController;

import static android.widget.Button.*;

public class MovieActivity extends AppCompatActivity {
    Toolbar mToolbar;
    CircleButton mSaveMovieButton;
    MovieDetailController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        // Ensure toolbar is assigned to current activity
        mToolbar = (Toolbar) findViewById(R.id.toolbar_movie);
        mSaveMovieButton = (CircleButton) findViewById(R.id.save_movie_button);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSaveMovieButton.setOnClickListener(saveButtonOnclickListener);
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
}
