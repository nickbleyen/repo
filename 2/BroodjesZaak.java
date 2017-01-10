import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BroodjesZaak implements Serializable{

	private static final long serialVersionUID = -1051083912878857271L;
	private Map<String, Integer> voorraad;
	private Queue<Klant> rij;
	private Verkoper v1, v2;
	private transient Thread t1, t2;
	private AtomicInteger leeg, hesp, kaas;
	private boolean stop;
	
	public BroodjesZaak(int kaas, int hesp){
		voorraad = new ConcurrentHashMap<>();
		voorraad.put("kaas", kaas);
		voorraad.put("hesp", hesp);
		this.leeg = new AtomicInteger(0); this.hesp = new AtomicInteger(0);; this.kaas = new AtomicInteger(0);;
		rij = new LinkedList<>();
		v1 = new Verkoper(this);
		v2 = new Verkoper(this);
		start();
	}
	
	public void start(){
		stop = false;
		t1 = new Thread(v1);
		t2 = new Thread(v2);
		try {
			t1.join();
			t2.join();
			t1.start();
			t2.start();
		} catch (InterruptedException e) {
		}
	}
	
	public synchronized void verwelkom(Klant klant){
		rij.offer(klant);
	}
	
	public synchronized Klant volgende(){
		if(rij.peek() != null)
			return rij.poll();
		else 
			return null;
	}
	
	public synchronized boolean neem(Beleg beleg){
		int aantal = voorraad.get(getBeleg(beleg));
		if(aantal == 0){
			rij.parallelStream().filter(s -> s.vraagBestelling() == beleg).forEach(Klant::gaWeg);;
			leeg.incrementAndGet();
			Queue<Klant> temp = rij.parallelStream().filter(s -> s.vraagBestelling() != beleg).collect(Collectors.toCollection(LinkedList<Klant>::new));
			rij = temp;
			return false;
		} else {
			if(getBeleg(beleg) == "kaas")
				kaas.incrementAndGet();
			else
				hesp.incrementAndGet();
			voorraad.put(getBeleg(beleg), voorraad.get(getBeleg(beleg))-1);
			return true;
		}
	}
	
	private String getBeleg(Beleg beleg){
		if(beleg instanceof Kaas)
			return "kaas";
		else
			return "hesp";
	}
	
	public void setStop(){
		stop = true;
	}
	
	public synchronized boolean closing(){
		int k, h;
		k = voorraad.get("kaas");
		h = voorraad.get("hesp");
		if(stop)
			return false;
		if(k == 0 && h == 0)
			return true;
		if(rij.size() == 0)
			return true;
		return false;
	}
	
	public void info(){
		System.out.println("leeg: "+leeg +", kaas: " +kaas+ ", hesp: "+hesp);
	}
	
}
