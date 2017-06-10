package uwaterloo.ca.lab2_204_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import static uwaterloo.ca.lab2_204_08.MainActivity.count;
import static uwaterloo.ca.lab2_204_08.MainActivity.graph;
import static uwaterloo.ca.lab2_204_08.MainActivity.maxLength;
import static uwaterloo.ca.lab2_204_08.MainActivity.records;
import static uwaterloo.ca.lab2_204_08.MainActivity.xA;
import static uwaterloo.ca.lab2_204_08.MainActivity.yA;
import static uwaterloo.ca.lab2_204_08.MainActivity.zA;

class AccelerometerSensorEventListener implements SensorEventListener {
    TextView output2;

    private final double FILTER_CONSTANT = 200.0; //subject to change
    private myFSM xFSM = new myFSM(); //finite state machine for x
    private myFSM yFSM = new myFSM(); //finite state machine for y

    public AccelerometerSensorEventListener(TextView outputView) {
        output2 = outputView;
    }

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        graph.addPoint(se.values);
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
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
//historical reading = historical reading + (new reading - old historical reading)/Constant
//the reason we don't need a check is because the smaller the change, the smaller the value after performing the subtraction- so when we divide by C, we get a small number if the change is small
                records[maxLength - 1][0] += (se.values[0] - records[maxLength-1][0])/FILTER_CONSTANT;
                records[maxLength - 1][1] += (se.values[1] - records[maxLength-1][1])/FILTER_CONSTANT;
                records[maxLength - 1][2] += (se.values[2] - records[maxLength-1][2])/FILTER_CONSTANT;

            }
        }
    }
}