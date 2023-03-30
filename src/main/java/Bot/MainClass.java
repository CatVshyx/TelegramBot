package Bot;

import Bot.driveAPI.DriveService;
import Bot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class MainClass {
    private static  EmailService email = new EmailService();
    private static DriveService service = new DriveService();
    public static void main(String[] args) throws TelegramApiException, IOException {
        TelegramBot bot = new TelegramBot();
    }
    public static EmailService getEmail(){
        return email;
    }
    public static DriveService getDriveService(){
        return service;
    }
}
