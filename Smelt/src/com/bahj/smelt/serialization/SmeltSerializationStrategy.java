package com.bahj.smelt.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A common interface used in Smelt for items which may be serialized to and from streams.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type of data which is serialized.
 */
public interface SmeltSerializationStrategy<T> {
    /**
     * Reads an item from the provided input stream. This object will not close the stream. If this method returns
     * normally, then the stream is left in a consistent and usable state; no data is consumed unnecessarily.
     * 
     * @param is
     *            The stream from which to read.
     * @return The resulting object.
     * @throws DeserializationException
     *             If deserialization fails due to a format error or other non-I/O issue.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public T deserialize(InputStream is) throws DeserializationException, IOException;

    /**
     * Writes an item to the provided output stream. This object will not close the stream.
     * 
     * @param os
     *            The stream to which to write.
     * @param obj
     *            The object to write.
     * @throws SerializationException
     *             If the object cannot be serialized safely.
     * @throws IOException
     *             If an I/O error occurs during serialization.
     */
    public void serialize(OutputStream os, T obj) throws SerializationException, IOException;
}
