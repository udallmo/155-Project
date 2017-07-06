package uwaterloo.ca.lab3_204_08;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public class GameBlock extends GameBlockTemplate{

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

//Setting the gameblock and define starting location
    public GameBlock(Context gbCTX, int coordX, int coordY) {

        super(gbCTX);
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
    }
//Set the block direction
    public void setBlockDirection(GameLoopTask.Directions thisDir){
        myDir = thisDir;
    }

    public void setDestination(){

    }

/*FSM for movement of the block (LEFT, RIGHT, UP, DOWN)
* Called from the GameLoopTask.run()
* Using information from the AccelerometerEventListener
* myVelocity starts at 0 and is either +/- from the acceleration
* Sets velocity equals to zero when it reaches the bounds*/
    public void move(){

        switch(myDir){

            case LEFT:
                targetCoordX = LEFT_BOUNDARY;

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
                targetCoordX = RIGHT_BOUNDARY;
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
                targetCoordY = TOP_BOUNDARY;

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
                targetCoordY= BOT_BOUNDARY;
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
        this.setX(myCoordX);
        this.setY(myCoordY);
    }
}
