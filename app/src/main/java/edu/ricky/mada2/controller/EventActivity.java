package edu.ricky.mada2.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import edu.ricky.mada2.R;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;

public class EventActivity extends ActionBarActivity {
    private ImageView mPoster;
    private TextView mTitle;
    private MaterialEditText mDatetime;
    private MovieModel mModel;
    private Movie movie;
    private String eName, eDatetime, eVenue, eLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mModel = MovieModel.getSingleton();
        mPoster = (ImageView) findViewById(R.id.toolbar_movie_poster_imageview);
        mTitle = (TextView) findViewById(R.id.toolbar_movie_title_textview);
        mDatetime = (MaterialEditText) findViewById(R.id.event_datetime_edittext);
        mDatetime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showDatePickerDialog();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mID = extras.getString("id");
            movie = mModel.getMovieById(mID);
            mTitle.setText(String.format("%s (%s)", movie.getTitle(), movie.getYear()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog() {

        int mYear, mMonth, mDay;
        // init Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // show date picker dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        eDatetime = String.format("%02d.%02d.%04d", dayOfMonth, (monthOfYear + 1), year);
                        showTimePickerDialog();
                        //setEvent();

                    }
                }, mYear, mMonth, mDay);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                // Cancel code here
                mDatetime.setText("");
                mDatetime.clearFocus();
            }
        });
        dpd.show();
    }

    public void showTimePickerDialog() {
        int mHour, mMinute;
        // init time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // show time picker
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        eDatetime += String.format(" %02d:%02d", hourOfDay, minute);
                        mDatetime.setText(eDatetime);
                        mDatetime.clearFocus();
                        //setEvent();
                    }
                }, mHour, mMinute, false);

        tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                // Cancel code here
                mDatetime.setText("");
                mDatetime.clearFocus();
            }
        });

        tpd.show();
    }
}
