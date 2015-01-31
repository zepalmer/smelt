package com.bahj.smelt;

import java.io.IOException;

import com.bahj.smelt.configuration.ApplicationModelCreationException;
import com.bahj.smelt.configuration.Configuration;
import com.bahj.smelt.serialization.DeserializationException;
import com.bahj.smelt.serialization.SerializationUtils;
import com.bahj.smelt.util.FileUtils;
import com.bahj.smelt.util.NotYetImplementedException;

/**
 * The main class for this application.
 * 
 * @author Zachary Palmer
 *
 */
public class Smelt {
    public static void main(String[] args) throws ClassCastException {
        Configuration configuration = null;
        try {
            configuration = SerializationUtils.readFile(FileUtils.CONFIGURATION_FILE,
                    Configuration.SerializationStrategy.INSTANCE, () -> {
                        try {
                            return Configuration.createDefaultConfiguration();
                        } catch (ClassCastException e) {
                            throw new IllegalStateException("Default plugin class is not a plugin type!", e);
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("Default plugin class not found!", e);
                        }
                    });
        } catch (DeserializationException e) {
            throw new NotYetImplementedException(e);
        } catch (IOException e) {
            throw new NotYetImplementedException(e);
        }
        try {
            new SmeltApplicationModel(configuration);
            // Now that the application model is created, the various plugins should have done what is necessary to
            // get the application started. This main thread is no longer necessary (in the same way that a main thread
            // isn't necessary once the Swing event thread has started).
        } catch (ApplicationModelCreationException e) {
            throw new NotYetImplementedException(e);
        }
    }
}
