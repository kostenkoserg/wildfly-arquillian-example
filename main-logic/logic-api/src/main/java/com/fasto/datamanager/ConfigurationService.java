package com.fasto.datamanager;


public interface ConfigurationService {

    String CONFIGURATION_SERVICE_NAME = "configuration-service";
    String CONFIGURATION_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + CONFIGURATION_SERVICE_NAME;

    String getProperty(String name);

}
