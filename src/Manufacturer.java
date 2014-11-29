import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manufacturer {
	/**
	 * This method will return the name of the MAC address manufacturer.
	 * 
	 * @return
	 */
	public String getManufacturer(String mac) {
		String manufacturer = this.getManufacturerFromWeb(mac);
		if (manufacturer == null) {
			manufacturer = "Unknown";
		}
		return manufacturer;
	}

	private String getManufacturerFromWeb(String mac) {
		/*
		 * TODO:perform search on first 6 chars AA:AA:AA & permanently cache it
		 */
		/*
		 * TODO: a possible implementation find & implement with an RPC service
		 */
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		try {

			url = new URL("http://www.coffer.com/mac_find/?string=" + java.net.URLEncoder.encode(mac, "UTF-8"));
			is = url.openStream(); // throws an IOException br = new
			br = new BufferedReader(new InputStreamReader(is));

			//<td class="table2"><a href="http://www.google.com/search?q=Apple, Inc">Apple, Inc</a></td>

			Pattern pattern = Pattern.compile("<td class=\"table2\"><a [^>]*>([^<]*)</a>");
			Matcher matcher = null;

			while ((line = br.readLine()) != null) {
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					br.close();
					return matcher.group(1);
				}
			}

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		return null;
	}
}
