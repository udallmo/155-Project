package uwaterloo.ca.lab3_204_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import sensortoy.LineGraphView;

public class AccelerometerEventListener implements SensorEventListener {

    private final float FILTER_CONSTANT = 9.0f;

    private TextView output2;
    private TextView outputGesture;
    private GameLoopTask GameLoop;

    private myFSM[] myFSMs = new myFSM[2]; //x|y|z
    private int myFSMCounter;
    private final int FSM_COUNTER_DEFAULT = 50; // timer value for determining gestures

    private float[][] records = new float[100][3];

//Based on the FSM readings set the output according to the gestures given
//If it is undetermined it the output is "WAITING"
//Sets the direction based on the gesture
    private void determineGesture(){

        myFSM.mySig[] sigs = new myFSM.mySig[2];

        for(int i = 0; i < 2; i++) {
            sigs[i] = myFSMs[i].getSignature();
            myFSMs[i].resetFSM();
        }
        outputGesture.setTextSize(50.0f);
        outputGesture.setTextColor(Color.RED);
        if(sigs[0] == myFSM.mySig.SIG_A && sigs[1] == myFSM.mySig.undetermined){
            outputGesture.setText("RIGHT");
            GameLoop.setDirection(GameLoopTask.Directions.RIGHT);
        }
        else if(sigs[0] == myFSM.mySig.SIG_B && sigs[1] == myFSM.mySig.undetermined){
            outputGesture.setText("LEFT");
            GameLoop.setDirection(GameLoopTask.Directions.LEFT);
        }
        else if(sigs[0] == myFSM.mySig.undetermined && sigs[1] == myFSM.mySig.SIG_A){
            outputGesture.setText("UP");
            GameLoop.setDirection(GameLoopTask.Directions.UP);
        }
        else if(sigs[0] == myFSM.mySig.undetermined && sigs[1] == myFSM.mySig.SIG_B){
            outputGesture.setText("DOWN");
            GameLoop.setDirection(GameLoopTask.Directions.DOWN);
        }
        else{
            outputGesture.setText("WAITING");
            GameLoop.setDirection(GameLoopTask.Directions.NO_MOVEMENT);
        }

    }

    public AccelerometerEventListener(TextView outputView, GameLoopTask GL) {
        outputGesture = outputView;
        GameLoop = GL;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 100; j++)
                records[j][i] = 0.0f;

        myFSMs[0] = new myFSM();
        myFSMs[1] = new myFSM();

        myFSMCounter = FSM_COUNTER_DEFAULT;
    }


    public float[][] getHistoryReading(){
        return records;
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

// Setting the first 100 records
            for(int i = 1; i < 100; i++){
                records[i - 1][0] = records[i][0];
                records[i - 1][1] = records[i][1];
                records[i - 1][2] = records[i][2];
            }
//Applying the LPF onto the newest record
            records[99][0] += (se.values[0] - records[99][0]) / FILTER_CONSTANT;
            records[99][1] += (se.values[1] - records[99][1]) / FILTER_CONSTANT;
            records[99][2] += (se.values[2] - records[99][2]) / FILTER_CONSTANT;


// Supplying readings to myFSM.java during the counter
// if the counter goes to 0 it starts to determine the gesture with the information given in the timer
                if (myFSMCounter > 0) {
                    for (int i = 0; i < 2; i++)
                        myFSMs[i].supplyInput(records[99][i]);
                    myFSMCounter--;
                } else if (myFSMCounter <= 0) {
                    determineGesture();
                    myFSMCounter = FSM_COUNTER_DEFAULT;
                }

            }
        }
    }

