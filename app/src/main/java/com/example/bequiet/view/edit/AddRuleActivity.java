package com.example.bequiet.view.edit;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import com.example.bequiet.R;
import com.example.bequiet.databinding.ActivityAddRuleBinding;
import com.example.bequiet.model.AppDatabase;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.WlanRule;
import com.example.bequiet.view.GPSCoordinateSelectedListener;
import com.example.bequiet.view.edit.SelectAreaFragment;
import com.example.bequiet.view.edit.SelectWifiFragment;
import com.example.bequiet.view.home.HomePageActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.room.Room;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.osmdroid.config.Configuration;

import java.util.Calendar;
import java.util.List;

public class AddRuleActivity extends AppCompatActivity implements GPSCoordinateSelectedListener, SelectWifiFragment.WifiSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddRuleBinding binding;

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 123; // Replace with your desired value


    private String ruleName = "";

    private int startHour = -1;
    private int endHour = -1;
    private int startMinute = -1;
    private int endMinute = -1;

    private String wifissid = "";

    private double lat = -1000;
    private double lon = -1000;

    private float radius = 40;

    private int zoom = 20;

    private int state = 0; //0 = GPS, 1 = WIFI

    private Button btnSaveRule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddRuleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner typesSpinner = (Spinner) findViewById(R.id.spinnerRuleTypes);
        String[] types = getResources().getStringArray(R.array.ruletypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        typesSpinner.setAdapter(adapter);

        EditText editTextRulename = findViewById(R.id.editTextRulename);
        editTextRulename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ruleName = s.toString();
                changeButtonState();
            }
        });

        EditText editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextStartDate.setShowSoftInputOnFocus(false);
        editTextStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddRuleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            editTextStartDate.setText(hourOfDay + ":" + minute);
                            startHour = hourOfDay;
                            startMinute = minute;
                            editTextStartDate.clearFocus();
                            changeButtonState();
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });

        EditText editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextEndDate.setShowSoftInputOnFocus(false);
        editTextEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddRuleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            editTextEndDate.setText(hourOfDay + ":" + minute);
                            endHour = hourOfDay;
                            endMinute = minute;
                            editTextEndDate.clearFocus();
                            changeButtonState();
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        state = 0;
                        SelectAreaFragment selectAreaFragment = new SelectAreaFragment();
                        selectAreaFragment.setGpsCoordinateSelectedListener(AddRuleActivity.this);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, selectAreaFragment)
                                .commit();
                        break;
                    case 1:
                        state = 1;
                        SelectWifiFragment selectWifiFragment = new SelectWifiFragment();
                        selectWifiFragment.setWifiSelectedListener(AddRuleActivity.this);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, selectWifiFragment)
                                .commit();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Configuration.getInstance().

                load(AddRuleActivity.this, PreferenceManager.getDefaultSharedPreferences(AddRuleActivity.this));

        this.btnSaveRule = findViewById(R.id.btnSaveRule);
        this.btnSaveRule.setEnabled(false);

        SelectAreaFragment selectAreaFragment = new SelectAreaFragment();
        selectAreaFragment.setGpsCoordinateSelectedListener(AddRuleActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, selectAreaFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void onClick(View view) {
        Thread thread = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(view.getContext(),
                    AppDatabase.class, "rules").build();
            if (this.state == 0) {
                db.ruleDAO().insertAreaRule(new AreaRule(this.ruleName, this.startHour, this.startMinute, this.endHour, this.endMinute, radius, lat, lon, zoom));
            } else {
                db.ruleDAO().insertWlanRule(new WlanRule(this.ruleName, this.startHour, this.startMinute, this.endHour, this.endMinute, this.wifissid));

            }
            Log.i("database", db.ruleDAO().loadAllAreaRules().toString());
            db.close();
        });
        thread.start();
        finish();
        /*
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                int desiredVolume = 0; // Replace with your desired volume level
                audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch (SecurityException e) {

        }

         */
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            int desiredVolume = 0; // Replace with your desired volume level
            audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            mNotificationPolicyLauncher.launch(intent);

        }
    }

    private ActivityResultLauncher<Intent> mNotificationPolicyLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle success, user granted permission
                    // You can perform the desired action here
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    int desiredVolume = 0; // Replace with your desired volume level
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
                } else {
                    // Handle failure, user denied permission or cancelled
                    // You can handle the failure scenario here
                }
            });

    @Override
    public void onWifiSelected(String ssid) {
        this.wifissid = ssid;
        changeButtonState();
    }

    @Override
    public void onGPSCoordinateSelected(double lat, double lon, float radius, int zoom) {
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.zoom = zoom;
        changeButtonState();
    }

    private void changeButtonState() {
        if (!insertedAllValues()) {
            btnSaveRule.setEnabled(false);

        } else {
            btnSaveRule.setEnabled(true);
        }
    }

    private boolean insertedAllValues() {
        if (ruleName.equals("")) return false;
        if (startHour == -1) return false;
        if (startMinute == -1) return false;
        if (endHour == -1) return false;
        if (endMinute == -1) return false;

        if (state == 0) {
            if (lat == -1000) return false;
            if (lon == -1000) return false;
        } else {
            if (wifissid.equals("")) return false;
        }
        return true;
    }

}