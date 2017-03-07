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

import static com.mooo.ewolvy.uppidown.R.string.dry;

public class MainActivity extends AppCompatActivity{

    AASuper state;
    SSLServer myServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read the preferences, if exists
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

        // Create AAKaysun object to manage the AA
        state = new AAKaysun(AASuper.AUTO_MODE,          // Modo automático
                AASuper.AUTO_FAN,                       // Ventilador automático
                27);                                    // 27 grados

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
        if (myServer != null){
            // Send the selected command.
            myServer.sendCode (state.getCommand(), getApplicationContext(), state, (ImageView) findViewById(R.id.onOffSign));
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void swingClick(View view) {
        if (state.getIsOn()) {
            if (myServer != null){
                // Send swing code, no ImageView needed (only for PowerOff / Send)
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
}