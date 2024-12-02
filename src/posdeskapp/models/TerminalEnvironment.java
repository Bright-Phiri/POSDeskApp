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
            String os = System.getProperty("os.name");
            String version = System.getProperty("os.version");
            osName = os;
            osVersion = version;

            // Get OS Build using command on Windows
            if (osName.contains("Windows")) {
                osBuild = getWindowsOSBuild();
            }

            System.out.println("OS Name: " + osName);
            System.out.println("OS Version: " + osVersion);
            System.out.println("OS Build: " + osBuild);

            // Retrieve MAC address using NetworkInterface (alternative approach)
            macAddress = getMacAddress();

            System.out.println("MAC Address: " + macAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private String getWindowsOSBuild() {
        try {
            // Execute the 'ver' command to get the Windows version info
            Process process = Runtime.getRuntime().exec("cmd /c ver");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Extract the OS build from the 'ver' command output
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
                if (ni.isUp() && ni.getHardwareAddress() != null) {
                    byte[] mac = ni.getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (byte b : mac) {
                        sb.append(String.format("%02X:", b));
                    }
                    return sb.toString().substring(0, sb.length() - 1); // Remove the last colon
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "MAC Address not found";
    }

    public String getOSBuild() {
        return osBuild;
    }

    public static void main(String[] args) {
        TerminalEnvironment test = new TerminalEnvironment();
        System.out.println("OS Build: " + test.getOSBuild());  // Should print the OS build version
    }
}