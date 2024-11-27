package com.eshop.app.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StringUtils {
    public static List<UUID> reverseArrayFromString(String input) {
        // Split the string by commas
        List<UUID> numbers = Arrays.asList(input.split(",")).stream().map(UUID::fromString).collect(Collectors.toList());

        // Reverse the list
        Collections.reverse(numbers);

        // Join the list back into a string with commas
        return numbers;
    }
}
