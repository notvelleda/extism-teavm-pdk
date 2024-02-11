package xyz.velleda.extism_pdk;

import java.util.Optional;

public class Pdk {
    /**
     * Gets the input data from the runtime
     * 
     * @return the input data copied into a byte array
     */
    public static byte[] getInput() throws Exception {
        // a potential resource leak here doesn't matter, as this memory region is allocated by the runtime, not the plugin
        return new MemoryRegion(Native.inputOffset()).read();
    }

    /**
     * Gets the input data from the runtime as a <code>String</code>
     * 
     * @return the input data decoded as a UTF-8 string
     */
    public static String getInputString() throws Exception {
        return new String(getInput());
    }

    /**
     * Sets the output of the current plugin method invocation to the data contained in the given memory region
     * 
     * @param region the region of memory to use as the output
     */
    public static void setOutput(MemoryRegion region) {
        Native.outputSet(region.offset(), region.length());
    }

    /**
     * Copies the given byte array into Extism managed memory and sets the output of the current plugin method invocation to it
     * 
     * @param bytes the byte array to use as the output
     */
    public static void setOutput(byte[] bytes) throws Exception {
        setOutput(MemoryRegion.allocateAndCopy(bytes));
    }

    /**
     * Copies the given string into Extism managed memory and sets the output of the current plugin method invocation to it
     * 
     * @param string the string to use as the output
     */
    public static void setOutput(String string) throws Exception {
        setOutput(string.getBytes());
    }

    /**
     * Copies the given string into Extism managed memory and sets the error message of the current plugin invocation to it
     * 
     * @param string the string to use as the error message
     */
    public static void setError(String string) {
        try {
            Native.errorSet(MemoryRegion.allocateAndCopy(string.getBytes()).offset());
        } catch (Exception e) {}
    }

    /**
     * Logs a message with the specified log level
     * 
     * @param level the log level of the message
     * @param message the message to log
     */
    public static void log(LogLevel level, String message) {
        try {
            try (MemoryRegion region = MemoryRegion.allocateAndCopy(message.getBytes())) {
                switch (level) {
                    case ERROR:
                        Native.logError(region.offset());
                        break;
                    case WARN:
                        Native.logWarn(region.offset());
                        break;
                    case INFO:
                        Native.logInfo(region.offset());
                        break;
                    case DEBUG:
                        Native.logDebug(region.offset());
                        break;
                }
            }
        } catch (Exception e) {}
    }

    /**
     * Gets the contents of the Extism variable with the provided name, if one exists
     * 
     * @param name the name of the variable
     * @return the contents of the variable, if it exists
     */
    public static Optional<MemoryRegion> getVariable(String name) {
        try {
            try (MemoryRegion region = MemoryRegion.allocateAndCopy(name.getBytes())) {
                return Optional.of(new MemoryRegion(Native.varGet(region.offset())));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Sets the contents of the Extism variable with the provided name
     * 
     * @param name the name of the variable to set
     * @param region the value of the variable, as a memory region
     */
    public static void setVariable(String name, MemoryRegion region) throws Exception {
        try (MemoryRegion nameRegion = MemoryRegion.allocateAndCopy(name.getBytes())) {
            Native.varSet(nameRegion.offset(), region.offset());
        }
    }

    /**
     * Sets the contents of the Extism variable with the provided name
     * 
     * @param name the name of the variable to set
     * @param bytes the value of the variable, to be copied into Extism memory
     */
    public static void setVariable(String name, byte[] bytes) throws Exception {
        setVariable(name, MemoryRegion.allocateAndCopy(bytes));
    }

    /**
     * Sets the contents of the Extism variable with the provided name
     * 
     * @param name the name of the variable to set
     * @param string the value of the variable, to be copied into Extism memory
     */
    public static void setVariable(String name, String string) throws Exception {
        setVariable(name, string.getBytes());
    }

    /**
     * Removes an Extism variable and frees its contents
     * 
     * @param name the name of the variable to remove
     */
    public static void removeVariable(String name) throws Exception {
        try (MemoryRegion region = MemoryRegion.allocateAndCopy(name.getBytes())) {
            // TODO: does Extism automatically free variable contents?
            long offset = Native.varGet(region.offset());
            if (offset != 0) {
                Native.free(offset);
            }

            Native.varSet(region.offset(), 0);
        }
    }
}
