/*
 * ContainerLoadingApp.java
 */

package org.jgap.ga.gui;

import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.apache.log4j.Logger;
import org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel;
/**
 * The main class of the application.
 */
public class ContainerLoadingApp extends SingleFrameApplication {
    static final String LOG_PROPERTIES_FILE = "log4j.properties";
    static Logger logger=Logger.getLogger(ContainerLoadingApp.class);
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        try {
            Properties logProperties = new Properties();

            try {
                logProperties.load(ClassLoader.getSystemResourceAsStream(LOG_PROPERTIES_FILE));
                PropertyConfigurator.configure(logProperties);
                logger.info("Logging initialized.");
            } catch (IOException e) {
                throw new RuntimeException("Unable to load logging property " + LOG_PROPERTIES_FILE);
            }
            UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
            show(new ContainerLoadingView(this));
        } catch (UnsupportedLookAndFeelException ex) {
            logger.error(ex);
        }
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ContainerLoadingApp
     */
    public static ContainerLoadingApp getApplication() {
        return Application.getInstance(ContainerLoadingApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        
        launch(ContainerLoadingApp.class, args);
    }
}
