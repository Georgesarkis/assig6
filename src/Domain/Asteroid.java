package Domain;
import java.awt.Polygon;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class Asteroid extends AsteroidsSprite{
	information info = new information();

	  public void initAsteroids(AsteroidsSprite[] asteroids, boolean[] asteroidIsSmall) {

		    int i, j;
		    int s;
		    double theta, r;
		    int x, y;

		    // Create random shapes, positions and movements for each asteroid.

		    for (i = 0; i < Asteroids.MAX_ROCKS; i++) {

		      // Create a jagged shape for the asteroid and give it a random rotation.

		      asteroids[i].shape = new Polygon();
		      s = Asteroids.MIN_ROCK_SIDES + (int) (Math.random() * (Asteroids.MAX_ROCK_SIDES - Asteroids.MIN_ROCK_SIDES));
		      for (j = 0; j < s; j ++) {
		        theta = 2 * Math.PI / s * j;
		        r = Asteroids.MIN_ROCK_SIZE + (int) (Math.random() * (Asteroids.MAX_ROCK_SIZE - Asteroids.MIN_ROCK_SIZE));
		        x = (int) -Math.round(r * Math.sin(theta));
		        y = (int)  Math.round(r * Math.cos(theta));
		        asteroids[i].shape.addPoint(x, y);
		      }
		      asteroids[i].active = true;
		      asteroids[i].angle = 0.0;
		      asteroids[i].deltaAngle = Math.random() * 2 * Asteroids.MAX_ROCK_SPIN - Asteroids.MAX_ROCK_SPIN;

		      // Place the asteroid at one edge of the screen.

		      if (Math.random() < 0.5) {
		        asteroids[i].x = -AsteroidsSprite.width / 2;
		        if (Math.random() < 0.5)
		          asteroids[i].x = AsteroidsSprite.width / 2;
		        asteroids[i].y = Math.random() * AsteroidsSprite.height;
		      }
		      else {
		        asteroids[i].x = Math.random() * AsteroidsSprite.width;
		        asteroids[i].y = -AsteroidsSprite.height / 2;
		        if (Math.random() < 0.5)
		          asteroids[i].y = AsteroidsSprite.height / 2;
		      }

		      // Set a random motion for the asteroid.

		      asteroids[i].deltaX = Math.random() * Asteroids.asteroidsSpeed;
		      if (Math.random() < 0.5)
		        asteroids[i].deltaX = -asteroids[i].deltaX;
		      asteroids[i].deltaY = Math.random() * Asteroids.asteroidsSpeed;
		      if (Math.random() < 0.5)
		        asteroids[i].deltaY = -asteroids[i].deltaY;

		      asteroids[i].render();
		      asteroidIsSmall[i] = false;
		    }

		    Asteroids.asteroidsCounter = Asteroids.STORM_PAUSE;
		    Asteroids.asteroidsLeft = Asteroids.MAX_ROCKS;
		    if (Asteroids.asteroidsSpeed < Asteroids.MAX_ROCK_SPEED)
		    	Asteroids.asteroidsSpeed += 0.5;
		  }

		  public void initSmallAsteroids(int n, boolean[] asteroidIsSmall, AsteroidsSprite[] asteroids) {

		    int count;
		    int i, j;
		    int s;
		    double tempX, tempY;
		    double theta, r;
		    int x, y;

		    // Create one or two smaller asteroids from a larger one using inactive
		    // asteroids. The new asteroids will be placed in the same position as the
		    // old one but will have a new, smaller shape and new, randomly generated
		    // movements.

		    count = 0;
		    i = 0;
		    tempX = asteroids[n].x;
		    tempY = asteroids[n].y;
		    do {
		      if (!asteroids[i].active) {
		        asteroids[i].shape = new Polygon();
		        s = Asteroids.MIN_ROCK_SIDES + (int) (Math.random() * (Asteroids.MAX_ROCK_SIDES - Asteroids.MIN_ROCK_SIDES));
		        for (j = 0; j < s; j ++) {
		          theta = 2 * Math.PI / s * j;
		          r = (Asteroids.MIN_ROCK_SIZE + (int) (Math.random() * (Asteroids.MAX_ROCK_SIZE - Asteroids.MIN_ROCK_SIZE))) / 2;
		          x = (int) -Math.round(r * Math.sin(theta));
		          y = (int)  Math.round(r * Math.cos(theta));
		          asteroids[i].shape.addPoint(x, y);
		        }
		        asteroids[i].active = true;
		        asteroids[i].angle = 0.0;
		        asteroids[i].deltaAngle = Math.random() * 2 * Asteroids.MAX_ROCK_SPIN - Asteroids.MAX_ROCK_SPIN;
		        asteroids[i].x = tempX;
		        asteroids[i].y = tempY;
		        asteroids[i].deltaX = Math.random() * 2 * Asteroids.asteroidsSpeed -Asteroids.asteroidsSpeed;
		        asteroids[i].deltaY = Math.random() * 2 *Asteroids.asteroidsSpeed - Asteroids.asteroidsSpeed;
		        asteroids[i].render();
		        asteroidIsSmall[i] = true;
		        count++;
		        Asteroids.asteroidsLeft++;
		      }
		      i++;
		    } while (i < Asteroids.MAX_ROCKS && count < 2);
		  }

		  public void updateAsteroids(AsteroidsSprite[] asteroids, AsteroidsSprite[] photons, boolean[] asteroidIsSmall, AsteroidsSprite ship, AsteroidsSprite ufo, AsteroidsSprite missle, AsteroidsSprite[] explosions) {

		    int i, j;

		    // Move any active asteroids and check for collisions.

		    for (i = 0; i < Asteroids.MAX_ROCKS; i++)
		      if (asteroids[i].active) {
		    	  asteroids[i].advance();
		    	  asteroids[i].render();

		        // If hit by photon, kill asteroid and advance score. If asteroid is
		        // large, make some smaller ones to replace it.

		        for (j = 0; j < Asteroids.MAX_SHOTS; j++)
		          if (photons[j].active && asteroids[i].active && asteroids[i].isColliding(photons[j])) {
		        	  Asteroids.asteroidsLeft--;
		            asteroids[i].active = false;
		            photons[j].active = false;
		            if (Asteroids.sound)
		            	Asteroids.explosionSound.play();
		            explode.explode(asteroids[i],explosions);
		            if (!asteroidIsSmall[i]) {
		            	Asteroids.score += Asteroids.BIG_POINTS;
		              initSmallAsteroids(i,asteroidIsSmall,asteroids);
		            }
		            else
		            	Asteroids.score += Asteroids.SMALL_POINTS;
		          }

		        // If the ship is not in hyperspace, see if it is hit.

		        if (ship.active && Asteroids.hyperCounter <= 0 &&
		            asteroids[i].active && asteroids[i].isColliding(ship)) {
		          if (Asteroids.sound)
		        	  Asteroids.crashSound.play();
		          explode.explode(ship,explosions);
		          ((ship) ship).stopShip();
		          ((ufo) ufo).stopUfo();
		          ((missle) missle).stopMissle();
		        }
		    }
		  }
}
