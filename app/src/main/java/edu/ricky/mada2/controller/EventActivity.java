package edu.ricky.mada2.controller;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ricky.mada2.MainActivity;
import edu.ricky.mada2.MapsActivity;
import edu.ricky.mada2.MovieGangApp;
import edu.ricky.mada2.R;
import edu.ricky.mada2.model.DbModel;
import edu.ricky.mada2.model.Event;
import edu.ricky.mada2.model.EventModel;
import edu.ricky.mada2.model.Invitee;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;

public class EventActivity extends ActionBarActivity {
    // Request codes
    private final static int PICK_REQUEST = 1337; // Contact picker activity
    private final static int MAP_REQUEST = 1338;  // Map activity

    private static final Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final Uri EURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private static final String ID = ContactsContract.Contacts._ID;
    private static final String CID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private static final String EID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    private static final String PNUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;
    private static final String DNAME = ContactsContract.Contacts.DISPLAY_NAME;
    // Init content_uri for contact picker
    private static Uri CONTENT_URI = null;

    static {
        int sdk = new Integer(Build.VERSION.SDK).intValue();

        if (sdk >= 5) {
            try {
                Class<?> clazz = Class.forName("android.provider.ContactsContract$Contacts");

                CONTENT_URI = (Uri) clazz.getField("CONTENT_URI").get(clazz);
            } catch (Throwable t) {
                Log.e("Contact Picker", "Exception when determining CONTENT_URI", t);
            }
        } else {
            CONTENT_URI = Contacts.People.CONTENT_URI;
        }
    }

    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private ImageView mPoster;
    private TextView mTitle;
    private Button mSaveButton;
    private Button mAddInvitee;
    private Button mListInvitee;
    private MaterialEditText mDatetime, mVenue, mName, mLoc;
    private MovieModel mModel;
    private EventModel eModel;
    private DbModel db;
    private Movie movie;
    private Event event;
    // Temp arraylist to put invitees in
    private HashMap<String, Invitee> invitees = new HashMap<>();
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
            if (hasFocus) openMap();
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

