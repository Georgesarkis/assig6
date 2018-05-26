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
	

		  
		  
	public String copyName = "Asteroids";
	  public String copyVers = "Version 1.3";
	  public String copyInfo = "Copyright 1998-2001 by Mike Hall";
	  public String copyLink = "http://www.brainjar.com";
	  public  String copyText = copyName + '\n' + copyVers + '\n'
	                  + copyInfo + '\n' + copyLink;

	  // Thread control variables.

	  public Thread loadThread;
	  public Thread loopThread;

	  // Constants
	   public static boolean paused;
	   public static final int DELAY = 20;             // Milliseconds between screen and
	   public static final int FPS   =                 // the resulting frame rate.
	    Math.round(1000 / DELAY);

	   public static final int MAX_SHOTS =  8;          // Maximum number of sprites
	   public static final int MAX_ROCKS =  8;          // for photons, asteroids and
	   public static final int MAX_SCRAP = 40;          // explosions.

	   public static final int SCRAP_COUNT  = 2 * FPS;  // Timer counter starting values
	   public static final int HYPER_COUNT  = 3 * FPS;  // calculated using number of
	   public static final int MISSLE_COUNT = 4 * FPS;  // seconds x frames per second.
	   public static final int STORM_PAUSE  = 2 * FPS;

	   public static final int    MIN_ROCK_SIDES =   6; // Ranges for asteroid shape, size
	   public static final int    MAX_ROCK_SIDES =  16; // speed and rotation.
	   public static final int    MIN_ROCK_SIZE  =  20;
	   public static final int    MAX_ROCK_SIZE  =  40;
	   public static final double MIN_ROCK_SPEED =  40.0 / FPS;
	   public static final double MAX_ROCK_SPEED = 240.0 / FPS;
	   public static final double MAX_ROCK_SPIN  = Math.PI / FPS;

	   public static final int MAX_SHIPS = 3;           // Starting number of ships for
	                                           // each game.
	   public static final int UFO_PASSES = 3;          // Number of passes for flying
	                                            // saucer per appearance.




	   public static final int FIRE_DELAY = 50;         // Minimum number of milliseconds
	                                           // required between photon shots.

	  // Probablility of flying saucer firing a missle during any given frame
	  // (other conditions must be met).

	   public static final double MISSLE_PROBABILITY = 0.45 / FPS;

	   public static final int BIG_POINTS    =  25;     // Points scored for shooting
	   public static final int SMALL_POINTS  =  50;     // various objects.
	   public static final int UFO_POINTS    = 250;
	   public static final int MISSLE_POINTS = 500;

	  // Number of points the must be scored to earn a new ship or to cause the
	  // flying saucer to appear.

	  public static final int NEW_SHIP_POINTS = 5000;
	  public static final int NEW_UFO_POINTS  = 2750;



	  // Game data.

	  public static int score;
	  public static int highScore;
	  public static int newShipScore;
	  public static int newUfoScore;

	  // Flags for game state and options.

	   public static boolean loaded = false;
	   public static boolean playing;
	   public static boolean sound;
	   public static boolean detail;

	  // Key flags.

	  public boolean left  = false;
	  public  boolean right = false;
	  public boolean up    = false;
	  public  boolean down  = false;

	  // Sprite objects.

	  public AsteroidsSprite   ship;
	  public AsteroidsSprite   fwdThruster, revThruster;
	  public AsteroidsSprite   ufo;
	  public AsteroidsSprite   missle;
	  public AsteroidsSprite photon = new Photons();
	  public AsteroidsSprite explosion = new explode();
	  public  AsteroidsSprite asteroid = new Asteroid();
	 
	  public AsteroidsSprite[] photons    = new AsteroidsSprite[MAX_SHOTS];
	  public AsteroidsSprite[] asteroids  = new AsteroidsSprite[MAX_ROCKS];
	  public AsteroidsSprite[] explosions = new AsteroidsSprite[MAX_SCRAP];
	  public KeyInputs keyInputs = new KeyInputs();

	  // Ship data.

	  public static int shipsLeft;       // Number of ships left in game, including current one.
	  public static int shipCounter;     // Timer counter for ship explosion.
	  public static int hyperCounter;    // Timer counter for hyperspace.

	  // Photon data.

	   public static int   photonIndex;    // Index to next available photon sprite.
	   public long  photonTime;     // Time value used to keep firing rate constant.

	  // Flying saucer data.

	   public static int ufoPassesLeft;    // Counter for number of flying saucer passes.
	   public static int ufoCounter;       // Timer counter used to track each flying saucer pass.

	  // Missle data.

	   public static int missleCounter;    // Counter for life of missle.

	  // Asteroid data.

	   public boolean[] asteroidIsSmall = new boolean[MAX_ROCKS];    // Asteroid size flag.
	   public static int       asteroidsCounter;                            // Break-time counter.
	   public static double    asteroidsSpeed;                              // Asteroid speed.
	   public static int       asteroidsLeft;                               // Number of active asteroids.

	  // Explosion data.

	   public static int[] explosionCounter = new int[MAX_SCRAP];  // Time counters for explosions.
	   public static int   explosionIndex;                         // Next available explosion sprite.

	  // Sound clips.

	   public static AudioClip crashSound;
	   public static AudioClip explosionSound;
	   public static AudioClip fireSound;
	   public static AudioClip missleSound;
	   public static AudioClip saucerSound;
	   public static AudioClip thrustersSound;
	   public static AudioClip warpSound;

	  // Flags for looping sound clips.

	   public boolean thrustersPlaying;
	   public  boolean saucerPlaying;
	   public  boolean misslePlaying;

	  // Counter and total used to track the loading of the sound clips.

	   public  int clipTotal   = 0;
	   public  int clipsLoaded = 0;

	  // Off screen image.

	  public Dimension offDimension;
	  public Image     offImage;
	  public  Graphics  offGraphics;

	  // Data for the screen font.

	  public  Font font      = new Font("Helvetica", Font.BOLD, 12);
	  public FontMetrics fm = getFontMetrics(font);
	   public int fontWidth  = fm.getMaxAdvance();
	   public int fontHeight = fm.getHeight();
	    
	    public int     numStars;
	    public  Point[] stars;
		
}
