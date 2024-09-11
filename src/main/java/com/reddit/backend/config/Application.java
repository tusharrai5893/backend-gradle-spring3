package com.reddit.backend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
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

    {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Skip loopback addresses and non-IPV4 addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        InetSocketAddress socketAddress = new InetSocketAddress(4200);
                        url = inetAddress.getHostAddress() + ":" + socketAddress.getPort();
                        System.out.println("IP Address: " + url);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
