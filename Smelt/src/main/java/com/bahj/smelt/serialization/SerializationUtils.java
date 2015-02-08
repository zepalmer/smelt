package com.bahj.smelt.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * A class containing utility functions for serialization.
 * 
 * @author Zachary Palmer
 */
public class SerializationUtils {
    /**
     * A utility function to open a file with the specified name and use the provided serialization strategy to read an
     * object from it. If the file does not exist, the provided supplier is used to create a default value. If the file
     * does exist but deserialization fails, the appropriate exceptions are thrown.
     * 
     * @param file
     *            The file to open.
     * @param strategy
     *            The strategy to use.
     * @param defaultSupplier
     *            The default supplier.
     * @return An object of the provided type.
     * @throws IOException
     *             If an I/O error occurs while reading the object.
     * @throws DeserializationException
     *             If a deserialization error occurs while reading the object.
     */
    public static <T> T readFile(File file, SmeltSerializationStrategy<T> strategy, Supplier<T> defaultSupplier)
            throws DeserializationException, IOException {
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            T ret = strategy.deserialize(fis);
            fis.close();
            return ret;
        } else {
            return defaultSupplier.get();
        }
    }

    /**
     * A utility function to open a file with the specified name and use the provided serialization strategy to write an
     * object to it.
     * 
     * @param file
     *            The file to write.
     * @param strategy
     *            The strategy to use.
     * @param object
     *            The object to serialize.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws SerializationException
     *             If a serialization error occurs while writing the object.
     */
    public static <T> void writeFile(File file, SmeltSerializationStrategy<T> strategy, T object)
            throws SerializationException, IOException {
        FileOutputStream fos = new FileOutputStream(file);
        strategy.serialize(fos, object);
        fos.close();
    }

    /**
     * A utility function to open a file with the specified name and use the provided serialization strategy to write an
     * object to it, preserving the original file in the case of an error. This method creates a temporary file based on
     * the name of the target file, writes to that, and then moves it into place. If an error occurs, the temporary file
     * will be deleted.
     * 
     * @param file
     *            The file to write.
     * @param strategy
     *            The strategy to use.
     * @param object
     *            The object to serialize.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws SerializationException
     *             If a serialization error occurs while writing the object.
     */
    public static <T> void writeFileSafely(File file, SmeltSerializationStrategy<T> strategy, T object)
            throws SerializationException, IOException {
        File tempFile = new File(file.getPath() + ".temp");
        try {
            writeFile(tempFile, strategy, object);
            tempFile.renameTo(file);
        } catch (Throwable t) {
            tempFile.delete();
            throw t;
        }
    }
}
