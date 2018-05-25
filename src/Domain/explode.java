package Domain;
import java.awt.Polygon;

import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class explode extends AsteroidsSprite {
	information info = new information();

	 public static int initExplosions(AsteroidsSprite[] explosions) {

		    int i;

		    for (i = 0; i < Asteroids.MAX_SCRAP; i++) {
		    	explosions[i].shape = new Polygon();
		    	explosions[i].active = false;
		    	Asteroids.explosionCounter[i] = 0;
		    }
		    return  0;
		  }

		  public static int explode(AsteroidsSprite s,AsteroidsSprite[] explosions) {

		    int c, i, j;
		    int cx, cy;

		    // Create sprites for explosion animation. The each individual line segment
		    // of the given sprite is used to create a new sprite that will move
		    // outward  from the sprite's original position with a random rotation.

		    s.render();
		    c = 2;
		    if (Asteroids.detail || s.sprite.npoints < 6)
		      c = 1;
		    for (i = 0; i < s.sprite.npoints; i += c) {
		    	Asteroids.explosionIndex++;
		      if (Asteroids.explosionIndex >= Asteroids.MAX_SCRAP)
		    	  Asteroids.explosionIndex = 0;
		      explosions[Asteroids.explosionIndex].active = true;
		      explosions[Asteroids.explosionIndex].shape = new Polygon();
		      j = i + 1;
		      if (j >= s.sprite.npoints)
		        j -= s.sprite.npoints;
		      cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
		      cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
		      explosions[Asteroids.explosionIndex].shape.addPoint(
		        s.shape.xpoints[i] - cx,
		        s.shape.ypoints[i] - cy);
		      explosions[Asteroids.explosionIndex].shape.addPoint(
		        s.shape.xpoints[j] - cx,
		        s.shape.ypoints[j] - cy);
		      explosions[Asteroids.explosionIndex].x = s.x + cx;
		      explosions[Asteroids.explosionIndex].y = s.y + cy;
		      explosions[Asteroids.explosionIndex].angle = s.angle;
		      explosions[Asteroids.explosionIndex].deltaAngle = 4 * (Math.random() * 2 * Asteroids.MAX_ROCK_SPIN - Asteroids.MAX_ROCK_SPIN);
		      explosions[Asteroids.explosionIndex].deltaX = (Math.random() * 2 * Asteroids.MAX_ROCK_SPEED - Asteroids.MAX_ROCK_SPEED + s.deltaX) / 2;
		      explosions[Asteroids.explosionIndex].deltaY = (Math.random() * 2 * Asteroids.MAX_ROCK_SPEED - Asteroids.MAX_ROCK_SPEED + s.deltaY) / 2;
		      Asteroids.explosionCounter[Asteroids.explosionIndex] = Asteroids.SCRAP_COUNT;
		    }
		    return Asteroids.explosionIndex;
		  }

		  public static AsteroidsSprite[] updateExplosions(AsteroidsSprite[] explosions, int[] explosionCounter) {

		    int i;
		    
		    // Move any active explosion debris. Stop explosion when its counter has
		    // expired.
		    	
		    for (i = 0; i < Asteroids.MAX_SCRAP; i++)
		      if (explosions[i].active) {
		        explosions[i].advance();
		        explosions[i].render();
		        if (--explosionCounter[i] < 0)
		        explosions[i].active = false;
		      }
		    return explosions;
		  }

}
