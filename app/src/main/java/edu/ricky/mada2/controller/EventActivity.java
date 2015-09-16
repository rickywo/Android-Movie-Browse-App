package edu.ricky.mada2.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ricky.mada2.MainActivity;
import edu.ricky.mada2.MapsActivity;
import edu.ricky.mada2.R;
import edu.ricky.mada2.model.Event;
import edu.ricky.mada2.model.EventModel;
import edu.ricky.mada2.model.Invitee;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;

public class EventActivity extends ActionBarActivity {
    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private ImageView mPoster;
    private TextView mTitle;
    private Button mSaveButton;
    private MaterialEditText mDatetime, mVenue, mName, mLoc;
    private MovieModel mModel;
    private EventModel eModel;
    private Movie movie;
    private Event event;
    private String eDatetime;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableSaveButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnFocusChangeListener vEditOnfocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if(hasFocus) openMap();
            enableSaveButton();
        }
    };

    private View.OnFocusChangeListener dEditOnfocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                showDatePickerDialog();
            enableSaveButton();
        }
    };

    private View.OnFocusChangeListener lEditOnfocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            enableSaveButton();
        }
    };

    private Button.OnClickListener saveButtonOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveEvent();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mModel = MovieModel.getSingleton();
        eModel = EventModel.getSingleton();
        getViews();
        setListeners();
        handleBundleExtras();
        parseDataToViews();
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
                        //eDate = new Date(year-1900, monthOfYear, dayOfMonth)
                        eDatetime = String.format("%02d.%02d.%04d", dayOfMonth, (monthOfYear + 1), year);
                        showTimePickerDialog();
                        //setEvent();

                    }
                }, mYear, mMonth, mDay);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                // Cancel code here
                //mDatetime.setText("");
                mDatetime.clearFocus();
            }
        });
        dpd.show();
    }

    private void getViews() {
        mPoster = (ImageView) findViewById(R.id.toolbar_movie_poster_imageview);
        mTitle = (TextView) findViewById(R.id.toolbar_movie_title_textview);
        mName = (MaterialEditText) findViewById(R.id.event_name_edittext);
        mVenue = (MaterialEditText) findViewById(R.id.event_venue_edittext);
        mDatetime = (MaterialEditText) findViewById(R.id.event_datetime_edittext);
        mLoc = (MaterialEditText) findViewById(R.id.event_loc_edittext);
        mSaveButton = (Button) findViewById(R.id.save_event_button);
    }

    private void parseDataToViews() {
        if(event!=null) {
            mName.setText(event.getName());
            mDatetime.setText(sdf.format(event.getEventDate()));
            mVenue.setText(event.getVenue());
            mLoc.setText(event.getLocation().toString());
        }
        mTitle.setText(String.format("%s (%s)", movie.getTitle(), movie.getYear()));
    }

    private void setListeners() {
        enableSaveButton();
        mSaveButton.setOnClickListener(saveButtonOnClickListener);
        mName.addTextChangedListener(textWatcher);
        mVenue.addTextChangedListener(textWatcher);
        mDatetime.setOnFocusChangeListener(dEditOnfocusChangeListener);
        mLoc.addTextChangedListener(textWatcher);
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
                // mDatetime.setText("");
                mDatetime.clearFocus();
            }
        });

        tpd.show();
    }

    private void enableSaveButton() {
        mSaveButton.setVisibility(validateEditFields() ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean validateEditFields() {
        if(mName.getText().length()==0 ||
                mDatetime.getText().length()==0 ||
                mVenue.getText().length()==0 ||
                mLoc.getText().length()==0 )
            return false;
        return true;
    }

    public void openMap() {
        Intent intent = new Intent(this.getBaseContext(), MapsActivity.class);
        this.startActivity(intent);
    }

    public void saveEvent() {


        ArrayList<Invitee> ai = new ArrayList<>();
        String name = mName.getText().toString();
        String dates = mDatetime.getText().toString();
        String venue = mVenue.getText().toString();
        String loc = mLoc.getText().toString();
        Date date = null;
        try {
            date = sdf.parse(dates);
            Log.e("MAD", "ParseDate correctly");
        } catch (ParseException e) {
            Log.e("MAD", "ParseDate failed");
        }
        // Handle updating an event
        if(event != null) {
            eModel.updateEvent(event, name, date, venue, "-36.4266534,145.23292019999997", movie.getImdbId(), ai);
        } else {
            // Handle adding a new event
            String id = eModel.addEvent(name, date, venue, "-36.4266534,145.23292019999997", movie.getImdbId(), ai);
        }
        backToMainActivity();
    }

    private void backToMainActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("frag_index", MainActivity.EVENT_FRAGMENT);
        startActivity(intent);
    }

    private void handleBundleExtras() {
        event = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mID = extras.getString("movieID");
            String eID = extras.getString("eventID");
            if(mID != null)
                movie = mModel.getMovieById(mID);
            else if (eID != null) {
                event = eModel.getEventById(eID);
                movie = mModel.getMovieById(event.getMovieID());
            }

        }
    }
}
