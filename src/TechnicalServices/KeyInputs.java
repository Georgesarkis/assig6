package TechnicalServices;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;

import Application.information;
import Domain.AsteroidsSprite;

public class KeyInputs {
	 information info = new information();
	public void keyPressed(KeyEvent e) {
	 
	  char c;
	
	  // Check if any cursor keys have been pressed and set flags.
	
	  if (e.getKeyCode() == KeyEvent.VK_LEFT)
		  info.left = true;
	  if (e.getKeyCode() == KeyEvent.VK_RIGHT)
	    info.right = true;
	  if (e.getKeyCode() == KeyEvent.VK_UP)
	    info.up = true;
	  if (e.getKeyCode() == KeyEvent.VK_DOWN)
	    info.down =  true;
	
	  if ((info.up || info.down) && info.ship.active && !info.thrustersPlaying) {
	    if (info.sound && !Asterodis.p)
	      info.thrustersSound.loop();
	    info.thrustersPlaying= true;
	  }
	
	  // Spacebar: fire a photon and start its counter.
	
	  if (e.getKeyChar() == ' ' && info.ship.active) {
	    if (info.sound & !info.paused)
	      info.fireSound.play();
	    info.photonTime =  System.currentTimeMillis();
	    info.photonIndex = info.photonIndex+1;
	    if (info.photonIndex >= info.MAX_SHOTS)
	    	info.photonIndex = 0;
	    info.photons[info.photonIndex].active = true;
	    info.photons[info.photonIndex].x = info.ship.x;
	    info.photons[info.photonIndex].y = info.ship.y;
	    info.photons[info.photonIndex].deltaX = 2 * info.MAX_ROCK_SPEED * -Math.sin(info.ship.angle);
	    info.photons[info.photonIndex].deltaY = 2 * info.MAX_ROCK_SPEED *  Math.cos(info.ship.angle);
	  }
	
	  // Allow upper or lower case characters for remaining keys.
	
	  c = Character.toLowerCase(e.getKeyChar());
	
	  // 'H' key: warp ship into hyperspace by moving to a random location and
	  // starting counter.
	
	  if (c == 'h' && info.ship.active && info.hyperCounter <= 0) {
		  info.ship.x = Math.random() * AsteroidsSprite.width;
		  info.ship.y = Math.random() * AsteroidsSprite.height;
		  info.hyperCounter = info.HYPER_COUNT;
	    if (info.sound & !info.paused)
	    	info.warpSound.play();
	  }
	
	  // 'P' key: toggle pause mode and start or stop any active looping sound
	  // clips.
	
	  if (c == 'p') {
	    if (info.paused) {
	      if (info.sound && info.misslePlaying)
	    	  info.missleSound.loop();
	      if (info.sound && info.saucerPlaying)
	    	  info.saucerSound.loop();
	      if (info.sound && info.thrustersPlaying)
	    	  info.thrustersSound.loop();
	    }
	    else {
	      if (info.misslePlaying)
	    	  info.missleSound.stop();
	      if (info.saucerPlaying)
	    	  info.saucerSound.stop();
	      if (info.thrustersPlaying)
	    	  info.thrustersSound.stop();
	    }
	    info.paused = !info.paused;
	  }
	
	  // 'M' key: toggle sound on or off and stop any looping sound clips.
	
	  if (c == 'm' && info.loaded) {
	    if (info.sound) {
	    	info.crashSound.stop();
	    	info.explosionSound.stop();
	    	info.fireSound.stop();
	    	info.missleSound.stop();
	    	info.saucerSound.stop();
	    	info.thrustersSound.stop();
	    	info.warpSound.stop();
	    }
	    else {
	      if (info.misslePlaying && !info.paused)
	    	  info.missleSound.loop();
	      if (info.saucerPlaying && !info.paused)
	    	  info.saucerSound.loop();
	      if (info.thrustersPlaying && !info.paused)
	    	  info.thrustersSound.loop();
	    }
	    info.sound = !info.sound;
	  }
	
	  // 'D' key: toggle graphics detail on or off.
	
	  if (c == 'd')
		  info.detail = !info.detail;
	
	  // 'S' key: start the game, if not already in progress.
	
	  if (c == 's' && info.loaded && !info.playing)
		  initGame();
	
	  // 'HOME' key: jump to web site (undocumented).
	
	  if (e.getKeyCode() == KeyEvent.VK_HOME)
	    try {
	    	getAppletContext().showDocument(new URL(info.copyLink));
	    }
	    catch (Exception excp) {}
	}
	
	public void keyReleased(KeyEvent e) {
	
	  // Check if any cursor keys where released and set flags.
	
	  if (e.getKeyCode() == KeyEvent.VK_LEFT)
		  info.left = false;
	  if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		  info.right = false;
	  if (e.getKeyCode() == KeyEvent.VK_UP)
		  info.up = false;
	  if (e.getKeyCode() == KeyEvent.VK_DOWN)
		  info.down = false;
	
	  if (!info.up && !info.down && info.thrustersPlaying) {
		  info.thrustersSound.stop();
		  info.thrustersPlaying = false;
	  }
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public void update(Graphics g) {
	
		paint(g);
	}
}
