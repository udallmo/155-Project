package uwaterloo.ca.lab2_204_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

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
