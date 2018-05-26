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
	KeyInputs keyInputs = new KeyInputs();
	explode explode1 = new explode();
	ship Ship = (ship) info.ship;
	
	public void updateShip(AsteroidsSprite fwdThruster, AsteroidsSprite revThruster, boolean playing, boolean left, boolean right, boolean up, boolean down, int hyperCounter, int shipCounter, int shipsLeft, int hyperCount) {

	    double dx, dy, speed;

	    if (!playing)
	      return ;

	    // Rotate the ship if left or right cursor key is down.

	    if (left) {
	    	Ship.angle += Ship.SHIP_ANGLE_STEP;
	      if (Ship.angle > 2 * Math.PI)
	    	  Ship.angle -= 2 * Math.PI;
	    }
	    if (right) {
	    	Ship.angle -= Ship.SHIP_ANGLE_STEP;
	      if (Ship.angle < 0)
	    	  Ship.angle += 2 * Math.PI;
	    }

	    // Fire thrusters if up or down cursor key is down.

	    dx = Ship.SHIP_SPEED_STEP * -Math.sin(Ship.angle);
	    dy = Ship.SHIP_SPEED_STEP *  Math.cos(Ship.angle);
	    if (up) {
	    	Ship.deltaX += dx;
	    	Ship.deltaY += dy;
	    }
	    if (down) {
	    	Ship.deltaX -= dx;
	    	Ship.deltaY -= dy;
	    }

	    // Don't let ship go past the speed limit.

	    if (up || down) {
	      speed = Math.sqrt(Ship.deltaX * Ship.deltaX + Ship.deltaY * Ship.deltaY);
	      if (speed > Ship.MAX_SHIP_SPEED) {
	        dx = Ship.MAX_SHIP_SPEED * -Math.sin(Ship.angle);
	        dy = Ship.MAX_SHIP_SPEED *  Math.cos(Ship.angle);
	        if (up)
	        	Ship.deltaX = dx;
	        else
	        	Ship.deltaX = -dx;
	        if (up)
	        	Ship.deltaY = dy;
	        else
	        	Ship.deltaY = -dy;
	      }
	    }

	    // Move the ship. If it is currently in hyperspace, advance the countdown.

	    if (Ship.active) {
	    	Ship.advance();
	    	Ship.render();
	      if (hyperCounter > 0)
	        hyperCounter--;

	      // Update the thruster sprites to match the ship sprite.

	      fwdThruster.x = Ship.x;
	      fwdThruster.y = Ship.y;
	      fwdThruster.angle = Ship.angle;
	      fwdThruster.render();
	      revThruster.x = Ship.x;
	      revThruster.y = Ship.y;
	      revThruster.angle = Ship.angle;
	      revThruster.render();
	    }

	    // Ship is exploding, advance the countdown or create a new ship if it is
	    // done exploding. The new ship is added as though it were in hyperspace.
	    // (This gives the player time to move the ship if it is in imminent
	    // danger.) If that was the last ship, end the game.

	    else
	      if (--shipCounter <= 0)
	        if (shipsLeft > 0) {
	          Ship.initShip(revThruster, revThruster, down, null, hyperCount);
	          hyperCounter = info.HYPER_COUNT;
	        }
	        else
	          endGame();
		
	  }
  public String getAppletInfo() {

    // Return copyright information.

    return(info.copyText);
  }
  int     numStars;
  Point[] stars;
  Graph graph = new Graph();
  public void init() {

    Dimension d = getSize();
    int i;

    // Display copyright information.

    System.out.println(info.copyText);

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

    info.ship = new ship();
    info.ship.shape.addPoint(0, -10);
    info.ship.shape.addPoint(7, 10);
    info.ship.shape.addPoint(-7, 10);

    // Create shapes for the ship thrusters.

    info.fwdThruster = new AsteroidsSprite() ;
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

    for (i = 0; i < info.MAX_SHOTS; i++) {
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

    for (i = 0; i < info.MAX_ROCKS; i++)
    	info.asteroids[i] = new AsteroidsSprite();

    // Create explosion sprites.

    for (i = 0; i < info.MAX_SCRAP; i++)
    	info.explosions[i] = new AsteroidsSprite();

    // Initialize game data and put us in 'game over' mode.

    info.highScore = 0;
    info.sound = true;
    info.detail = true;
    initGame();
    endGame();
  }

  public void initGame() {

    // Initialize game data and sprites.

	  info.score = 0;
	  info.shipsLeft = info.MAX_SHIPS;
	  info.asteroidsSpeed = info.MIN_ROCK_SPEED;
	  info.newShipScore = info.NEW_SHIP_POINTS;
	  info.newUfoScore = info.NEW_UFO_POINTS;
	  info.thrustersPlaying = ((ship) info.ship).initShip(info.fwdThruster,info.revThruster,info.loaded,info.thrustersSound ,info.hyperCounter );
    ((Photons) info.photon).initPhotons(info.photons);
    ((ufo) info.ufo).stopUfo();
    ((missle) info.missle).stopMissle();
    ((Asteroid) info.asteroid).initAsteroids(info.asteroids,info.asteroidIsSmall);
    info.explosionIndex  = explode1.initExplosions(info.explosions);
    info.playing = true;
    info.paused = false;
    info.photonTime = System.currentTimeMillis();
  }

  public void endGame() {

    // Stop ship, flying saucer, guided missle and associated sounds.

	  info.playing = false;
    ((ship) info.ship).stopShip();
    ((ufo) info.ufo).stopUfo();
    ((missle) info.missle).stopMissle();
  }

  public void start() {

    if (info.loopThread == null) {
    	info.loopThread = new Thread(this);
    	info.loopThread.start();
    }
    if (!info.loaded && info.loadThread == null) {
    	info.loadThread = new Thread(this);
    	info.loadThread.start();
    }
  }

  public void stop() {

    if (info.loopThread != null) {
    	info.loopThread.stop();
    	info.loopThread = null;
    }
    if (info.loadThread != null) {
    	info.loadThread.stop();
    	info.loadThread = null;
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

    if (!info.loaded && Thread.currentThread() == info.loadThread) {
      audio.loadSounds();
      info.crashSound = audio.getCrashSound();
      info.explosionSound = audio.getExplosionSound();
      info.fireSound = audio.getFireSound();
      info.missleSound = audio.getMissleSound();
      info.saucerSound = audio.getSaucerSound();
      info.thrustersSound = audio.getThrustersSound();
      info.warpSound = audio.getWarpSound();
      info.clipsLoaded = audio.getClipsLoaded();
      info.clipTotal = audio.getClipTotal();
      info.loaded = true;
      info.loadThread.stop();
    }

    // This is the main loop.

    while (Thread.currentThread() == info.loopThread) {

      if (!info.paused) {

        // Move and process all sprites.
    	  
    	updateShip(info.fwdThruster,info.revThruster,info.playing,info.left,info.right, info.up ,info.down,info.hyperCounter,info.shipCounter,info.shipsLeft,info.HYPER_COUNT);
        ((Photons) info.photon).updatePhotons(info.photons);
        ((ufo) info.ufo).updateUfo(info.photons,info.missle,info.explosion);
        ((missle) info.missle).updateMissle(info.photons,info.ship,info.ufo,info.explosion);
        ((Asteroid) info.asteroid).updateAsteroids(info.asteroids,info.photons,info.asteroidIsSmall,info.ship,info.ufo,info.missle,info.explosions);
        info.explosions = explode1.updateExplosions(info.explosions,info.explosionCounter);

        // Check the score and advance high score, add a new ship or start the
        // flying saucer as necessary.

        if (info.score > info.highScore)
        	info.highScore = info.score;
        if (info.score > info.newShipScore) {
        	info.newShipScore += info.NEW_SHIP_POINTS;
        	info.shipsLeft++;
        }
        if (info.playing && info.score > info.newUfoScore && !info.ufo.active) {
        	info.newUfoScore += info.NEW_UFO_POINTS;
        	info.ufoPassesLeft = info.UFO_PASSES;
          ((ufo) info.ufo).initUfo();
        }

        // If all asteroids have been destroyed create a new batch.

        if (info.asteroidsLeft <= 0)
            if (--info.asteroidsCounter <= 0)
            	((Asteroid) info.asteroid).initAsteroids(info.asteroids,info.asteroidIsSmall);
      }

      // Update the screen and set the timer for the next loop.

      repaint();
      try {
        startTime += info.DELAY;
        Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
      }
      catch (InterruptedException e) {
        break;
      }
    }
  }


}