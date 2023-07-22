package main.java.com.kaalka;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;

public class KaalkaNTP extends Kaalka {
    public KaalkaNTP() {
        super();
    }

    @Override
    protected void updateTimestamp() {
        try {
            NTPUDPClient ntpClient = new NTPUDPClient();
            InetAddress inetAddress = InetAddress.getByName("pool.ntp.org");
            TimeInfo timeInfo = ntpClient.getTime(inetAddress);
            long currentTime = timeInfo.getReturnTime();
            ntpClient.close();

            this.second = (int) ((currentTime / 1000) % 60);
        } catch (Exception e) {
            System.err.println("Error fetching NTP time: " + e.getMessage());
            // Fallback to system time if NTP fails
            long timestamp = System.currentTimeMillis();
            this.second = (int) ((timestamp / 1000) % 60);
        }
    }
}

