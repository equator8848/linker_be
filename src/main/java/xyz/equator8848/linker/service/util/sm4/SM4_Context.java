package xyz.equator8848.linker.service.util.sm4;

/**
 * @author Equator
 */
public class SM4_Context {
    public int mode = 1;
    public long[] sk = new long[32];
    public boolean isPadding = true;

    protected SM4_Context() {
    }
}
