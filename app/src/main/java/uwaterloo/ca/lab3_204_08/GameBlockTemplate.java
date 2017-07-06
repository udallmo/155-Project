package uwaterloo.ca.lab3_204_08;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public abstract class GameBlockTemplate extends AppCompatImageView {
    public GameBlockTemplate(Context gbCTX){
        super(gbCTX);
    }

    public abstract void setDestination();

    public abstract void move();

}
