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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.*;
import edu.ricky.mada2.controller.MainRecyclerViewAdapter;
import edu.ricky.mada2.model.DbModel;
import edu.ricky.mada2.model.Movie;


public class MainActivity extends AppCompatActivity implements MovielistFragment.OnFragmentInteractionListener{

    private Toolbar mToolbar;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private MovielistFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initial DbModel with application context
        // DbModel.getSingleton(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        initToolbar();
        initDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.vNavigation);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        mListFragment = new MovielistFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, mListFragment);
        ft.commit();
        setupNavigationDrawerContent(navigationView);
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListFragment.reloadDataset();
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

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

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
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                        doSearch(v.getText().toString());
                        v.setText("");
                    }

                    return (true);
                }
            });

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

    @Override
    protected void onStop() {
        mListFragment.releaseResource();
        super.onStop();
    }

    private void doSearch(String movieTitle) {
        Intent intent = new Intent(getBaseContext(), MovieActivity.class);
        Toast.makeText(getApplicationContext(), movieTitle,
                Toast.LENGTH_SHORT).show();
        intent.putExtra("title", movieTitle);
        startActivity(intent);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_my_movies:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_my_events:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
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
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_logout:
                                menuItem.setChecked(true);
                                show(menuItem.getTitle().toString());
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
}
