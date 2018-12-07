package ro.ligaac;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
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

            StringBuilder completedEvents = new StringBuilder();
            ArrayList<Character> startWith = new ArrayList<>(allEvents);
            ArrayList<Character> assignedTasks = new ArrayList<>();
            ArrayList<Worker> workers = new ArrayList<Worker>() {{
                add(new Worker());
                add(new Worker());
                add(new Worker());
                add(new Worker());
                add(new Worker());
            }};

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

            System.out.println(dependencyMap);
            System.out.println("T\t\t1\t\t2\t\t3\t\t4\t\t5\t\ts");

            /**
             * while not all events were completed
             */
            int seconds = 0;
            boolean working = true;
            while (completedEvents.length() < allEvents.size() || working) {
                /**
                 * Find next event
                 */
                List<Character> eventsChecked = Arrays.stream(completedEvents.toString().split("")).map(item -> item.length() > 0 ? item.charAt(0) : null).collect(Collectors.toList());
                dependencyMap.forEach((Character key, List<Character> deps) -> {

                    if (!deps.isEmpty()) {
                        for (Character e : eventsChecked) {
                            if (deps.contains(e)) {
                                deps.remove(e);
                            }
                        }
                    }

                    if (!completedEvents.toString().contains(String.valueOf(key))) {
                        if (deps.isEmpty() && !startWith.contains(key) && !assignedTasks.contains(key)) {
                            startWith.add(key);
                        }
                    }

                });
//                Collections.sort(startWith);

                if (startWith.size() > 0) {
                    for (int i = 0; i < startWith.size(); i++) {
                        if (startWith.size() > 0) {
                            Character task = startWith.get(0);

                            for (Worker w : workers) {
                                if (w.isPaused()) {
                                    w.assignTask(task);
                                    assignedTasks.add(task);
                                    startWith.remove(task);
                                    i--;
                                    break;
                                }
                            }
                        }
                    }
                }

                System.out.print(seconds + "\t\t");
                for (Worker w : workers) {
                    System.out.print((w.getTimeLeft() > 0 ? w.getTask() + "(" + w.getTimeLeft() + ")" : "....") + "\t\t");
                    w.decrementTime();

                    if (assignedTasks.contains(w.getTask()) && w.isPaused()) {
                        //add current event
                        completedEvents.append(w.getTask());
                        assignedTasks.remove(w.getTask());
                    }
                }
                System.out.println(completedEvents);

                working = workers.stream().map(Worker::isWorking).reduce((acc, current) -> acc || current).orElseThrow(NoSuchElementException::new);
                seconds++;
            }


            System.out.println(completedEvents);
            System.out.println(seconds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
