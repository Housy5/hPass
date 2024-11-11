package housy.pass;

public class Countdown extends Thread {

    private Runnable action;
    private long sleepTime;
    private volatile long count;
    private long duration;
    private volatile boolean stopped = false;

    public Countdown(long minutes, Runnable action) {
        this.action = action;
        this.duration = minutes * 60 * 1_000;
        count = duration;
        sleepTime = 500;
    }

    @Override
    public void run() {
        while (duration == 0) { sleep(); }
        for (; count > 0; count -= sleepTime) {
            sleep();
            if (stopped) {
                return;
            }
        }
        action.run();
    }

    private void sleep() throws RuntimeException {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void waitForStop() {
        try {
            join();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void reset() {
        count = duration;
        stopped = false;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        stopped = true;
        waitForStop();
        this.action = action;
        reset();
        start();
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        stopped = true;
        waitForStop();
        this.sleepTime = sleepTime;
        reset();
        start();
    }

    public long getCount() {
        return count;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        stopped = true;
        waitForStop();
        this.duration = duration;
        reset();
        start();
    }
}
