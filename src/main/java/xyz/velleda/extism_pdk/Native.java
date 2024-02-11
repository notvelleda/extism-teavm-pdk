package xyz.velleda.extism_pdk;

import org.teavm.interop.Import;

public class Native {
    @Import(name = "input_offset", module = "extism:host/env")
    public static native long inputOffset();

    @Import(name = "input_length", module = "extism:host/env")
    public static native long inputLength();

    @Import(name = "length", module = "extism:host/env")
    public static native long length(long offset);

    @Import(name = "length_unsafe", module = "extism:host/env")
    public static native long lengthUnsafe(long offset);

    @Import(name = "alloc", module = "extism:host/env")
    public static native long alloc(long n);

    @Import(name = "free", module = "extism:host/env")
    public static native void free(long offset);

    @Import(name = "input_load_u8", module = "extism:host/env")
    public static native byte inputLoadByte(long index);

    @Import(name = "input_load_u64", module = "extism:host/env")
    public static native long inputLoadLong(long index);

    @Import(name = "output_set", module = "extism:host/env")
    public static native void outputSet(long offset, long n);

    @Import(name = "error_set", module = "extism:host/env")
    public static native void errorSet(long offset);

    @Import(name = "config_get", module = "extism:host/env")
    public static native long configGet(long offset);

    @Import(name = "var_get", module = "extism:host/env")
    public static native long varGet(long offset);

    @Import(name = "var_set", module = "extism:host/env")
    public static native void varSet(long keyOffset, long valueOffset);

    @Import(name = "store_u8", module = "extism:host/env")
    public static native void storeByte(long offset, byte value);

    @Import(name = "load_u8", module = "extism:host/env")
    public static native byte loadByte(long index);

    @Import(name = "store_u64", module = "extism:host/env")
    public static native void storeLong(long offset, long value);

    @Import(name = "load_u64", module = "extism:host/env")
    public static native long loadLong(long offset);

    @Import(name = "http_request", module = "extism:host/env")
    public static native long httpRequest(long requestOffset, long bodyOffset);

    @Import(name = "http_status_code", module = "extism:host/env")
    public static native short httpStatusCode();

    @Import(name = "log_info", module = "extism:host/env")
    public static native void logInfo(long offset);

    @Import(name = "log_debug", module = "extism:host/env")
    public static native void logDebug(long offset);

    @Import(name = "log_warn", module = "extism:host/env")
    public static native void logWarn(long offset);

    @Import(name = "log_error", module = "extism:host/env")
    public static native void logError(long offset);
}
