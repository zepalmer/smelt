package com.bahj.smelt.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface for serialization strategies that can use strings rather than streams. The default implementation of the
 * {@link SmeltSerializationStrategy} methods writes to the underlying stream a textual representation of string length
 * followed by the string itself.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type of data which is serialized and deserialized.
 */
public interface SmeltStringSerializationStrategy<T> extends SmeltSerializationStrategy<T> {
    /**
     * The standard encoding used in this serialization strategy.
     */
    public static final String CHARSET_ENCODING = "UTF-8";

    /**
     * Transforms an object into a string.
     * 
     * @param obj
     *            The object to transform.
     * @return The string representation of that object.
     * @throws SerializationException
     *             If the object cannot be correctly serialized.
     */
    public String objectToString(T obj) throws SerializationException;

    /**
     * Transforms a string into an object.
     * 
     * @param str
     *            The string to transform.
     * @return The resulting object.
     * @throws DeserializationException
     *             If the object cannot be correctly deserialized.
     */
    public T stringToObject(String str) throws DeserializationException;

    default public T deserialize(InputStream is) throws DeserializationException, IOException {
        StringBuilder sb = new StringBuilder();
        int byt;
        // Read up to MAX_HEADER_DIGITS digits from the stream. (If the stream is invalid, this keeps us from reading
        // a huge file looking for the terminator.)
        final int MAX_HEADER_DIGITS = 12;
        while ((byt = is.read()) != -1 && byt >= '0' && byt <= '9' && sb.length() <= MAX_HEADER_DIGITS) {
            sb.append((char) byt);
        }
        if (sb.length() > MAX_HEADER_DIGITS) {
            throw new DeserializationException("Could not find valid string length header in stream.");
        }
        // Check the most recent byte to ensure that it was the expected period terminator.
        if (byt != '.') {
            throw new DeserializationException("Could not find valid string length header in stream.");
        }
        // That should've been the length of the byte array.
        int length;
        try {
            length = Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            throw new DeserializationException("Header length was not a number: " + e.getMessage());
        }
        // So now read the bytes.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[16384];
        while (length > 0) {
            int numRead = is.read(readBuffer, 0, Math.min(readBuffer.length, length));
            if (numRead == -1) {
                throw new DeserializationException("Unexpected end of stream.");
            }
            baos.write(readBuffer, 0, numRead);
            length -= numRead;
        }
        // Turn those bytes into the string.
        String data = baos.toString(CHARSET_ENCODING);
        // And finally deserialize the string.
        return stringToObject(data);
    }

    default public void serialize(OutputStream os, T obj) throws SerializationException, IOException {
        // Transform the object into some data.
        byte[] data = objectToString(obj).getBytes(CHARSET_ENCODING);
        // Write the length of the array as a base 10 number followed by a terminator period (in ASCII).
        os.write((data.length + ".").getBytes("US-ASCII"));
        // Now write the data itself.
        os.write(data);
        // And we're done. (This direction is a bit easier.)
    }
}
