package app.bean;

public class IPbean {
	
	public String ip;
	
	public String port;
	
	@Override
	public String toString() {
		return "IPbean [ip=" + ip + ", port=" + port + ", city=" + city + "]";
	}

	public String city;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
}
