package com.adventofcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        run();
    }

    private Scanner inputScanner;

    private void run() {
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            String input = stream.collect(Collectors.joining(""));
            inputScanner = new Scanner(input);

            Node root = readData();

            Integer metadataSum = calculateMetadataSum(root);

            System.out.println(metadataSum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer calculateMetadataSum(Node node) {
        if(node == null) {
            return 0;
        }

        if(node.getChildren().size() == 0) {
            Integer metadataSum = 0;

            for(Integer metadata : node.getMetadata()) {
                metadataSum += metadata;
            }

            return metadataSum;
        }

        Integer partialSum = 0;

        for(Integer i : node.getMetadata()) {
            if(i == 0) continue;
            i--;

            Node n = i < node.getChildren().size() ? node.getChildren().get(i) : null;
            if(n != null) {
                partialSum += calculateMetadataSum(n);
            }
        }

        return partialSum;
    }

    private Node readData() {
        Integer childNodes = inputScanner.nextInt();
        Integer metadataEntries = inputScanner.nextInt();

        Node node = new Node(childNodes, metadataEntries);

        for(int i = 0; i < childNodes; i++) {
            Node n = readData();

            node.getChildren().add(n);
        }

        for(int i = 0; i < metadataEntries; i++) {
            Integer metadata = inputScanner.nextInt();

            node.getMetadata().add(metadata);
        }

        return node;
    }
}
