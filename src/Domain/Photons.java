package Domain;
import Application.*;
import Domain.*;
import TechnicalServices.*;
import UI.*;

public class Photons extends AsteroidsSprite{
	information info = new information();

	public void initPhotons(AsteroidsSprite[] photons) {

		  int i;

		  for (i = 0; i < info.MAX_SHOTS; i++)
		    photons[i].active = false;
		  info.photonIndex = 0;
		}

		public void updatePhotons(AsteroidsSprite[] photons) {

		  int i;

		  // Move any active photons. Stop it when its counter has expired.

		  for (i = 0; i < info.MAX_SHOTS; i++)
		    if (photons[i].active) {
		      if (!photons[i].advance())
		        photons[i].render();
		      else
		        photons[i].active = false;
		    }
		}
}
