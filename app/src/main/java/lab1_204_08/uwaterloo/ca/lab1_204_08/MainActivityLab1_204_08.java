package lab1_204_08.uwaterloo.ca.lab1_204_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ca.uwaterloo.sensortoy.LineGraphView;

public class MainActivityLab1_204_08 extends AppCompatActivity {
    public static int displaywidth;
    public static int displayheight;
    LineGraphView graph;
    int count = 0;
    int maxLength = 100;
    double[][] records = new double[maxLength][3];
    float light_max;
    float xA, yA, zA, xR, yR, zR, xM, yM, zM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView out1;
        TextView out2;
        TextView out3;
        TextView out4;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab1_204_08);

        LinearLayout layout = ((LinearLayout) findViewById(R.id.label2));

//Get screen measurements and set the graph display to the width and 1/3 of the height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayheight = displayMetrics.heightPixels;
        displaywidth = displayMetrics.widthPixels;

// Generates a graph displaying ACCELEROMETER information
// Readings are being gathered from the ACCELEROMETER sensor

        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("X", "Y", "Z"));

        layout.addView(graph);

        graph.setVisibility(View.VISIBLE);

// Generates a button for record CSV Records
// OnClick Function creates a CSV file "AccReadings.csv" inside the project directory
// Records are labeled from 0-99 where 99 is the newest value

        final Button genCSV = new Button(getApplicationContext());
        genCSV.setText("Generate CSV Record for ACC. Sen");
        layout.addView(genCSV);
        genCSV.setOnClickListener(new View.OnClickListener() {

            File file = new File(getExternalFilesDir(""), "AccReadings.csv");

//  Function writes a CSV file
//  Displays an error message if file cannot be written

            public void onClick(View v) {
                FileWriter fileWriter = null;
                PrintWriter printWriter = null;
                try {

                    fileWriter = new FileWriter(file.getAbsoluteFile());
                    printWriter = new PrintWriter(fileWriter);

//Displays 100 records from newest to oldest
                    for (int i = maxLength - 1; i >= 0; i--) {
                        printWriter.println(String.format("%f,%f,%f", records[i][0], records[i][1], records[i][2]));
                    }
                } catch (IOException e) {
                    Log.i("File Write Error: %s", e.toString());
                } finally {

                    if (printWriter != null) {
                        printWriter.flush();
                        printWriter.close();

                    }
                    Log.i("STATE", "File Write Ended.");
                }
            }
        });

// Reset the Historical High Button
//HH = historical high
        final Button resetHH = new Button(getApplicationContext());
        resetHH.setText("Reset Historical Highs");
        layout.addView(resetHH);

        resetHH.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                light_max = 0;
                xA = 0;
                yA = 0;
                zA = 0;
                xR = 0;
                yR = 0;
                zR = 0;
                xM = 0;
                yM = 0;
                zM = 0;
            }
        });

        out1 = new TextView(getApplicationContext());
        out2 = new TextView(getApplicationContext());
        out3 = new TextView(getApplicationContext());
        out4 = new TextView(getApplicationContext());

        layout.addView(out1);
        layout.addView(out2);
        layout.addView(out3);
        layout.addView(out4);

//Define Sensor Readings and Register the listeners to the functions

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor acSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor mgSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor rotSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        SensorEventListener light = new LightSensorEventListener(out1);
        SensorEventListener ac = new AccelerometerSensorEventListener(out2);
        SensorEventListener magnet = new MagneticSensorEventListener(out3);
        SensorEventListener rotation = new RotationSensorEventListener(out4);

        sensorManager.registerListener(light, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(ac, acSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(magnet, mgSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(rotation, rotSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    class LightSensorEventListener implements SensorEventListener {
        TextView output;
        float light_max = 0;

        public LightSensorEventListener(TextView outputView) {
            output = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_LIGHT) {

                if (se.values[0] > light_max) {
                    light_max = se.values[0];
                }

                output.setText("The Light Sensor Reading is: \n" + String.valueOf(se.values[0]) +
                        "\nThe Record-High Light Sensor Reading is: \n" + light_max);
            }
        }
    }

    class AccelerometerSensorEventListener implements SensorEventListener {
        TextView output2;

        public AccelerometerSensorEventListener(TextView outputView) {
            output2 = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            graph.addPoint(se.values);
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (Math.abs(se.values[0]) > Math.abs(xA)) {
                    xA = Math.abs(se.values[0]);
                }

                if (Math.abs(se.values[1]) > Math.abs(yA)) {
                    yA = Math.abs(se.values[1]);
                }

                if (Math.abs(se.values[2]) > Math.abs(zA)) {
                    zA = Math.abs(se.values[2]);
                }
                String read = "The Accelerometer Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Accelerometer Reading is: \n ( " + String.format("%.3f", xA) + ", "
                        + String.format("%.3f", yA) + ", " + String.format("%.3f", zA) + ")";
                output2.setText(read + "\n" + max);

//Develops first 100 records of the Accelerometer
//After it reaches the first 100 records removes the oldest record and shifts the records down
// Add the newest record to the list as 99

                if (count < maxLength) {
                    records[count][0] = se.values[0];
                    records[count][1] = se.values[1];
                    records[count][2] = se.values[2];
                    count++;
                } else {
                    double[][] temp = records.clone();

                    for (int i = 0; i < maxLength - 1; i++) {
                        for (int x = 0; x < 3; x++) {
                            records[i][x] = temp[i + 1][x];
                        }
                    }
                    records[maxLength - 1][0] = se.values[0];
                    records[maxLength - 1][1] = se.values[1];
                    records[maxLength - 1][2] = se.values[2];
                }
            }
        }
    }

    class RotationSensorEventListener implements SensorEventListener {
        TextView output;

        public RotationSensorEventListener(TextView outputView) {
            output = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                if (Math.abs(se.values[0]) > Math.abs(xR)) {
                    xR = Math.abs(se.values[0]);
                }
                if (Math.abs(se.values[1]) > Math.abs(yR)) {
                    yR = Math.abs(se.values[1]);
                }

                if (Math.abs(se.values[2]) > Math.abs(zR)) {
                    zR = Math.abs(se.values[2]);
                }
                String read = "The Rotational Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Rotational Reading is: \n ( " + String.format("%.3f", xR) + ", "
                        + String.format("%.3f", yR) + ", " + String.format("%.3f", zR) + ")";
                output.setText(read + "\n" + max);
            }
        }
    }

    class MagneticSensorEventListener implements SensorEventListener {
        TextView output;

        public MagneticSensorEventListener(TextView outputView) {
            output = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                if (Math.abs(se.values[0]) > Math.abs(xM)) {
                    xM = Math.abs(se.values[0]);
                }
                if (Math.abs(se.values[1]) > Math.abs(yM)) {
                    yM = Math.abs(se.values[1]);
                }

                if (Math.abs(se.values[2]) > Math.abs(zM)) {
                    zM = Math.abs(se.values[2]);
                }
                String read = "The Magnetic Field Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Magnetic Field Reading is: \n ( " + String.format("%.3f", xM) + ", "
                        + String.format("%.3f", yM) + ", " + String.format("%.3f", zM) + ")";
                output.setText(read + "\n" + max);
            }
        }

    }

}