    private Button.OnClickListener inviteButtonOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            addInvitee();
        }
    };

    private Button.OnClickListener listButtonOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            listInvitee();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mModel = MovieModel.getSingleton();
        eModel = EventModel.getSingleton();
        db = DbModel.getSingleton(getApplicationContext());
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

    /**
     * @param reqCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case PICK_REQUEST:
                if (resultCode == RESULT_OK) {
                    handleAddInvitee(data);
                }
                break;
            case MAP_REQUEST:
                handleSetVenue(data);

                break;
            default:
                break;
        }
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

                    }
                }, mYear, mMonth, mDay);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                // Cancel code here
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
        mAddInvitee = (Button) findViewById(R.id.invite_button);
        mListInvitee = (Button) findViewById(R.id.list_invitee_button);
    }

    private void parseDataToViews() {
        if (event != null) {
            mName.setText(event.getName());
            mDatetime.setText(sdf.format(event.getEventDate()));
            mVenue.setText(event.getVenue());
            mLoc.setText(event.getLocation().toString());
            parseInviteesJsonString(event.getInvitees());
            Log.e("EventActivity", event.toString());
        }
        mTitle.setText(String.format("%s (%s)", movie.getTitle(), movie.getYear()));
        updateListInviteeButton();
    }

    private void setListeners() {
        enableSaveButton();
        mSaveButton.setOnClickListener(saveButtonOnClickListener);
        mAddInvitee.setOnClickListener(inviteButtonOnClickListener);
        mListInvitee.setOnClickListener(listButtonOnClickListener);
        mName.addTextChangedListener(textWatcher);
        mVenue.addTextChangedListener(textWatcher);
        mVenue.setOnFocusChangeListener(vEditOnfocusChangeListener);
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
        if (mName.getText().length() == 0 ||
                mDatetime.getText().length() == 0 ||
                mVenue.getText().length() == 0 ||
                mLoc.getText().length() == 0)
            return false;
        return true;
    }

    public void openMap() {
        if (((MovieGangApp) getApplication()).isConnected()) {
            Intent intent = new Intent(this.getBaseContext(), MapsActivity.class);
            startActivityForResult(intent, MAP_REQUEST);
        }
    }

    public void saveEvent() {

        String name = mName.getText().toString();
        String dates = mDatetime.getText().toString();
        String venue = mVenue.getText().toString();
        String loc = mLoc.getText().toString();
        Date date = null;
        JSONArray tempInviteesArray = new JSONArray(invitees.values());
        try {
            date = sdf.parse(dates);
        } catch (ParseException e) {
            Log.e("saveEvent", "ParseDate failed");
        }
        try {
            // Handle updating an event
            if (event != null) {
                eModel.updateEvent(event, name, date, venue, loc, movie.getImdbId(), getInviteesJsonString());
            } else {
                // Handle adding a new event
                event = eModel.addEvent(name, date, venue, loc, movie.getImdbId(), getInviteesJsonString());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.WRONG_LOCATION_FORMAT),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        onDatasetChanged();
        backToMainActivity();
    }

    private void onDatasetChanged() {
        db.saveAllEvents(eModel.getEventMap());
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
            if (mID != null)
                movie = mModel.getMovieById(mID);
            else if (eID != null) {
                event = eModel.getEventById(eID);
                movie = mModel.getMovieById(event.getMovieID());
            }

        }
    }

    private void addInvitee() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_REQUEST);
    }

    private void listInvitee() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        ArrayList<Invitee> tempAl = new ArrayList<>(invitees.values());
        alt_bld.setTitle("Invitees");
        CharSequence[] digitList = new CharSequence[tempAl.size()];
        for (int i = 0; i < tempAl.size(); i++) {
            digitList[i] = tempAl.get(i).getName();
        }

        alt_bld.setMultiChoiceItems(digitList, new boolean[]{false, true,
                        false},
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton, boolean isChecked) {

                    }
                });
        alt_bld.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListView list = ((AlertDialog) dialog).getListView();

                for (int i = 0; i < list.getCount(); i++) {
                    boolean checked = list.isItemChecked(i);

                    if (checked) {
                        invitees.remove(list.getItemAtPosition(i).toString());
                        updateListInviteeButton();
                    }
                }
            }
        });
        alt_bld.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void handleSetVenue(Intent data) {
        Bundle results = data.getExtras();
        if (results != null) {
            mVenue.setText(results.getString("venue"));
            mLoc.setText(results.getString("loc"));
        }
    }

    private void handleAddInvitee(Intent data) {

                /*startActivity(new Intent(Intent.ACTION_VIEW,
                        data.getData()));*/

        Uri contactData = data.getData();
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(contactData, null, null, null, null);


        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndex(ID));
            String name = c.getString(c.getColumnIndex(DNAME));
            String phone = "";
            String email = "";
            Cursor pCur = cr.query(PURI, null, CID + " = ?", new String[]{id}, null);
            if (pCur.moveToFirst()) {
                phone = pCur.getString(pCur.getColumnIndex(PNUM));
            }
            pCur.close();

            Cursor emailCur = cr.query(EURI, null, EID + " = ?", new String[]{id}, null);
            if (emailCur.moveToFirst()) {
                email = emailCur.getString(emailCur.getColumnIndex(EMAIL));
            }
            emailCur.close();
            Invitee i = new Invitee(name, phone, email);
            invitees.put(i.getName(), i);
            updateListInviteeButton();
        }
    }

    private String getInviteesJsonString() {
        int i = 0;
        String s;
        JSONArray jsonArray = new JSONArray();
        for (Invitee in : invitees.values()) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("name", in.getName());
            hm.put("email", in.getEmail());
            hm.put("phone", in.getPhone());
            jsonArray.put(new JSONObject(hm));
        }
        Log.e("jsonArray", jsonArray.toString());
        return jsonArray.toString();
    }

    private void parseInviteesJsonString(String jsonArrayString) {
        JSONArray jsonArray;
        try {
            Log.e("parseInviteesJsonString", jsonArrayString);
            jsonArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < jsonArray.length(); i++) {
                Invitee inv = new Invitee(jsonArray.getJSONObject(i));
                invitees.put(inv.getName(), inv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateListInviteeButton() {
        mListInvitee.setText("+" + invitees.size());
    }
}
