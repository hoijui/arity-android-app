// Copyright (C) 2009 Mihai Preda

package calculator;

import android.content.Context;
import java.io.*;

abstract class FileHandler {

    private final String fileName;
    private final Context context;
    private final int version;
    boolean fileNotFound;

    private DataInputStream openInput() throws IOException {
        try {
            return new DataInputStream(new BufferedInputStream(context.openFileInput(fileName), 256));
        } catch (FileNotFoundException e) {
            fileNotFound = true;
            return null;
        }
    }

    private DataOutputStream openOutput() throws IOException {
    return new DataOutputStream(new BufferedOutputStream(context.openFileOutput(fileName, 0), 256));
    }

    FileHandler(Context context, String fileName, int version) {
    this.context = context;
    this.fileName = fileName;
    this.version = version;
    }

    void load() {
        try {
            DataInputStream is = openInput();
            if (is != null) {
                int readVersion = is.readInt();
                if (readVersion != version) {
                    throw new IllegalStateException("invalid version " + readVersion);
                }
                doRead(is);
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("" + e); // FIXME
        }
    }

    void save() {
        try (DataOutputStream os = openOutput()) {
            os.writeInt(version);
            doWrite(os);
        } catch (IOException e) {
            throw new RuntimeException("" + e); // FIXME
        }
    }

    abstract void doRead(DataInputStream is) throws IOException;
    abstract void doWrite(DataOutputStream os) throws IOException;
}
