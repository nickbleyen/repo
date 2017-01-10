import java.io.Serializable;
import java.util.function.Consumer;

public class Klant implements Serializable {

	private static final long serialVersionUID = -3110335467736578217L;
	private Broodje<Beleg> broodje;
	private transient Consumer<Boolean> reference;
	private Beleg choice;
	
	public Klant(Consumer<Boolean> reference, Beleg beleg){
		this.reference = reference;
		this.choice = beleg;
	}
	
	public Beleg vraagBestelling(){
		return choice;
	}
	
	public void geef(Broodje<Beleg> broodje){
		this.broodje = broodje;
	}
	
	public void gaWeg(){
		if(broodje == null)
			reference.accept(false);
		else
			reference.accept(true);
	}
	
}
