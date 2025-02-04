package DNAnalyzer.utils.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class FastQ {
    private String filename;
    // HashMap: key = sequence ([ATGC+]), value = quality sequence
    private HashMap<String, String> sequences;
    private ArrayList<Character> qualities;

    public FastQ(String fname) {
        filename = fname;
        sequences = new HashMap<String, String>();
        qualities = new ArrayList<Character>();
    }

    public HashMap<String, String> readFastQ() throws IOException {
        // read in all the lines from the file and store in one string, and then iterate
        // over each line
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.lines().collect(Collectors.joining("\n"));
        String[] lines = line.split("\n");
        for (int i = 0; i < lines.length; i += 4) {
            String sequence = lines[i + 1];
            String quality = lines[i + 3];
            sequences.put(sequence, quality);
            // put all the qualities into an arraylist
            for (char c : quality.toCharArray()) {
                qualities.add(c);
            }
        }
        br.close();
        return sequences;
    }

    public char averageQuality() {
        // calculate the average quality of the sequences
        int total = 0;
        int sum = 0;
        for (char c : qualities) {
            sum += (int) c;
            total++;
        }
        return (char) (sum / total);
    }

    public int phred33ToQ(char c) {
        // convert a character to a quality score
        return (int) c - 33;
    }

    public char qToPhred33(int q) {
        // convert a quality score to a character
        return (char) (q + 33);
    }

    public int[] createHistogram() {
        int[] hist = new int[50];
        for (char phred : qualities) {
            int q = phred33ToQ(phred);
            if (!(q < 0 || q >= hist.length)) {
                hist[q]++;
            }
        }
        return hist;
    }
}
