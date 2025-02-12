package com.dieti.dietiestates25.views.upload.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorUtils {
    public static List<String> generateFloorsList() {
        List<String> floors = new ArrayList<>();
        floors.addAll(getNumberCollectionToString(-5, -1));
        floors.addAll(Arrays.asList("basement", "ground floor", "raised floor"));
        floors.addAll(getNumberCollectionToString(1, 60));
        return floors;
    }

    private static List<String> getNumberCollectionToString(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }
}