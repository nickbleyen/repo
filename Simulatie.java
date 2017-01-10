import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

@SuppressWarnings("serial")
public class Simulatie implements Serializable{
	private int aantalKlanten, kansOpKaas;
	private BroodjesZaak zaak;
	
	public Simulatie()
	{
		/*
		 * Inlezen gegevens
		 */
		int aantalKaas, aantalHesp;
		Scanner s = new Scanner(System.in);
		System.out.println("Aantal stukken kaas: ");
		aantalKaas = s.nextInt();
		System.out.println("Aantal stukken hesp: ");
		aantalHesp = s.nextInt();
		System.out.println("Aantal klanten: ");
		aantalKlanten = s.nextInt();
		System.out.println("Kans op een broodje met kaas: ");
		kansOpKaas = s.nextInt();
		s.close();
		zaak = new BroodjesZaak(aantalKaas, aantalHesp);
	}
	
	public void resumeSimulation()
	{
		//Resume the simulation after it was loaded in (manually start the threads again)
		zaak.startVerkopers();
	}
	
	public synchronized void offerCustomer()
	{
		Klant k;
		Random r = new Random();
		if(r.nextInt(100) < kansOpKaas )
		{
			k = new Klant(new Broodje<Kaas>(new Kaas()));
		}
		else
			k = new Klant(new Broodje<Hesp>(new Hesp()));
		
		zaak.verwelkom(k);
		aantalKlanten--;
		if(aantalKlanten == 0)
		{
			zaak.setNogKlanten(false);
		}
	}
	
	public boolean isWorking()
	{
		return zaak.getNogKlanten();
	}
	
	public static void main(String[] args)
	{
		Simulatie s = null;
		final Path path = Paths.get("Simulatie");
		
		if(Files.exists(path)) // If there is a backup, read the backup, otherwise make a new simulation
		{
			System.out.println("Restoring backup!");
			try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path.toString())))
			{
				s = (Simulatie) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			s.resumeSimulation();
			// The main thread will offer the remaining customers to the store and the Verkopers have been activated again
		}
		else
		{
			s = new Simulatie();
		}
		
		Timer backupTimer = new Timer(true);
		
		backupTimer.schedule(new BackupTask(s, path), 10 * 100);
		
		while(s.isWorking())
		{
			s.offerCustomer();
		}
		
		System.out.println(Thread.activeCount());
		System.out.println("simulation end");
		
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void toonBlij(Klant k)
	{
		if(k.isHappy())
			System.out.println(":)");
		else
			System.out.println(":(");
	}
}
