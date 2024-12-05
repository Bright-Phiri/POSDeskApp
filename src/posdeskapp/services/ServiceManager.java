/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.services;

/**
 *
 * @author biphiri
 */
public class ServiceManager {
    private static ServiceManager instance;
    private final TransmissionService transmissionService;

    private ServiceManager() {
        transmissionService = new TransmissionService();
        transmissionService.start();
    }

    public static synchronized ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public TransmissionService getTransmissionService() {
        return transmissionService;
    }
}