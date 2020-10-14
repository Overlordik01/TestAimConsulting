package by.aim;

import by.aim.service.ServiceReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        ServiceReader serviceReader = new ServiceReader();
        serviceReader.start(Objects.requireNonNull(readConsoleUrl()));
    }

    private static String[] readConsoleUrl() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Input url for start program (Input through ';')");
            return reader.readLine().split(";");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
