import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.TimerTask;

public class BackupTask extends TimerTask{
	
	private Simulatie s;
	private Path p;
	public BackupTask(Simulatie s, Path p)
	{
		this.s = s;
		this.p = p;
	}
	@Override
	public synchronized void run() {
		System.out.println("WRITING BACKUP");
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(p.toString())))
		{
			out.writeObject(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
