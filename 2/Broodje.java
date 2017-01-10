import java.io.Serializable;

public class Broodje<T extends Beleg> implements Serializable{

	private static final long serialVersionUID = -3766642287106564378L;
	private T beleg;
	
	public Broodje(T beleg){
		this.beleg = beleg;
	}
}
