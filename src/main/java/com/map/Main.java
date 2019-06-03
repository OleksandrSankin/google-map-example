package com.map;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please specify Google API key");
            return;
        }

        String tempFile = pathToTempFile();

        Scanner scanner = new Scanner(System.in);
        System.out.print("origin: ");
        String origin = scanner.nextLine();
        System.out.print("destination: ");
        String destination = scanner.nextLine();

        String rawHtml = readHtmlFileFromResources();
        String preparedHtml = prepareHtmlFile(rawHtml, args[0], origin, destination);

        saveHtmlToTempFile(tempFile, preparedHtml);
        openTempFileInBrowser(tempFile);
    }

    private static String pathToTempFile() {
        return System.getProperty("java.io.tmpdir") + "/" +
               System.currentTimeMillis() + ".html";
    }

    private static String readHtmlFileFromResources() throws URISyntaxException, IOException {
        URI uri = Main.class.getClassLoader().getResource("map.html").toURI();
        Path path = Paths.get(uri);
        return new String(Files.readAllBytes(path));
    }

    private static String prepareHtmlFile(
            String html,
            String key,
            String origin,
            String destination) {
        return html
                .replace("{key}", key)
                .replace("{origin}", origin)
                .replace("{destination}", destination);
    }

    private static void saveHtmlToTempFile(String tempFile, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(data);
        writer.close();
    }

    private static void openTempFileInBrowser(String tempFile) throws IOException {
        File htmlFile = new File(tempFile);
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
