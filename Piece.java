import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Piece {

    //This is the parent class to every other piece classes.
    //It has all of the default methods that will be used so that if one child class (like Pawn.java) doesn't have a method like isKing() than it will automatically default to false
    //note that all methods within this class have been properly defined/commented by this classes children

    public Piece(){

    }

    public BufferedImage getImage(){
        return null;
    }

    public int getXPos(){
        return -1;
    }

    public int getYPos(){
        return -1;
    }

    public void setX(int xPos){}

    public void setY(int yPos){}

    public String getSide(){
        return null;
    }

    public ArrayList<Integer> moves(Piece[][] pieces, int kingXPos, int kingYPos, boolean kingDanger){
        return null;
    }

    public boolean canEnPasant(){
        return false;
    }
    public void setEnPasant(boolean setEnPasant){}

    public boolean isPawn(){
        return false;
    }

    public boolean isKing(){
        return false;
    }

    public boolean isKnight(){
        return false;
    }

    public boolean isQueen(){
        return false;
    }

    public boolean isBishop(){
        return false;
    }

    public boolean isRook(){
        return false;
    }
    public boolean hasMoved(){
        return false;
    }
    public void setBlackHasNotMoved(boolean bool){
    }

    public boolean getBlackHasNotMoved(){
        return false;
    }

    public void setWhiteHasNotMoved(boolean bool){
        
    }

    public boolean getWhiteHasNotMoved(){
        return false;
    }

    public void setBlackLeftHasMoved(boolean bool){
    }

    public boolean getBlackLeftHasMoved(){
        return false;
    }

    public void setBlackRightHasMoved(boolean bool){
    }

    public boolean getBlackRightHasMoved(){
        return false;
    }

    public void setWhiteLeftHasMoved(boolean bool){
    }

    public boolean getWhiteLeftHasMoved(){
        return false;
    }

    public void setWhiteRightHasMoved(boolean bool){
    }

    public boolean getWhiteRightHasMoved(){
        return false;
    }
}
