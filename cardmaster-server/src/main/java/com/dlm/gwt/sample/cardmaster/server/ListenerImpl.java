package com.dlm.gwt.sample.cardmaster.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// quando il server Jetty viene avviato/chiuso, se necessarie, si possono eseguire delle rispettive operazioni
public class ListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server Jetty avviato");
        //qui fai operazioni utili quando il server viene avviato
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //qui fai operazioni utili quando il server viene chiuso
    }
}