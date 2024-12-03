/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author biphiri
 */

public class TerminalEnvironment {

    private String osName;
    private String osVersion;
    private String osBuild;
    private String macAddress;

    public TerminalEnvironment() {
        getMachineEnvironmentInfo();
    }

    public void getMachineEnvironmentInfo() {
        try {
            // Retrieve OS information
            osName = System.getProperty("os.name");
            osVersion = System.getProperty("os.version");

            // Get OS Build for Windows
            if (osName.contains("Windows")) {
                osBuild = getWindowsOSBuild();
            }

            // Retrieve MAC address
            macAddress = getMacAddress();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private String getWindowsOSBuild() {
        try {
            Process process = Runtime.getRuntime().exec("cmd /c ver");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Build")) {
                    return line.trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Build";
    }

    private String getMacAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface ni : Collections.list(networkInterfaces)) {
                if (ni.isUp() && !ni.isLoopback() && ni.getHardwareAddress() != null) {
                    byte[] mac = ni.getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (byte b : mac) {
                        sb.append(String.format("%02X:", b));
                    }
                    return sb.toString().substring(0, sb.length() - 1);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "MAC Address not found";
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOsBuild() {
        return osBuild;
    }

    public String getMacAddressValue() {
        return macAddress;
    }
}

