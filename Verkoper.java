import java.io.Serializable;

@SuppressWarnings("serial")
public class Verkoper implements Runnable, Serializable{
	BroodjesZaak zaak;
	
	public Verkoper(BroodjesZaak zaak)
	{
		this.zaak = zaak;
	}
	
	public void run()
	{
		while(zaak.getKlantenLijst().size() > 0 || zaak.getNogKlanten())
		{
			Klant k = zaak.volgende();
			if(k == null)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(zaak.neem(k.vraagBeleg()))
			{
				k.geef();
			}
		}
	}

}
