package com.eshop.client.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    public static List<Long> reverseArrayFromString(String input) {
        // Split the string by commas
        List<Long> numbers = Arrays.asList(input.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());

        // Reverse the list
        Collections.reverse(numbers);

        // Join the list back into a string with commas
        return numbers;
    }
}
