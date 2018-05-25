package UI;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

import Application.Asteroids;
import Application.information;
import Domain.missle;
import Domain.ship;
import Domain.ufo;

public class Graph {
	  information info = new information();
	  public void getNumStars(int numstars) {
		  info.setNumStars(numstars);
	  }
	  public void getStars(Point [] star) {
		  info.setStars(star);
	  }


		  public void paint(Graphics g) {

		    Dimension d = getSize();
		    int i;
		    int c;
		    String s;
		    int w, h;
		    int x, y;

		    // Create the off screen graphics context, if no good one exists.

		    if (offGraphics == null || d.width != offDimension.width || d.height != offDimension.height) {
		      offDimension = d;
		      offImage = createImage(d.width, d.height);
		      offGraphics = offImage.getGraphics();
		    }

		    // Fill in background and stars.

		    offGraphics.setColor(Color.black);
		    offGraphics.fillRect(0, 0, d.width, d.height);
		    if (detail) {
		      offGraphics.setColor(Color.white);
		      for (i = 0; i < numStars; i++)
		        offGraphics.drawLine(stars[i].x, stars[i].y, stars[i].x, stars[i].y);
		    }

		    // Draw photon bullets.

		    offGraphics.setColor(Color.white);
		    for (i = 0; i < Asteroids.MAX_SHOTS; i++)
		      if (photons[i].active)
		        offGraphics.drawPolygon(photons[i].sprite);

		    // Draw the guided missle, counter is used to quickly fade color to black
		    // when near expiration.

		    c = Math.min(Asteroids.missleCounter * 24, 255);
		    offGraphics.setColor(new Color(c, c, c));
		    if (missle.active) {
		      offGraphics.drawPolygon(missle.sprite);
		      offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);
		    }

		    // Draw the asteroids.

		    for (i = 0; i < Asteroids.MAX_ROCKS; i++)
		      if (asteroids[i].active) {
		        if (Asteroids.detail) {
		          offGraphics.setColor(Color.black);
		          offGraphics.fillPolygon(asteroids[i].sprite);
		        }
		        offGraphics.setColor(Color.white);
		        offGraphics.drawPolygon(asteroids[i].sprite);
		        offGraphics.drawLine(asteroids[i].sprite.xpoints[asteroids[i].sprite.npoints - 1], asteroids[i].sprite.ypoints[asteroids[i].sprite.npoints - 1],
		                             asteroids[i].sprite.xpoints[0], asteroids[i].sprite.ypoints[0]);
		      }

		    // Draw the flying saucer.

		    if (ufo.active) {
		      if (Asteroids.detail) {
		        offGraphics.setColor(Color.black);
		        offGraphics.fillPolygon(ufo.sprite);
		      }
		      offGraphics.setColor(Color.white);
		      offGraphics.drawPolygon(ufo.sprite);
		      offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);
		    }

		    // Draw the ship, counter is used to fade color to white on hyperspace.

		    c = 255 - (255 / Asteroids.HYPER_COUNT) * Asteroids.hyperCounter;
		    if (ship.active) {
		      if (Asteroids.detail && Asteroids.hyperCounter == 0) {
		        offGraphics.setColor(Color.black);
		        offGraphics.fillPolygon(ship.sprite);
		      }
		      offGraphics.setColor(new Color(c, c, c));
		      offGraphics.drawPolygon(ship.sprite);
		      offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);

		      // Draw thruster exhaust if thrusters are on. Do it randomly to get a
		      // flicker effect.

		      if (!Asteroids.paused && Asteroids.detail && Math.random() < 0.5) {
		        if (up) {
		          offGraphics.drawPolygon(fwdThruster.sprite);
		          offGraphics.drawLine(fwdThruster.sprite.xpoints[fwdThruster.sprite.npoints - 1], fwdThruster.sprite.ypoints[fwdThruster.sprite.npoints - 1],
		                               fwdThruster.sprite.xpoints[0], fwdThruster.sprite.ypoints[0]);
		        }
		        if (down) {
		          offGraphics.drawPolygon(revThruster.sprite);
		          offGraphics.drawLine(revThruster.sprite.xpoints[revThruster.sprite.npoints - 1], revThruster.sprite.ypoints[revThruster.sprite.npoints - 1],
		                               revThruster.sprite.xpoints[0], revThruster.sprite.ypoints[0]);
		        }
		      }
		    }

		    // Draw any explosion debris, counters are used to fade color to black.

		    for (i = 0; i < Asteroids.MAX_SCRAP; i++)
		      if (explosions[i].active) {
		        c = (255 / Asteroids.SCRAP_COUNT) * Asteroids.explosionCounter [i];
		        offGraphics.setColor(new Color(c, c, c));
		        offGraphics.drawPolygon(explosions[i].sprite);
		      }

		    // Display status and messages.

		    offGraphics.setFont(font);
		    offGraphics.setColor(Color.white);

		    offGraphics.drawString("Score: " + Asteroids.score, fontWidth, fontHeight);
		    offGraphics.drawString("Ships: " + Asteroids.shipsLeft, fontWidth, d.height - fontHeight);
		    s = "High: " + Asteroids.highScore;
		    offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), fontHeight);
		    if (!Asteroids.sound) {
		      s = "Mute";
		      offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), d.height - fontHeight);
		    }

		    if (!Asteroids.playing) {
		      s = copyName;
		      offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - 2 * fontHeight);
		      s = copyVers;
		      offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - fontHeight);
		      s = copyInfo;
		      offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + fontHeight);
		      s = copyLink;
		      offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + 2 * fontHeight);
		      if (!Asteroids.loaded) {
		        s = "Loading sounds...";
		        w = 4 * fontWidth + fm.stringWidth(s);
		        h = fontHeight;
		        x = (d.width - w) / 2;
		        y = 3 * d.height / 4 - fm.getMaxAscent();
		        offGraphics.setColor(Color.black);
		          offGraphics.fillRect(x, y, w, h);
		        offGraphics.setColor(Color.gray);
		        if (clipTotal > 0)
		          offGraphics.fillRect(x, y, (int) (w * clipsLoaded / clipTotal), h);
		        offGraphics.setColor(Color.white);
		        offGraphics.drawRect(x, y, w, h);
		        offGraphics.drawString(s, x + 2 * fontWidth, y + fm.getMaxAscent());
		      }
		      else {
		        s = "Game Over";
		        offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
		        s = "'S' to Start";
		        offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4 + fontHeight);
		      }
		    }
		    else if (Asteroids.paused) {
		      s = "Game Paused";
		      offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
		    }

		    // Copy the off screen buffer to the screen.

		    g.drawImage(offImage, 0, 0, (ImageObserver) this);
		  }
}
