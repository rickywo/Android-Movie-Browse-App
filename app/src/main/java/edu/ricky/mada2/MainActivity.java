package edu.ricky.mada2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.ricky.mada2.model.User;
import edu.ricky.mada2.utility.*;

public class MainActivity extends AppCompatActivity implements
        MovielistFragment.OnFragmentInteractionListener,
        EventlistFragment.OnFragmentInteractionListener {
    public static final int MOVIE_FRAGMENT = 0;
    public static final int EVENT_FRAGMENT = 1;
    // Indicates the state of search component is currently enabled/disabled
    private boolean isSearchOpened = false;
    // Flag of show/hide search component
    private boolean showSearch = true;
    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private EditText edtSeach;
    private TextView mEmailTextView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    // Check for internet connection
    private NetworkStateManager netWorkStateManager;
    // Movie List Fragment
    private MovielistFragment mListFragment = new MovielistFragment();
    // Event List Fragment
    private EventlistFragment eListFragment = new EventlistFragment();

    private TextView.OnEditorActionListener mSearchActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                doSearch(v.getText().toString());
                v.setText("");
            }

            return (true);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initial DbModel with application context
        // DbModel.getSingleton(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        // Start thread for checking internet connection
        ((MovieGangApp)this.getApplication()).initStateManager(getApplicationContext());
        initToolbar();
        initDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.vNavigation);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mListFragment =  MovielistFragment.newInstance();
        eListFragment = EventlistFragment.newInstance();
        handleBundleExtras();
        setupNavigationDrawerContent(navigationView);
        Log.e("MAD", "onCreate");
    }

    // Reload dataset from models and redraw the list
    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
        Log.e("MAD", "onResume");
        try {
            mListFragment.reloadDataset();
            eListFragment.reloadDataset();
        } catch (Exception e){

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        mSearchAction.setVisible(showSearch);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Handle searching movie behaviour
     */
    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar
        if (!((MovieGangApp) getApplication()).isConnected()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.NO_OMDB_SEARCH_FUNCTION),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.mipmap.ic_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(mSearchActionListener);

            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.mipmap.ic_close_white_24dp));

            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    /**
     * Override onStop method.
     * release DB resources and save data (Movies and Events)
     * to sqlite db permanently.
     */
    @Override
    protected void onStop() {
        try {
            mListFragment.releaseResource();
            eListFragment.releaseResource();
        } catch (Exception e) {

        }
        super.onStop();
    }

    /**
     * doSearch
     * param movieTitle: movie name string
     * TODO: send input string from searching bar to MovieActivity
     */

    private void doSearch(String movieTitle) {
        Intent intent = new Intent(getBaseContext(), MovieActivity.class);
        intent.putExtra("title", movieTitle);
        startActivity(intent);
    }
    /**
     * Initial drawer layout
    */
    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mEmailTextView = (TextView) findViewById(R.id.email);
    }

    /**
     * Initial toolbar layout
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Setup listener for drawer menu
     * R.id.menu_my_movie: goto MovielistFragment
     * R.id.menu_my_event: goto EventlistFragment
     * R.id.menu_invitation:
     * R.id.menu_notifications
     * R.id.menu_login: goto LoginActivity
     * R.id.menu_logout: logout user
     */
    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            //
                            case R.id.menu_my_movies:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                switchFragment(0);
                                // Show search bar
                                mSearchAction.setVisible(true);
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_my_events:
                                menuItem.setChecked(showSearch);
                                show(menuItem.getTitle().toString());
                                switchFragment(1);
                                // Hide search bar
                                mSearchAction.setVisible(false);
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_invitations:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_notifications:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_login:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                openLoginActivity();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_logout:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                logout();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void show(String s) {
        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void handleBundleExtras() {
        int frag_index = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // After add/or update an event
            frag_index = extras.getInt("frag_index");
            showSearch = false;
        } else {
            showSearch = true;
        }
        switchFragment(frag_index);
    }

    private void switchFragment(int fragment_index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(fragment_index) {
            case 0:
                ft.replace(R.id.fragment_placeholder, mListFragment);
                break;
            case 1:
                ft.replace(R.id.fragment_placeholder, eListFragment);
                break;
            default:
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this.getBaseContext(), LoginActivity.class);
        this.startActivity(intent);
    }

    private void updateUserInfo() {
        if(((MovieGangApp) getApplication()).isLoginState()) {
            mEmailTextView.setText(((MovieGangApp) getApplication()).getCurrentUser().username);
        } else {
            mEmailTextView.setText(R.string.anonymous);
        }
    }

    private void logout() {
        ((MovieGangApp) getApplication()).logout();
        updateUserInfo();
    }
}
//