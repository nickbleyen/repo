import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class Simulatie extends Thread{

	private BroodjesZaak zaak;
	private Path path;
	private Console console;
	private static int teller = 0;
	private int chance;
	
	public Simulatie(){
		String home = System.getProperty("user.home");
		path = Paths.get(home);
		path = path.resolve("Documents");
		path = path.resolve("Simulatie.ser");
		console = System.console();
	}
	
	public void run(){
		init();
		zaak.start();
		while(!zaak.closing()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			//save();
			//load(); niet mogelijk
		}
		zaak.info();
		System.out.println("aantal keer printfunctie: " + teller);
	}
	
	private void init(){
		try {
			String aantal_kaas = console.readLine("aantal kaas: ");
			String aantal_hesp = console.readLine("aantal hesp: ");
			String aantal_klanten = console.readLine("aantal klanten: ");
			String kans = console.readLine("kans op 100: ");
			
			int kaas = Integer.parseInt(aantal_kaas);
			int hesp = Integer.parseInt(aantal_hesp);
			zaak = new BroodjesZaak(kaas, hesp);
			
			int klanten = Integer.parseInt(aantal_klanten);
			chance = Integer.parseInt(kans);
			maakKlanten(klanten);
			
		} catch(NumberFormatException ex) {
			console.printf(ex.getMessage());
		} catch (Exception ex) {
			console.printf(ex.getMessage());
		}
	}
	
	private void maakKlanten(int aantal){
		Consumer<Boolean> consumer = (b) -> toonBlij(b);
		while(aantal > 0){
			Beleg b;
			if(Math.random() * 100 < chance)
				b = new Kaas();
			else
				b = new Hesp();
			zaak.verwelkom(new Klant(consumer, b));
			aantal--;
		}
	}
	
	public void toonBlij(boolean reactie){
		console.printf(reactie ? ":)\n" : ":(\n");
		teller++;
	}
	
	private void save(){
		zaak.setStop();
		console.printf("saving...\n");
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path.toFile()))){
			Thread.sleep(1000);
			output.writeObject(zaak);
		}catch (IOException ex) {
			console.printf(ex.getMessage());
			ex.printStackTrace();
		}catch (Exception ex) {
			console.printf(ex.getMessage());
		}
	}
	
	private void load(){
		console.printf("loading...\n");
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(path.toFile()))){
			zaak = (BroodjesZaak) input.readObject();
			zaak.start();
		}catch (IOException ex) {
			console.printf(ex.getMessage());
			ex.printStackTrace();
		}catch (Exception ex) {
			console.printf(ex.getMessage());
		}
	}
	
	public static void main(String []args){
		new Simulatie().start();
	}

}
