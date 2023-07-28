import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Rook extends Piece{

    //stores the side that the Rook is on (black/white)
    private String side;
    //stores the x position of the Rook
    private int xPos;
    //stores the y position of the Rook
    private int yPos;
    //stores the image of the Rook
    private BufferedImage image;
    //stores wether or not the left black rook has moved
    private static boolean blackLeftHasNotMoved;
    //stores wether or not the right black rook has moved
    private static boolean blackRightHasNotMoved;
    //stores wether or not the left white rook has moved
    private static boolean whiteLeftHasNotMoved;
    //stores wether or not the right white rook has moved
    private static boolean whiteRightHasNotMoved;


    //constructor of all Rook Objects must take in its x position, y position and weather its on the "black" or "white" side
    public Rook(int xPos, int yPos, String side){
        //updates all of the instance variables
        this.xPos = xPos;
        this.yPos = yPos;
        this.side = side;

        //used to hold the image of the rook
        try {
            //checks to see if the side parameter of the constructor was "white"
            if(side.equals("white")){
                //means the image instance variable will hold the white rook image from the images folder
                this.image = ImageIO.read(new File("Images/WhiteRook.png"));
            }
            //means that the side that was chosen was "black"
            else{
                //means the image instance variable will hold the black rook image from the images folder
                this.image = ImageIO.read(new File("Images/BlackRook.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Accessor (getter) method for the private xPos instance variable
    public int getXPos(){
        return this.xPos;
    }

    //Accessor (getter) method for the private yPos instance variable
    public int getYPos(){
        return this.yPos;
    }

    //Mutator (setter) method for the private xPos instance variable
    public void setX(int xPos){
        this.xPos = xPos;
    }

    //Mutator (setter) method for the private yPos instance variable
    public void setY(int yPos){
        this.yPos = yPos;
    }

    //Accessor (getter) method for the private side instance variable
    public String getSide(){
        return this.side;
    }

    //Accessor (getter) method for the private image instance variable
    public BufferedImage getImage(){
        return this.image;
    }

    public ArrayList<Integer> moves(Piece[][] p, int kingXPos, int kingYPos, boolean kingDanger){
        //creates a new array called pieces (since we don't want to change the actual board (p))
        Piece[][] pieces = new Piece[8][8];
        //loops through all of the rows ot the given array
        for(int i = 0; i < p.length; i++){
            //loops through all of the columns of the given array
            for(int j = 0; j < p[0].length; j++){
                //sets the current row/column of the new pieces array to the current row/column of the p array
                pieces[i][j] = p[i][j];
            }
        }
        //stores all of the legal moves of the piece
        ArrayList<Integer> moves = new ArrayList<>();

        //loops through the top to the bottom
        for(int i = 1; i < 8; i++){
            //checks to see if they remain within the bounds of the chess board
            if(this.yPos+i <= 7){
                //updates the legal moves
                moves = newMove(pieces, moves, i, 0, kingDanger, kingXPos, kingYPos, p);
                //checks to see if there is a piece blocking the horizontal path
                if(pieces[yPos+i][xPos] != null){
                    //break is needed so that the rook does not phase through materials
                    break;
                }
            }
            else{
                break;
            }
        }

        //loops through the bottom to the top
        for(int i = 1; i < 8; i++){
            //checks to see if they remain within the bounds of the chess board
            if(this.yPos-i >= 0){               
                //updates the legal moves 
                moves = newMove(pieces, moves, -i, 0, kingDanger, kingXPos, kingYPos, p);
                //checks to see if there is a piece blocking the horizontal path
                 if(pieces[yPos-i][xPos] != null){
                    //break is needed so that the rook does not phase through materials
                    break;
                }
            }
            else{
                break;
            }
        }

        //loops from the left to the right
        for(int i = 1; i < 8; i++){
            //checks to see if they remain within the bounds of the chess board
            if(this.xPos+i <= 7){
                //updates the legal moves
                moves = newMove(pieces, moves, 0, i, kingDanger, kingXPos, kingYPos, p);
                //checks to see if there is a piece blocking the horizontal path
                if(pieces[yPos][xPos+i] != null){
                    //break is needed so that the rook does not phase through materials
                    break;
                }
            }
            else{
                break;
            }
        }

        //loops from the right to the left
        for(int i = 1; i < 8; i++){
            //checks to see if they remain within the bounds of the chess board
            if(this.xPos-i >= 0){
                //updates the legal moves 
                moves = newMove(pieces, moves, 0, -i, kingDanger, kingXPos, kingYPos, p);
                //checks to see if there is a piece blocking the horizontal path
                if(pieces[yPos][xPos-i] != null){
                    //break is needed so that the rook does not phase through materials
                    break;
                }
            }
            else{
                break;
            }
        }
        return moves;
    }
    
    private ArrayList<Integer> newMove(Piece[][] pieces, ArrayList<Integer> moves, int y, int x, boolean kingDanger, int kingXPos, int kingYPos, Piece[][] p){
        ///checks if the next square that the horse will enter is empty or has a piece that is on the opposite side (that it can capture)
        if(pieces[yPos+y][xPos+x] == null || !pieces[yPos+y][xPos+x].getSide().equals(this.side)){
            //checks to see if it is our turn (meaning he can only move a piece if his king is safe at the end of the move)
            if(kingDanger){
                //removes the old spot of the rook from the board
                pieces[yPos][xPos] = null;
                //holds the opponents color
                String enemy = "";
                //checks to see if our piece is on the white side
                if(side.equals("white")){
                    //sets our enemy to the black side
                    enemy = "black";
                }
                //means that our piece is on the black side
                else{
                    //sets our enemy to the white side
                    enemy = "white";
                }
                //restores the piece that the rook took
                pieces[yPos+y][xPos+x] = new Rook(xPos+x, yPos+y, side);
                //checks if king is not in check
                if(kingIsOkay(pieces, xPos+x, yPos+y, kingXPos, kingYPos, enemy)){
                    //adds the move to the moves list
                    moves.add(yPos+y);
                    moves.add(xPos+x);
                }
                //restores the piece that the rook took
                pieces[yPos+y][xPos+x] = p[yPos+y][xPos+x];
                //restores the rook to its original position
                pieces[yPos][xPos] = new Rook(xPos, yPos, side);
            }
            else{
                //adds the move to the moves list
                moves.add(yPos+y);
                moves.add(xPos+x);
            }
        }
        return moves;
    }

    //checks to see if the piece is a rook
    public boolean isRook(){
        return true;
    }

    private boolean kingIsOkay(Piece[][] pieces, int newX, int newY, int kingXPos, int kingYPos, String enemy){
        //loops through all of thr rows of the board
        for(int i = 0; i < 8; i++){
            //loops through all of the columns of the board
            for(int j = 0; j < 8; j++){
                if(pieces[i][j] != null && pieces[i][j].moves(pieces, -1, -1, false) != null && pieces[i][j].moves(pieces, -1, -1, false).size() > 0 && pieces[i][j].getSide().equals(enemy)){
                    //stores all of the legal/possible moves of the enemy player
                    ArrayList<Integer> enemyMoves = pieces[i][j].moves(pieces, -1, -1, false);
                    //loops through all of the enemy moves
                    for(int m = 0; m < enemyMoves.size(); m+=2){
                        //checks to see if any of the enemy's next moves will capture (end up on the same square) as our king
                        if(enemyMoves.get(m) == kingYPos && enemyMoves.get(m+1) == kingXPos){
                            //means that the king is not safe
                            return false;
                        }
                    }
                }
  
            }
        }
        //means that the king is safe
        return true;
    }



    //Mutator (setter) method for wether or not the left black rook has moved
    public void setBlackLeftHasMoved(boolean bool){
        this.blackLeftHasNotMoved = bool;
    }

    //Accessor (getter) method for getting wether or not the left black rook has moved
    public boolean getBlackLeftHasMoved(){
        return this.blackLeftHasNotMoved;
    }

    //Mutator (setter) method for wether or not the right black rook has moved
    public void setBlackRightHasMoved(boolean bool){
        this.blackRightHasNotMoved = bool;
    }

    //Accessor (getter) method for getting wether or not the right black rook has moved
    public boolean getBlackRightHasMoved(){
        return this.blackRightHasNotMoved;
    }

    //Mutator (setter) method for wether or not the left white rook has moved
    public void setWhiteLeftHasMoved(boolean bool){
        this.whiteLeftHasNotMoved = bool;
    }

    //Accessor (getter) method for getting wether or not the left white rook has moved
    public boolean getWhiteLeftHasMoved(){
        return this.whiteLeftHasNotMoved;
    }
    //Mutator (setter) method for wether or not the right white rook has moved
    public void setWhiteRightHasMoved(boolean bool){
        this.whiteRightHasNotMoved = bool;
    }

    //Accessor (getter) method for getting wether or not the right white rook has moved
    public boolean getWhiteRightHasMoved(){
        return this.whiteRightHasNotMoved;
    }

}


