package housy.pass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class FileIO {

    private static File home = new File(System.getProperty("user.home") + "\\hPass");
    private static File users = home.toPath().resolve("users").toFile();

    static {
        if (!home.exists()) {
            home.mkdirs();
        }

        if (!users.exists()) {
            users.mkdirs();
        }
    }

    public static final int HOME_PATH = 0;
    public static final int USER_PATH = 1;

    public static File resolve(int mode, String path) {
        return switch (mode) {
            case HOME_PATH ->
                home.toPath().resolve(path).toFile();
            case USER_PATH ->
                users.toPath().resolve(path).toFile();
            default -> {
                throw new IllegalArgumentException("Invalid mode!");
            }
        };
    }

    public static boolean checkUserExists(String name) {
        return resolve(USER_PATH, name + ".nsf").exists();
    }

    public static boolean writeUser(User user) {
        try {
            File userFile = resolve(USER_PATH, user.username + ".nsf");

            if (!userFile.exists()) {
                userFile.createNewFile();
            }

            FileOutputStream fout = new FileOutputStream(userFile);

            String result = user.username + "\n"
                    + Base64.getEncoder().encodeToString(user.hash) + "\n"
                    + Base64.getEncoder().encodeToString(user.salt) + "\n"
                    + Base64.getEncoder().encodeToString(user.iv) + "\n"
                    + Base64.getEncoder().encodeToString(user.key.getEncoded()) + "\n"
                    + Long.toHexString(user.autoLogoutTime) + "\n";

            String separator = String.valueOf((char) 31);

            for (Entry entry : user.entries) {
                result += entry.appName + separator + entry.password + "\n";
            }

            fout.write(result.getBytes());
            fout.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static User readUser(String username) {
        try {
            File userFile = resolve(USER_PATH, username + ".nsf");
            String[] data = Files.readString(userFile.toPath()).split("\n");
            User user = new User();
            user.username = data[0];
            user.hash = Base64.getDecoder().decode(data[1]);
            user.salt = Base64.getDecoder().decode(data[2]);
            user.iv = Base64.getDecoder().decode(data[3]);
            user.key = new SecretKeySpec(Base64.getDecoder().decode(data[4]), "AES");
            user.autoLogoutTime = Long.parseLong(data[5], 16);
            user.entries = new ArrayList<>();

            for (int i = 6; i < data.length; i++) {
                Entry entry = new Entry();
                String[] entryParts = data[i].split(String.valueOf((char) 31));
                entry.appName = entryParts[0];
                entry.password = entryParts[1];
                user.entries.add(entry);
            }

            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
