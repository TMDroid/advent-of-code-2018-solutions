package ro.ligaac;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        run();
    }

    private void run() {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            List<String> items = new ArrayList<>();
            stream.forEach(items::add);

            List<String> twice = new ArrayList<>();
            List<String> thrice = new ArrayList<>();

            for (String item : items) {
                ArrayList<Letter> letters = new ArrayList<>();
                for (int i = 0; i < item.length(); i++) {
                    char c = item.charAt(i);

                    boolean incremented = false;
                    for (Letter l : letters) {
                        if (l.letter == c) {
                            l.frequency++;
                            incremented = true;
                            break;
                        }
                    }

                    if (!incremented) {
                        letters.add(new Letter(c));
                    }
                }

                boolean letterTwice = false;
                boolean letterThreeTimes = false;
                for (Letter l : letters) {
                    if (l.frequency == 2) {
                        letterTwice = true;
                    } else if (l.frequency == 3) {
                        letterThreeTimes = true;
                    }
                }

                if(letterTwice && twice.indexOf(item) == -1) {
                    twice.add(item);
                }

                if(letterThreeTimes && thrice.indexOf(item) == -1) {
                    thrice.add(item);
                }
            }

            int result = twice.size() * thrice.size();
            System.out.println(result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Letter {
        public char letter;
        public int frequency;

        public Letter(char c) {
            this.letter = c;
            this.frequency = 1;
        }
    }
}
