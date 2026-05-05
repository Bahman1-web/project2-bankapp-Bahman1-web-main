package edu.brooklyn.project2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

public abstract class AbstractBankTests {
    protected static final String CHOOSE_ACCOUNT_LOGIN = "1";
    protected static final String CHOOSE_CREATE_ACCOUNT = "2";
    protected static final String CHOOSE_EXIT = "0";

    protected static final String CHOOSE_BALANCE = "1";
    protected static final String CHOOSE_WITHDRAWAL = "2";
    protected static final String CHOOSE_DEPOSIT = "3";

    protected List<String> responses;

    protected Path tmpDir;

    @BeforeEach
    public void setTestDir(@TempDir final Path tmpDir) {
        this.tmpDir = tmpDir;
    }

    protected List<String> runMain(String... input) {
        String accounts = tmpDir.resolve("accounts.txt").toString();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        LinkedOutputStream outputStream = new LinkedOutputStream();
        LinkedInputStream inputStream = new LinkedInputStream(input, outputStream);

        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            BankApp.main(new String[] {accounts});
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        outputStream.flushResponse();
        return outputStream.getResponses();
    }

    /** Returns the first non-empty line of string, or null if none exists. */
    protected String firstLineOf(String string) {
        Optional<String> firstLine = string.stripLeading().lines().findFirst();
        if (firstLine.isPresent())
            return firstLine.get();
        return null;
    }

    protected Stream<String> linesIn(List<String> strings) {
        return String.join(System.lineSeparator(), strings).lines();
    }
}

class LinkedInputStream extends InputStream {
    private byte[] buffer;
    private int pos = 0;
    private int count = 0;
    private ByteArrayInputStream byteStream;
    private LinkedOutputStream outputStream;

    public LinkedInputStream(String[] input, LinkedOutputStream outputStream) {
        this(String.join(System.lineSeparator(), input).getBytes(), outputStream);
    }

    public LinkedInputStream(byte[] buf, LinkedOutputStream outputStream) {
        buffer = new byte[256];
        byteStream = new ByteArrayInputStream(buf);
        this.outputStream = outputStream;
    }

    protected void bufferLine() {
        outputStream.waitForInput();
        pos = 0;
        int c = 0;
        for (count = 0; count < buffer.length && byteStream.available() > 0 && c != '\n'; count++) {
            c = byteStream.read();
            buffer[count] = (byte) c;

        }
    }

    @Override
    public int read() {
        if (pos == count)
            bufferLine();
        if (count == 0)
            return -1;
        return (int) buffer[pos++];
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (len == 0)
            return 0;
        if (pos == count)
            bufferLine();
        if (count == 0)
            return -1;
        int i = 0;
        while (pos < count && i < len) {
            b[off + i++] = buffer[pos++];
        }
        return i;
    }

    @Override
    public byte[] readAllBytes() {
        return readNBytes(count - pos + byteStream.available());
    }

    @Override
    public byte[] readNBytes(int len) {
        byte[] b = new byte[len];
        int off = 0;
        int r = read(b, 0, b.length);
        while (r >= 0 && off < b.length) {
            off += r;
            r = read(b, off, b.length - off);
        }
        return b;
    }
}

class LinkedOutputStream extends OutputStream {
    private boolean newResponse = false;
    private List<String> responses;
    private ByteArrayOutputStream byteStream;

    public LinkedOutputStream() {
        responses = new ArrayList<String>();
        byteStream = new ByteArrayOutputStream();
    }

    public void waitForInput() {
        newResponse = true;
    }

    public void flushResponse() {
        if (byteStream.size() > 0) {
            responses.add(byteStream.toString());
            byteStream.reset();
        }
    }

    @Override
    public void write(int b) {
        if (newResponse) {
            flushResponse();
            newResponse = false;
        }
        byteStream.write(b);
    }

    public List<String> getResponses() {
        return new ArrayList<>(responses);
    }
}
