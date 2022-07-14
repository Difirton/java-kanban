package config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

public class GsonFileAdapter extends TypeAdapter<File> {
    @Override
    public void write(JsonWriter writer, File file) throws IOException {
        writer.beginObject();
        writer.name("file");
        writer.value(file.getPath());
        writer.endObject();
    }

    @Override
    public File read(JsonReader reader) throws IOException {
        reader.beginObject();
        String fieldName = null;
        String path = null;
        JsonToken token = reader.peek();
        if (token.equals(JsonToken.NAME)) {
            fieldName = reader.nextName();
        }
        if ("file".equals(fieldName)) {
            token = reader.peek();
            path = reader.nextString();
        }
        reader.endObject();
        return new File(path);
    }
}
