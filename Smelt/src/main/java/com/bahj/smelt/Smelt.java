package com.bahj.smelt;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        // Load the configuration file
        Configuration configuration = null;
        try {
            configuration = SerializationUtils.readFileWithDefault(FileUtils.CONFIGURATION_FILE,
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

        // Now set up exception handling for the application
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());

        // Launch the application
        try {
            new SmeltApplicationModel(configuration);
            // Now that the application model is created, the various plugins should have done what is necessary to
            // get the application started. This main thread is no longer necessary (in the same way that a main thread
            // isn't necessary once the Swing event thread has started).
        } catch (ApplicationModelCreationException e) {
            throw new NotYetImplementedException(e);
        }
    }

    /**
     * This exception handler class handles both Swing EDT exceptions as well as exceptions raised by other threads. It
     * responds by opening a dialog. The original design was taken from
     * <tt>http://stackoverflow.com/questions/4448523/how-can-i-catch-event-dispatch-thread-edt-exceptions</tt>
     */
    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void handle(Throwable thrown) {
            // for EDT exceptions
            handleException(thrown);
        }

        public void uncaughtException(Thread thread, Throwable thrown) {
            // for other uncaught exceptions
            handleException(thrown);
        }

        protected void handleException(Throwable throwable) {
            // Print to stderr
            throwable.printStackTrace();
            
            // Create a Swing dialog box containing the exception.
            JDialog dialog = new JDialog((Frame) null, "Error");

            JTextArea area = new JTextArea(20, 60);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(buffer);
            throwable.printStackTrace(ps);
            ps.close();
            area.setText(buffer.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane pane = new JScrollPane(area);

            JLabel topLabel = new JLabel("An unhandled error has occurred:");

            JLabel bottomLabel = new JLabel("The application may misbehave from this point onward.");

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(topLabel, BorderLayout.NORTH);
            centerPanel.add(bottomLabel, BorderLayout.SOUTH);
            centerPanel.add(pane, BorderLayout.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener((ActionEvent e) -> dialog.dispose());

            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.add(centerPanel);
            dialogPanel.add(okButton);

            dialog.setContentPane(dialogPanel);

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }
}
