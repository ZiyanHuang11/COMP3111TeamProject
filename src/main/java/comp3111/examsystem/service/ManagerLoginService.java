package comp3111.examsystem.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ManagerLoginService {
    private String managerFilePath;
    //
    public ManagerLoginService(String managerFilePath) {
        this.managerFilePath = managerFilePath;
        File managerFile = new File(managerFilePath);
        if (managerFile.exists()) {
            System.out.println("Manager file found at: " + managerFile.getAbsolutePath());
        } else {
            System.out.println("Manager file not found!");
        }
    }

    public boolean validate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(managerFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}