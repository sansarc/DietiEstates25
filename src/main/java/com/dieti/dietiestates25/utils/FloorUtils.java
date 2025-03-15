package com.dieti.dietiestates25.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorUtils {
    public static List<String> generateFloorsList() {
        List<String> floors = new ArrayList<>(getNumberCollectionToString(-5, -1));
        floors.add("ground floor");
        floors.addAll(getNumberCollectionToString(1, 60));
        return floors;
    }

    private static List<String> getNumberCollectionToString(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }

    public static int parseFloorToInt(String floor) {
        if (floor.equals("ground floor"))
            return 0;
        return Integer.parseInt(floor);
    }
}