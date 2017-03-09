package com.mooo.ewolvy.uppidown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import com.mooo.ewolvy.uppidown.AARemotes.*;

public class MainActivity extends AppCompatActivity{

    AASuper state;
    SSLServer myServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read the preferences or set default parameters
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String address = sharedPrefs.getString(getString(R.string.settings_address_key), "");
        String port_str = sharedPrefs.getString(getString(R.string.settings_port_key), "0");
        String username = sharedPrefs.getString(getString(R.string.settings_username_key), "");
        String password = sharedPrefs.getString(getString(R.string.settings_password_key), "");
        int port;
        try {
            port = Integer.parseInt(port_str);
        } catch(NumberFormatException nfe) {
            port = 0;
        }

        // If preferences are not set ask the user to set them, else create the SSLServer object to manage it
        if (Objects.equals(address, "") || port == 0 || Objects.equals(username, "") || Objects.equals(password, "")) {
            myServer = null;
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }else {
            myServer = new SSLServer(address, port, username, password);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("mode", state.getMode());
        editor.putInt("fan", state.getFan());
        editor.putInt("temperature", state.getCurrentTemp());
        editor.putBoolean("on", state.getIsOn());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int mode = sharedPrefs.getInt("mode", 99);
        int fan = sharedPrefs.getInt("fan", 99);
        int temperature = sharedPrefs.getInt("temperature", 99);
        boolean isOn = sharedPrefs.getBoolean("on", false);

        // Create AAKaysun object to manage the AA with the preferences (if there was no preference, it will go to default)
        state = new AAKaysun(mode,
                fan,
                temperature,
                isOn);
        updateView();
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

    public void modeClick(View view) {
        View modeView;
        int actualMode = state.getMode();
        int nextMode = state.getNextMode(actualMode);

        if (nextMode != AASuper.NONEXSISTING_MODE) {
            switch (actualMode) {
                case AASuper.AUTO_MODE:
                    modeView = findViewById(R.id.autoMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.COOL_MODE:
                    modeView = findViewById(R.id.coolMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.DRY_MODE:
                    modeView = findViewById(R.id.dryMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.HEAT_MODE:
                    modeView = findViewById(R.id.heatMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.FAN_MODE:
                    modeView = findViewById(R.id.fanMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            switch (nextMode) {
                case AASuper.AUTO_MODE:
                    modeView = findViewById(R.id.autoMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.COOL_MODE:
                    modeView = findViewById(R.id.coolMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.DRY_MODE:
                    modeView = findViewById(R.id.dryMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.HEAT_MODE:
                    modeView = findViewById(R.id.heatMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.FAN_MODE:
                    modeView = findViewById(R.id.fanMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            state.setMode(nextMode);
        }
    }

    public void fanClick(View view) {
        if (!state.isActiveFan()){return;}
        TextView fanView;
        switch (state.getFan()) {
            case AASuper.AUTO_FAN:
                fanView = (TextView) findViewById(R.id.fanLevelAuto);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) findViewById(R.id.fanLevel1);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL1_FAN);
                }
                break;

            case AASuper.LEVEL1_FAN:
                fanView = (TextView) findViewById(R.id.fanLevel2);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL2_FAN);
                }
                break;

            case AASuper.LEVEL2_FAN:
                fanView = (TextView) findViewById(R.id.fanLevel3);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL3_FAN);
                }
                break;

            case AASuper.LEVEL3_FAN:
                fanView = (TextView) findViewById(R.id.fanLevel1);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) findViewById(R.id.fanLevel2);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) findViewById(R.id.fanLevel3);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) findViewById(R.id.fanLevelAuto);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.AUTO_FAN);
                }
                break;
        }
    }

    public void tempMinusClick(View view) {
        if (state.getCurrentTemp() > state.TEMP_MIN && state.isActiveTemp()){
            state.setMinusTemp();
            TextView tempView = (TextView) findViewById(R.id.tempView);
            String temperature = Integer.toString(state.getCurrentTemp());
            if (tempView != null) tempView.setText(temperature);
        }
    }

    public void tempPlusClick(View view) {
        if (state.getCurrentTemp() < state.TEMP_MAX && state.isActiveTemp()){
            state.setPlusTemp();
            TextView tempView = (TextView) findViewById(R.id.tempView);
            String temperature = Integer.toString(state.getCurrentTemp());
            if (tempView != null) tempView.setText(temperature);
        }
    }

    public void offClick(View view) {
        if (myServer != null){
            // Send the PowerOff code.
            myServer.sendCode (state.getPowerOff(), getApplicationContext(), state, (ImageView) findViewById(R.id.onOffSign));
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void sendClick(View view) {
        if (myServer != null){      // If the options were set, send the command, else ask the user to fullfill the settings
            myServer.sendCode (state.getCommand(), getApplicationContext(), state, (ImageView) findViewById(R.id.onOffSign));
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void swingClick(View view) {
        if (state.getIsOn()) {      // If the system is on check to send command, else warn the user to switch on before activating / deactivating swing
            if (myServer != null){  // If the options were set, send the swing command, else ask the user to fullfill the settings
                myServer.sendCode (state.getSwing(), getApplicationContext(), state, null);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.is_off_message), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void updateView(){

        // Put all modes invisible, then set visible the active one
        findViewById(R.id.autoMode).setVisibility(View.INVISIBLE);
        findViewById(R.id.coolMode).setVisibility(View.INVISIBLE);
        findViewById(R.id.dryMode).setVisibility(View.INVISIBLE);
        findViewById(R.id.heatMode).setVisibility(View.INVISIBLE);
        findViewById(R.id.fanMode).setVisibility(View.INVISIBLE);
        switch (state.getMode()){
            case AASuper.AUTO_MODE:
                findViewById(R.id.autoMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.COOL_MODE:
                findViewById(R.id.coolMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.DRY_MODE:
                findViewById(R.id.dryMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.HEAT_MODE:
                findViewById(R.id.heatMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.FAN_MODE:
                findViewById(R.id.fanMode).setVisibility(View.VISIBLE);
                break;
        }

        // Set on/off sign visible if its on, invisible if not
        if (state.getIsOn()) {
            findViewById(R.id.onOffSign).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.onOffSign).setVisibility(View.INVISIBLE);
        }

        // Set temperature text
        TextView tempView = (TextView) findViewById(R.id.tempView);
        String temperature = Integer.toString(state.getCurrentTemp());
        if (tempView != null) tempView.setText (temperature);

        // Put all fan levels invisible, then set visible the active one(s)
        findViewById(R.id.fanLevel1).setVisibility(View.INVISIBLE);
        findViewById(R.id.fanLevel2).setVisibility(View.INVISIBLE);
        findViewById(R.id.fanLevel3).setVisibility(View.INVISIBLE);
        findViewById(R.id.fanLevelAuto).setVisibility(View.INVISIBLE);
        switch (state.getFan()){
            case AASuper.AUTO_FAN:
                findViewById(R.id.fanLevelAuto).setVisibility(View.VISIBLE);
                break;
            case AASuper.LEVEL3_FAN:
                findViewById(R.id.fanLevel3).setVisibility(View.VISIBLE);
            case AASuper.LEVEL2_FAN:
                findViewById(R.id.fanLevel2).setVisibility(View.VISIBLE);
            case AASuper.LEVEL1_FAN:
                findViewById(R.id.fanLevel1).setVisibility(View.VISIBLE);
        }
    }
}