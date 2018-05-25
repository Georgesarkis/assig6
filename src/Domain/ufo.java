package Domain;
import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class ufo extends AsteroidsSprite{
	information info = new information();
	  public void initUfo() {

		    double angle, speed;

		    // Randomly set flying saucer at left or right edge of the screen.

		    active = true;
		    x = -AsteroidsSprite.width / 2;
		    y = Math.random() * 2 * AsteroidsSprite.height - AsteroidsSprite.height;
		    angle = Math.random() * Math.PI / 4 - Math.PI / 2;
		    speed = info.getMaxRockSpeed() / 2 + Math.random() * (info.getMaxRockSpeed() / 2);
		    deltaX = speed * -Math.sin(angle);
		    deltaY = speed *  Math.cos(angle);
		    if (Math.random() < 0.5) {
		      x = AsteroidsSprite.width / 2;
		      deltaX = -deltaX;
		    }
		    if (y > 0)
		      deltaY = deltaY;
		    render();
		    info.setSaucerPlaying(true);
		    if (info.isSound())
		    	info.getSaucerSound().loop();
		    info.setUfoCounter((int) Math.abs(AsteroidsSprite.width / deltaX));
		  }

		  public void updateUfo( AsteroidsSprite[] photons, AsteroidsSprite missle, AsteroidsSprite explosion ) {

		    int i, d;
		    boolean wrapped;

		    // Move the flying saucer and check for collision with a photon. Stop it
		    // when its counter has expired.

		    if (active) {
		      if (--Asteroids.ufoCounter <= 0) {
		        if (--Asteroids.ufoPassesLeft > 0)
		          this.initUfo();
		        else
		          this.stopUfo();
		      }
		      if (active) {
		        advance();
		        render();
		        for (i = 0; i < Asteroids.MAX_SHOTS; i++)
		          if (photons[i].active && isColliding(photons[i])) {
		            if (Asteroids.sound)
		            	Asteroids.crashSound.play();
		            ((explode) explosion).explode(explosion, photons);
		            stopUfo();
		            Asteroids.score += Asteroids.UFO_POINTS;
		          }

		          // On occassion, fire a missle at the ship if the saucer is not too
		          // close to it.

		          d = (int) Math.max(Math.abs(x - x), Math.abs(y - y));
		          if (active && Asteroids.hyperCounter <= 0 &&
		              active && !missle.active &&
		              d > Asteroids.MAX_ROCK_SPEED * Asteroids.FPS / 2 &&
		              Math.random() < Asteroids.MISSLE_PROBABILITY)
		            ((missle) missle).initMissle(explosion);
		       }
		    }
		  }

		  public void stopUfo() {

		    active = false;
		    Asteroids.ufoCounter = 0;
		    Asteroids.ufoPassesLeft = 0;
		    if (Asteroids.loaded)
		    	Asteroids.saucerSound.stop();
		    
		  }

}
