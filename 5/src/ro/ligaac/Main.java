package ro.ligaac;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        (new Main()).run("");

    }

    public Main() {
    }

    public String run(String s) {

        String polymer = s;
        if (s.isEmpty()) {
            try (Stream<String> stream = Files.lines(Paths.get("input"))) {
                polymer = (String) stream.toArray()[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean reacted;
        do {
            reacted = false;

            for (int i = 0; i < polymer.length() - 1; i++) {
                char current = polymer.charAt(i);
                char next = polymer.charAt(i + 1);

                if (Character.toLowerCase(current) == Character.toLowerCase(next)) { //if the same as lower case
                    if (current != next) { //but different cases by themselves
                        polymer = removeCharAtIndex(polymer, i);
                        polymer = removeCharAtIndex(polymer, i);
                        reacted = true;
                        break;
                    }
                }
            }
        } while (reacted);

        return polymer;
    }

    private String removeCharAtIndex(String str, int index) {
        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(index);

        return sb.toString();
    }
}
