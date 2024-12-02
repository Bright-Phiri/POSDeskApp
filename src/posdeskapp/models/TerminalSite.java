/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

/**
 *
 * @author biphiri
 */
public class TerminalSite {

    private String SiteId;
    private String SiteName;

    public TerminalSite() {}

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        this.SiteId = siteId;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        this.SiteName = siteName;
    }
}

