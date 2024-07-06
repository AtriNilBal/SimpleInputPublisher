package org.atrinils.generatevolumefromlocalstorage;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class TestDataFromStaticSource {

    public static List<Map<String, Object>> getTestData() {

        List<Map<String, Object>> inputData = new ArrayList<>();
        Map<String, Object> dataSet = new HashMap<>();
        dataSet.put("BONDNAME", "jpy_apple_5Y");
        dataSet.put("DIRECTION", "SELL");
        dataSet.put("time", "00:00:00");
        dataSet.put("SELL", 120.22);
        dataSet.put("BUY", 33.211);
        dataSet.put("CURR", "JPY");
        dataSet.put("REGION", "Japan");

        inputData.add(dataSet);

        return inputData;
    }

    public static Runnable prepareInputVolumeGenerator(List<Map<String, Object>> inputData) {

        Random random = new Random();
        BiFunction<String, Object, Object> randomInputGenFunction = (k,v) -> {
            double randomNum = (random.nextInt(100)/100.0);
            return randomNum + random.nextInt(900);
        };
        AtomicInteger publishedSamplesCounter = new AtomicInteger(1);

        Runnable publishSingleDataSet = () -> {
            System.out.printf("Input Sample no - %d \n",publishedSamplesCounter.getAndIncrement());
            for(Map<String, Object> dataSet : inputData) {
                dataSet.compute("SELL", randomInputGenFunction);
                dataSet.compute("BUY", randomInputGenFunction);
                System.out.println("Publishing input : " + dataSet.values());
            }
        };
        return publishSingleDataSet;
    }

    public static void main(String[] args) {

        long durationInMinutes = 1;
        long waitTimeBetweenEachInputInSeconds = 1;

        Instant startTime = Instant.now();
        Instant endTime = Instant.now();
        ScheduledExecutorService inputVolumePublisher = Executors.newSingleThreadScheduledExecutor();
        try {
            while(Duration.between(startTime, Instant.now()).getSeconds() <= (durationInMinutes*60)) {
                inputVolumePublisher.scheduleAtFixedRate(prepareInputVolumeGenerator(getTestData()),
                        1000, waitTimeBetweenEachInputInSeconds*1000,
                        TimeUnit.MILLISECONDS);
            }
            endTime = Instant.now();
        } finally {
            inputVolumePublisher.shutdown();
        }

        System.out.printf("Start time %s and End time %s\n", startTime, endTime);

    }
}
