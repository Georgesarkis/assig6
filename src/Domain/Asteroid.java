package Domain;
import java.awt.Polygon;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class Asteroid extends AsteroidsSprite{
	information info = new information();
	explode explode1 = new explode();

	  public void initAsteroids(AsteroidsSprite[] asteroids, boolean[] asteroidIsSmall) {

		    int i, j;
		    int s;
		    double theta, r;
		    int x, y;

		    // Create random shapes, positions and movements for each asteroid.

		    for (i = 0; i < info.MAX_ROCKS; i++) {

		      // Create a jagged shape for the asteroid and give it a random rotation.

		      asteroids[i].shape = new Polygon();
		      s = info.MIN_ROCK_SIDES + (int) (Math.random() * (info.MAX_ROCK_SIDES - info.MIN_ROCK_SIDES));
		      for (j = 0; j < s; j ++) {
		        theta = 2 * Math.PI / s * j;
		        r = info.MIN_ROCK_SIZE + (int) (Math.random() * (info.MAX_ROCK_SIZE - info.MIN_ROCK_SIZE));
		        x = (int) -Math.round(r * Math.sin(theta));
		        y = (int)  Math.round(r * Math.cos(theta));
		        asteroids[i].shape.addPoint(x, y);
		      }
		      asteroids[i].active = true;
		      asteroids[i].angle = 0.0;
		      asteroids[i].deltaAngle = Math.random() * 2 * info.MAX_ROCK_SPIN - info.MAX_ROCK_SPIN;

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

		      asteroids[i].deltaX = Math.random() * info.asteroidsSpeed;
		      if (Math.random() < 0.5)
		        asteroids[i].deltaX = -asteroids[i].deltaX;
		      asteroids[i].deltaY = Math.random() * info.asteroidsSpeed;
		      if (Math.random() < 0.5)
		        asteroids[i].deltaY = -asteroids[i].deltaY;

		      asteroids[i].render();
		      asteroidIsSmall[i] = false;
		    }

		    info.asteroidsCounter = info.STORM_PAUSE;
		    info.asteroidsLeft = info.MAX_ROCKS;
		    if (info.asteroidsSpeed < info.MAX_ROCK_SPEED)
		    	info.asteroidsSpeed += 0.5;
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
		        s = info.MIN_ROCK_SIDES + (int) (Math.random() * (info.MAX_ROCK_SIDES - info.MIN_ROCK_SIDES));
		        for (j = 0; j < s; j ++) {
		          theta = 2 * Math.PI / s * j;
		          r = (info.MIN_ROCK_SIZE + (int) (Math.random() * (info.MAX_ROCK_SIZE - info.MIN_ROCK_SIZE))) / 2;
		          x = (int) -Math.round(r * Math.sin(theta));
		          y = (int)  Math.round(r * Math.cos(theta));
		          asteroids[i].shape.addPoint(x, y);
		        }
		        asteroids[i].active = true;
		        asteroids[i].angle = 0.0;
		        asteroids[i].deltaAngle = Math.random() * 2 * info.MAX_ROCK_SPIN - info.MAX_ROCK_SPIN;
		        asteroids[i].x = tempX;
		        asteroids[i].y = tempY;
		        asteroids[i].deltaX = Math.random() * 2 * info.asteroidsSpeed -info.asteroidsSpeed;
		        asteroids[i].deltaY = Math.random() * 2 *info.asteroidsSpeed - info.asteroidsSpeed;
		        asteroids[i].render();
		        asteroidIsSmall[i] = true;
		        count++;
		        info.asteroidsLeft++;
		      }
		      i++;
		    } while (i < info.MAX_ROCKS && count < 2);
		  }

		  public void updateAsteroids(AsteroidsSprite[] asteroids, AsteroidsSprite[] photons, boolean[] asteroidIsSmall, AsteroidsSprite ship, AsteroidsSprite ufo, AsteroidsSprite missle, AsteroidsSprite[] explosions) {

		    int i, j;

		    // Move any active asteroids and check for collisions.

		    for (i = 0; i < info.MAX_ROCKS; i++)
		      if (asteroids[i].active) {
		    	  asteroids[i].advance();
		    	  asteroids[i].render();

		        // If hit by photon, kill asteroid and advance score. If asteroid is
		        // large, make some smaller ones to replace it.

		        for (j = 0; j < info.MAX_SHOTS; j++)
		          if (photons[j].active && asteroids[i].active && asteroids[i].isColliding(photons[j])) {
		        	  info.asteroidsLeft--;
		            asteroids[i].active = false;
		            photons[j].active = false;
		            if (info.sound)
		            	info.explosionSound.play();
		            explode1.explode(asteroids[i],explosions);
		            if (!asteroidIsSmall[i]) {
		            	info.score += info.BIG_POINTS;
		              initSmallAsteroids(i,asteroidIsSmall,asteroids);
		            }
		            else
		            	info.score += info.SMALL_POINTS;
		          }

		        // If the ship is not in hyperspace, see if it is hit.

		        if (ship.active && info.hyperCounter <= 0 &&
		            asteroids[i].active && asteroids[i].isColliding(ship)) {
		          if (info.sound)
		        	  info.crashSound.play();
		          explode1.explode(ship,explosions);
		          ((ship) ship).stopShip();
		          ((ufo) ufo).stopUfo();
		          ((missle) missle).stopMissle();
		        }
		    }
		  }
}
