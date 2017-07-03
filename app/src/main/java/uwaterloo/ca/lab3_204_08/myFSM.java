package uwaterloo.ca.lab3_204_08;

public class myFSM {

    //FSM Constants
    enum FSMState{WAIT, A_RISE, A_FALL, B_FALL, B_RISE, DETERMINED};
    private FSMState myState;

    enum mySig{SIG_A, SIG_B, undetermined};
    private mySig Signature;

//RISE AND FALL thresholds determining if it's rising or falling
    private final float[] THRESHOLD_A = {0.5f, 3.0f, -0.4f};
    private final float[] THRESHOLD_B = {-0.5f, -3.0f, 0.4f};

    private float previousInput;

    private void mainFSM(float currentInput){

// Using the current value passing through the FMS and the previousFMS determine the slope
//Compared the slope to different cases to determine the gestures
// In each case if the current value still hitting the requries the gesture is cleared
        float slope = currentInput - previousInput;

        switch(myState){

            case WAIT:
                if(slope >= THRESHOLD_A[0]) {
                    myState = FSMState.A_RISE;
                }
                else if(slope <= THRESHOLD_B[0]){
                    myState = FSMState.B_FALL;
                }

                break;

            case A_RISE:
                if(slope <= 0){
                    if(currentInput >= THRESHOLD_A[1])
                        myState = FSMState.A_FALL;
                    else {
                        Signature = mySig.undetermined;
                        myState = FSMState.DETERMINED;
                    }
                }
                break;

            case A_FALL:
                if(slope >= 0){
                    if(currentInput <= THRESHOLD_A[2]){
                        Signature = mySig.SIG_A;
                        myState = FSMState.DETERMINED;
                    }
                    else {
                        Signature = mySig.undetermined;
                        myState = FSMState.DETERMINED;
                    }
                }
                break;

            case B_FALL:

                if(slope >= 0){
                    if(currentInput <= THRESHOLD_B[1])
                        myState = FSMState.B_RISE;
                    else {
                        Signature = mySig.undetermined;
                        myState = FSMState.DETERMINED;
                    }
                }

                break;

            case B_RISE:

                if(slope <= 0){
                    if(currentInput >= THRESHOLD_B[2]){
                        Signature = mySig.SIG_B;
                        myState = FSMState.DETERMINED;
                    }
                    else {
                        Signature = mySig.undetermined;
                        myState = FSMState.DETERMINED;
                    }
                }

                break;

            case DETERMINED:
                break;

            default:
                myState = FSMState.WAIT;
                Signature = mySig.undetermined;
                previousInput = 0.0f;
                break;

        }

    }

//Constructor for the FSM
    public myFSM(){
        myState = FSMState.WAIT;
        Signature = mySig.undetermined;
        previousInput = 0.0f;
    }

// Resets the values for the FSM
    public void resetFSM(){
        myState = FSMState.WAIT;
        Signature = mySig.undetermined;
        previousInput = 0.0f;
    }

//Storing the previous input and sending the new to the FMS
    public void supplyInput(float input){
        mainFSM(input);
        previousInput = input;
    }
//Determining the Signature and sending a undetermined sig if it cannot be found
    public mySig getSignature(){
        if(myState == FSMState.DETERMINED){
            return Signature;
        }
        else{
            return mySig.undetermined;
        }
    }

}
