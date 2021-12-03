import java.io.*;

public class FileLoader {
    static void writeFile (String s) throws IOException {
        FileLoader.class.getResource("parser.txt").getFile();
        FileWriter myWriter;
        myWriter = FileLoader.class.getResource("parser.txt").getFile();
//        myWriter = new FileWriter("src/main/resources/old_coin.txt");
        myWriter.write(s);
        myWriter.close();
    }

    static String readFile (String name) throws IOException {
        InputStream is = FileLoader.class.getResourceAsStream("/" + name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
}

