package uwaterloo.ca.lab3_204_08;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import java.util.TimerTask;

public class GameLoopTask extends TimerTask{

    public enum Directions {LEFT, RIGHT, UP, DOWN, NO_MOVEMENT};

    private Directions currentDirection = Directions.NO_MOVEMENT;
    GameBlock newBlock;
    private RelativeLayout gameloopRL;
    private Context gameloopCTX;
    private Activity thisActivity;

/*Creates the block and set the starting the location
* Adds the block to the view*/
    private void createBlock(){
        newBlock = new GameBlock(gameloopCTX, -65, -65);
        gameloopRL.addView(newBlock);
    }

/* Constructor for GameloopTask*/
    public GameLoopTask(Activity myActivity, RelativeLayout rl, Context ctx){
        thisActivity = myActivity;
        gameloopCTX = ctx;
        gameloopRL = rl;

        createBlock();
    }

    public void setDirection(Directions currentDirection){
            newBlock.setBlockDirection(currentDirection);
    }

//Runs move method to the newBlock
    public void run(){
        thisActivity.runOnUiThread(
                new Runnable(){
                    public void run() {
                        newBlock.move();
                    }
                }
        );
    }
}
