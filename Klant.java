import java.io.Serializable;

@SuppressWarnings("serial")
public class Klant implements Serializable{
	private Broodje<? extends Beleg> brood;
	private boolean happy;
	
	public Klant(Broodje<? extends Beleg> brood)
	{
		this.brood = brood;
		happy = false;
	}
	
	public void geef()
	{
		happy = true;
		Simulatie.toonBlij(this);
	}
	
	public Beleg vraagBeleg()
	{
		return brood.getBeleg();
	}
	
	public boolean isHappy()
	{
		return happy;
	}
	
	public void gaWeg()
	{
		Simulatie.toonBlij(this);
	}
}
