package uwaterloo.ca.lab3_204_08;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.util.LinkedList;
import java.util.Random;

public class GameBlock extends GameBlockTemplate{

    private GameLoopTask GLT;

    private GameLoopTask.Directions myDir;

    private final float IMAGE_SCALE = 0.65f;
    private final float acc = 5.0f;

    public int myCoordX;
    public int myCoordY;
    private int targetCoordX;
    private int targetCoordY;
    private int myVelocity;

// The declaration of the of the boundaries
    private int LEFT_BOUNDARY = -60;
    private int RIGHT_BOUNDARY = 750;
    private int TOP_BOUNDARY = -60;
    private int BOT_BOUNDARY = 750;

    Random Rand = new Random();
    private String[] numberArray = {"2","4"};
    private TextView number;

    private int LRUP = 0; //LRUP = Left Right Up Down - This will store an integer value corresponding to
                          // the boundary that the block would move to IF there are no blocks in the way

//Setting the gameblock and define starting location
    public GameBlock(Context gbCTX, RelativeLayout gameloopRL, int coordX, int coordY) {

        super(gbCTX);

        TextView number = new TextView(gbCTX);
        this.number = number;

        this.setImageResource(R.drawable.gameblock);
        this.setX(coordX);
        this.setY(coordY);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);

        myCoordX = coordX;
        myCoordY = coordY;
        targetCoordX = myCoordX;
        targetCoordY = myCoordY;
        myVelocity=0;

        myDir = GameLoopTask.Directions.NO_MOVEMENT;

        number.setTextSize(50.0f);
        number.setTextColor(Color.BLACK);
        number.setText(numberArray[Rand.nextInt(2)]);
        gameloopRL.addView(number);
        number.setX(coordX + 150);
        number.setY(coordY + 90);

        //gameloopRL.addView(this);
    }
//Set the block direction
    public void setBlockDirection(GameLoopTask.Directions thisDir){
        myDir = thisDir;
    }

    public int[] getCoords(){
        int[] coordArray = {myCoordX, myCoordY};
        return coordArray;
    }

    //This method will take in the Game Block list, an x coordinate and a y coordinate,
    //and check if the supplied x and y coordinates match any of the coordinates of the already exisiting blocks
    public boolean isOccupied(LinkedList<GameBlock> GBList, int x, int y){
        for(GameBlock newBlock : GBList) {
            if ((newBlock.myCoordX == x) && (newBlock.myCoordY == y)){ //if the specified coordinates are already occupied by a block in GBList
                return true; //return true
            }
        }
        return false; //if it runs through all the created gameblocks and none of them fill the specified coordinates, return false
    }

    //This set destination block is what will be our pseudo-collision detection method
    //It will take in the gameblock list, the target coordinate, which way the block is going to move (LRUP), and the current coordinates of the block in question
    //It will then run some calculations that takes into account if there is a block at the current target coordinate, if there are blocks between itself and the
    //target coordinate, and then modify the target coordinate respectively
    public int setDestination(LinkedList<GameBlock> GBList, int targetCoord, int LRUP, int myCoordX, int myCoordY){
        int blockCount = 0; //this will count how many blocks are between the block in question up to and including the target coordinate
        int slotCount = 0; //this will count how many slots are between the block in question up to and including the target coordinate
        boolean ready = false;

        while (!ready) { //until we've calculated everything and finalized the target coordinates, keep looping
            //Log.d("SLOTCOUNT:", " " + slotCount);
            //Log.d("BLOCKCOUNT:", " " + blockCount);
            if (LRUP == 1) //if target destination is LEFT_BOUNDARY
            {
                if (targetCoord >= myCoordX){ //if target coordinate is equal to current coordinate, or for some reason if it's larger (by a small calculation bug for example, to prevent against infinite loops)
                    targetCoord = myCoordX - 270*(slotCount - blockCount);
                    ready = true;
                }
                else if (isOccupied(GBList, targetCoord, myCoordY)){
                    blockCount++; //if the target coordinate is occupied, increment the block counter (so the block counter is representative of how many blocks are in the way)
                    slotCount++; //also increment the slot counter (the slot counter increments regardless, it's just to show how many slots there are in front of the block in question
                    targetCoord += 270; //move the target coordinate one block over if there is a block in the way (this is also done regardless, as we want to check all slots)
                } else {
                    slotCount++;
                    targetCoord += 270;
                }
            } else if (LRUP == 2) //if target destination is RIGHT_BOUNDARY
            {
                if (targetCoord <= myCoordX){
                    targetCoord = myCoordX + 270*(slotCount - blockCount);
                    ready = true;
                }
                else if (isOccupied(GBList, targetCoord, myCoordY)){
                    blockCount++;
                    slotCount++;
                    targetCoord -= 270;
                } else {
                    slotCount++;
                    targetCoord -= 270;
                }
            } else if (LRUP == 3) //if target destination is TOP_BOUNDARY
            {
                if (targetCoord >= myCoordY){
                    targetCoord = myCoordY - 270*(slotCount - blockCount);
                    ready = true;
                }
                else if (isOccupied(GBList, myCoordX, targetCoord)){
                    blockCount++;
                    slotCount++;
                    targetCoord += 270;
                } else {
                    slotCount++;
                    targetCoord += 270;
                }
            } else if (LRUP == 4) //if target destination is BOT_BOUNDARY
            {
                if (targetCoord <= myCoordY){
                    targetCoord = myCoordY + 270*(slotCount - blockCount);
                    ready = true;
                }
                else if (isOccupied(GBList, myCoordX, targetCoord)){
                    blockCount++;
                    slotCount++;
                    targetCoord -= 270;
                } else {
                    slotCount++;
                    targetCoord -= 270;
                }
            }

        }
        return targetCoord;
    }

