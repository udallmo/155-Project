package uwaterloo.ca.lab3_204_08;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.widget.RelativeLayout;

import java.util.LinkedList;

public abstract class GameBlockTemplate extends AppCompatImageView {
    public GameBlockTemplate(Context gbCTX){
        super(gbCTX);
    }

    public abstract int setDestination(LinkedList<GameBlock> GBList, int targetCoord, int LRUP, int myCoordX, int myCoordY);

    public abstract void move(LinkedList<GameBlock> GBList);

}
