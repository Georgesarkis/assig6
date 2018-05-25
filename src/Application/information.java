package Application;

import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import Domain.Asteroid;
import Domain.AsteroidsSprite;
import Domain.Photons;
import Domain.explode;
import TechnicalServices.KeyInputs;

public class information  {
	// Copyright information.
	

		  
		  
	  String copyName = "Asteroids";
	  String copyVers = "Version 1.3";
	  String copyInfo = "Copyright 1998-2001 by Mike Hall";
	  String copyLink = "http://www.brainjar.com";
	  String copyText = copyName + '\n' + copyVers + '\n'
	                  + copyInfo + '\n' + copyLink;

	  // Thread control variables.

	  Thread loadThread;
	  Thread loopThread;

	  // Constants
	   private static boolean paused;
	   private static final int DELAY = 20;             // Milliseconds between screen and
	   private static final int FPS   =                 // the resulting frame rate.
	    Math.round(1000 / DELAY);

	   private static final int MAX_SHOTS =  8;          // Maximum number of sprites
	   private static final int MAX_ROCKS =  8;          // for photons, asteroids and
	   private static final int MAX_SCRAP = 40;          // explosions.

	   private static final int SCRAP_COUNT  = 2 * FPS;  // Timer counter starting values
	   private static final int HYPER_COUNT  = 3 * FPS;  // calculated using number of
	   private static final int MISSLE_COUNT = 4 * FPS;  // seconds x frames per second.
	   private static final int STORM_PAUSE  = 2 * FPS;

	   private static final int    MIN_ROCK_SIDES =   6; // Ranges for asteroid shape, size
	   private static final int    MAX_ROCK_SIDES =  16; // speed and rotation.
	   private static final int    MIN_ROCK_SIZE  =  20;
	   private static final int    MAX_ROCK_SIZE  =  40;
	   private static final double MIN_ROCK_SPEED =  40.0 / FPS;
	   private static final double MAX_ROCK_SPEED = 240.0 / FPS;
	   private static final double MAX_ROCK_SPIN  = Math.PI / FPS;

	   private static final int MAX_SHIPS = 3;           // Starting number of ships for
	                                           // each game.
	   private static final int UFO_PASSES = 3;          // Number of passes for flying
	                                            // saucer per appearance.




	   private static final int FIRE_DELAY = 50;         // Minimum number of milliseconds
	                                           // required between photon shots.

	  // Probablility of flying saucer firing a missle during any given frame
	  // (other conditions must be met).

	   private static final double MISSLE_PROBABILITY = 0.45 / FPS;

	   private static final int BIG_POINTS    =  25;     // Points scored for shooting
	   private static final int SMALL_POINTS  =  50;     // various objects.
	   private static final int UFO_POINTS    = 250;
	   private static final int MISSLE_POINTS = 500;

	  // Number of points the must be scored to earn a new ship or to cause the
	  // flying saucer to appear.

	  private static final int NEW_SHIP_POINTS = 5000;
	  private static final int NEW_UFO_POINTS  = 2750;



	  // Game data.

	  private static int score;
	  private static int highScore;
	  private static int newShipScore;
	  private static int newUfoScore;

	  // Flags for game state and options.

	   private static boolean loaded = false;
	   private static boolean playing;
	   private static boolean sound;
	   private static boolean detail;

	  // Key flags.

	  boolean left  = false;
	  boolean right = false;
	  boolean up    = false;
	  boolean down  = false;

	  // Sprite objects.

	  AsteroidsSprite   ship;
	  AsteroidsSprite   fwdThruster, revThruster;
	  AsteroidsSprite   ufo;
	  AsteroidsSprite   missle;
	  AsteroidsSprite photon = new Photons();
	  AsteroidsSprite explosion = new explode();
	  AsteroidsSprite asteroid = new Asteroid();
	 
