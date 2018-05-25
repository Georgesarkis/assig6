package Application;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

/******************************************************************************
  Main applet code.
******************************************************************************/

public class Main extends Applet implements Runnable {

	information info = new information();
	UICreater uicreate = new UICreater();

  public String getAppletInfo() {

    // Return copyright information.

    return(info.getCopyText());
  }
  int     numStars;
  Point[] stars;
  Graph graph = new Graph();
  public void init() {

    Dimension d = getSize();
    int i;

    // Display copyright information.

    System.out.println(info.getCopyText());

    // Set up key event handling and set focus to applet window.

    addKeyListener((KeyListener) this);
    requestFocus();

    // Save the screen size.

    AsteroidsSprite.width = d.width;
    AsteroidsSprite.height = d.height;

    // Generate the starry background.

    numStars = AsteroidsSprite.width * AsteroidsSprite.height / 5000;
    ((Graph) graph).getNumStars(numStars);
    stars = new Point[numStars];
    for (i = 0; i < numStars; i++)
      stars[i] = new Point((int) (Math.random() * AsteroidsSprite.width), (int) (Math.random() * AsteroidsSprite.height));
    ((Graph) graph).getStars(stars);
    // Create shape for the ship sprite.

    info.setShip(new ship());
    info.ship.shape.addPoint(0, -10);
    info.ship.shape.addPoint(7, 10);
    info.ship.shape.addPoint(-7, 10);

    // Create shapes for the ship thrusters.

    info.setFwdThruster(new AsteroidsSprite() );
    info.fwdThruster.shape.addPoint(0, 12);
    info.fwdThruster.shape.addPoint(-3, 16);
    info.fwdThruster.shape.addPoint(0, 26);
    info.fwdThruster.shape.addPoint(3, 16);
    info.revThruster = new AsteroidsSprite();
    info.revThruster.shape.addPoint(-2, 12);
    info.revThruster.shape.addPoint(-4, 14);
    info.revThruster.shape.addPoint(-2, 20);
    info.revThruster.shape.addPoint(0, 14);
    info.revThruster.shape.addPoint(2, 12);
    info.revThruster.shape.addPoint(4, 14);
    info.revThruster.shape.addPoint(2, 20);
    info.revThruster.shape.addPoint(0, 14);

    // Create shape for each photon sprites.

    for (i = 0; i < info.getMaxShots(); i++) {
      info.photons[i] = new AsteroidsSprite();
      info.photons[i].shape.addPoint(1, 1);
      info.photons[i].shape.addPoint(1, -1);
      info.photons[i].shape.addPoint(-1, 1);
      info.photons[i].shape.addPoint(-1, -1);
    }

    // Create shape for the flying saucer.

    info.ufo = new ufo();
    info.ufo.shape.addPoint(-15, 0);
    info.ufo.shape.addPoint(-10, -5);
    info.ufo.shape.addPoint(-5, -5);
    info.ufo.shape.addPoint(-5, -8);
    info.ufo.shape.addPoint(5, -8);
    info.ufo.shape.addPoint(5, -5);
    info.ufo.shape.addPoint(10, -5);
    info.ufo.shape.addPoint(15, 0);
    info.ufo.shape.addPoint(10, 5);
    info.ufo.shape.addPoint(-10, 5);

    // Create shape for the guided missle.

    info.missle = new missle();
    info.missle.shape.addPoint(0, -4);
    info.missle.shape.addPoint(1, -3);
    info.missle.shape.addPoint(1, 3);
    info.missle.shape.addPoint(2, 4);
    info.missle.shape.addPoint(-2, 4);
    info.missle.shape.addPoint(-1, 3);
    info.missle.shape.addPoint(-1, -3);

    // Create asteroid sprites.

    for (i = 0; i < info.getMaxRocks(); i++)
    	info.asteroids[i] = new AsteroidsSprite();

    // Create explosion sprites.

    for (i = 0; i < info.getMaxScrap(); i++)
    	info.explosions[i] = new AsteroidsSprite();

    // Initialize game data and put us in 'game over' mode.

    highScore = 0;
    sound = true;
    detail = true;
    initGame();
    endGame();
  }

