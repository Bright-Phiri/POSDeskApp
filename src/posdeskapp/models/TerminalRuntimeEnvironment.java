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
public class TerminalRuntimeEnvironment {

    @Expose
    private Platform Platform;
    @Expose
    private POS Pos;

   public TerminalRuntimeEnvironment(Platform platform, POS pos) {
        this.Platform = platform;
        this.Pos = pos;
    }

    public Platform getPlatform() {
        return Platform;
    }

    public void setPlatform(Platform Platform) {
        this.Platform = Platform;
    }

    public POS getPos() {
        return Pos;
    }

    public void setPos(POS Pos) {
        this.Pos = Pos;
    }
}