	  AsteroidsSprite[] photons    = new AsteroidsSprite[MAX_SHOTS];
	  AsteroidsSprite[] asteroids  = new AsteroidsSprite[MAX_ROCKS];
	  AsteroidsSprite[] explosions = new AsteroidsSprite[MAX_SCRAP];
	  KeyInputs keyInputs = new KeyInputs();

	  // Ship data.

	  private static int shipsLeft;       // Number of ships left in game, including current one.
	  private static int shipCounter;     // Timer counter for ship explosion.
	  private static int hyperCounter;    // Timer counter for hyperspace.

	  // Photon data.

	   private static int   photonIndex;    // Index to next available photon sprite.
	   private long  photonTime;     // Time value used to keep firing rate constant.

	  // Flying saucer data.

	   private static int ufoPassesLeft;    // Counter for number of flying saucer passes.
	   private static int ufoCounter;       // Timer counter used to track each flying saucer pass.

	  // Missle data.

	   private static int missleCounter;    // Counter for life of missle.

	  // Asteroid data.

	   private boolean[] asteroidIsSmall = new boolean[MAX_ROCKS];    // Asteroid size flag.
	   private static int       asteroidsCounter;                            // Break-time counter.
	   private static double    asteroidsSpeed;                              // Asteroid speed.
	   private static int       asteroidsLeft;                               // Number of active asteroids.

	  // Explosion data.

	   private static int[] explosionCounter = new int[MAX_SCRAP];  // Time counters for explosions.
	   private static int   explosionIndex;                         // Next available explosion sprite.

	  // Sound clips.

	   private static AudioClip crashSound;
	   private static AudioClip explosionSound;
	   private static AudioClip fireSound;
	   private static AudioClip missleSound;
	   private static AudioClip saucerSound;
	   private static AudioClip thrustersSound;
	   private static AudioClip warpSound;

	  // Flags for looping sound clips.

	   private static boolean thrustersPlaying;
	   private static boolean saucerPlaying;
	   private static boolean misslePlaying;

	  // Counter and total used to track the loading of the sound clips.

	  int clipTotal   = 0;
	  int clipsLoaded = 0;

	  // Off screen image.

	  Dimension offDimension;
	  Image     offImage;
	  Graphics  offGraphics;

	  // Data for the screen font.

	    Font font      = new Font("Helvetica", Font.BOLD, 12);
	   FontMetrics fm = getFontMetrics(font);
	    int fontWidth  = fm.getMaxAdvance();
	    int fontHeight = fm.getHeight();
	    
