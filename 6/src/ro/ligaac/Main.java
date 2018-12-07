package ro.ligaac;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Main {
    public static final int MAX_DISTANCE = 10000;

    public static void main(String[] args) {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            ArrayList<ManhattanPoint> points = new ArrayList<>();

            stream.forEach(row -> {
                String[] coordinates = row.split(", ");

                if (coordinates.length == 2) {
                    Point p = new Point(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
                    ManhattanPoint point = new ManhattanPoint(p);
                    points.add(point);
                }
            });

            ManhattanPoint xMax = points.stream().max((o1, o2) -> o1.p.x > o2.p.x ? 1 : -1).orElseThrow(NoSuchElementException::new);
            ManhattanPoint yMax = points.stream().max((o1, o2) -> o1.p.y > o2.p.y ? 1 : -1).orElseThrow(NoSuchElementException::new);

            Integer width = xMax.p.x + 2;
            Integer height = yMax.p.y + 2;

            Character[][] matrix = new Character[height][width];

            points.forEach(point -> {
                if (matrix[point.p.y][point.p.x] == null) {
                    matrix[point.p.y][point.p.x] = point.letter;
                    point.times++;
                }
            });

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (matrix[i][j] == null) {
                        Point p = new Point(j, i);

                        ManhattanPoint nearest = ManhattanPoint.findNearest(p, points);
                        if (nearest == null) {
                            matrix[i][j] = '.';
                        } else {
                            matrix[i][j] = Character.toLowerCase(nearest.letter);
                        }
                    }
                }
            }

            HashMap<Character, Integer> intalniri = new HashMap<>();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Character c = Character.toLowerCase(matrix[i][j]);
                    if (!intalniri.containsKey(c)) {
                        intalniri.put(c, 0);
                    }

                    intalniri.put(c, intalniri.get(c) + 1);
                }
            }

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    System.out.print(matrix[i][j]);

                    Character c = Character.toLowerCase(matrix[i][j]);
                    if ((i == 0 || i == height - 1 || j == 0 || j == width - 1) && intalniri.containsKey(c)) {
                        intalniri.remove(c);
                    }
                }

                System.out.println("");
            }

            for (Character c : intalniri.keySet()) {
                System.out.println(c + ": " + intalniri.get(c));
            }

            Integer maximum = intalniri.values().stream().max(Comparator.comparing(Integer::intValue)).orElseThrow(NoSuchElementException::new);

            System.out.println("Maximum is " + maximum);

            int areaSizeWithDistanceLessThanMax = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Point p = new Point(j, i);

                    int sum = 0;
                    for (ManhattanPoint manhattanPoint : points) {
                        sum += manhattanPoint.distanceTo(p);
                    }

                    if(sum < MAX_DISTANCE) {
                     areaSizeWithDistanceLessThanMax++;
                    }
                }
            }

            System.out.println("Area size is: " + areaSizeWithDistanceLessThanMax);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
