package Domain;
import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class missle extends AsteroidsSprite {
	information info = new information();

	public void initMissle(AsteroidsSprite ufo) {

	    active = true;
	    angle = 0.0;
	    deltaAngle = 0.0;
	    x = ufo.x;
	    y = ufo.y;
	    deltaX = 0.0;
	    deltaY = 0.0;
	    render();
	    Asteroids.missleCounter = Asteroids.MISSLE_COUNT;
	    if (Asteroids.sound)
	      Asteroids.missleSound.loop();
	    Asteroids.misslePlaying = true;
	  }

	  public void updateMissle(AsteroidsSprite[] photons, AsteroidsSprite ship, AsteroidsSprite ufo, AsteroidsSprite explosion) {

	    int i;

	    // Move the guided missle and check for collision with ship or photon. Stop
	    // it when its counter has expired.

	    if (active) {
	      if (--Asteroids.missleCounter <= 0)
	        stopMissle();
	      else {
	        guideMissle(ship);
	        advance();
	        render();
	        for (i = 0; i < Asteroids.MAX_SHOTS; i++)
	          if (photons[i].active && isColliding(photons[i])) {
	            if (Asteroids.sound)
	            	Asteroids.crashSound.play();
	            ((explode) explosion).explode(explosion, photons);
	            stopMissle();
	            Asteroids.score += Asteroids.MISSLE_POINTS;
	          }
	        if (active && ship.active &&
	        		Asteroids.hyperCounter <= 0 && ship.isColliding(this)) {
	          if (Asteroids.sound)
	        	  Asteroids.crashSound.play();
	          ((explode) explosion).explode(explosion, photons);
	          ((ship) ship).stopShip();
	          ((ufo) ufo).stopUfo();
	          stopMissle();
	        }
	      }
	    }
	  }

	  public void guideMissle(AsteroidsSprite ship) {

	    double dx, dy, angle;

	    if (!ship.active || Asteroids.hyperCounter > 0)
	      return;

	    // Find the angle needed to hit the ship.

	    dx = ship.x - x;
	    dy = ship.y - y;
	    if (dx == 0 && dy == 0)
	      angle = 0;
	    if (dx == 0) {
	      if (dy < 0)
	        angle = -Math.PI / 2;
	      else
	        angle = Math.PI / 2;
	    }
	    else {
	      angle = Math.atan(Math.abs(dy / dx));
	      if (dy > 0)
	        angle = -angle;
	      if (dx < 0)
	        angle = Math.PI - angle;
	    }

	    // Adjust angle for screen coordinates.

	    angle = angle - Math.PI / 2;

	    // Change the missle's angle so that it points toward the ship.

	    deltaX = 0.75 * Asteroids.MAX_ROCK_SPEED * -Math.sin(angle);
	    deltaY = 0.75 * Asteroids.MAX_ROCK_SPEED *  Math.cos(angle);
	  }

	  public void stopMissle() {

	    active = false;
	    Asteroids.missleCounter = 0;
	    if (Asteroids.loaded)
	    	Asteroids.missleSound.stop();
	    Asteroids. misslePlaying = false;
	  }

}
