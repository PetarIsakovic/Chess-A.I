import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.util.*;

import javax.annotation.processing.SupportedOptions;
import javax.crypto.spec.GCMParameterSpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

public class Chess {

  // Set up all the game variables to make the window work
  final int FPS = 60;
  final int WIDTH = 600;
  final int HEIGHT = 600;
  final String TITLE = "Chess";
  Drawing drawing;

  //2D array that stores every single piece in the game (null == no piece)
  Piece[][] peices = new Piece[8][8];

  //used to check if its blacks turn (false by default (meaning they dont play first))
  boolean blacksTurn;
  //used to check if its whites turn (true means it is whites turn first)
  boolean whitesTurn = true;

  //holds the white kings x position
  int whiteKingXPos;
  //holds the white kings y position
  int whiteKingYPos;
  //holds the black kings x position
  int blackKingXPos;
  //holds the black kings y position
  int blackKingYPos;

  //used to check if the game is running (no checkmate / tie)
  boolean gameRunning = true;
  
  //used to check if the program should display all the legal moves white has (when they hover/click a piece)
  boolean showAllPossibleMoves = false;
  //used to hold the x coordinate of the piece that the white player is planning on moving
  int hoveringX = -1;
  //used to hold the y coordinate of the piece that the white player is planning on moving
  int hoveringY = -1;

  //used to hold the current x position of the users mouse (for dragging chess pieces)
  int mouseXPos;
  //used to hold the current y position of the users mouse (for dragging chess pieces)
  int mouseYPos;

  //holds all of the possible legal moves that the white player can make (depending on the current piece they are hovering/clicking)
  ArrayList<Integer> allMovesOfHoveringPiece;

  //used to check if the mouse is being clicked
  boolean mouseClicked = false;

  //used to check where the last piece the white player was "trying" to move
  int lastHoveringX = -1;
  int lastHoveringY = -1;

  //used to hold/store the winner of the game (white/black)
  String whoWon = "";
  

  
  // ☠ DO NOT TOUCH THE CODE IN THE Chess METHOD ☠
  // this is what helps create the window
  // I have moved a bunch of code into another file to try and hide it from you
  public Chess() {
    // initialize anything you need to before we see it on the screen
    start();
    // create the screen and show it
    drawing = new Drawing(TITLE, WIDTH, HEIGHT, FPS, this);
    // make sure key and mouse events work
    // this is like an actionListener on buttons except it's the keyboard and mouse
    drawing.addKeyListener(new Keyboard());
    Mouse m = new Mouse();
    drawing.addMouseListener(m);
    drawing.addMouseMotionListener(m);
    drawing.addMouseWheelListener(m);
    // start the game loop
    drawing.loop();
  }

  // use this method to set values to anything BEFORE the game starts
  // this only runs at the very beginning
  public void start() {

    //creates all 8 pawns
    for(int i = 0; i < 8 ; i++){
        //sets all 8 black pawns
        peices[1][i] = new Pawn(i, 1, "black");
        //sets all 8 white pawns
        peices[6][i] = new Pawn(i, 6, "white");
    }

    //sets the positions of both the black rooks
    peices[0][0] = new Rook(0, 0, "black");
    peices[0][7] = new Rook(7, 0, "black");
    //means that the left black rook has not moved
    peices[0][0].setBlackLeftHasMoved(true);
    //means that the right black rook has not moved
    peices[0][7].setBlackRightHasMoved(true);

    //sets the positions of both thw white rooks
    peices[7][0] = new Rook(0, 7, "white");
    peices[7][7] = new Rook(7, 7, "white");
    //means that the left white rook has not moved
    peices[7][0].setWhiteLeftHasMoved(true);
    //means that the right white rook has not moved
    peices[7][7].setWhiteRightHasMoved(true);
    
    //sets the positions of the black knights (horses)
    peices[0][1] = new Knight(1, 0, "black");
    peices[0][6] = new Knight(6, 0, "black");

    //sets the positions of the white knights (horses)
    peices[7][1] = new Knight(1, 7, "white");
    peices[7][6] = new Knight(6, 7, "white");

    //sets the positions of the black bishops
    peices[0][2] = new Bishop(2, 0, "black");
    peices[0][5] = new Bishop(5, 0, "black");

    //sets the positions of the white bishops
    peices[7][2] = new Bishop(2, 7, "white");
    peices[7][5] = new Bishop(5, 7, "white");

    //sets the position of the black king
    peices[0][4] = new King(4, 0, "black");
    //means that the black king has not moved
    peices[0][4].setBlackHasNotMoved(true);

    //sets the position of thw white queen
    peices[0][3] = new Queen(3, 0, "black");

    //sets the position of the white king
    peices[7][4] = new King(4, 7, "white");
    //means that the white king has not moved
    peices[7][4].setWhiteHasNotMoved(true);

    //sets the position of the white queen
    peices[7][3] = new Queen(3, 7, "white");

    //holds the white kings x position
    whiteKingXPos = 4;
    //holds the white kings y position
    whiteKingYPos = 7;

    //holds the black kings x position
    blackKingXPos = 4;
    //holds the black kings y position
    blackKingYPos = 0;
  }

