import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Pawn extends Piece{

    //stores the side that the pawn is on (black/white)
    private String side;
    //stores the x position of the pawn
    private int xPos;
    //stores the y position of the pawn
    private int yPos;
    //stores the image of the Pawn
    private BufferedImage image;
    //stores wether or not the pawn can be enpasanted
    private boolean enPasant;


    //constructor of all Pawn Objects must take in its x position, y position and weather its on the "black" or "white" side
    public Pawn(int xPos, int yPos, String side){
        //updates all of the instance variables
        this.xPos = xPos;
        this.yPos = yPos;
        this.side = side;

        //used to hold the image of the pawn
        try {
            //checks to see if the side parameter of the constructor was "white"
            if(side.equals("white")){
                //means the image instance variable will hold the white Pawn image from the images folder
                this.image = ImageIO.read(new File("Images/WhitePawn.png"));
            }
            //means that the side that was chosen was "black"
            else{
                //means the image instance variable will hold the black Pawn image from the images folder
                this.image = ImageIO.read(new File("Images/BlackPawn.png"));
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

    //Accessor (getter) method used to check if the pawn can be enpasanted
    public boolean canEnPasant(){
        return this.enPasant;
    }

    //Mutator (setter) method used for updating the enpasant state of the pawn
    public void setEnPasant(boolean enPasant){
        this.enPasant = enPasant;
    }

    //used to check if this piece is a pawn
    public boolean isPawn(){
        return true;
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
        ArrayList<Integer> allMoves = new ArrayList<>();

        //checks if the piece is on the white side
        if(side.equals("white")){
            //checks if the pawn has not moved yet
            if(yPos == 6){
                //checks to see if there is nothing in the front of the pawn by one square
                if(pieces[yPos-1][xPos] == null){
                    //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                    if(kingDanger){
                        //removes the old spot of the pawn
                        pieces[yPos][xPos] = null;
                        //check to see if the king wont be in check in new position
                        if(kingIsOkay(pieces, xPos, yPos-1, kingXPos, kingYPos, "black")){
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos-1);
                            allMoves.add(xPos);
                        }
                        //resets the pawn back to its old spot
                        pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                    }
                    //means that it does not matter if our king will be in check or not after the move is made
                    else{
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos);
                    }
                    //checks to see if we can go two squares forward instead of just one square forward
                    if(pieces[yPos-2][xPos] == null){
                         //checks to see if it is our turn (meaning he can only move a piece if his king is safe at the end of the move)
                        if(kingDanger){
                            //removes the old spot of the pawn
                            pieces[yPos][xPos] = null;
                            //check to see if the king wont be in check in new position
                            if(kingIsOkay(pieces, xPos, yPos-2, kingXPos, kingYPos, "black")){
                                //adds the new moves to the allMoves list
                                allMoves.add(yPos-2);
                                allMoves.add(xPos);
                            }
                            //resets the pawn back to its old spot
                            pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                        }
                        //means that it does not matter if our king will be in check or not after the move is made
                        else{
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos-2);
                            allMoves.add(xPos);
                        }
                    }
                }
            }
            //means that pawn has moved at least once already
            else{
                //checks to see if there is nothing in the front of the pawn by one square
                if(yPos-1 >= 0 && pieces[yPos-1][xPos] == null){
                    //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                    if(kingDanger){
                        //removes the old spot of the pawn
                        pieces[yPos][xPos] = null;
                        //check to see if the king wont be in check in new position
                        if(kingIsOkay(pieces, xPos, yPos-1, kingXPos, kingYPos, "black")){
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos-1);
                            allMoves.add(xPos);
                        }
                        //resets the pawn back to its old spot
                        pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                    }
                    //means that it does not matter if our king will be in check or not after the move is made
                    else{
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos);
                    }
                }
            }
            //checks to see if the pawn is attacking on the left diagonal
            if(xPos > 0 && yPos > 0 && pieces[yPos-1][xPos-1] != null && pieces[yPos-1][xPos-1].getSide().equals("black")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos-1, yPos-1, kingXPos, kingYPos, "black")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos-1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos-1);
                    allMoves.add(xPos-1);
                }
            }
            //checks to see if the pawn is attacking on the right diagonal
            if(xPos < 7 && yPos > 0 && pieces[yPos-1][xPos+1] != null && pieces[yPos-1][xPos+1].getSide().equals("black")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos+1, yPos-1, kingXPos, kingYPos, "black")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos+1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                     //adds the new moves to the allMoves list
                    allMoves.add(yPos-1);
                    allMoves.add(xPos+1);
                }
            }

            //checks to see if the pawn is enpasanting on the left
            if(xPos > 0 && yPos > 0 && pieces[yPos][xPos-1] != null && pieces[yPos][xPos-1].canEnPasant() && pieces[yPos][xPos-1].getSide().equals("black")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos-1, yPos-1, kingXPos, kingYPos, "black")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos-1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos-1);
                    allMoves.add(xPos-1);
                }
            }

            //checks to the if the pawn is enpassanting on the right
            if(xPos < 7 && yPos > 0 && pieces[yPos][xPos+1] != null && pieces[yPos][xPos+1].canEnPasant() && pieces[yPos][xPos+1].getSide().equals("black")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos+1, yPos-1, kingXPos, kingYPos, "black")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos-1);
                        allMoves.add(xPos+1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "white");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos-1);
                    allMoves.add(xPos+1);
                }
            }
        }
        //means that the piece is black
        else{
            //checks if the pawn has not moved yet
            if(yPos == 1){
                //checks to see if there is nothing in the front of the pawn by one square
                if(pieces[yPos+1][xPos] == null){
                    //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                    if(kingDanger){
                        //removes the old spot of the pawn
                        pieces[yPos][xPos] = null;
                        //check to see if the king wont be in check in new position
                        if(kingIsOkay(pieces, xPos, yPos+1, kingXPos, kingYPos, "white")){
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos+1);
                            allMoves.add(xPos);
                        }
                         //resets the pawn back to its old spot
                        pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                    }
                    //means that it does not matter if our king will be in check or not after the move is made
                    else{
                         //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos);
                    }
                    //cbecks to see if we can go two squares forward instead of one
                    if(pieces[yPos+2][xPos] == null){
                        //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                        if(kingDanger){
                            //removes the old spot of the pawn
                            pieces[yPos][xPos] = null;
                            //check to see if the king wont be in check in new position
                            if(kingIsOkay(pieces, xPos, yPos+2, kingXPos, kingYPos, "white")){
                                //adds the new moves to the allMoves list
                                allMoves.add(yPos+2);
                                allMoves.add(xPos);
                            }
                            //resets the pawn back to its old spot
                            pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                        }
                        //means that it does not matter if our king will be in check or not after the move is made
                        else{
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos+2);
                            allMoves.add(xPos);
                        }
                    }
                }
                
            }
            //means that the pawn has moved once already
            else{
                //checks to see if pawn is going one forward
                if(yPos+1 <= 7 && pieces[yPos+1][xPos] == null){
                    //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                    if(kingDanger){
                        //removes the old spot of the pawn
                        pieces[yPos][xPos] = null;
                        //check to see if the king wont be in check in new position
                        if(kingIsOkay(pieces, xPos, yPos+1, kingXPos, kingYPos, "white")){
                            //adds the new moves to the allMoves list
                            allMoves.add(yPos+1);
                            allMoves.add(xPos);
                        }
                        //resets the pawn back to its old spot
                        pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                    }
                    //means that it does not matter if our king will be in check or not after the move is made
                    else{
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos);
                    }
                }
            }
            //checks to see if pawn is attacking on the left diagonal
            if(xPos > 0 && yPos < 7 && pieces[yPos+1][xPos-1] != null && pieces[yPos+1][xPos-1].getSide().equals("white")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos-1, yPos+1, kingXPos, kingYPos, "white")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos-1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos+1);
                    allMoves.add(xPos-1);
                }
            }
            //checks if pawn is attacking on the right diagonal
            if(xPos < 7 && yPos < 7 && pieces[yPos+1][xPos+1] != null && pieces[yPos+1][xPos+1].getSide().equals("white")){
                //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos+1, yPos+1, kingXPos, kingYPos, "white")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos+1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos+1);
                    allMoves.add(xPos+1);
                }
            }

            //checks to see if pawn is enpassanting on the left
            if(xPos > 0 && yPos > 0 && pieces[yPos][xPos-1] != null && pieces[yPos][xPos-1].canEnPasant() && pieces[yPos][xPos-1].getSide().equals("white")){
                 //checks to see if we can go two squares forward instead of just one square forward
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos-1, yPos+1, kingXPos, kingYPos, "white")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos-1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                }
                //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos+1);
                    allMoves.add(xPos-1);
                }
            }

            //checks to see if pawn is enpassanting on the right
            if(xPos < 7 && yPos > 0 && pieces[yPos][xPos+1] != null && pieces[yPos][xPos+1].canEnPasant() && pieces[yPos][xPos+1].getSide().equals("white")){
                 //checks to see if it is our turn (meaning we can only move a piece if the king is safe at the end of the move)
                if(kingDanger){
                    //removes the old spot of the pawn
                    pieces[yPos][xPos] = null;
                    //check to see if the king wont be in check in new position
                    if(kingIsOkay(pieces, xPos+1, yPos+1, kingXPos, kingYPos, "white")){
                        //adds the new moves to the allMoves list
                        allMoves.add(yPos+1);
                        allMoves.add(xPos+1);
                    }
                    //resets the pawn back to its old spot
                    pieces[yPos][xPos] = new Pawn(xPos, yPos, "black");
                }
                 //means that it does not matter if our king will be in check or not after the move is made
                else{
                    //adds the new moves to the allMoves list
                    allMoves.add(yPos+1);
                    allMoves.add(xPos+1);
                }
            }

        }


        return allMoves;
    }

    //helper method is use to see if the king is not in checl
    private boolean kingIsOkay(Piece[][] pieces, int newX, int newY, int kingXPos, int kingYPos, String enemy){
        //booleans are used to remember what type of piece the pawn captured/replaced
        boolean wasPawn = false;
        boolean wasBishop = false;
        boolean wasKnight = false;
        boolean wasQueen = false;
        boolean wasRook = false;
        boolean wasKing = false;
        //checks if the pawn is attacking an actual piece
        if(pieces[newY][newX] != null){
            //all if statments check to see what type of piece it was and than update the corresponding boolean in order to restore the board
            if(pieces[newY][newX].isPawn()){
                wasPawn = true;
            }
            else if(pieces[newY][newX].isBishop()){
                wasBishop = true;
            }
            else if(pieces[newY][newX].isKnight()){
                wasKnight = true;
            }
            else if(pieces[newY][newX].isQueen()){
                wasQueen = true;
            }
            else if(pieces[newY][newX].isRook()){
                wasRook = true;
            }
            else if(pieces[newY][newX].isKing()){
                wasKing = true;
            }
        }
        //adds the new pawn position
        pieces[newY][newX] = new Pawn(newX, newY, side);

        //loops through all of the rows of the board
        for(int i = 0; i < 8; i++){
            //loops through all of the columns of the bpard
            for(int j = 0; j < 8; j++){
                //checks to see if the current piece (based on the current row/column) is on the enemy side (if its whites turn enemy is black and vise versa)
                if(pieces[i][j] != null && pieces[i][j].moves(pieces, kingXPos, kingYPos, false) != null && pieces[i][j].moves(pieces, kingXPos, kingYPos, false).size() > 0 && pieces[i][j].getSide().equals(enemy)){
                    //stores all of the legal/possible moves of the enemy player
                    ArrayList<Integer> enemyMoves = pieces[i][j].moves(pieces, kingXPos, kingYPos, false);
                    //loops through all of the enemy moves
                    for(int m = 0; m < enemyMoves.size(); m+=2){
                        //checks to see if any of the enemy's next moves will capture (end up on the same square) as our king
                        if(enemyMoves.get(m) == kingYPos && enemyMoves.get(m+1) == kingXPos){
                            //restores the piece that was tooken
                            pieces = restore(pieces, newX, newY, enemy, wasPawn, wasBishop, wasKnight, wasQueen, wasRook, wasKing);
                            return false;
                        }
                    }
                }
  
            }
        }
        //restores the piece that was tooken
        pieces = restore(pieces, newX, newY, enemy, wasPawn, wasBishop, wasKnight, wasQueen, wasRook, wasKing);
        return true;
    }

    //helper method that helps restore whatever piece was taken through booleans
    private Piece[][] restore(Piece[][] pieces, int x, int y, String enemy, boolean wasPawn, boolean wasBishop, boolean wasKnight, boolean wasQueen, boolean wasRook, boolean wasKing){
        if(wasPawn){
            pieces[y][x] = new Pawn(x, y, enemy);
        }
        else if(wasBishop){
            pieces[y][x] = new Bishop(x, y, enemy);
        }
        else if(wasKnight){
            pieces[y][x] = new Knight(x, y, enemy);
        }
        else if(wasQueen){
            pieces[y][x] = new Queen(x, y, enemy);
        }
        else if(wasRook){
            pieces[y][x] = new Rook(x, y, enemy);
        }
        else if(wasKing){
            pieces[y][x] = new King(x, y, enemy);
        }
        else{
            pieces[y][x] = null;
        }
        return pieces;
    }

}
