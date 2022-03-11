import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args)  {
        String pathToDict = "F:\\nlpDatasets\\dict.opcorpora.xml";
        String strPathToCorpus = "F:\\nlpDatasets\\news.txt";
        String strPathToOutput = "C:\\Users\\vvpvo\\Desktop\\nsu\\Lematizer\\output.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(strPathToCorpus))) {
            Dict dict = Utils.parseDict(pathToDict);
            // Если забить на эфективность по памяти и оставить только по времени,
            // то что может быть эффективнее хэшмапы?
            HashMap<String, ArrayList<Lemma>> hash = Utils.dictToMap(dict);

            String line = reader.readLine();
            HashMap<Lemma, Integer> stats = new HashMap<>();
            int unrecognized = 0;
            long totalWords = 0;
            int certain = 0;
            int amogus = 0;
            while (line != null) {
                var lemmas = Utils.lemmatizeLine(Utils.tokenizeLine(line), hash);

                for (var lemma : lemmas) {
                    if (lemma != null) {
                        if (lemma.size() == 1)
                            certain++;
                        else
                            amogus++;
                        Lemma curLemma = lemma.get(0);
                        if (stats.containsKey(curLemma)) {
                            stats.put(curLemma, stats.get(curLemma) + 1);
                        } else {
                            stats.put(curLemma, 1);
                        }
                    }
                    else
                    {
                        unrecognized+=1;
                    }
                    totalWords+=1;
                }
                line = reader.readLine();
            }

            LinkedList<Map.Entry<Lemma, Integer>> list = new LinkedList<Map.Entry<Lemma, Integer> >(stats.entrySet());

            list.sort(Map.Entry.comparingByValue());
            try (FileWriter fileWriter = new FileWriter(strPathToOutput)) {
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println("STATISTICS");
                printWriter.printf("total amount of words: %d%n", totalWords);
                printWriter.printf("couldn't recognize: %d, that's %f percent%n", unrecognized, ((double) unrecognized / (double) totalWords) * 100);
                printWriter.printf("omonims: %d, that's %f percent%n", amogus, ((double) amogus / (double) totalWords) * 100);

                printWriter.printf("accuracy %f%n", ( (double) certain / (double)totalWords)*100);
                printWriter.println("MOST FREQUENT LEMMAS");
                for (int i = 0; i < 50; i++) {
                    printWriter.println(list.peekLast().getKey());
                    printWriter.printf("occurrences : %d%n", list.pollLast().getValue());
                }


            }
        }
        catch (XMLStreamException | InvalidPathException | IOException e )
        {
            e.printStackTrace();
        }
    }
}
