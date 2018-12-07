package com.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main2 {

    public static void main(String[] args) {
        new Main2();
    }

    private Main2() {
        run();
    }

    private void run() {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            List<String> items = new ArrayList<>();
            stream.forEach(items::add);

            for(String a : items) {
                for(String b : items) {
                    if(countDifferences(a, b) == 1) {
                        String noDifferences = "";

                        for(int i = 0; i < a.length(); i++) {
                            char x = a.charAt(i);
                            char y = b.charAt(i);

                            if(x == y) noDifferences += x;
                        }

                        System.out.println(noDifferences);
                        System.exit(0);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countDifferences(String a, String b) {
        if(a.length() != b.length()) return a.length();

        int count = 0;
        for(int i = 0; i < a.length(); i++) {
            char x = a.charAt(i);
            char y = b.charAt(i);

            if(x != y) count++;
        }

        return count;
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
