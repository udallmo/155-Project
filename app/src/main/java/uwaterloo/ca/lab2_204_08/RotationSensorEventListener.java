package uwaterloo.ca.lab2_204_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import static uwaterloo.ca.lab2_204_08.MainActivity.xR;
import static uwaterloo.ca.lab2_204_08.MainActivity.yR;
import static uwaterloo.ca.lab2_204_08.MainActivity.zR;

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