import java.io.*;
import java.net.URL;

public class FileLoader {
    static void writeFile (String s) throws IOException {
        URL resource = FileLoader.class.getClassLoader().getResource("parser.txt");
        FileWriter myWriter;
        myWriter = new FileWriter(String.valueOf(resource));
        myWriter.write(s);
        myWriter.close();
    }

    static String readFile (String name) throws IOException {
        InputStream is = FileLoader.class.getResourceAsStream("/" + name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
}

