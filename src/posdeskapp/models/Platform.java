/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.Expose;

/**
 *
 * @author biphiri
 */
public class Platform {

    @Expose
    private String OsName;
    @Expose
    private String OsVersion;
    @Expose
    private String OsBuild;
    @Expose
    private String MacAddress;

    public Platform() {
    }

    public String getOsName() {
        return OsName;
    }

    public void setOsName(String OsName) {
        this.OsName = OsName;
    }

    public String getOsVersion() {
        return OsVersion;
    }

    public void setOsVersion(String OsVersion) {
        this.OsVersion = OsVersion;
    }

    public String getOsBuild() {
        return OsBuild;
    }

    public void setOsBuild(String OsBuild) {
        this.OsBuild = OsBuild;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        this.MacAddress = MacAddress;
    }
}
