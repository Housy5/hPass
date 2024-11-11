package housy.pass;

import housy.pass.ui.*;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;
import housy.pass.encrypt.EncryptionUtils;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

public class Application {

    private MainFrame frame;
    private User currentUser;

    private Application() {
        initLaf();
        frame = new MainFrame();
        frame.showPage(new LoginPage());
        frame.setVisible(true);
    }

    private void initLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    public MainFrame getMainFrame() {
        return frame;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void signUp(String username, String password) {
        if (FileIO.checkUserExists(username)) {
            JOptionPane.showMessageDialog(frame, "This username is already in use!");
            return;
        }

        User user = new User();
        user.entries = new ArrayList<>();
        user.username = username;
        user.salt = EncryptionUtils.generateBytes(16);
        user.iv = EncryptionUtils.generateBytes(16);
        user.hash = EncryptionUtils.hash(password.getBytes(), user.salt);
        user.key = EncryptionUtils.generateSecretKey();
        user.autoLogoutTime = 5;

        FileIO.writeUser(user);
        currentUser = user;
        showMainPage();
    }

    public void login(String username, String password) {
        if (!FileIO.checkUserExists(username)) {
            JOptionPane.showMessageDialog(frame, "This user doesn't exist!");
            return;
        }

        User user = FileIO.readUser(username);
        byte[] testHash = EncryptionUtils.hash(password.getBytes(), user.salt);
        if (!Arrays.equals(testHash, user.hash)) {
            JOptionPane.showMessageDialog(frame, "Incorrect password!");
            return;
        }

        currentUser = user;
        showMainPage();
    }
    
    private void showMainPage() {
        Countdown countdown = new Countdown(currentUser.autoLogoutTime, () -> {logout();});
        MainPage mp = new MainPage(countdown);
        frame.showPage(mp);
        countdown.start();
    }

    public void changeAutoLogoutTiming(int newTime) {
        currentUser.autoLogoutTime = newTime;
        save();
        EventQueue.invokeLater(() -> showMainPage());
    }
    
    public void changePassword(String oldPass, String newPass) {
        byte[] testHash = EncryptionUtils.hash(oldPass.getBytes(), currentUser.salt);
        if (!Arrays.equals(testHash, currentUser.hash)) {
            JOptionPane.showMessageDialog(frame, "Incorrect password!");
            return;
        }
        
        currentUser.hash = EncryptionUtils.hash(newPass.getBytes(), currentUser.salt);
        save();
    }
    
    public void save() {
        FileIO.writeUser(currentUser);
    }

    public void logout() {
        currentUser = null;
        frame.showPage(new LoginPage());
    }

    public static Application instance;

    public static void main(String[] args) {
        instance = new Application();
    }
}
