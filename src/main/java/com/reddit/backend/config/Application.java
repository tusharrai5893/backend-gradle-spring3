package com.reddit.backend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Component
@EnableConfigurationProperties
@Data
@ConfigurationProperties(prefix = "app")
public class Application {

    @NotNull
    private String url;

    static {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Skip loopback addresses and non-IPV4 addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4)
                        System.out.println("USE THIS IP Address INSTEAD OF LOCALHOST" + inetAddress.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.fillInStackTrace();
        }
    }
}// class ends


