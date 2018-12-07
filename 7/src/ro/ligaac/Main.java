package ro.ligaac;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            /**
             * Parse input into key = value pairs
             */
            List<Pair<Character, Character>> dependencies = stream.map(line -> {
                line = line.substring(5);
                Character first = line.charAt(0);

                line = line.substring(31);
                Character second = line.charAt(0);

                return new Pair<>(first, second);
            }).collect(Collectors.toList());

            List<Character> allEvents = new ArrayList<>();

            /**
             * Map the key value pairs to some sort of dependency list for each event(letter)
             */
            HashMap<Character, List<Character>> dependencyMap = new HashMap<>();
            for (Pair<Character, Character> parent : dependencies) {
                Character key = parent.getKey();
                Character value = parent.getValue();

                if (!dependencyMap.containsKey(value)) {
                    dependencyMap.put(value, new ArrayList<>());
                }

                dependencyMap.get(value).add(key);

                if (!allEvents.contains(key)) allEvents.add(key);
                if (!allEvents.contains(value)) allEvents.add(value);
            }

            String completedEvents = "";
            ArrayList<Character> startWith = new ArrayList<>(allEvents);

            /**
             * from all the events, eliminate the ones that depend on any other
             */
            dependencyMap.keySet().forEach(key -> {
                if (startWith.contains(key)) {
                    int index = startWith.indexOf(key);
                    startWith.remove(index);
                }
            });

            if (startWith.size() == 0) {
                throw new Exception("startWith is empty");
            }

            /**
             * sort and get the first one to start with it
             */
            Collections.sort(startWith);
            Character toStartWith = startWith.get(0);

            System.out.println(dependencyMap);

            /**
             * while not all events were completed
             */
            while (completedEvents.length() < allEvents.size()) {
                //add current event
                completedEvents += toStartWith;

                startWith.remove(toStartWith);

                /**
                 * Find next event
                 */
                List<Character> eventsChecked = Arrays.stream(completedEvents.split("")).map(item -> item.charAt(0)).collect(Collectors.toList());
                String finalCompletedEvents = completedEvents;
                dependencyMap.forEach((Character key, List<Character> deps) -> {
                    List<Character> depss = new ArrayList<>(deps);

                    for (Character e : eventsChecked) {
                        if (depss.contains(e)) {
                            depss.remove(e);
                        }
                    }

                    boolean alreadyUsed = finalCompletedEvents.indexOf(key) != -1;
                    if (depss.size() == 0 && !alreadyUsed && !startWith.contains(key)) {
                        startWith.add(key);
                    }
                });
                Collections.sort(startWith);

                if(startWith.size() == 0) break;
                toStartWith = startWith.get(0);
            }


            System.out.println(completedEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
