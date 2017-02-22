import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;

import java.util.HashSet;

public class RunCommandInShell {

	private static class EscapeBashSpecialCharacters {
		// note: white space is excluded from the set of
		// characters to be escaped. escaping white space changes
		// the meaning of a command, unlike other characters.

		private String bash_special_characters = "#`\"'${}?+%|&^*;~!@<>().:[]";
		private HashSet<Character> bsc_set = new HashSet<Character>();

		public EscapeBashSpecialCharacters() {
			for (int i = 0; i < this.bash_special_characters.length(); ++i) {
				this.bsc_set.add(this.bash_special_characters.charAt(i));
			}
		}

		public String escape_string(String input) {
			Character prev_c = null;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < input.length(); ++i) {
				Character c = input.charAt(i);
				if (this.bsc_set.contains(c)) {
					if (prev_c != null) {
						if (prev_c != '\\') {
							sb.append("\\");
						}
					}
				}
				sb.append(c);
				prev_c = c;
			}
			return sb.toString();
		}
	}

	private static EscapeBashSpecialCharacters ebsc = new EscapeBashSpecialCharacters();

	private void WaitOnProcess(java.lang.Process proc) throws java.lang.InterruptedException, java.io.IOException {
		int rc = proc.waitFor();
		System.out.printf("rc=%d\n", rc);
		java.io.BufferedReader stdOutReader = new java.io.BufferedReader(
				new java.io.InputStreamReader(proc.getInputStream()));
		java.io.BufferedReader stdErrReader = new java.io.BufferedReader(
				new java.io.InputStreamReader(proc.getErrorStream()));
		StringBuffer sb = null;
		String line = "";
		System.out.printf("stdout:\n");
		sb = new StringBuffer();
		line = "";
		while ((line = stdOutReader.readLine()) != null) {
			sb.append(line + "\n");
		}
		System.out.printf("%s\n", sb.toString());
		System.out.printf("stderr:\n");
		sb = new StringBuffer();
		line = "";
		while ((line = stdErrReader.readLine()) != null) {
			sb.append(line + "\n");
		}
	}

	private void RunCommand(String cmd, int i) throws java.lang.InterruptedException, java.io.IOException {
		System.out.printf("------------------------\ncase %d\n", i);
		System.out.printf("original command: %s\n", cmd);
		java.lang.Process p = null;
		try {
			p = java.lang.Runtime.getRuntime().exec(cmd);
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
			return;
		}
		this.WaitOnProcess(p);
	}

	private void RunCommandInShell(String cmd, int i) throws java.lang.InterruptedException, java.io.IOException {
		System.out.printf("------------------------\ncase %d\n", i);
		System.out.printf("original command: %s\n", cmd);

		String[] cmdarray = { "/bin/bash", "-c", cmd };
		System.out.printf("transformed command: %s %s %s\n", cmdarray[0], cmdarray[1], cmdarray[2]);
		java.lang.Process p = null;
		try {
			p = java.lang.Runtime.getRuntime().exec(cmdarray);
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
			return;
		}
		this.WaitOnProcess(p);
	}

	public static void main(String[] args) throws java.lang.InterruptedException, java.io.IOException {
		String proc_name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		System.out.printf("proc name: %s\n", proc_name);
		RunCommandInShell rcis = new RunCommandInShell();

		java.util.ArrayList<String[]> rces = new java.util.ArrayList<String[]>();
		String[] p = { "p", "./bin/cmd.py " };
		rces.add(p);
		String[] j = { "j", "/usr/bin/java -cp bin Command " };
		rces.add(j);

		int case_number = -1;
		for (String[] rce : rces) {
			java.util.ArrayList<String> cmds = new java.util.ArrayList<String>();
			String rce_type = rce[0];
			String rce_cmd = rce[1];
			cmds.add("/bin/ls a b <a>");
			cmds.add("/bin/ls<$(" + rce_cmd + rce_type + "1X)");
			cmds.add("/bin/ls<(" + rce_cmd + rce_type + "2X)");
			cmds.add("/bin/ls;" + rce_cmd + rce_type + "3X");
			cmds.add("/bin/ls<`" + rce_cmd + rce_type + "4X`");
			cmds.add("/bin/ls;" + rce_cmd + rce_type + "5X");
			cmds.add("/bin/ls ; " + rce_cmd + rce_type + "6X");
			cmds.add("/bin/ls ; " + rce_cmd + rce_type + "7X");
			cmds.add(rce_cmd + rce_type + "8lX " + " ; " + rce_cmd + rce_type + "8rX");
			cmds.add(rce_cmd + rce_type + "9lX" + " && " + rce_cmd + rce_type + "9rX");
			cmds.add("/bin/ls < (" + rce_cmd + rce_type + "10X)");
			for (int i = 0; i < cmds.size(); ++i) {
				case_number += 1;
				String cmd = cmds.get(i);
				rcis.RunCommand(cmd.replace("X", "O"), case_number);
				rcis.RunCommandInShell(cmd.replace("X", "B"), case_number);
				String escaped_cmd = RunCommandInShell.ebsc.escape_string(cmd.replace("X", "E"));
				rcis.RunCommandInShell(escaped_cmd, case_number);
			}
		}
	}
}