  // this is where all the drawing happens
  // put all of your drawing code in this method
  public void paintComponent(Graphics2D g) {
    //draws the checker pattern board to the screen
    drawBoard(g);

    if(!gameRunning){
        g.setColor(new Color(227, 221, 220, 100));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(0, 0, 0, 120));
        g.setFont(new Font("Monospaced", Font.BOLD, 100));
        if(whoWon.equals("TIE")){
            g.drawString(whoWon, 210, 300);
        }
        else{
            g.drawString(whoWon, 0, 300);
        }
    }
  }

  public void drawBoard(Graphics2D g){
    //holds the size of each square on the chess board
    int sizeOfSquare = WIDTH/8;
    //loops through all rows of the chess board
    for(int i = 0; i < 8; i++){
        //loops through all columns of the chess board
        for(int j = 0; j < 8; j++){
            //checks if the current row + the current column will have no remainder when divided by 2 (used to make checkerboard pattern)
            if((i+j)%2 != 0){
                //sets the current color to a light brown
                g.setColor(new Color(209,140,71));
            }
            else{
                //sets the current color to a beige color
                g.setColor(new Color(255,206,157));
            }
            //draws a single square pad of the chess board onto the specific row and column given by the current iteration of the for loop
            g.fillRect(j*sizeOfSquare, i*sizeOfSquare, sizeOfSquare, sizeOfSquare);

        }
    }

    //checks to see if the program should show all of the users legal moves for the peace they are hovering
    if(showAllPossibleMoves && peices[hoveringY][hoveringX] != null && allMovesOfHoveringPiece != null){
        //sets the color to a light and transparent blue (to signify legal moves)
        g.setColor(new Color(121, 213, 252, 100));
        //loops through all of the legal moves arraylist for the current piece that is being hovered
        for(int i = 0; i < allMovesOfHoveringPiece.size(); i+=2){
            //draws the slightly transparent blue square over all of the legal move squares that the user can play (given the specific x and y coordinate in the arraylist)
            g.fillRect(allMovesOfHoveringPiece.get(i+1) * sizeOfSquare, allMovesOfHoveringPiece.get(i)*sizeOfSquare, sizeOfSquare, sizeOfSquare);
        }
    }

    //loops through all of the rows of the chess board
    for(int i = 0; i < 8 ; i++){
        //loops through all of the columns of the chess board
        for(int j = 0; j < 8; j++) {
            //checks to see if there is a piece in the current row and column (given the iteration of both the inner and outer for loops)
            if(peices[i][j] != null){
                //checks to see if the user if the user was hovering/clicking a piece and the current row and column corresponds to the piece they are moving (meaning the program wont draw the image of the piece in its normal spot/position)
                if(!(showAllPossibleMoves && i == hoveringY && j == hoveringX && mouseClicked)){
                    //draws all of the pieces on the board/screen given their x and y position on their board
                    g.drawImage(peices[i][j].getImage(), peices[i][j].getXPos()*sizeOfSquare, peices[i][j].getYPos()*sizeOfSquare, sizeOfSquare, sizeOfSquare, null);
                }
            }
        }
    }

    //checks to see if the user is current holding the piece with their mouse
    if(showAllPossibleMoves && mouseClicked){
    //draws the piece the user is holding at the position of their mouse
        g.drawImage(peices[hoveringY][hoveringX].getImage(), mouseXPos-sizeOfSquare/2, mouseYPos-sizeOfSquare/2, sizeOfSquare, sizeOfSquare, null);
    }
  }

  // this is the Chess game loop
  // this is where all of the program logic goes
  // this method automatically repeats over and over again
  // it tries to update as fast as the frames per second you set above (deault is
  // 60 updates each second)
  public void loop() {

    //checks to see if the game is running (no checkmate/draw)
    if(gameRunning){
        //checks to see if its blacks turn
        if(blacksTurn){
            //used to hold all of the possible pieces black can move
            ArrayList<Integer> placmentsY = new ArrayList<>();
            ArrayList<Integer> placmentsX = new ArrayList<>();

            //loops through all the rows of the board
            for(int i = 0; i < peices.length; i++){
                //loops through all of the columns of the board
                for(int j = 0; j < peices.length; j++){
                    //checks to see if the current row and column is a black piece that has at least one legal move
                    if(peices[i][j] != null && peices[i][j].getSide().equals("black") && peices[i][j].moves(peices, blackKingXPos, blackKingYPos, true) != null && peices[i][j].moves(peices, blackKingXPos, blackKingYPos, true).size() > 0){
                        //used to check if the current black piece (pawn) had the ability to be enpasanted (last turn)
                        if(peices[i][j] != null && peices[i][j].canEnPasant()){
                            //makes it so that that pawn can no longer be enpasanted by the white player
                            peices[i][j].setEnPasant(false);
                        }
                        //adds the current positions of the chess piece that could possibly be moved to the possible placements arraylist
                        placmentsY.add(i);
                        placmentsX.add(j);
                        
                    }
                }
            }

            //picks a random index/piece of the placmentsY arraylist
            int randomIndex = (int)(Math.random() * (placmentsY.size()));
            //checks to see if its size is greater than zero (meaning that the black player has a legal move they can play)
            if(placmentsY.size() > 0){
                //used to hold all of the legal moves that the chosen piece can play (only legal moves)
                ArrayList<Integer> moves = peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].moves(peices, blackKingXPos, blackKingYPos, true);

                //picks a random move index from the arraylist of all the legal moves of that piece
                int randomMove = (int)(Math.random() * (moves.size()));

                //checks to see if the random move was a even number
                if(randomMove % 2 == 0){
                    //if it was an even number than the program needs to understand that that randomMove value represents the index of a y coordinate
                    indexIssue(placmentsY, placmentsX, randomIndex, randomMove, moves, 1, 0);

                }
                //means that the random move was an odd number
                else{
                    //if it was an odd number than the program needs to undestand that the randomMove value represents the index of a x coordinate
                    indexIssue(placmentsY, placmentsX, randomIndex, randomMove, moves, 0, 1);
                }
            }
            //means that the player has no legal moves
            else{
                //checks to see if the black king is in checkmate
                if(!kingIsOkay(peices, blackKingXPos, blackKingYPos, "white")){
                    //means white won the game
                    whoWon = "WHITE WINS";
                }
                //means the black king is not in checkmate but has no legal moves
                else{
                    //means the game ended in a draw
                    whoWon = "TIE";
                }
                //means that the game has ended (win/tie)
                gameRunning = false;
            }
            //means it is no longer blacks turn
            blacksTurn = false;
            //means it is whites turn
            whitesTurn = true;

            
            //loops through all of the rows of the chess board
            for(int i = 0; i < 8; i++){
                //loops through all of the columns of the chess board
                for(int j = 0; j < 8; j++){
                    //checks to see if that specific row and column is a white piece
                    if(peices[i][j] != null && peices[i][j].getSide().equals("white")){
                        //means that the piece (if it was a pawn that could have been enpasanted by black) can no longer be enpasanted
                        peices[i][j].setEnPasant(false);
                    }
                }
            }

            //used to check if white has any legal moves
            boolean ok = false;
            //loops through all of the rows of the chess board
            for(int i = 0; i < 8; i++){
                //loops through all of the columns of the chess board
                for(int j = 0; j < 8; j++){
                    //checks to see if there is at least one legal move for the white piece on the row/column currently selected (given the iteration of both inner and outer loops)
                    if(peices[i][j] != null && peices[i][j].getSide().equals("white") && peices[i][j].moves(peices, whiteKingXPos, whiteKingYPos, true).size() > 0){
                        //means that white still has a legal move
                        ok = true;
                        //breaks out of the inner for loop
                        break;
                    }
                }
                //checks to see if white has at least one legal move
                if(ok){
                    //breaks out of the outer for loop
                    break;
                }
            }
            //checks to see if the white player does not have a legal move and its king is in check
            if(!ok && !kingIsOkay(peices, whiteKingXPos, whiteKingYPos, "black")){
                //means black wins
                whoWon = "BLACK WINS";
            }
            //checks to see if the white player does not have any legal moves but is not in check
            else if(!ok){
                //means its a tie/draw
                whoWon = "TIE";
            }
            //checks to see if white has no legal moves
            if(!ok){
                //means that the game should sto running
                gameRunning = false;
            }
            
        }
    }
  }

  //this method is used to understand that the randomMove value that black has chosen represents the index of a y coordinate or a x coordinate
  public void indexIssue(ArrayList<Integer> placmentsY, ArrayList<Integer> placmentsX, int randomIndex, int randomMove, ArrayList<Integer> moves, int xIndex, int yIndex){
    //checks to see if black player has the ability to enpasant a white pawn on the left
    if(enpesantingLeft(placmentsY, placmentsX, randomIndex)){
        //checks to see if the chosen move of the black pawns next move is to enpasant on the left
        if(moves.get(randomMove+xIndex) < peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getXPos()){
            //removes the pawn that has been enpasanted off the board
            peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)-1] = null;
        }
    }
        //checks to see if black player has the ability to enpasant a white pawn on the right
    if(enpesantigRight(placmentsY, placmentsX, randomIndex)){
        //checks to see if the chosen move of the black pawns next move is to enpasant on the right
        if(moves.get(randomMove+xIndex) > peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getXPos()){
            //removes the pawn that has been enpasanted off the board
            peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)+1] = null;
        }
    }

    //checks to see if the chosen black piece is a pawn
    if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isPawn()){
        //updates the current position of the black pawn
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new Pawn(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
        //checks to see if the black pawn has reached the end of the chess board
        if(moves.get(randomMove-yIndex) == 7){
            //chooses a random piece for the black pawn to become (queen/knight/rook/bishop)
            peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = randomPiece(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
        }
        
        //checks to see if the chosen move of the pawn was to move two blocks forward
        if(moves.get(randomMove-yIndex) == placmentsY.get(randomIndex)+2){
            //means that the pawn can be enpasanted next move
            peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)].setEnPasant(true);
        }
    }
    //checks to see if the chosen black piece is a king
    else if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isKing()){
        //means that the black king can no longer castle after this turn
        peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].setBlackHasNotMoved(false);

        //checks to see if castling was the move that the black king was going to play on the left side
        if(placmentsX.get(randomIndex) - 2 == moves.get(randomMove+xIndex)){
            //removes the rook in the top left corner
            peices[moves.get(randomMove-yIndex)][0] = null;
            //moves the rook 3 units to the right
            peices[moves.get(randomMove-yIndex)][3] = new Rook(3, moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
        }
        //checks to see if castling was the move that the black king was going to play on the right side
        else if(placmentsX.get(randomIndex) + 2 == moves.get(randomMove+xIndex)){
            //removes the black rook on the top right corner
            peices[moves.get(randomMove-yIndex)][7] = null;
            //moves the rook 2 units to the left
            peices[moves.get(randomMove-yIndex)][5] = new Rook(5, moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());

        }

        //updates the position of the king
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new King(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
        //stores the new x position of the black king
        blackKingXPos =  moves.get(randomMove+xIndex);
        //stores the new y position of the black king
        blackKingYPos = moves.get(randomMove-yIndex);
    }
    //checks to see if the chosen piece was a knight
    else if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isKnight()){
        //updates the knights position
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new Knight(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
    }
    //checks to see if the chosen piece was a queen
    else if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isQueen()){
        //updates the queens position
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new Queen(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
    }
    //checks to see if the chosen piece was a bishop
    else if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isBishop()){
        //updates the bishops position
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new Bishop(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
    }
    //checks to see if the chosen piece was a rook
    else if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isRook()){
        //checks to see if the left rook is moving
        if(placmentsX.get(randomIndex) == 0){
            //means that the left rook can no longer castle with the king
            peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].setBlackLeftHasMoved(false);
        }
        //checks to see if the right rook is moving
        else if(placmentsX.get(randomIndex) == 7){
            //means that the right rook can no longer castle with the king
            peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].setBlackRightHasMoved(false);
        }
        //updates the rooks position
        peices[moves.get(randomMove-yIndex)][moves.get(randomMove+xIndex)] = new Rook(moves.get(randomMove+xIndex), moves.get(randomMove-yIndex), peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getSide());
    }

    //removes the old position of the piece
    peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)] = null;
  }

  //used to choose a random piece for the black king to promote to once its pawn reaches the end of the board
  public Piece randomPiece(int x, int y, String side){
    //picks a random number from 0-3
    int random = (int)(Math.random() * 4);

    //checks to see what random number was chosen
     if(random == 1){
        //promotes the pawn to a knight
        return new Knight(x, y, side);
    }
    else if(random == 2){
        //promotes the pawn to a bishop
        return new Bishop(x, y, side);
    }
    else if(random == 3){
        //promotes the pawn to a rook
        return new Rook(x, y, side);
    }
    //means the random number was zero and promotes the pawn to a queen
    return new Queen(x, y, side);
  }

  //checks to see if the pawn can enpasant on the left
  public boolean enpesantingLeft(ArrayList<Integer> placmentsY, ArrayList<Integer> placmentsX, int randomIndex){
    //checks to see if the chosen piece was a pawn (since pawns can only enpasant pawns)
    if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isPawn()){
        //checks to see if blacks pawn is not hugging the left wall and checks to see if the block/position to its left is not empty
        if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getXPos() > 0 && peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)-1] != null){
            //checks to see if the piece on the left of the black pawn was able to be enpasanted
            if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)-1].canEnPasant()){
                //means that the black pawn is able to enpasant the white pawn
                return true;
            }
        }
    }
    //means that the black pawn is not able to enpasant the white pawn
    return false;
  }
