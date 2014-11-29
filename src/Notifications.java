import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

public class Notifications {
	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * This method show a message in the Apple notification center
	 * 
	 * @param text
	 * @throws ScriptException
	 */
	public static void notificationCenterMessage(String title, String text) throws ScriptException {
		appleScript("display notification \"" + text + "\" with title \"" + title + "\"");//FIXME won't work
	}

	/**
	 * This method perform a speech
	 * 
	 * @param text
	 * @throws ScriptException
	 */
	public static void speack(String text) throws ScriptException {
		appleScript("say \"" + text + "\"");
	}

	/**
	 * This method execute an apple script
	 * 
	 * @param text
	 * @throws ScriptException
	 */
	private static void appleScript(String text) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScript");
		engine.eval(text);
	}
}
