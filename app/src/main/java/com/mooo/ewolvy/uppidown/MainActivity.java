package com.mooo.ewolvy.uppidown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity{

    Spinner spinner;
    int selectedAA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Activity specific options (spinner selected item)
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedItem = sharedPrefs.getInt("selectedItem", 0);

        // Setup the spinner
        spinner = (Spinner) findViewById(R.id.aaSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.AAArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedItem);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // If position was changed replace the fragment
                if (position != selectedAA){
                    ControlsFragment newFragment = new ControlsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainLayout, newFragment);
                    transaction.commit();
                    selectedAA = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        if (findViewById(R.id.mainLayout) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ControlsFragment controlsFragment = new ControlsFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainLayout, controlsFragment).commit();
        }
    }

    // Add function for the layout parameters for the fragment
    public RelativeLayout.LayoutParams fetchLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // We can add any rule available for RelativeLayout and hence can position accordingly
        params.addRule(RelativeLayout.BELOW, spinner.getId());
        return params;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("selectedItem", spinner.getSelectedItemPosition());
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedItem = sharedPrefs.getInt("selectedItem", 0);
        spinner.setSelection(selectedItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, com.mooo.ewolvy.uppidown.SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}