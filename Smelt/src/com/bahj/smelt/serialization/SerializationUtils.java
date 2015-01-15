package com.bahj.smelt.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * A class containing utility functions for serialization.
 * 
 * @author Zachary Palmer
 */
public class SerializationUtils {
    /**
     * A utility function to open a file with the specified base name and use the provided serialization strategy to
     * read an object from it. If the file does not exist, the provided supplier is used to create a default value. If
     * the file does exist but serialization fails, the appropriate exceptions are thrown.
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
}
