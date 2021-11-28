import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {
    static void writeFile (String s) throws IOException {
        FileWriter myWriter = new FileWriter("/src/main/resources/old_coin.txt");
        myWriter.write(s);
        myWriter.close();
    }

    static String readFile () throws IOException {
        InputStream is = FileLoader.class.getResourceAsStream("/old_coin.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
}

