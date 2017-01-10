import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class BroodjesZaak implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient final String KAAS = "KAAS";
	private transient final String HESP = "HESP";
	
	private Verkoper v1, v2;
	private Map<String, Integer> stock = new HashMap<String, Integer>();
	private LinkedList<Klant> klanten = new LinkedList<>();
	private transient Thread t1;
	private transient Thread t2;
	private boolean nogKlanten = true;
	
	public void startVerkopers()
	{
		t1.start();
		t2.start();
	}
	
	public BroodjesZaak(int aantalKaas, int aantalHesp)
	{
		stock.put(KAAS, aantalKaas);
		stock.put(HESP, aantalKaas);
		v1 = new Verkoper(this);
		v2 = new Verkoper(this);
		t1 = new Thread(v1);
		t2 = new Thread(v2);
		this.startVerkopers();
	}
	
	public synchronized void verwelkom(Klant k)
	{
		if(stock.get(k.vraagBeleg().toString()) > 0 )
			klanten.offer(k);
	}
	
	public synchronized Klant volgende()
	{
		if(klanten.peek() != null)
			return klanten.poll();
		else 
		{
			return null;
		}
	}
	
	public synchronized LinkedList<Klant> getKlantenLijst()
	{
		return this.klanten;
	}
	
	public synchronized boolean neem(Beleg b)
	{
		String id = b.toString().toUpperCase();
		if(stock.get(id) > 0)
		{
			stock.put(id, stock.get(id)-1);
			return true;
		}
		else
		{
			klanten.stream().filter(k -> k.vraagBeleg().toString().equals(b.toString()))
					.forEach(k -> k.gaWeg());
			klanten = klanten.stream().filter(k -> !(k.vraagBeleg().toString().equals(b.toString())))
					.collect(Collectors.toCollection(() -> new LinkedList<Klant	>()));
			
			return false;
		}
	}
	
	public boolean getNogKlanten()
	{
		return nogKlanten;
	}
	public synchronized void setNogKlanten(boolean zaakOpen)
	{
		this.nogKlanten = zaakOpen;
	}
}
