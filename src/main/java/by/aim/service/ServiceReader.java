package by.aim.service;

public class ServiceReader {

    public void start(final String... arg) throws InterruptedException {
        for (final String url : arg) {
            CSVReader csvReader = new CSVReader(url);
            csvReader.start();
        }
        Thread.currentThread().join(1000);
        CSVReader.print();
    }

}
