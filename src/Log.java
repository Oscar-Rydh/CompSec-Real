import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {
	
	public Log(){

	}
	
	public void write(String s){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
		    out.println(getTime() + " - " + s);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	private String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	public static void main(String args[]) {
		System.out.println();
	}
}
