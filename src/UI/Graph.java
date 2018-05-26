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

import Application.info;
import Application.information;
import Domain.missle;
import Domain.ship;
import Domain.ufo;

public class Graph {
	  information info = new information();
	  public void getNumStars(int numstars) {
		  info.numStars = numstars;
	  }
	  public void getStars(Point [] star) {
		  info.stars = star;
	  }


		  public void paint(Graphics g) {

		    Dimension d = getSize();
		    int i;
		    int c;
		    String s;
		    int w, h;
		    int x, y;

		    // Create the off screen graphics context, if no good one exists.

		    if (info.offGraphics == null || d.width != info.offDimension.width || d.height !=info.offDimension.height) {
		    	info.offDimension = d;
		    	info.offImage = Asteroids.createImage(d.width, d.height);
		      info.offGraphics = info.offImage.getGraphics();
		    }

		    // Fill in background and stars.

		    info.offGraphics.setColor(Color.black);
		    info.offGraphics.fillRect(0, 0, d.width, d.height);
		    if (info.detail) {
		      info.offGraphics.setColor(Color.white);
		      for (i = 0; i < info.numStars; i++)
		        info.offGraphics.drawLine(info.stars[i].x, info.stars[i].y, info.stars[i].x, info.stars[i].y);
		    }

		    // Draw photon bullets.

		    info.offGraphics.setColor(Color.white);
		    for (i = 0; i < info.MAX_SHOTS; i++)
		      if (info.photons[i].active)
		        info.offGraphics.drawPolygon(info.photons[i].sprite);

		    // Draw the guided missle, counter is used to quickly fade color to black
		    // when near expiration.

		    c = Math.min(info.missleCounter * 24, 255);
		    info.offGraphics.setColor(new Color(c, c, c));
		    if (info.missle.active) {
		      info.offGraphics.drawPolygon(info.missle.sprite);
		      info.offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);
		    }

		    // Draw the info.

		    for (i = 0; i < info.MAX_ROCKS; i++)
		      if (info.active) {
		        if (info.detail) {
		          info.offGraphics.setColor(Color.black);
		          info.offGraphics.fillPolygon(info[i].sprite);
		        }
		        info.offGraphics.setColor(Color.white);
		        info.offGraphics.drawPolygon(Asteroids[i].sprite);
		        info.offGraphics.drawLine(Asteroids[i].sprite.xpoints[Asteroids[i].sprite.npoints - 1], Asteroids[i].sprite.ypoints[Asteroids[i].sprite.npoints - 1],
		        		Asteroids[i].sprite.xpoints[0], Asteroids[i].sprite.ypoints[0]);
		      }

		    // Draw the flying saucer.

		    if (info.ufo.active) {
		      if (info.detail) {
		        info.offGraphics.setColor(Color.black);
		        info.offGraphics.fillPolygon(info.ufo.sprite);
		      }
		      info.offGraphics.setColor(Color.white);
		      info.offGraphics.drawPolygon(info.ufo.sprite);
		      info.offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);
		    }

		    // Draw the ship, counter is used to fade color to white on hyperspace.

		    c = 255 - (255 / info.HYPER_COUNT) * info.hyperCounter;
		    if (info.ship.active) {
		      if (info.detail && info.hyperCounter == 0) {
		        info.offGraphics.setColor(Color.black);
		        info.offGraphics.fillPolygon(info.ship.sprite);
		      }
		      info.offGraphics.setColor(new Color(c, c, c));
		      info.offGraphics.drawPolygon(info.ship.sprite);
		      info.offGraphics.drawLine(Application.sprite.xpoints[Application.sprite.npoints - 1], Application.sprite.ypoints[Application.sprite.npoints - 1],
		                           Application.sprite.xpoints[0], Application.sprite.ypoints[0]);

		      // Draw thruster exhaust if thrusters are on. Do it randomly to get a
		      // flicker effect.

		      if (!info.paused && info.detail && Math.random() < 0.5) {
		        if (info.up) {
		          info.offGraphics.drawPolygon(info.fwdThruster.sprite);
		          info.offGraphics.drawLine(info.fwdThruster.sprite.xpoints[info.fwdThruster.sprite.npoints - 1], info.fwdThruster.sprite.ypoints[info.fwdThruster.sprite.npoints - 1],
		                               info.fwdThruster.sprite.xpoints[0], info.fwdThruster.sprite.ypoints[0]);
		        }
		        if (info.down) {
		          info.offGraphics.drawPolygon(info.revThruster.sprite);
		          info.offGraphics.drawLine(info.revThruster.sprite.xpoints[info.revThruster.sprite.npoints - 1], info.revThruster.sprite.ypoints[info.revThruster.sprite.npoints - 1],
		                               info.revThruster.sprite.xpoints[0], info.revThruster.sprite.ypoints[0]);
		        }
		      }
		    }

		    // Draw any explosion debris, counters are used to fade color to black.

		    for (i = 0; i < info.MAX_SCRAP; i++)
		      if (info.explosions[i].active) {
		        c = (255 / info.SCRAP_COUNT) * info.explosionCounter [i];
		        info.offGraphics.setColor(new Color(c, c, c));
		        info.offGraphics.drawPolygon(info.explosions[i].sprite);
		      }

		    // Display status and messages.

		    info.offGraphics.setFont(info.font);
		    info.offGraphics.setColor(Color.white);

		    info.offGraphics.drawString("Score: " + info.score, info.fontWidth, info.fontHeight);
		    info.offGraphics.drawString("Ships: " + info.shipsLeft, info.fontWidth, d.height - info.fontHeight);
		    s = "High: " + info.highScore;
		    info.offGraphics.drawString(s, d.width - (info.fontWidth + info.fm.stringWidth(s)), info.fontHeight);
		    if (!info.sound) {
		      s = "Mute";
		      info.offGraphics.drawString(s, d.width - (info.fontWidth + info.fm.stringWidth(s)), d.height - info.fontHeight);
		    }

		    if (!info.playing) {
		      s = info.copyName;
		      info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 2 - 2 * info.fontHeight);
		      s = info.copyVers;
		      info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 2 - info.fontHeight);
		      s = info.copyInfo;
		      info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 2 + info.fontHeight);
		      s = info.copyLink;
		      info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 2 + 2 * info.fontHeight);
		      if (!info.loaded) {
		        s = "Loading sounds...";
		        w = 4 * info.fontWidth + info.fm.stringWidth(s);
		        h = info.fontHeight;
		        x = (d.width - w) / 2;
		        y = 3 * d.height / 4 - info.fm.getMaxAscent();
		        info.offGraphics.setColor(Color.black);
		          info.offGraphics.fillRect(x, y, w, h);
		        info.offGraphics.setColor(Color.gray);
		        if (info.clipTotal > 0)
		          info.offGraphics.fillRect(x, y, (int) (w * info.clipsLoaded / info.clipTotal), h);
		        info.offGraphics.setColor(Color.white);
		        info.offGraphics.drawRect(x, y, w, h);
		        info.offGraphics.drawString(s, x + 2 * info.fontWidth, y + info.fm.getMaxAscent());
		      }
		      else {
		        s = "Game Over";
		        info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 4);
		        s = "'S' to Start";
		        info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 4 + info.fontHeight);
		      }
		    }
		    else if (info.paused) {
		      s = "Game Paused";
		      info.offGraphics.drawString(s, (d.width - info.fm.stringWidth(s)) / 2, d.height / 4);
		    }

		    // Copy the off screen buffer to the screen.

		    g.drawImage(info.offImage, 0, 0, (ImageObserver) this);
		  }
		  
}
