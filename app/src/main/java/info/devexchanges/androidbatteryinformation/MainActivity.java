package info.devexchanges.androidbatteryinformation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<BatteryFeature> batteryFeatures;
    private TextView textView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        batteryFeatures = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, batteryFeatures);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(battery_receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(battery_receiver);
    }

    private BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            int level = 0;

            //If a battery is present
            if (isPresent) {
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                batteryFeatures.clear();
                batteryFeatures.add(new BatteryFeature("Battery level", String.valueOf(level)));
                batteryFeatures.add(new BatteryFeature("Technology", technology));
                batteryFeatures.add(new BatteryFeature("Plugged", getPlugTypeString(plugged)));
                batteryFeatures.add(new BatteryFeature("Health", getHealthString(health)));
                batteryFeatures.add(new BatteryFeature("Status", getStatusString(status)));
                batteryFeatures.add(new BatteryFeature("Voltage", String.valueOf(voltage)));
                batteryFeatures.add(new BatteryFeature("Temperature", String.valueOf(temperature)));

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                //This case can be happened when you use a virtual device like Genymotion mobile
                textView.setText("Battery not present!!!");
                //textView.setVisibility(View.VISIBLE);
                //recyclerView.setVisibility(View.GONE);
            }
        }
    };

    private String getPlugTypeString(int plugged) {
        String plugType = "No Plugged";

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }

        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }

        return healthString;
    }

    private String getStatusString(int status) {
        String statusString = "Unknown";

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }

        return statusString;
    }
}