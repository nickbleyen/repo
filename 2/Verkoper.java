import java.io.Serializable;

public class Verkoper implements Runnable, Serializable {

	private static final long serialVersionUID = 4951505619896680155L;
	private BroodjesZaak zaak;
	
	public Verkoper(BroodjesZaak zaak) {
		this.zaak = zaak;
	}
	
	@Override
	public void run() {
		while(!zaak.closing()){
			Klant k;
			if((k = zaak.volgende()) != null){
				Beleg b = k.vraagBestelling();
				if(zaak.neem(b))
					k.geef(new Broodje<>(b));
				k.gaWeg();	
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
			}
		}
	}

}
