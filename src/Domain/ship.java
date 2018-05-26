package Domain;
import java.applet.AudioClip;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class ship extends AsteroidsSprite {
		information info = new information();
		 public static final int DELAY = 20;             // Milliseconds between screen and
		   public static final int FPS   =                 // the resulting frame rate.
		    Math.round(1000 / DELAY);
	 public  static final double MAX_ROCK_SPEED = 240.0 / FPS;
	 public static final double MAX_SHIP_SPEED  = 1.25 * MAX_ROCK_SPEED;
	  // Ship's rotation and acceleration rates and maximum speed.

	 public  static final double SHIP_ANGLE_STEP = Math.PI / FPS;
	 public  static final double SHIP_SPEED_STEP = 15.0 / FPS;
	  
	 public boolean initShip(AsteroidsSprite fwdThruster, AsteroidsSprite revThruster, boolean loaded, AudioClip thrustersSound, int hyperCounter) {

		    // Reset the ship sprite at the center of the screen.

		    this.active = true;
		    angle = 0.0;
		    deltaAngle = 0.0;
		    x = 0.0;
		    y = 0.0;
		    deltaX = 0.0;
		    deltaY = 0.0;
		    render();

		    // Initialize thruster sprites.

		    fwdThruster.x = x;
		    fwdThruster.y = y;
		    fwdThruster.angle = angle;
		    fwdThruster.render();
		    revThruster.x = x;
		    revThruster.y = y;
		    revThruster.angle = angle;
		    revThruster.render();

		    if (loaded)
		      thrustersSound.stop();
		    hyperCounter = 0;
		    return false;
		  }

		  

		  public boolean stopShip() {

		    active = false;
		    info.shipCounter = info.SCRAP_COUNT;
		    if (info.shipsLeft > 0)
		    	info.shipsLeft--;
		    if (info.loaded)
		    	info.thrustersSound.stop();
		    return false;
		  }
}
