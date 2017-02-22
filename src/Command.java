import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Command {

	public static void main(String[] args) throws java.io.IOException {
		String filename = args[0] + ".txt";
		FileWriter fw = new FileWriter(filename, true);
		BufferedReader p_stat = new BufferedReader(new FileReader("/proc/self/stat"));
		String[] tokens = p_stat.readLine().split(" ");
		fw.write(tokens[0] + " " + tokens[3] + " " + tokens[4] + " " + args[0] + "\n");
		fw.close();
	}
}
