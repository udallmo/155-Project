package uwaterloo.ca.lab3_204_08;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;


public class GameLoopTask extends TimerTask{

    public enum Directions {LEFT, RIGHT, UP, DOWN, NO_MOVEMENT};
    private int gridBlockSeparation = 203;

    private Directions currentDirection = Directions.NO_MOVEMENT;
    GameBlock newBlock;
    private RelativeLayout gameloopRL;
    private Context gameloopCTX;
    private Activity thisActivity;
    private int[] coordArray = {-60, 210, 480, 750};
    private boolean initialBlock = true; //If we have yet to create the initial block on the game board, this is true.

    LinkedList<GameBlock> GBList = new LinkedList();
    Random Rand = new Random();
    private int randX;
    private int randY;
    private int numBlocks = 0;


/*Creates the block and set the starting the location
* Adds the block to the view*/
    public boolean isOccupied2(LinkedList<GameBlock> GBList, int x, int y){
        for(GameBlock newBlock : GBList) {
            if ((newBlock.myCoordX == x) && (newBlock.myCoordY == y)){ //if the specified coordinates are already occupied by a block in GBList
                return true; //return true
            }
        }
        return false; //if it runs through all the created gameblocks and none of them fill the specified coordinates, return false
    }

    private void createBlock() {
        if (numBlocks != 16){ //as long as the board is not filled
            randX = coordArray[Rand.nextInt(4)];
            randY = coordArray[Rand.nextInt(4)];
            while (isOccupied2(GBList, randX, randY)) { //keep generating new numbers if isOccupied2 returns true (IE if there is a block in the chosen spot)
                randX = coordArray[Rand.nextInt(4)];
                randY = coordArray[Rand.nextInt(4)];
            }


            if (initialBlock) {
                newBlock = new GameBlock(gameloopCTX, gameloopRL, -60, -60);
                initialBlock = false;
            } else {
                newBlock = new GameBlock(gameloopCTX, gameloopRL, randX, randY);
            }
            gameloopRL.addView(newBlock);
            GBList.add(newBlock);
            numBlocks++;
        }
    }

/* Constructor for GameloopTask */
    public GameLoopTask(Activity myActivity, RelativeLayout rl, Context ctx){
        thisActivity = myActivity;
        gameloopCTX = ctx;
        gameloopRL = rl;

        createBlock();
    }

    public void setDirection(Directions currentDirection){
            for(GameBlock newBlock : GBList) {
                newBlock.setBlockDirection(currentDirection);
            }
            if (currentDirection != Directions.NO_MOVEMENT){ //make sure blocks are only being created on a movement
                createBlock();
            }
    }

//Runs move method to the newBlock
    public void run(){
        thisActivity.runOnUiThread(
                new Runnable(){
                    public void run() {
                        for(GameBlock newBlock : GBList) {
                            newBlock.move(GBList, newBlock.myCoordX, newBlock.myCoordY);
                    }
                    }
                }
        );
    }
}