/*FSM for movement of the block (LEFT, RIGHT, UP, DOWN)
* Called from the GameLoopTask.run()
* Using information from the AccelerometerEventListener
* myVelocity starts at 0 and is either +/- from the acceleration
* Sets velocity equals to zero when it reaches the bounds*/
    public void move(LinkedList<GameBlock> GBList){
        number.bringToFront();
        //Log.d("MYCOORDX:", " " + myCoordX);
        switch(myDir){

            case LEFT:
                targetCoordX = setDestination(GBList, LEFT_BOUNDARY, 1, myCoordX, myCoordY); //LRUP = 1 is for the left boundary
                //Log.d("TARGETLEFT", "Value = " + targetCoordX);

                if (myCoordX > targetCoordX){
                    myCoordX -= myVelocity;

                    if(myCoordX <= targetCoordX){
                        myCoordX = targetCoordX;
                        myVelocity = 0;
                    }
                    else {
                        myCoordX -= myVelocity;
                        myVelocity += acc;
                    }
                }
                break;

            case RIGHT:
                targetCoordX = setDestination(GBList, RIGHT_BOUNDARY, 2, myCoordX, myCoordY); //LRUP = 2 is for the right boundary
                //Log.d("TARGETRIGHT", "Value = " + targetCoordX);
                if (myCoordX < targetCoordX){
                    myCoordX += myVelocity;

                    if(myCoordX >= targetCoordX){
                        myCoordX = targetCoordX;
                        myVelocity = 0;
                    }
                    else {
                        myCoordX += myVelocity;
                        myVelocity += acc;
                    }
                }

                break;

            case UP:
                targetCoordY = setDestination(GBList, TOP_BOUNDARY, 3, myCoordX, myCoordY); //LRUP = 3 is for the top boundary
                //Log.d("TARGETUP", "Value = " + targetCoordY);
                if (myCoordY > targetCoordY){
                    myCoordY -= myVelocity;

                    if(myCoordY <= targetCoordY){
                        myCoordY = targetCoordY;
                        myVelocity = 0;
                    }
                    else {
                        myCoordY -= myVelocity;
                        myVelocity += acc;
                    }
                }

                break;

            case DOWN:
                targetCoordY = setDestination(GBList, BOT_BOUNDARY, 4, myCoordX, myCoordY); //LRUP = 4 is for the top boundary
                //Log.d("TARGETDOWN", "Value = " + targetCoordY);
                if (myCoordY < targetCoordY){
                    myCoordY += myVelocity;

                    if(myCoordY >= targetCoordY){
                        myCoordY = targetCoordY;
                        myVelocity = 0;
                    }
                    else {
                        myCoordY += myVelocity;
                        myVelocity += acc;
                    }
                }
                break;
            default:
                break;
        }
//Sets the new coordinates of the block
        if (myCoordX != -60 && myCoordX >= -330 && myCoordX < 75){ //correcting any slight variations in X coordinates
            myCoordX = -60;
        } else if (myCoordX != 210 && myCoordX > 75 && myCoordX < 345){
            myCoordX = 210;
        } else if (myCoordX != 480 && myCoordX > 345 && myCoordX < 615){
            myCoordX = 480;
        } else if (myCoordX != 750 && myCoordX > 615 && myCoordX <= 1010){
            myCoordX = 750;
        }
        if (myCoordY != -60 && myCoordY >= -330 && myCoordY < 75){ //correcting any slight variations in y coordinates
            myCoordY = -60;
        } else if (myCoordY != 210 && myCoordY > 75 && myCoordY < 345){
            myCoordY = 210;
        } else if (myCoordY != 480 && myCoordY > 345 && myCoordY < 615){
            myCoordY = 480;
        } else if (myCoordY != 750 && myCoordY > 615 && myCoordY <= 1010){
            myCoordY = 750;
        }

        this.setX(myCoordX); //not an error, just a warning- works as intended like this
        this.setY(myCoordY);
        number.setX(myCoordX + 150);
        number.setY(myCoordY + 90);
    }
}
