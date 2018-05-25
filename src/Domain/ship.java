package Domain;
import java.applet.AudioClip;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class ship extends AsteroidsSprite {
	information info = new information();
	  static final double MAX_ROCK_SPEED = 240.0 / info.getFPS;
	  static final double MAX_SHIP_SPEED  = 1.25 * MAX_ROCK_SPEED;
	  // Ship's rotation and acceleration rates and maximum speed.

	  static final double SHIP_ANGLE_STEP = Math.PI / info.getFPS;
	  static final double SHIP_SPEED_STEP = 15.0 / info.getFPS;
	  
	 public boolean initShip(AsteroidsSprite fwdThruster, AsteroidsSprite revThruster, boolean loaded, AudioClip thrustersSound, int hyperCounter) {

		    // Reset the ship sprite at the center of the screen.

		    active = true;
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

		  public void updateShip(AsteroidsSprite fwdThruster, AsteroidsSprite revThruster, boolean playing, boolean left, boolean right, boolean up, boolean down, int hyperCounter, int shipCounter, int shipsLeft, int hyperCount) {

		    double dx, dy, speed;

		    if (!playing)
		      return ;

		    // Rotate the ship if left or right cursor key is down.

		    if (left) {
		      angle += SHIP_ANGLE_STEP;
		      if (angle > 2 * Math.PI)
		        angle -= 2 * Math.PI;
		    }
		    if (right) {
		      angle -= SHIP_ANGLE_STEP;
		      if (angle < 0)
		        angle += 2 * Math.PI;
		    }

		    // Fire thrusters if up or down cursor key is down.

		    dx = SHIP_SPEED_STEP * -Math.sin(angle);
		    dy = SHIP_SPEED_STEP *  Math.cos(angle);
		    if (up) {
		      deltaX += dx;
		      deltaY += dy;
		    }
		    if (down) {
		        deltaX -= dx;
		        deltaY -= dy;
		    }

		    // Don't let ship go past the speed limit.

		    if (up || down) {
		      speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		      if (speed > MAX_SHIP_SPEED) {
		        dx = MAX_SHIP_SPEED * -Math.sin(angle);
		        dy = MAX_SHIP_SPEED *  Math.cos(angle);
		        if (up)
		          deltaX = dx;
		        else
		          deltaX = -dx;
		        if (up)
		          deltaY = dy;
		        else
		          deltaY = -dy;
		      }
		    }

		    // Move the ship. If it is currently in hyperspace, advance the countdown.

		    if (active) {
		      advance();
		      render();
		      if (hyperCounter > 0)
		        hyperCounter--;

		      // Update the thruster sprites to match the ship sprite.

		      fwdThruster.x = x;
		      fwdThruster.y = y;
		      fwdThruster.angle = angle;
		      fwdThruster.render();
		      revThruster.x = x;
		      revThruster.y = y;
		      revThruster.angle = angle;
		      revThruster.render();
		    }

		    // Ship is exploding, advance the countdown or create a new ship if it is
		    // done exploding. The new ship is added as though it were in hyperspace.
		    // (This gives the player time to move the ship if it is in imminent
		    // danger.) If that was the last ship, end the game.

		    else
		      if (--shipCounter <= 0)
		        if (shipsLeft > 0) {
		          initShip(revThruster, revThruster, down, null, hyperCount);
		          hyperCounter = Asteroids.HYPER_COUNT;
		        }
		        else
		          endGame();
			
		  }

		  public boolean stopShip() {

		    active = false;
		    Asteroids.shipCounter = Asteroids.SCRAP_COUNT;
		    if (Asteroids.shipsLeft > 0)
		    	Asteroids.shipsLeft--;
		    if (Asteroids.loaded)
		    	Asteroids.thrustersSound.stop();
		    return false;
		  }
}
