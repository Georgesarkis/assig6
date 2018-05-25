package TechnicalServices;


import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

public class AudioClip1 extends Applet {
	private int clipsLoaded =0;
	private int clipTotal = 0;
	
	private static final int DELAY =  20; 
	private AudioClip crashSound;
	private AudioClip explosionSound;
	private AudioClip fireSound;
	private AudioClip missleSound;
	private AudioClip saucerSound;
	private AudioClip thrustersSound;
	private AudioClip warpSound;
	
	public void loadSounds() {

		  // Load all sound clips by playing and immediately stopping them. Update
		  // counter and total for display.

		  try {
		    crashSound     = (AudioClip)getAudioClip(new URL(getCodeBase(), "crash.au"));
		    clipTotal++;
		    explosionSound = (AudioClip)getAudioClip(new URL(getCodeBase(), "explosion.au"));
		    clipTotal++;
		    fireSound      = (AudioClip)getAudioClip(new URL(getCodeBase(), "fire.au"));
		    clipTotal++;
		    missleSound    = (AudioClip)getAudioClip(new URL(getCodeBase(), "missle.au"));
		    clipTotal++;
		    saucerSound    = (AudioClip)getAudioClip(new URL(getCodeBase(), "saucer.au"));
		    clipTotal++;
		    thrustersSound =(AudioClip) getAudioClip(new URL(getCodeBase(), "thrusters.au"));
		    clipTotal++;
		    warpSound      = (AudioClip)getAudioClip(new URL(getCodeBase(), "warp.au"));
		    clipTotal++;
		  }
		  catch (MalformedURLException e) {}
		  try {
		      crashSound.play();     crashSound.stop();     clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      explosionSound.play(); explosionSound.stop(); clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      fireSound.play();      fireSound.stop();      clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      missleSound.play();    missleSound.stop();    clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      saucerSound.play();    saucerSound.stop();    clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      thrustersSound.play(); thrustersSound.stop(); clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		      warpSound.play();      warpSound.stop();      clipsLoaded++;
		      repaint(); Thread.currentThread().sleep(DELAY);
		    }
		    catch (InterruptedException e) {}
		}
	public int getClipsLoaded() {
		return clipsLoaded;
	}
	public int getClipTotal() {
		return clipTotal;
	}
	public AudioClip getCrashSound() {
		return crashSound;
	}
	public AudioClip getExplosionSound() {
		return explosionSound;
	}
	public AudioClip getFireSound() {
		return fireSound;
	}
	public AudioClip getMissleSound() {
		return missleSound;
	}
	public AudioClip getSaucerSound() {
		return saucerSound;
	}
	public AudioClip getThrustersSound() {
		return thrustersSound;
	}
	public AudioClip getWarpSound() {
		return warpSound;
	}
	
}
