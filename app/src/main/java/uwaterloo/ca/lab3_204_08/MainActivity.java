package uwaterloo.ca.lab3_204_08;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Timer;



public class MainActivity extends AppCompatActivity {
    public static int displaywidth;
    public static int displayheight;
    RelativeLayout layout;

    final int GAMELOOPRATE = 50;

    GameLoopTask myGameLoop;
    Timer myGameLoopTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView out2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Get screen measurements and set the graph display to the width and 1/3 of the height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayheight = displayMetrics.heightPixels;
        displaywidth = displayMetrics.widthPixels;

//Setting up game board by getting the width of the device and creating the board dynamically
        layout = (RelativeLayout) findViewById(R.id.label2);
        layout .getLayoutParams().width = displaywidth;
        layout .getLayoutParams().height = displaywidth;
        layout .setBackgroundResource(R.drawable.gameboard);

        myGameLoop = new GameLoopTask(this, layout, getApplicationContext());
        myGameLoopTimer = new Timer();
        myGameLoopTimer.schedule(myGameLoop, GAMELOOPRATE, GAMELOOPRATE);

//SETTING the outputs dynamically
        out2 = new TextView(getApplicationContext());
        TextView outputGesture = new TextView(getApplicationContext());

        layout.addView(out2);
        layout.addView(outputGesture);

//Define Sensor Readings and Register the listeners to the functions
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor acSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final AccelerometerEventListener ac = new AccelerometerEventListener(out2, myGameLoop);
        sensorManager.registerListener(ac, acSensor, SensorManager.SENSOR_DELAY_GAME);
    }
}

