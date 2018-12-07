package ro.ligaac;

import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        run();
    }

    private void run() {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            List<Action> allActions = stream.map(line -> {
                LinkedList<String> list = new LinkedList<>();
                Matcher matcher = Pattern.compile("\\d+").matcher(line);

                while (matcher.find()) {
                    list.add(matcher.group());
                }

                int[] dates = list.stream().mapToInt(Integer::valueOf).toArray();
                int year = dates[0];
                int month = dates[1];
                int day = dates[2];
                int hours = dates[3];
                int minutes = dates[4];
                String content = line.split("] ")[1];

                Calendar calendar = new GregorianCalendar();
                calendar.clear();
                calendar.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
                calendar.set(year, month - 1, day, hours, minutes, 0);

                return new Action(calendar, content);
            }).sorted(Comparator.comparing(a -> a.calendar)).collect(Collectors.toList());

            HashMap<Long, List<Action>> actionsByDay = new HashMap<>();

            PrintStream ps = new PrintStream(new FileOutputStream(new File("output")));
            for (Action a : allActions) {
                String pattern = "[%4d-%02d-%02d %02d:%02d] %s";
                ps.println(String.format(pattern, a.calendar.get(Calendar.YEAR), a.calendar.get(Calendar.MONTH), a.calendar.get(Calendar.DAY_OF_MONTH),
                        a.calendar.get(Calendar.HOUR), a.calendar.get(Calendar.MINUTE), a.theAction));

                Long key = Long.valueOf("" + a.calendar.get(Calendar.YEAR) + a.calendar.get(Calendar.MONTH) + a.calendar.get(Calendar.DAY_OF_MONTH));

                if (!actionsByDay.containsKey(a.calendar.getTimeInMillis())) {
                    actionsByDay.put(key, new ArrayList<>());
                }
                actionsByDay.get(key).add(a);
            }
            ps.close();

            int id = -1;
            Calendar startSleep = null;

            Action previous;

            Map<Integer, Guard> guards = new HashMap<>();

            for (Action currentAction : allActions) {
                if (currentAction.theAction.contains("#")) {
                    Scanner s = new Scanner(currentAction.theAction);
                    s.skip("Guard #");

                    id = s.nextInt();
                    continue;
                }

                if (currentAction.theAction.equals("falls asleep")) {
                    startSleep = currentAction.calendar;
                }

                if (currentAction.theAction.equals("wakes up")) {
                    if (startSleep == null) {
                        System.out.println("Some error encountered with startSleep");
                        System.exit(0);
                    }

                    double minutesSlept = (currentAction.calendar.getTimeInMillis() - startSleep.getTimeInMillis()) / 1000. / 60.;

                    guards.putIfAbsent(id, new Guard(id));
                    guards.get(id).sleepMinutes.add(new Pair<>(startSleep.get(Calendar.MINUTE), currentAction.calendar.get(Calendar.MINUTE)));
                    startSleep = null;
                }

                previous = currentAction;
            }

            Guard maxSleeping = guards.values().stream().max(Comparator.comparing(Guard::getSleepMinutes)).orElseThrow(NoSuchElementException::new);

            List<Integer[]> sleepTime = maxSleeping.sleepMinutes.stream().map(item -> {
                Integer[] sleepMinutes = new Integer[60];

                for (int i = item.getKey(); i < item.getValue(); i++) {
                    sleepMinutes[i] = 1;
                }

                return sleepMinutes;
            }).collect(Collectors.toList());

            int[] asleepStatsForMinutes = new int[60];
            for (int j = 0; j < sleepTime.size(); j++) {
                Integer[] minutes = sleepTime.get(j);

                System.out.print('[');
                for (int i = 0; i < minutes.length; i++) {
                    Integer minute = minutes[i];
                    if(minute != null) {
                        asleepStatsForMinutes[i]++;
                    }
                    System.out.print(minute == null ? '.' : '#');
                }
                System.out.println(']');
            }

            int maxIndex = 0;
            int maxValue = 0;
            for(int i = 0; i < asleepStatsForMinutes.length; i++) {
                if(maxValue < asleepStatsForMinutes[i]) {
                    maxValue = asleepStatsForMinutes[i];
                    maxIndex = i;

                }
                System.out.println(i + " => " + asleepStatsForMinutes[i]);
            }

            System.out.println(maxIndex);
            System.out.println(maxSleeping.id);


            Optional<Pair<Integer, Pair<Integer, Integer>>> bestOptional = guards.values().stream().map((Guard guard) -> {
                List<Integer[]> slept = guard.sleepMinutes.stream().map((Pair<Integer, Integer> item) -> {
                    Integer[] sleepMinutes = new Integer[60];

                    for (int i = item.getKey(); i < item.getValue(); i++) {
                        sleepMinutes[i] = 1;
                    }

                    return sleepMinutes;
                }).collect(Collectors.toList());

                int[] nightsSleptForEachMinute = new int[60];
                for(int i = 0; i < 60; i++) {
                    for(Integer[] night: slept) {
                        if(night[i] != null) {
                            nightsSleptForEachMinute[i]++;
                        }
                    }
                }

                int mValue = 0;
                int mIndex = 0;
                for(int i = 0; i < 60; i++){
                    if(mValue < nightsSleptForEachMinute[i]) {
                        mValue = nightsSleptForEachMinute[i];
                        mIndex = i;
                    }
                }

                Pair bestAsleep = new Pair<>(mIndex, mValue);
                return new Pair<Integer, Pair<Integer, Integer>>(guard.id, bestAsleep);
            }).reduce(new BinaryOperator<Pair<Integer, Pair<Integer, Integer>>>() {
                @Override
                public Pair<Integer, Pair<Integer, Integer>> apply(Pair<Integer, Pair<Integer, Integer>> o1, Pair<Integer, Pair<Integer, Integer>> o2) {
                    return o1.getValue().getValue() > o2.getValue().getValue() ? o1 : o2;
                }
            });

            if(bestOptional.isPresent()) {
                Pair<Integer, Pair<Integer, Integer>> best = bestOptional.get();

                System.out.println(best.getKey() * best.getValue().getKey());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Action {
        Calendar calendar;
        String theAction;

        public Action(Calendar date, String theAction) {
            this.calendar = date;
            this.theAction = theAction;
        }

        @Override
        public String toString() {
            return calendar.toString();
        }
    }

    class Guard {
        int id;
        List<Pair<Integer, Integer>> sleepMinutes;

        public Guard(int id) {
            this.id = id;
            sleepMinutes = new ArrayList<>();
        }

        public int getSleepMinutes() {
            int total = 0;

            for (Pair<Integer, Integer> sleep : sleepMinutes) {
                total += (sleep.getValue() - sleep.getKey());
            }

            return total;
        }
    }
}