  public void initGame() {

    // Initialize game data and sprites.

    score = 0;
    shipsLeft = MAX_SHIPS;
    asteroidsSpeed = MIN_ROCK_SPEED;
    newShipScore = NEW_SHIP_POINTS;
    newUfoScore = NEW_UFO_POINTS;
    thrustersPlaying = ((ship) ship).initShip(fwdThruster,revThruster,loaded,thrustersSound ,hyperCounter );
    ((Photons) photon).initPhotons(photons);
    ((ufo) ufo).stopUfo();
    ((missle) missle).stopMissle();
    ((Asteroid) asteroid).initAsteroids(asteroids,asteroidIsSmall);
    explosionIndex  = explode.initExplosions(explosions);
    playing = true;
    paused = false;
    photonTime = System.currentTimeMillis();
  }

  public void endGame() {

    // Stop ship, flying saucer, guided missle and associated sounds.

    playing = false;
    ((ship) ship).stopShip();
    ((ufo) ufo).stopUfo();
    ((missle) missle).stopMissle();
  }

  public void start() {

    if (loopThread == null) {
      loopThread = new Thread(this);
      loopThread.start();
    }
    if (!loaded && loadThread == null) {
      loadThread = new Thread(this);
      loadThread.start();
    }
  }

  public void stop() {

    if (loopThread != null) {
      loopThread.stop();
      loopThread = null;
    }
    if (loadThread != null) {
      loadThread.stop();
      loadThread = null;
    }
  }
  AudioClip1 audio = new AudioClip1();
  public void run() {

    int i, j;
    long startTime;

    // Lower this thread's priority and get the current time.

    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    startTime = System.currentTimeMillis();

    // Run thread for loading sounds.

    if (!loaded && Thread.currentThread() == loadThread) {
      audio.loadSounds();
      crashSound = audio.getCrashSound();
      explosionSound = audio.getExplosionSound();
      fireSound = audio.getFireSound();
      missleSound = audio.getMissleSound();
      saucerSound = audio.getSaucerSound();
      thrustersSound = audio.getThrustersSound();
      warpSound = audio.getWarpSound();
      clipsLoaded = audio.getClipsLoaded();
      clipTotal = audio.getClipTotal();
      loaded = true;
      loadThread.stop();
    }

    // This is the main loop.

    while (Thread.currentThread() == loopThread) {

      if (!paused) {

        // Move and process all sprites.
    	  
    	((ship) ship).updateShip(fwdThruster,revThruster,playing,left,right, up ,down,hyperCounter,shipCounter,shipsLeft,HYPER_COUNT);
        ((Photons) photon).updatePhotons(photons);
        ((ufo) ufo).updateUfo(photons,missle,explosion);
        ((missle) missle).updateMissle(photons,ship,ufo,explosion);
        ((Asteroid) asteroid).updateAsteroids(asteroids,photons,asteroidIsSmall,ship,ufo,missle,explosions);
        explosions = explode.updateExplosions(explosions,explosionCounter);

        // Check the score and advance high score, add a new ship or start the
        // flying saucer as necessary.

        if (score > highScore)
          highScore = score;
        if (score > newShipScore) {
          newShipScore += NEW_SHIP_POINTS;
          shipsLeft++;
        }
        if (playing && score > newUfoScore && !ufo.active) {
          newUfoScore += NEW_UFO_POINTS;
          ufoPassesLeft = UFO_PASSES;
          ((ufo) ufo).initUfo();
        }

        // If all asteroids have been destroyed create a new batch.

        if (asteroidsLeft <= 0)
            if (--asteroidsCounter <= 0)
            	((Asteroid) asteroid).initAsteroids(asteroids,asteroidIsSmall);
      }

      // Update the screen and set the timer for the next loop.

      repaint();
      try {
        startTime += DELAY;
        Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
      }
      catch (InterruptedException e) {
        break;
      }
    }
  }


}