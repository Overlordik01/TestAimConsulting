package by.aim.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//src/main/resources/input1.csv;src/main/resources/input2.csv
public class CSVReader extends Thread {

    private final String URL;
    private static List<String> dataFormatted = Collections.synchronizedList(new LinkedList<>());

    public CSVReader(final String URL) {
        this.URL = URL;
    }

    private static class Handler {

        private static final Object lock = new Object();

        private static List<String> headers = Collections.synchronizedList(new LinkedList<>());

        public void findHeaders(final String line) {
            headers.addAll(Arrays.asList(line.split(";")));
            headers = headers.stream().distinct().map((a) -> a + ":\n").collect(Collectors.toList());
        }

        public List<String> toHandleData(final String[] line) {
            String header;
            for (int i = 0; i < line[0].split(";").length; i++) {
                header = line[0].split(";")[i];
                int elementI = findElement(header);
                for (int j = 1; j < line.length; j++) {
                    synchronized (lock) {
                        String lineHeader = line[j].split(";")[i];
                        if (!headers.get(elementI).contains(lineHeader)) {
                            headers.set(elementI, headers.get(elementI) + lineHeader + ";");
                        }
                    }
                }
            }
            return headers;
        }

        private int findElement(final String header) {
            return IntStream.range(0, headers.size())
                    .filter(il -> Objects.nonNull(headers.get(il)))
                    .filter(il -> headers.get(il).contains(header))
                    .findFirst()
                    .orElse(-1);
        }
    }

    @Override
    public void run() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(URL))) {
            Handler handler = new Handler();
            String[] data = read(reader);
            handler.findHeaders(data[0]);
            dataFormatted = Collections.synchronizedList(handler.toHandleData(data));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Critical error");
        }
    }

    private String[] read(final BufferedReader reader) throws IOException {
        String line;

        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        String str = stringBuilder.toString();
        return str.split("\n");
    }

    public static void print() {
        dataFormatted.forEach(System.out::println);
    }
}