	    int     numStars;
		  Point[] stars;
		public String getCopyName() {
			return copyName;
		}
		public void setCopyName(String copyName) {
			this.copyName = copyName;
		}
		public String getCopyVers() {
			return copyVers;
		}
		public void setCopyVers(String copyVers) {
			this.copyVers = copyVers;
		}
		public String getCopyInfo() {
			return copyInfo;
		}
		public void setCopyInfo(String copyInfo) {
			this.copyInfo = copyInfo;
		}
		public String getCopyLink() {
			return copyLink;
		}
		public void setCopyLink(String copyLink) {
			this.copyLink = copyLink;
		}
		public String getCopyText() {
			return copyText;
		}
		public void setCopyText(String copyText) {
			this.copyText = copyText;
		}
		public Thread getLoadThread() {
			return loadThread;
		}
		public void setLoadThread(Thread loadThread) {
			this.loadThread = loadThread;
		}
		public Thread getLoopThread() {
			return loopThread;
		}
		public void setLoopThread(Thread loopThread) {
			this.loopThread = loopThread;
		}
		public static boolean isPaused() {
			return paused;
		}
		public static void setPaused(boolean paused) {
			information.paused = paused;
		}
		public static int getScore() {
			return score;
		}
		public static void setScore(int score) {
			information.score = score;
		}
		public static int getHighScore() {
			return highScore;
		}
		public static void setHighScore(int highScore) {
			information.highScore = highScore;
		}
		public static int getNewShipScore() {
			return newShipScore;
		}
		public static void setNewShipScore(int newShipScore) {
			information.newShipScore = newShipScore;
		}
		public static int getNewUfoScore() {
			return newUfoScore;
		}
		public static void setNewUfoScore(int newUfoScore) {
			information.newUfoScore = newUfoScore;
		}
		public static boolean isLoaded() {
			return loaded;
		}
		public static void setLoaded(boolean loaded) {
			information.loaded = loaded;
		}
		public static boolean isPlaying() {
			return playing;
		}
		public static void setPlaying(boolean playing) {
			information.playing = playing;
		}
		public static boolean isSound() {
			return sound;
		}
		public static void setSound(boolean sound) {
			information.sound = sound;
		}
		public static boolean isDetail() {
			return detail;
		}
		public static void setDetail(boolean detail) {
			information.detail = detail;
		}
		public boolean isLeft() {
			return left;
		}
		public void setLeft(boolean left) {
			this.left = left;
		}
		public boolean isRight() {
			return right;
		}
		public void setRight(boolean right) {
			this.right = right;
		}
		public boolean isUp() {
			return up;
		}
		public void setUp(boolean up) {
			this.up = up;
		}
		public boolean isDown() {
			return down;
		}
		public void setDown(boolean down) {
			this.down = down;
		}
		public AsteroidsSprite getShip() {
			return ship;
		}
		public void setShip(AsteroidsSprite ship) {
			this.ship = ship;
		}
		public AsteroidsSprite getFwdThruster() {
			return fwdThruster;
		}
		public void setFwdThruster(AsteroidsSprite fwdThruster) {
			this.fwdThruster = fwdThruster;
		}
		public AsteroidsSprite getRevThruster() {
			return revThruster;
		}
		public void setRevThruster(AsteroidsSprite revThruster) {
			this.revThruster = revThruster;
		}
		public AsteroidsSprite getUfo() {
			return ufo;
		}
		public void setUfo(AsteroidsSprite ufo) {
			this.ufo = ufo;
		}
		public AsteroidsSprite getMissle() {
			return missle;
		}
		public void setMissle(AsteroidsSprite missle) {
			this.missle = missle;
		}
		public AsteroidsSprite getPhoton() {
			return photon;
		}
		public void setPhoton(AsteroidsSprite photon) {
			this.photon = photon;
		}
		public AsteroidsSprite getExplosion() {
			return explosion;
		}
		public void setExplosion(AsteroidsSprite explosion) {
			this.explosion = explosion;
		}
		public AsteroidsSprite getAsteroid() {
			return asteroid;
		}
		public void setAsteroid(AsteroidsSprite asteroid) {
			this.asteroid = asteroid;
		}
		public AsteroidsSprite[] getPhotons() {
			return photons;
		}
		public void setPhotons(AsteroidsSprite[] photons) {
			this.photons = photons;
		}
		public AsteroidsSprite[] getAsteroids() {
			return asteroids;
		}
		public void setAsteroids(AsteroidsSprite[] asteroids) {
			this.asteroids = asteroids;
		}
		public AsteroidsSprite[] getExplosions() {
			return explosions;
		}
		public void setExplosions(AsteroidsSprite[] explosions) {
			this.explosions = explosions;
		}
		public KeyInputs getKeyInputs() {
			return keyInputs;
		}
		public void setKeyInputs(KeyInputs keyInputs) {
			this.keyInputs = keyInputs;
		}
		public static int getShipsLeft() {
			return shipsLeft;
		}
		public static void setShipsLeft(int shipsLeft) {
			information.shipsLeft = shipsLeft;
		}
		public static int getShipCounter() {
			return shipCounter;
		}
		public static void setShipCounter(int shipCounter) {
			information.shipCounter = shipCounter;
		}
		public static int getHyperCounter() {
			return hyperCounter;
		}
		public static void setHyperCounter(int hyperCounter) {
			information.hyperCounter = hyperCounter;
		}
		public static int getPhotonIndex() {
			return photonIndex;
		}
		public static void setPhotonIndex(int photonIndex) {
			information.photonIndex = photonIndex;
		}
		public long getPhotonTime() {
			return photonTime;
		}
		public void setPhotonTime(long photonTime) {
			this.photonTime = photonTime;
		}
		public static int getUfoPassesLeft() {
			return ufoPassesLeft;
		}
		public static void setUfoPassesLeft(int ufoPassesLeft) {
			information.ufoPassesLeft = ufoPassesLeft;
		}
		public static int getUfoCounter() {
			return ufoCounter;
		}
		public static void setUfoCounter(int ufoCounter) {
			information.ufoCounter = ufoCounter;
		}
		public static int getMissleCounter() {
			return missleCounter;
		}
		public static void setMissleCounter(int missleCounter) {
			information.missleCounter = missleCounter;
		}
		public boolean[] getAsteroidIsSmall() {
			return asteroidIsSmall;
		}
		public void setAsteroidIsSmall(boolean[] asteroidIsSmall) {
			this.asteroidIsSmall = asteroidIsSmall;
		}
		public static int getAsteroidsCounter() {
			return asteroidsCounter;
		}
		public static void setAsteroidsCounter(int asteroidsCounter) {
			information.asteroidsCounter = asteroidsCounter;
		}
		public static double getAsteroidsSpeed() {
			return asteroidsSpeed;
		}
		public static void setAsteroidsSpeed(double asteroidsSpeed) {
			information.asteroidsSpeed = asteroidsSpeed;
		}
		public static int getAsteroidsLeft() {
			return asteroidsLeft;
		}
		public static void setAsteroidsLeft(int asteroidsLeft) {
			information.asteroidsLeft = asteroidsLeft;
		}
		public static int[] getExplosionCounter() {
			return explosionCounter;
		}
		public static void setExplosionCounter(int[] explosionCounter) {
			information.explosionCounter = explosionCounter;
		}
		public static int getExplosionIndex() {
			return explosionIndex;
		}
		public static void setExplosionIndex(int explosionIndex) {
			information.explosionIndex = explosionIndex;
		}
		public static AudioClip getCrashSound() {
			return crashSound;
		}
		public static void setCrashSound(AudioClip crashSound) {
			information.crashSound = crashSound;
		}
		public static AudioClip getExplosionSound() {
			return explosionSound;
		}
		public static void setExplosionSound(AudioClip explosionSound) {
			information.explosionSound = explosionSound;
		}
		public static AudioClip getFireSound() {
			return fireSound;
		}
		public static void setFireSound(AudioClip fireSound) {
			information.fireSound = fireSound;
		}
		public static AudioClip getMissleSound() {
			return missleSound;
		}
		public static void setMissleSound(AudioClip missleSound) {
			information.missleSound = missleSound;
		}
		public static AudioClip getSaucerSound() {
			return saucerSound;
		}
		public static void setSaucerSound(AudioClip saucerSound) {
			information.saucerSound = saucerSound;
		}
		public static AudioClip getThrustersSound() {
			return thrustersSound;
		}
		public static void setThrustersSound(AudioClip thrustersSound) {
			information.thrustersSound = thrustersSound;
		}
		public static AudioClip getWarpSound() {
			return warpSound;
		}
		public static void setWarpSound(AudioClip warpSound) {
			information.warpSound = warpSound;
		}
		public static boolean isThrustersPlaying() {
			return thrustersPlaying;
		}
		public static void setThrustersPlaying(boolean thrustersPlaying) {
			information.thrustersPlaying = thrustersPlaying;
		}
		public static boolean isSaucerPlaying() {
			return saucerPlaying;
		}
		public static void setSaucerPlaying(boolean saucerPlaying) {
			information.saucerPlaying = saucerPlaying;
		}
		public static boolean isMisslePlaying() {
			return misslePlaying;
		}
		public static void setMisslePlaying(boolean misslePlaying) {
			information.misslePlaying = misslePlaying;
		}
		public int getClipTotal() {
			return clipTotal;
		}
		public void setClipTotal(int clipTotal) {
			this.clipTotal = clipTotal;
		}
		public int getClipsLoaded() {
			return clipsLoaded;
		}
		public void setClipsLoaded(int clipsLoaded) {
			this.clipsLoaded = clipsLoaded;
		}
		public Dimension getOffDimension() {
			return offDimension;
		}
		public void setOffDimension(Dimension offDimension) {
			this.offDimension = offDimension;
		}
		public Image getOffImage() {
			return offImage;
		}
		public void setOffImage(Image offImage) {
			this.offImage = offImage;
		}
		public Graphics getOffGraphics() {
			return offGraphics;
		}
		public void setOffGraphics(Graphics offGraphics) {
			this.offGraphics = offGraphics;
		}
		public Font getFont() {
			return font;
		}
		public void setFont(Font font) {
			this.font = font;
		}
		public FontMetrics getFm() {
			return fm;
		}
		public void setFm(FontMetrics fm) {
			this.fm = fm;
		}
		public int getFontWidth() {
			return fontWidth;
		}
		public void setFontWidth(int fontWidth) {
			this.fontWidth = fontWidth;
		}
		public int getFontHeight() {
			return fontHeight;
		}
		public void setFontHeight(int fontHeight) {
			this.fontHeight = fontHeight;
		}
		public int getNumStars() {
			return numStars;
		}
		public void setNumStars(int numStars) {
			this.numStars = numStars;
		}
		public Point[] getStars() {
			return stars;
		}
		public void setStars(Point[] stars) {
			this.stars = stars;
		}
		public static int getDelay() {
			return DELAY;
		}
		public static int getFps() {
			return FPS;
		}
		public static int getMaxShots() {
			return MAX_SHOTS;
		}
		public static int getMaxRocks() {
			return MAX_ROCKS;
		}
		public static int getMaxScrap() {
			return MAX_SCRAP;
		}
		public static int getScrapCount() {
			return SCRAP_COUNT;
		}
		public static int getHyperCount() {
			return HYPER_COUNT;
		}
		public static int getMissleCount() {
			return MISSLE_COUNT;
		}
		public static int getStormPause() {
			return STORM_PAUSE;
		}
		public static int getMinRockSides() {
			return MIN_ROCK_SIDES;
		}
		public static int getMaxRockSides() {
			return MAX_ROCK_SIDES;
		}
		public static int getMinRockSize() {
			return MIN_ROCK_SIZE;
		}
		public static int getMaxRockSize() {
			return MAX_ROCK_SIZE;
		}
		public static double getMinRockSpeed() {
			return MIN_ROCK_SPEED;
		}
		public static double getMaxRockSpeed() {
			return MAX_ROCK_SPEED;
		}
		public static double getMaxRockSpin() {
			return MAX_ROCK_SPIN;
		}
		public static int getMaxShips() {
			return MAX_SHIPS;
		}
		public static int getUfoPasses() {
			return UFO_PASSES;
		}
		public static int getFireDelay() {
			return FIRE_DELAY;
		}
		public static double getMissleProbability() {
			return MISSLE_PROBABILITY;
		}
		public static int getBigPoints() {
			return BIG_POINTS;
		}
		public static int getSmallPoints() {
			return SMALL_POINTS;
		}
		public static int getUfoPoints() {
			return UFO_POINTS;
		}
		public static int getMisslePoints() {
			return MISSLE_POINTS;
		}
		public static int getNewShipPoints() {
			return NEW_SHIP_POINTS;
		}
		public static int getNewUfoPoints() {
			return NEW_UFO_POINTS;
		}
}
