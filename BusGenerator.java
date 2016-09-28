import java.util.Random;

/**
 * Class to spawn the threads for the bus arrival
 */
public class BusGenerator implements Runnable {

    private float arrivalMeanTime;
    private WaitingArea waitingArea;
    private static Random random;

    public BusGenerator(float arrivalMeanTime, WaitingArea waitingArea) {
        this.arrivalMeanTime = arrivalMeanTime;
        this.waitingArea = waitingArea;
        random = new Random();
    }

    @Override
    public void run() {

        int busIndex = 1;

        // Spawning bus threads for the user specified value
        while (!Thread.currentThread().isInterrupted()) {

            try {
                // Initializing and starting the bus threads
                Bus bus = new Bus(waitingArea.getRiderBoardingAreaEntranceSem(), waitingArea.getBusDepartureSem(), waitingArea.getMutex(), busIndex, waitingArea);
                (new Thread(bus)).start();

                busIndex++;
                // Sleeping the thread to obtain the inter arrival time between the bus threads
                Thread.sleep(getExponentiallyDistributedBusInterArrivalTime());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All buses have finished arriving");
    }

    // Method to get the exponentially distributed bus inter arrival time
    public long getExponentiallyDistributedBusInterArrivalTime() {
        float lambda = 1 / arrivalMeanTime;
        return Math.round(-Math.log(1 - random.nextFloat()) / lambda);
    }
}