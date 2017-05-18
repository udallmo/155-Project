package lab1_204_08.uwaterloo.ca.lab1_204_08;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    LineGraphView graph;
    int count = 0;
    int maxLength = 100;
    double[][] records = new double[maxLength][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView out1;
        TextView out2;
        TextView out3;
        TextView out4;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab1_204_08);

        LinearLayout layout = ((LinearLayout) findViewById(R.id.label2));

        final Button myButton = new Button(getApplicationContext());
        myButton.setText("Generate CSV Record for ACC. Sen");
        layout.addView(myButton);

        myButton.setOnClickListener(new View.OnClickListener() {

            File file = new File(getExternalFilesDir(""), "AccReadings.csv");

            public void onClick(View v) {
                FileWriter fileWriter = null;
                PrintWriter printWriter = null;
                try {

                    fileWriter = new FileWriter(file.getAbsoluteFile());
                    printWriter = new PrintWriter(fileWriter);

                    for (int i = maxLength-1; i >0; i--) {
                        printWriter.println(i + String.format("; %.2f; %.2f; %.2f", records[i][0], records[i][1], records[i][2]));
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


        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("X", "Y", "Z"));

        layout.addView(graph);

        graph.setVisibility(View.VISIBLE);

        out1 = (TextView) findViewById(R.id.label1);
        out2 = (TextView) findViewById(R.id.ac);
        out3 = (TextView) findViewById(R.id.magnet);
        out4 = (TextView) findViewById(R.id.rot);

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
        sensorManager.registerListener(ac, acSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        float x = 0, y = 0, z = 0;

        public AccelerometerSensorEventListener(TextView outputView) {
            output2 = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            graph.addPoint(se.values);
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (Math.abs(se.values[0]) > Math.abs(x)) {
                    x = se.values[0];
                }

                if (Math.abs(se.values[1]) > Math.abs(y)) {
                    y = se.values[1];
                }

                if (Math.abs(se.values[2]) > Math.abs(z)) {
                    z = se.values[2];
                }
                String read = "The Accelerometer Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Accelerometer Reading is: \n ( " + String.format("%.3f", x) + ", "
                        + String.format("%.3f", y) + ", " + String.format("%.3f", z) + ")";
                output2.setText(read + "\n" + max);


                if (count < maxLength) {
                    records[count][0] = se.values[0];
                    records[count][1] = se.values[1];
                    records[count][2] = se.values[2];
                    count++;
                } else {
                    double[][] temp = records.clone();

                    for (int i = 0; i < maxLength-1 ; i++) {
                        for (int x=0;x<3;x++){
                            records[i][x]=temp[i+1][x];
                        }
                    }
                    records[maxLength-1][0]= se.values[0];
                    records[maxLength-1][1]= se.values[1];
                    records[maxLength-1][2]= se.values[2];
                }

            }
        }


    }

    class RotationSensorEventListener implements SensorEventListener {
        TextView output;
        float x = 0, y = 0, z = 0;

        public RotationSensorEventListener(TextView outputView) {
            output = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                if (Math.abs(se.values[0]) > Math.abs(x)) {
                    x = se.values[0];
                }
                if (Math.abs(se.values[1]) > Math.abs(y)) {
                    y = se.values[1];
                }

                if (Math.abs(se.values[2]) > Math.abs(z)) {
                    z = se.values[2];
                }
                String read = "The Rotational Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Rotational Reading is: \n ( " + String.format("%.3f", x) + ", "
                        + String.format("%.3f", y) + ", " + String.format("%.3f", z) + ")";
                output.setText(read + "\n" + max);
            }
        }
    }

    class MagneticSensorEventListener implements SensorEventListener {
        TextView output;
        float x = 0, y = 0, z = 0;

        public MagneticSensorEventListener(TextView outputView) {
            output = outputView;
        }

        public void onAccuracyChanged(Sensor s, int i) {
        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                if (Math.abs(se.values[0]) > Math.abs(x)) {
                    x = se.values[0];
                }
                if (Math.abs(se.values[1]) > Math.abs(y)) {
                    y = se.values[1];
                }

                if (Math.abs(se.values[2]) > Math.abs(z)) {
                    z = se.values[2];
                }
                String read = "The Magnetic Field Reading is: \n ( " + String.format("%.3f", se.values[0]) + ", "
                        + String.format("%.3f", se.values[1]) + ", " + String.format("%.3f", se.values[2]) + ")";

                String max = "The Record-High Magnetic Field Reading is: \n ( " + String.format("%.3f", x) + ", "
                        + String.format("%.3f", y) + ", " + String.format("%.3f", z) + ")";
                output.setText(read + "\n" + max);
            }
        }

    }

}
