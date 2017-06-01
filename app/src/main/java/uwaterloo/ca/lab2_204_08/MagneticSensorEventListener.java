package uwaterloo.ca.lab2_204_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import static uwaterloo.ca.lab2_204_08.MainActivity.xM;
import static uwaterloo.ca.lab2_204_08.MainActivity.yM;
import static uwaterloo.ca.lab2_204_08.MainActivity.zM;

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