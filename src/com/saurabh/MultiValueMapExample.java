package com.saurabh;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

public class MultiValueMapExample {
    public static void main(String[] args) {
        MultiValueMap<String, String> multiMap = new LinkedMultiValueMap<>();

        // Adding values to the multi-map
        multiMap.add("key1", "value1");
        multiMap.add("key1", "value2");
        multiMap.add("key2", "value3");
        multiMap.add("key3", "value4");
        multiMap.add("key3", "value5");

        // Getting values for a key
        System.out.println("Values for key1: " + multiMap.get("key1")); // Output: [value1, value2]

        // Iterating over the multi-map
        multiMap.forEach((key, values) -> {
            System.out.println("Key: " + key + ", Values: " + values);
        });
    }
}
