import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements bindings between MAC and IPv4/6 address.
 * 
 * @author IacopoOrtis
 *
 */
public final class Binding {

	/**
	 * This variable store the MAC address of the binding
	 */
	private String mac = null;
	/**
	 * This variable store the IPv4/6 address of the binding
	 */
	private InetAddress ip = null;
	/**
	 * This variable store the Manufacturer name of the MAC address
	 */
	private String manufacturer = null;
	/**
	 * This variable is to store the oldness of the binding
	 */
	long onLineSince = 0;

	/**
	 * Constructor
	 * 
	 * @param ip
	 *            String representing the IPv4/6 address
	 * @param mac
	 *            String representing the MAC address
	 * @throws Exception
	 *             Invalid MAC address format
	 */
	public Binding(String ip, String mac) throws Exception {
		this.setBinding(ip, mac);
	}

	/**
	 * This function will set a new IPv4/6 & MAC address
	 * 
	 * @param ip
	 *            String representing the IPv4/6 address
	 * @param mac
	 *            String representing the MAC address
	 * @throws Exception
	 *             Invalid MAC address format
	 * 
	 */
	public void setBinding(String ip, String mac) throws Exception {
		this.ip = InetAddress.getByName(ip);
		Pattern pattern = Pattern.compile("[\\w{1,2}:]{5}\\w{1,2}");
		Matcher matcher = pattern.matcher(mac);
		if (matcher.find() == true) {
			this.mac = mac;
		} else {
			throw new Exception("Invalid MAC address format!");
		}

		this.onLineSince = System.currentTimeMillis();
	}

	/**
	 * This function will return the IPv4/6 and MAC address concatenated
	 * 
	 * @throws UnknownHostException
	 *             In case of invalid IPv4/6 address
	 * @throws NullPointerException
	 *             in case the IPv4/6 & MAC has not been set yet
	 */
	public String getBinding() throws NullPointerException {
		return this.getIp().concat(" " + this.getMac());
	}

	/**
	 * This function will return the IPv4/6 address of the binding
	 * 
	 * @return String of IPv4/6 address of the binding
	 * @throws NullPointerException
	 *             in case the IPv4/6 has not been set yet
	 */
	public String getIp() throws NullPointerException {
		if (this.ip == null) {
			throw new NullPointerException("IPv4/6 has not been set yet");
		}
		return this.ip.getHostAddress();
	}

	/**
	 * This function will return the MAC address of the binding
	 * 
	 * @return String of MAC address of the binding
	 * @throws NullPointerException
	 *             in case the MAC has not been set yet
	 */
	public String getMac() throws NullPointerException {
		if (this.mac == null) {
			throw new NullPointerException("MAC has not been set yet");
		}
		return this.mac;
	}

	public String getManufacturerIdentifier() throws NullPointerException, Exception {
		Pattern pattern = Pattern.compile("^((\\w{1,2}[:-]){3})(\\w{1,2}[:-]){2}(\\w{2})$");
		Matcher matcher = pattern.matcher(this.getMac());
		if (matcher.find() == true) {
			return matcher.group(1).substring(0, matcher.group(1).length() - 1);
		} else {
			throw new Exception("I can't parse this MAC address: '" + this.getMac() + "'");
		}
	}

	/**
	 * This function will give you the MAC manufacturer name.
	 * 
	 * @return String of manufacturer name
	 * @throws Exception
	 * @throws NullPointerException
	 */
	public String getManufacturer() throws NullPointerException, Exception {
		/*
		 * The first time I call this method it calls the
		 * getManufacturerFromWeb() method to retrieve informations from web. It
		 * won't do it twice because after that the result will be stored!
		 */
		if (this.manufacturer == null) {
			Manufacturer m = new Manufacturer();
			this.manufacturer = m.getManufacturer(this.getManufacturerIdentifier()); // from web + store
			return this.manufacturer;
		} else {
			return this.manufacturer;
		}
	}

	public boolean equals(Binding binding) {
		return (this.getMac().compareTo(binding.getMac()) == 0 & this.getIp().compareTo(binding.getIp()) == 0);
	}
}
