package xyz.velleda.extism_pdk;

import java.lang.AutoCloseable;

/**
 * A region of memory managed by the Extism runtime
 */
public class MemoryRegion implements AutoCloseable {
    private long offset;
    private long length;

    /**
     * Creates a new MemoryRegion from the given offset and length.
     * If <code>length</code> is zero, an exception will be thrown
     * 
     * @param offset the offset of the allocation in Extism's memory space
     * @param length the length of the allocation
     * @throws Exception if <code>length</code> is zero
     */
    public MemoryRegion(long offset, long length) throws Exception {
        if (length == 0) {
            throw new Exception("the length of a memory region cannot be 0");
        }

        this.offset = offset;
        this.length = length;
    }

    /**
     * Creates a new MemoryRegion from the given offset, requesting its length from the Extism runtime.
     * If <code>offset</code> doesn't correspond to a valid allocation, an exception will be thrown
     * 
     * @param offset the offset of the allocation in Extism's memory space
     * @throws Exception if the allocation pointed to by <code>offset</code> is invalid
     */
    public MemoryRegion(long offset) throws Exception {
        this.offset = offset;
        this.length = Native.length(offset);

        if (this.length == 0) {
            throw new Exception("could not find a valid allocation at offset " + offset);
        }
    }

    /**
     * Allocates a region of memory with the given length
     * 
     * @param length the length of the memory region to allocate
     */
    public static MemoryRegion allocate(long length) throws Exception {
        return new MemoryRegion(Native.alloc(length), length); // TODO: can alloc() fail?
    }

    /**
     * Allocates a region of memory and copies the contents of the given byte array into it
     * 
     * @param bytes the byte array to copy into Extism managed memory
     * @return the region of memory that was allocated
     */
    public static MemoryRegion allocateAndCopy(byte[] bytes) throws Exception {
        MemoryRegion region = allocate(bytes.length);
        region.write(bytes);
        return region;
    }

    /**
     * Gets the offset associated with this memory region
     * 
     * @return the offset associated with this memory region
     */
    public long offset() {
        return this.offset;
    }

    /**
     * Gets the length of this memory region
     * 
     * @return the length of this memory region
     */
    public long length() {
        return this.length;
    }

    public void close() {
        Native.free(this.offset);
    }

    /**
     * Writes the given byte array to the memory region.
     * If the byte array is larger than the memory region, an exception will be thrown
     * 
     * @param bytes the array of bytes to write to this memory region
     * @throws Exception if the array is too big
     */
    public void write(byte[] bytes) throws Exception {
        if (bytes.length > this.length) {
            throw new Exception("length of memory region and length of byte array to write differ");
        }

        for (int i = 0; i < bytes.length; i ++) {
            Native.storeByte(this.offset + i, bytes[i]); // TODO: optimize with storeLong() when applicable
        }
    }

    /**
     * Writes the given <code>String</code> to the memory region.
     * If the length of the string when UTF-8 encoded and the length of this memory region differ, an exception will be thrown
     * 
     * @param string the string to write to this memory region
     * @throws Exception if the string is too long when encoded as UTF-8
     */
    public void writeString(String string) throws Exception {
        this.write(string.getBytes());
    }

    /**
     * Reads the contents of this memory region
     * 
     * @return the contents of this memory region copied into a byte array
     */
    public byte[] read() {
        byte[] bytes = new byte[(int) this.length];

        for (int i = 0; i < bytes.length; i ++) {
            bytes[i] = Native.loadByte(this.offset + i); // TODO: optimize with loadLong() when applicable
        }

        return bytes;
    }

    /**
     * Reads the contents of this memory region to a <code>String</code>
     * 
     * @return the contents of this memory region copied into a string
     */
    public String readString() {
        return new String(this.read());
    }
}
