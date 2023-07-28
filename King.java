import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class King extends Piece{

    //stores the side that the king is on (black/white)
    private String side;
    //stores the x position of the king
    private int xPos;
    //stores the y position of the king
    private int yPos;
    //stores the image of the king
    private BufferedImage image;
    //stores whether or not the black king has moved
    private static boolean blackHasNotMoved;
    //stores whether or not the white king has moved
    private static boolean whiteHasNotMoved;

    //constructor of all King Objects must take in its x position, y position and weather its on the "black" or "white" side
    public King(int xPos, int yPos, String side){
        //updates all of the instance variables
        this.xPos = xPos;
        this.yPos = yPos;
        this.side = side;

        //used to hold the image of the king
        try {
            //checks to see if the side parameter of the constructor was "white"
            if(side.equals("white")){
                //means the image instance variable will hold the white king image from the images folder
                this.image = ImageIO.read(new File("Images/WhiteKing.png"));
            }
            //means that the side that was chosen was "black"
            else{
                //means the image instance variable will hold the black king image from the images folder
                this.image = ImageIO.read(new File("Images/BlackKing.png"));
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

    //used to calculate all of the legal moves of the king
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

        //stores the enemy of the current piece
        String enemy = "";

        //checks to see if the current piece is white
        if(side.equals("white")){
            //means that the enemy is black
            enemy = "black";
        }
        //means that the current piece is black
        else{
            //means that the enemy is white
            enemy = "white";
        }

        //checks to see if the king is not on the edge of the left side of the board
        if(xPos > 0){
            if(kingDanger){
                //checks to see if the there is a free space for the king to move to the left or if an enemy piece is positioned to its left
                if(pieces[yPos][xPos-1] == null || !pieces[yPos][xPos-1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, 0, -1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos);
                moves.add(xPos-1);
            }
        }
        //checks to see if the king is not on the edge of the right side of the board
        if(xPos < 7){
            if(kingDanger){
                //checks to see if the there is a free space for the king to move to the right or if an enemy piece is positioned to its right
                if(pieces[yPos][xPos+1] == null || !pieces[yPos][xPos+1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, 0, +1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos);
                moves.add(xPos+1);
            }
        }

        //checks to see if king can move up
        if(yPos > 0){
            if(kingDanger){
                //checks to see if there is an empty square for the king to move up or if there is an enemy piece that the king can take
                if(pieces[yPos-1][xPos] == null || !pieces[yPos-1][xPos].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, -1, 0, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos-1);
                moves.add(xPos);
            }
        }

        //checks to see if king can move down
        if(yPos < 7){
            if(kingDanger){
                //checks to see if there is an empty square for the king to down up or if there is an enemy piece that the king can take
                if(pieces[yPos+1][xPos] == null || !pieces[yPos+1][xPos].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, 1, 0, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos+1);
                moves.add(xPos);
            }
        }

        //checks to see if king can move up and left
        if(xPos > 0 && yPos > 0){
            if(kingDanger){
                //checks to see if there is an empty space for the king to go up and left or if it needs to take an enemy piece to do so
                if(pieces[yPos-1][xPos-1] == null || !pieces[yPos-1][xPos-1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, -1, -1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos-1);
                moves.add(xPos-1);
            }
        }

        //checks to see if the king can move up and right
        if(xPos < 7 && yPos > 0){
            if(kingDanger){
                //checks to see if their is an empty or enemy occupied square up and right one square for the king to take
                if(pieces[yPos-1][xPos+1] == null || !pieces[yPos-1][xPos+1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, -1, 1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos-1);
                moves.add(xPos+1);
            }
        }

        //checks to see if the king can move down and left
        if(xPos > 0 && yPos < 7){
            if(kingDanger){
                //checks to see if there is an empty space or enemy occupied square down and left one square for the king to take
                if(pieces[yPos+1][xPos-1] == null ||!pieces[yPos+1][xPos-1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, 1, -1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos+1);
                moves.add(xPos-1);
            }
        }

        //checks to see if the king can move down and right
        if(xPos < 7 && yPos < 7){
            if(kingDanger){
                //checks to see if there is an empty space or enemy occupied square down and right one square for the king to take
                if(pieces[yPos+1][xPos+1] == null || !pieces[yPos+1][xPos+1].getSide().equals(this.side)){
                    //updates moves list
                    moves = newMove(pieces, moves, 1, 1, kingDanger, kingXPos, kingYPos, enemy, p);
                }
            }
            else{
                moves.add(yPos+1);
                moves.add(xPos+1);
            }
        }

        //checks to see if left black rook and king can castle
        if(kingDanger && blackHasNotMoved && pieces[0][0] != null && pieces[0][0].isRook() && pieces[0][0].getBlackLeftHasMoved() && side.equals("black")){
            //updates moves list
            moves = castleLeft(pieces, enemy, moves);
        }
        //checks to see if left white rook and king can castle
        if(kingDanger && whiteHasNotMoved && pieces[7][0] != null && pieces[7][0].isRook() && pieces[7][0].getWhiteLeftHasMoved() && side.equals("white")){
            //updates moves list
            moves = castleLeft(pieces, enemy, moves);
        }
        //checks to see if right black rook and king can castle
        if(kingDanger && blackHasNotMoved && pieces[0][7] != null && pieces[0][7].isRook() && pieces[0][7].getBlackRightHasMoved() && side.equals("black")){
            //updates moves list
            moves = castleRight(pieces, enemy, moves);
        }
        //checks to see if right white rook and king can castle
        if(kingDanger && whiteHasNotMoved && pieces[7][7] != null && pieces[7][7].isRook() && pieces[7][7].getWhiteRightHasMoved() && side.equals("white")){
            //updates moves list
            moves = castleRight(pieces, enemy, moves);
        }
        return moves;
    }

    //used to check if piece is king
    public boolean isKing(){
        return true;
    }


    //used to check if king is in check
    private boolean kingIsOkay(Piece[][] pieces, int newX, int newY, String enemy){
        //loops through all of the rows of the board
        for(int i = 0; i < 8; i++){
            //loops through all of the columns of the board
            for(int j = 0; j < 8; j++){
                //checks to see if the current piece (based on the current row/column) is on the enemy side (if its whites turn enemy is black and vise versa)
                if(pieces[i][j] != null && pieces[i][j].moves(pieces, newX, newY, false) != null && pieces[i][j].moves(pieces, newX, newY, false).size() > 0 && pieces[i][j].getSide().equals(enemy)){
                    //stores all of the legal/possible moves of the enemy player
                    ArrayList<Integer> enemyMoves = pieces[i][j].moves(pieces, newX, newY, false);
                    //loops through all of the enemy moves
                    for(int m = 0; m < enemyMoves.size(); m+=2){
                        //checks to see if any of the enemy's next moves will capture (end up on the same square) as our king
                        if(enemyMoves.get(m) == newY && enemyMoves.get(m+1) == newX){
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

    private ArrayList<Integer> newMove(Piece[][] pieces, ArrayList<Integer> moves, int y, int x, boolean kingDanger, int kingXPos, int kingYPos, String enemy, Piece[][] p){
         //checks to see if it is our turn (meaning he can only move a piece if his king is safe at the end of the move)
         if(kingDanger){
            //removes the old spot of the king from the board
            pieces[yPos][xPos] = null;
            
            //updates the kings position
            pieces[yPos+y][xPos+x] = new King(xPos+x, yPos+y, side);
            //checks if king is not in check
            if(kingIsOkay(pieces, xPos+x, yPos+y, enemy)){
                //adds the move to the moves list
                moves.add(yPos+y);
                moves.add(xPos+x);
            }
            //restores the piece that the king took
            pieces[yPos+y][xPos+x] = p[yPos+y][xPos+x];
            //restores the king to its original position
            pieces[yPos][xPos] = new King(xPos, yPos, side);
        }
        //means that it does not matter if the king is safe or not after its move
        else{
            //adds the move to the moves list
            moves.add(yPos);
            moves.add(xPos+1);
        }
        return moves;
    }

    //Setter/mutator method that updates wether or not the black king has moved
    public void setBlackHasNotMoved(boolean bool){
        this.blackHasNotMoved = bool;
    }

    //Getter/accessor method that returns wether or not the black king has moved
    public boolean getBlackHasNotMoved(){
        return this.blackHasNotMoved;
    }

    //Setter/mutator method that updates wether or not the white king has moved
    public void setWhiteHasNotMoved(boolean bool){
        this.whiteHasNotMoved = bool;
    }

    //Getter/accessor method that returns weather or not the white king has moved
    public boolean getWhiteHasNotMoved(){
        return this.whiteHasNotMoved;
    }

    //method that is used to check if the king can castle on the left
    public ArrayList<Integer> castleLeft(Piece[][] pieces, String enemy, ArrayList<Integer> moves){
        //checks to see if the king has not moved from original square
        if(xPos == 4){

            //used to check to see if castle can be done legally
            boolean goodToGo = true;

            //loops through all pieces between the king and the left rook
            for(int i = 0; i < 3; i++){
                //checks to see if there is something in the way of the user castling (like another piece)
                if(pieces[yPos][i+1] != null){
                    //means the castling is not good to go
                    goodToGo = false;
                    break;
                }
            }

            //if goodToGo is TRUE that means that there was nothing between the rook and the king to stop the castle
            if(goodToGo){
                //loops through all of the spots that the king has to travel to, in order to fully castle
                for(int i = 0; i < 4; i++){
                    //removes the kings old position
                    pieces[yPos][xPos] = null;
                    //adds the kings new position
                    pieces[yPos][xPos-i] = new King(xPos-i, yPos, side);
                    //checks if there is no checkmate while the king is making its journey to its fully castled positioned
                    if(!kingIsOkay(pieces, xPos-i, yPos, enemy)){
                        //means that the castle would not occur (not legal move)
                        goodToGo = false;
                        //removes the new position of the king
                        pieces[yPos][xPos-i] = null;
                        //adds the old position of the king
                        pieces[yPos][xPos] = new King(xPos, yPos, side);
                        break;
                    }
                    //removes the new position of the king
                    pieces[yPos][xPos-i] = null;
                    //adds the old position of the king
                    pieces[yPos][xPos] = new King(xPos, yPos, side);
                }
                //checks to see if the goodToGo boolean still reads TRUE (meaning that the rook and king can castle together)
                if(goodToGo){
                    //adds the moves to the moves list
                    moves.add(yPos);
                    moves.add(xPos-2);
                }
            }
        }
        return moves;
    }

    //method that is used to check if the king can castle on the right
    public ArrayList<Integer> castleRight(Piece[][] pieces, String enemy, ArrayList<Integer> moves){
        //checks to see if the king has not moved from original square
        if(xPos == 4){            
            //used to check to see if castle can be done legally
            boolean goodToGo = true;
            
            //loops through all pieces between the king and the right rook
            for(int i = 0; i < 2; i++){
                //checks to see if there is something in the way of the user castling (like another piece)
                if(pieces[yPos][6-i] != null){
                    //means the right rook anc king can not castle
                    goodToGo = false;
                    break;
                }
            }

            //if goodToGo is TRUE that means that there was nothing between the rook and the king to stop the castle
            if(goodToGo){
                //loops through all of the spots that the king has to travel to, in order to fully castle
                for(int i = 0; i < 3; i++){
                    //removes the kings old position
                    pieces[yPos][xPos] = null;
                    //adds the kings new position
                    pieces[yPos][xPos+i] = new King(xPos+i, yPos, side);
                    //checks to see if the king is un check
                    if(!kingIsOkay(pieces, xPos+i, yPos, enemy)){
                        //means that the castle would not occur (not legal move)
                        goodToGo = false;
                        //removes the new position of the king
                        pieces[yPos][xPos+i] = null;
                        //adds the old position of the king
                        pieces[yPos][xPos] = new King(xPos, yPos, side);
                        break;
                    }
                    //removes the new position of the king
                    pieces[yPos][xPos+i] = null;
                    
                    //adds the old position of the king
                    pieces[yPos][xPos] = new King(xPos, yPos, side);
                }
                //checks to see if the goodToGo boolean still reads TRUE (meaning that the rook and king can castle together)
                if(goodToGo){
                    //adds the moves to the moves list
                    moves.add(yPos);
                    moves.add(xPos+2);
                }

            }
        }
        return moves;

    }


}
