package comp3111.examsystem.tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * Reads a file line by line and returns the content as a list of strings.
     *
     * @param filePath The path of the file to read.
     * @return A list of strings, each representing a line in the file.
     */
    public static List<String> readFileByLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Writes a list of strings to a file, overwriting its content.
     *
     * @param filePath The path of the file to write.
     * @param lines    The content to write, with each element representing a line.
     */
    public static void writeFile(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a list of strings to a file, overwriting its content (alternative method signature).
     *
     * @param content The content to write as a single string (with line breaks).
     * @param file    The file to write.
     */
    public static void writeTxtFile(String content, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a string to a file.
     *
     * @param filePath The path of the file to append to.
     * @param content  The content to append.
     */
    public static void appendToFile(String filePath, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 读取文件
    public static List<String> readFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Deletes a file at the specified path.
     *
     * @param filePath The path of the file to delete.
     */
    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
