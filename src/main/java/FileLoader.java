import java.io.*;
import java.net.URL;

public class FileLoader {
    static void writeFile (String s) throws IOException {
        URL resource = FileLoader.class.getClassLoader().getResource("parser.txt");
        System.out.println(resource);
        FileWriter myWriter;
        myWriter = new FileWriter(String.valueOf(resource));
        myWriter.write(s);
        myWriter.close();
    }

    static String readFile (String name) throws IOException {
        InputStream is = FileLoader.class.getResourceAsStream("/" + name);
//        System.out.println(FileLoader.class.getClassLoader().getResource("old_coin.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
}

