package ro.ligaac;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

            int[][] material = new int[1000][1000];
            List<Cut> cuts = new ArrayList<>();
            for (String line : items) {
                line = line.replaceAll("[^-?0-9]+", " ");

                List<Integer> arguments = Arrays.asList(line.trim().split(" ")).stream()
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                int id = arguments.get(0);
                int lo = arguments.get(1);
                int to = arguments.get(2);
                int w = arguments.get(3);
                int h = arguments.get(4);

                cuts.add(new Cut(id, lo, to, h, w));

                for (int i = to; i < to + h; i++) {
                    for (int j = lo; j < lo + w; j++) {
                        if (material[i][j] == 0) {
                            material[i][j] = id;
                        } else {
                            material[i][j] = -1;
                        }
                    }
                }
            }

            for (Cut c : cuts) {
                boolean overlaps = false;

                for (int i = c.marginTop; i < c.marginTop + c.height; i++) {
                    for (int j = c.marginLeft; j < c.marginLeft + c.width; j++) {
                        if (material[i][j] != c.id) {
                            overlaps = true;
                        }
                    }
                }

                if (!overlaps) {
                    System.out.println(c.id + " does not overlap");
                }
            }

            int count = 0;
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < 1000; j++) {
                    if (material[i][j] == -1) count++;
                }
            }

            System.out.println(count);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Cut {
        int id;
        int marginLeft;
        int marginTop;
        int height;
        int width;

        public Cut(int id, int marginLeft, int marginTop, int height, int width) {
            this.id = id;
            this.marginLeft = marginLeft;
            this.marginTop = marginTop;
            this.height = height;
            this.width = width;
        }
    }
}
