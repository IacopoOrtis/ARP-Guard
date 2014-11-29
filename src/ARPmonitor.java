import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ~~~ ARP monitor Description ~~~
 * 
 * This application monitors the ARP system table every 30 seconds to check new
 * entrances and exits. The main purpose of this application is to discover ARP
 * attacks.
 * 
 * ~~~ ARP monitor Description ~~~
 * 
 * 
 * 
 * ~~~ ARP monitor Features ~~~
 * 
 * Feature 1) resolving hardware manufacturer
 * 
 * Feature 2)
 * 
 * ~~~ ARP monitor Features ~~~
 * 
 * 
 * 
 * 
 * ~~~ ARP monitor Further development ( ordered for descending importance ) ~~~
 * 
 * TODO graphics alerts for MAC with factory objects ( based on OS type )
 * 
 * TODO ARP manufacturer resolver just for manufacturer MAC part
 * 
 * TODO cache for ARP manufacturer
 * 
 * TODO graphics for MAC with factory objects ( based on OS type )
 * 
 * TODO getARPCache based on the OS type layout ( not only Apple style )
 * 
 * ~~~ ARP monitor Further development ( ordered for descending importance ) ~~~
 * 
 * 
 * 
 * 
 * @author IacopoOrtis
 *
 */
public class ARPmonitor {

	public static void main(String[] args) {

		HashSet<Binding> activeBinding = new HashSet<Binding>();

		try {

			/*
			 * 
			 * System.setProperty
			 * ("com.apple.mrj.application.apple.menu.about.name", "Test");
			 * UIManager
			 * .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			 */
			java.awt.SystemTray.getSystemTray().add(new java.awt.TrayIcon(java.awt.Toolkit.getDefaultToolkit().getImage("shield.png")));

		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			//Notifications.speack("ARP monitor active!");
			//Notifications.notificationCenterMessage("prova", "proa2");
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					// getting new bindings
					HashSet<Binding> actualBindings = getARPCache();

					// marking old bindings
					HashSet<Binding> oldBindings = activeBinding;

					// after this 'oldBindings' will have only old bindings
					oldBindings.removeAll(actualBindings);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 30 * 1000);// every 30 seconds
	}

	/**
	 * This function will read the ARP system cache.
	 * 
	 * @return List<Binding> list of founded bindings in the ARP table
	 * @throws Exception
	 *             There is a different layout from the expected one
	 */
	private static HashSet<Binding> getARPCache() throws Exception {
		String cmd = "arp -a";
		Runtime run = Runtime.getRuntime();
		HashSet<Binding> bindings = new HashSet<Binding>();

		try {
			Process proc = run.exec(cmd);
			proc.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;

			// ? (192.168.1.254) at 0:60:3b:32:94:93 on en0 ifscope [ethernet]
			Pattern pattern = Pattern.compile("^.* \\(([^)]*)\\) at (.*) on .*"); // based on the OS type & OS version
			Matcher matcher = null;
			while ((line = buf.readLine()) != null) {
				matcher = pattern.matcher(line);
				if (matcher.find() == true) {
					bindings.add(new Binding(matcher.group(1), matcher.group(2)));//TODO verificare dove vanno le sue exceptions
				} else {
					throw new Exception("ARP system table layout is differnet from the expected. Modify the source code and ricompile.");
				}
			}
		} catch (IOException | InterruptedException ex) {
			System.out.println(ex.getMessage());
		}

		return bindings;
	}
}