//checks to see if the pawn can enpasant on the right
  public boolean enpesantigRight(ArrayList<Integer> placmentsY, ArrayList<Integer> placmentsX, int randomIndex){
    //checks to see if the chosen piece was a pawn (since pawns can only enpasant pawns)
    if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].isPawn()){
        //checks to see if blacks pawn is not hugging the right wall and checks to see if the block/position to its right is not empty
        if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)].getXPos() < 7 && peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)+1] != null){
            //checks to see if the piece on the right of the black pawn was able to be enpasanted
            if(peices[placmentsY.get(randomIndex)][placmentsX.get(randomIndex)+1].canEnPasant()){
                //means that the black pawn is able to enpasant the white pawn
                return true;
            }
        }
    }
    //means that the black pawn is not able to enpasant the white pawn
    return false;
  }

  //used to check if the king is in check (checks to see if king is being attacked)
  public boolean kingIsOkay(Piece[][] pieces, int kingXPos, int kingYPos, String enemy){
    //loops through all of the rows of the board
    for(int i = 0; i < 8; i++){
        //loops through all of the columns of the board
        for(int j = 0; j < 8; j++){
            //checks to see if the current piece (based on the current row/column) is on the enemy side (if its whites turn enemy is black and vise versa)
            if(pieces[i][j] != null && pieces[i][j].moves(pieces, -1, -1, false) != null && pieces[i][j].moves(pieces, kingXPos, kingYPos, false).size() > 0 && pieces[i][j].getSide().equals(enemy)){
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

  // Used to implement any of the Mouse Actions
  private class Mouse extends MouseAdapter {

    // if a mouse button has been pressed down
    @Override
    public void mousePressed(MouseEvent e) {
        //checks to see ifs whites turn and the game is running (no draw/win)
        if(whitesTurn && gameRunning){
            //gets the specific board coordinates that the mouse was hovering
            hoveringX = e.getX()/(WIDTH/8);
            hoveringY = e.getY()/(WIDTH/8);

            //checks to see if the piece that the user is hovering is white and has at least one legal move
            if(peices[hoveringY][hoveringX] != null && peices[hoveringY][hoveringX].getSide().equals("white") && peices[hoveringY][hoveringX].moves(peices, whiteKingXPos, whiteKingYPos, true).size() > 0){
                //means that the user has clicked a valid piece they can move
                mouseClicked = true;
                //used for the program to highlight all of the legal moves that the user can play with the selected piece
                showAllPossibleMoves = true;
                //holds the new mouse x position
                mouseXPos = e.getX();
                //holds the new mouse y position
                mouseYPos = e.getY();
                //stores all of the moves that the white player can play with the selected piece
                allMovesOfHoveringPiece = peices[hoveringY][hoveringX].moves(peices, whiteKingXPos, whiteKingYPos, true);
            }
        }
    }

    // if a mouse button has been released
    @Override
    public void mouseReleased(MouseEvent e) {
        //checks to see if a piece with valid moves was selected in the first place
        if(showAllPossibleMoves){
            //means the user is no longer holding their chosen piece
            mouseClicked = false;
            //means that the program will no longer display all the valid moves the user can play
            showAllPossibleMoves = false;
            //stores the current x and y position of the board that the users mouse was hovering as they released the piece
            int currentX = e.getX()/(WIDTH/8);
            int currentY = e.getY()/(WIDTH/8);

            //loops through all of the legal moves of the piece that was dropped
            for(int i = 0; i < allMovesOfHoveringPiece.size(); i+=2){
                //checks to see if the user dropped the piece in a valid position
                if(allMovesOfHoveringPiece.get(i) == currentY && allMovesOfHoveringPiece.get(i+1) == currentX){
                    //means that it is now blacks turn to move
                    blacksTurn = true;
                    //means that white has ended their turn
                    whitesTurn = false;
                    
                    //checks to see if the piece being moved was a pawn
                    if(peices[hoveringY][hoveringX].isPawn()){
                        //checks to see if the pawn is now at the end of the board (on the black side)
                        if(currentY == 0){
                            //promotes the white pawn to a queen
                            peices[currentY][currentX] = new Queen(currentX, currentY, "white");
                        }
                        //means that the pawn was not being promoted
                        else{
                            //checks to see if the pawn is being moved two spaces
                            if(hoveringY == 3){
                                //checks to see if the pawn can enpasant a black pawn on its left
                                if(hoveringX > 0 && peices[hoveringY][hoveringX-1] != null && currentX < hoveringX && peices[hoveringY-1][hoveringX-1] == null && peices[hoveringY][hoveringX-1].canEnPasant()){
                                    //removes the black pawn that has been enpasanted
                                    peices[hoveringY][hoveringX-1] = null;

                                }
                                //checks to see if the pawn can enpasant a black pawn on its right
                                else if(hoveringX < 7 && peices[hoveringY][hoveringX+1] != null && currentX > hoveringX && peices[hoveringY-1][hoveringX+1] == null && peices[hoveringY][hoveringX+1].canEnPasant()){
                                    //removes the black pawn that has been enpasanted
                                    peices[hoveringY][hoveringX+1] = null;
                                }

                            }
                            //updates the pawn position
                            peices[currentY][currentX] = new Pawn(currentX, currentY, "white");
                            //checks to see if the pawn moved two squares forward
                            if(hoveringY == currentY+2){
                                //means that the pawn can be enpasanted by a black pawn next turn
                                peices[currentY][currentX].setEnPasant(true);
                            }
                        }
                    }
                    //checks to see if the piece being moved is a bishop
                    else if(peices[hoveringY][hoveringX].isBishop()){
                        //updates the position of the bishop
                        peices[currentY][currentX] = new Bishop(currentX, currentY, "white");
                    }
                    //checks to see if the piece being moved is a knight
                    else if(peices[hoveringY][hoveringX].isKnight()){
                        //updates the position of the knight
                        peices[currentY][currentX] = new Knight(currentX, currentY, "white");
                    }
                    //checks to see if the piece being moved is a queen
                    else if(peices[hoveringY][hoveringX].isQueen()){
                        //updates the position of the queen
                        peices[currentY][currentX] = new Queen(currentX, currentY, "white");
                    }
                    //checks to see if the piece being moved is a rook
                    else if(peices[hoveringY][hoveringX].isRook()){
                        //updates the position of the rook
                        peices[currentY][currentX] = new Rook(currentX, currentY, "white");
                        
                        //checks to see if the right rook moved
                        if(hoveringY == 7 && hoveringX == 7){
                            //means the right rook can no longer castle with the white king
                            peices[currentY][currentX].setWhiteRightHasMoved(false);
                        }
                        //checks to see if the left rook moved
                        else if(hoveringY == 7 && hoveringX == 0){
                            //means the left rook can longer castle with the white king
                            peices[currentY][currentX].setWhiteLeftHasMoved(false);
                        }
                    }
                    //checks to see if the piece being moved is a king
                    else if(peices[hoveringY][hoveringX].isKing()){
                        //checks to see if the king was castling on the right side
                        if(hoveringX+2 == currentX){
                            //moves the rook to the left of the new position of the king
                            peices[hoveringY][hoveringX+1] = new Rook(hoveringX+1, hoveringY, "white");
                            //removes the old position of the rook
                            peices[7][7] = null;
                        }
                        //checks to see if the king was castling on the left side
                        else if(hoveringX-2 == currentX){
                            //moves the rook to the right of the new position of the king
                            peices[hoveringY][hoveringX-1] = new Rook(hoveringX-1, hoveringY, "white");
                            //removes the old position of the rook
                            peices[7][0] = null;
                        }

                        //stores the white kings new x position
                        whiteKingXPos = currentX;
                        //stores the white kings new y position
                        whiteKingYPos = currentY;

                        //updates the position of the king
                        peices[currentY][currentX] = new King(currentX, currentY, "white");
                        //means that the white king can no longer castle
                        peices[currentY][currentX].setWhiteHasNotMoved(false);
                    }
                    //removes the old position of the piece from the board
                    peices[hoveringY][hoveringX] = null;
                    //exists the loop
                    break;
                }
            }
        }
    }

    // if the scroll wheel has been moved
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

      
    }

    // if the mouse has moved positions
    @Override
    public void mouseMoved(MouseEvent e) {
        //checks if its the white players turn and if the game is running (non win/tie)
        if(whitesTurn && gameRunning){
            //holds the x pos and y pos of the piece that is being hovered over with the mouse
            hoveringX = e.getX()/(WIDTH/8);
            hoveringY = e.getY()/(WIDTH/8);

            //checks to see if the user is hovering a white piece that was not being hovered previously
            if((lastHoveringX != hoveringX || lastHoveringY != hoveringY) && peices[hoveringY][hoveringX] != null && peices[hoveringY][hoveringX].getSide().equals("white") && peices[hoveringY][hoveringX].moves(peices, whiteKingXPos, whiteKingYPos, true) != null){
                //updates the last white piece position (x and y) that was being hovered
                lastHoveringX = hoveringX;
                lastHoveringY = hoveringY;
                //means that the program will display all of the possible (legal) moves that the user has with the selected piece
                showAllPossibleMoves = true;
                //updates the arraylist that stores all of the possible (legal) moves that the user has with the selected piece
                allMovesOfHoveringPiece = peices[hoveringY][hoveringX].moves(peices, whiteKingXPos, whiteKingYPos, true);
            }
            //checks to see if the user is no longer hovering a white piece 
            else if(!mouseClicked && (peices[hoveringY][hoveringX] == null || !peices[hoveringY][hoveringX].getSide().equals("white"))){
                //means that no piece was being hovered previously
                lastHoveringX = -1;
                lastHoveringY = -1;
                //means that the program will not display any moves that the user has (because they have no moves when they select no piece)
                showAllPossibleMoves = false;
            }

        }
      
    }
    //if the mouse is being pressed and moved
    @Override
    public void mouseDragged(MouseEvent e){
        //checks if its the white players turn and if the game is running (non win/tie)
        if(whitesTurn && gameRunning){
            //checks to see if all possible moves that the player can choose/play are being displayed
            if(showAllPossibleMoves){
                //holds the new mouse x position
                mouseXPos = e.getX();
                //holds the new mouse y position 
                mouseYPos = e.getY();
            }
        }
    }
  }

  // Used to implements any of the Keyboard Actions
  private class Keyboard extends KeyAdapter {

    // if a key has been pressed down
    @Override
    public void keyPressed(KeyEvent e) {
      // determine which key was pressed
      int key = e.getKeyCode();

      
    }

    // if a key has been released
    @Override
    public void keyReleased(KeyEvent e) {
      // determine which key was pressed
      int key = e.getKeyCode();

    }
  }


  // the Chess method that launches the game when you hit run
  public static void main(String[] args) {
    Chess game = new Chess();
  }

}
