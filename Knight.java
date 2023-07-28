import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Knight extends Piece{
    //stores the side that the Knight is on (black/white)
    private String side;
    //stores the x position of the Knight
    private int xPos;
    //stores the y position of the Knight
    private int yPos;
    //stores the image of the Knight
    private BufferedImage image;


    //constructor of all Knight Objects must take in its x position, y position and weather its on the "black" or "white" side
    public Knight(int xPos, int yPos, String side){
        //updates all of the instance variables
        this.xPos = xPos;
        this.yPos = yPos;
        this.side = side;

        //used to hold the image of the knight
        try {
            //checks to see if the side parameter of the constructor was "white"
            if(side.equals("white")){
                //means the image instance variable will hold the white Knight image from the images folder
                this.image = ImageIO.read(new File("Images/WhiteKnight.png"));
            }
            //means that the side that was chosen was "black"
            else{
                //means the image instance variable will hold the black Knight image from the images folder
                this.image = ImageIO.read(new File("Images/BlackKnight.png"));
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

        //all of the following if statements are used to determine what direction the knight can travel without falling out of the screen
        if(xPos > 0 && yPos > 1){
            //updates moves list
            moves = newMove(pieces, moves, -2, -1, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos < 7 && yPos > 1){
            //updates moves list
            moves = newMove(pieces, moves, -2, 1, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos > 0 && yPos < 6){
            //updates moves list
            moves = newMove(pieces, moves, 2, -1, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos < 7 && yPos < 6){
            //updates moves list
            moves = newMove(pieces, moves, 2, 1, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos > 1 && yPos > 0){
            //updates moves list
            moves = newMove(pieces, moves, -1, -2, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos > 1 && yPos < 7){
            //updates moves list
            moves = newMove(pieces, moves, 1, -2, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos < 6 && yPos > 0){
            //updates moves list
            moves = newMove(pieces, moves, -1, 2, kingDanger, kingXPos, kingYPos, p);
        }

        if(xPos < 6 && yPos < 7){
            //updates moves list
            moves = newMove(pieces, moves, 1, 2, kingDanger, kingXPos, kingYPos, p);
        }
        
        return moves;
    }

    private ArrayList<Integer> newMove(Piece[][] pieces, ArrayList<Integer> moves, int y, int x, boolean kingDanger, int kingXPos, int kingYPos, Piece[][] p){
        ///checks if the next square that the horse will enter is empty or has a piece that is on the opposite side (that it can capture)
        if(pieces[yPos+y][xPos+x] == null || !pieces[yPos+y][xPos+x].getSide().equals(this.side)){
            //checks to see if it is our turn (meaning he can only move a piece if his king is safe at the end of the move)
            if(kingDanger){
                //removes the old spot of the knight from the board
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
                //updates the knights position
                pieces[yPos+y][xPos+x] = new Knight(xPos+x, yPos+y, side);
                //checks if king is not in check
                if(kingIsOkay(pieces, xPos+x, yPos+y, kingXPos, kingYPos, enemy)){
                    //adds the move to the moves list
                    moves.add(yPos+y);
                    moves.add(xPos+x);
                }
                //restores the piece that the knight took
                pieces[yPos+y][xPos+x] = p[yPos+y][xPos+x];
                //restores the knight to its original position
                pieces[yPos][xPos] = new Knight(xPos, yPos, side);
            }
            //means that it does not matter if the king is safe or not after its move
            else{
                //adds the move to the moves list
                moves.add(yPos+y);
                moves.add(xPos+x);
            }
        }
        return moves;
    }

    //used to check if the piece is a knight
    public boolean isKnight(){
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

}