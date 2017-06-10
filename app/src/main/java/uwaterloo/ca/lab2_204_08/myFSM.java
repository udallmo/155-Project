package uwaterloo.ca.lab2_204_08;

public class myFSM {
    //For the FSM
    enum FSMStates{WAIT, RISE_A, FALL_A, FALL_B, RISE_B, DETERMINED};
    private FSMStates myState;

    //signature constants
    enum Sigs{LEFT, RIGHT, UP, DOWN, UNDETERMINED};
    private Sigs mySig;

    //series of constants
    private final float[] THRESHOLD_A = {0.6f, 0.9f, -0.3f}; //subject to change
    private final float[] THRESHOLD_B = {-0.6f, -0.9f, 0.3f}; //subject to change

    //tracking my own history reading so we can calculate slope
    private float previousReading;
    private int delayCounter = 0; //we need a delay counter, since after we determine a signature, we don't want to immediately display another signature, and another, etc until some time has passed
    //so count down from something like 40 (so that the signature is displayed on the screen for a default amount of time)
    private int counter = 0; //this other counter is the time period for a sequence of events- if we see a rise in A (for example) past the threshold, we put set the counter to 40 and count down with each input
    //if the expected dip following the rise does not occur within that time frame, we will reset back to the WAIT state

    //Constructor
    public myFSM(){
        myState = FSMStates.WAIT;
        mySig = Sigs.UNDETERMINED;
        previousReading = 0.0f;
    }

    public void resetFSM(){
        myState = FSMStates.WAIT;
        mySig = Sigs.UNDETERMINED;
        previousReading = 0.0f;
    }

    public int outputFSMState(){ //this function will allow us to, from the outside, check whether or not a state other than WAIT has been determined- this will be useful for cross-checking
        int stateNum;
        if (myState == FSMStates.WAIT){
            stateNum = 0;
        }
        else{
            stateNum = 1;
        }
        return stateNum;
    }

    public int outputFSMSig(){ //this will allow us to get which signature was detected from the outside
        int sigNum;

        if (mySig == Sigs.RIGHT){
            sigNum = 1;
        }
        else if (mySig == Sigs.LEFT){
            sigNum = 2;
        }
        else if (mySig == Sigs.UP){
            sigNum = 3;
        }
        else if (mySig == Sigs.DOWN){
            sigNum = 4;
        }
        else {
            sigNum = 0; //UNDETERMINED
        }
        return sigNum;
    }

    public void supplyReading(float input, int variable){ //variable = 0 --> x, variable = 1 --> y
        //calculate slope
        float slope = input - previousReading;
        if (delayCounter == 0){
            switch (myState){

                case WAIT:

                    Log.d("WAITING");

                    if ((slope >= THRESHOLD_A[0]) && (slope <= THRESHOLD_A[1]))
                    {
                        counter = 40;
                        myState = FSMStates.RISE_A;
                    }
                    if ((slope <= THRESHOLD_B[0]) && (slope >= THRESHOLD_B[1])){
                        counter = 40;
                        myState = FSMStates.FALL_B;
                    }

                    break;

                case RISE_A:

                    Log.d("A RISING");

                    if (slope < 0){
                        counter = 40;
                        myState = FSMStates.FALL_A;
                    }
                    break;

                case FALL_A:

                    if (slope <= THRESHOLD_A[2]){
                        counter = 0;
                        if (variable == 0){ //if this is for x
                            mySig = Sigs.RIGHT;
                        }
                        else {
                            mySig = Sigs.UP;
                        }

                        myState = FSMStates.DETERMINED;
                    }

                    break;

                case FALL_B:

                    Log.d("B FALLING");

                    if (slope > 0){
                        counter = 40;
                        myState = FSMStates.RISE_B;
                    }
                    break;

                case RISE_B:
                    if (slope >= THRESHOLD_B[2]){
                        counter = 0;
                        if (variable == 0){
                            mySig = Sigs.LEFT;
                        }
                        else{
                            mySig = Sigs.DOWN;
                        }
                        myState = FSMStates.DETERMINED;
                    }
                    break;

                case DETERMINED:
                    delayCounter = 40;
                    myState = FSMStates.WAIT;
                    break;

				default:
				    break;
            }
            previousReading = input;
            if (counter > 0){
                counter--;
            }
            if (counter == 0){
                myState = FSMStates.WAIT;
            }
        } else {
            delayCounter--;
            if (delayCounter == 0){
                mySig = Sigs.UNDETERMINED;
            }
        }

    }
}
