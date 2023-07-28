// ☠☠☠ DO NOT TOUCH THE CODE IN THIS FILE ☠☠☠
// This file is used to create the window and set up the game loop
// You can have a loop at it and see how it works though!

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

// creates the canvas that we will actually draw on
public class Drawing extends Canvas {
  // basic variables used to set size, titles, and how fast to update the screen
  final int WIDTH;
  final int HEIGHT;
  final String TITLE;
  final int FPS;
  final long drawInterval;
  // The stragey that allows us to use accelerAate page flipping 
  BufferStrategy strategy;
  // true if the game is currently "running", i.e. the it is updating the screen
  boolean gameRunning = true;
  // the other file that we do all the work in
  Chess game;

  /**
   * Construct our game and set it running.
   */
  public Drawing(String title, int width, int height, int fps, Chess game) {
    // set all the constants when we make the window
    this.TITLE = title;
    this.WIDTH = width;
    this.HEIGHT = height;
    this.FPS = fps;
    this.game = game;
    this.drawInterval = 2000 / FPS;

    // create a frame to contain our game
    JFrame container = new JFrame(TITLE);

    // get hold the content of the frame and set up the resolution of the game
    JPanel panel = (JPanel) container.getContentPane();
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    panel.setLayout(null);

    // setup our canvas size and put it into the content of the frame
    this.setBounds(0, 0, WIDTH, HEIGHT);
    panel.add(this);

    // Tell AWT not to bother repainting our canvas since we're
    // going to do that our self in accelerated mode
    this.setIgnoreRepaint(true);

    // finally make the window visible
    container.pack();
    container.setResizable(false);
    container.setVisible(true);
    // make the X button work
    container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // center the window
    container.setLocationRelativeTo(null);

    // create the buffering strategy which will allow AWT
    // to manage our accelerated graphics
    createBufferStrategy(2);
    strategy = getBufferStrategy();
  }

  // what do you want to draw?
  public void render(Graphics2D g) {
    // clear the screen with a white box
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    g.setColor(Color.BLACK);
    // draw the stuff in the Main file
    game.paintComponent(g);
  }

  // what do you want to do for the game loop?
  public void update() {
    // update from the Main file
    game.loop();
  }

  /**
   * The main game loop. This loop is running during all game
   * play as is responsible for the following activities:
   * <p>
   * - Working out the speed of the game loop to update moves
   * - Moving the game entities
   * - Drawing the screen contents (entities, text)
   * - Updating game events
   * - Checking Input
   * <p>
   */
  public void loop() {
    long lastLoopTime = System.currentTimeMillis();

    // keep looping round til the game ends
    while (gameRunning) {
      // call the update method above to keep things "clean"
      update();
      // Get hold of a graphics context for the accelerated
      // surface and blank it out
      Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
      // call the render method above to keep things "clean"
      render(g);
      // we've completed drawing so clear up the graphics
      // and flip the buffer over to show the screen
      g.dispose();
      strategy.show();
      // determine how long that took and determine how much time needs to be used to
      // match the FPS
      long delta = System.currentTimeMillis() - lastLoopTime;
      lastLoopTime = System.currentTimeMillis();
      long waitTime = drawInterval - delta;
      // if we have taken up too much time, don't wait!
      if (waitTime < 0) {
        waitTime = 0;
      }

      // finally pause for a bit.
      // because of a bad implementation of timer, it may not be exactly the same as
      // the FPS
      try {
        Thread.sleep(waitTime);
      } catch (Exception e) {

      }
    }
  }

  // stops the game loop
  public void stopLoop() {
    gameRunning = false;
  }

}