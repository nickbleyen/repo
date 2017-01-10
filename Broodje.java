import java.io.Serializable;

@SuppressWarnings("serial")
public class Broodje<E extends Beleg> implements Serializable{
	private E beleg;
	
	public Broodje(E beleg)
	{
		this.beleg = beleg;
	}
	
	public E getBeleg()
	{
		return beleg;
	}
}
