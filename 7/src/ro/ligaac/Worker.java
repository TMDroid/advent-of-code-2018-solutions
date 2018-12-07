package ro.ligaac;

public class Worker {
    private Character task;
    private int timeLeft;

    public Worker() {
        this.timeLeft = -1;
    }

    public void assignTask(Character task) {
        this.task = task;
        this.timeLeft = 60 + (task - 'A' + 1);
//        this.timeLeft = (task - 'A' + 1);
    }

    public void decrementTime() {
        this.timeLeft--;
    }

    public boolean isWorking() {
        return this.timeLeft >= 0;
    }

    public boolean isPaused() {
        return !this.isWorking();
    }

    public Character getTask() {
        return this.task;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
