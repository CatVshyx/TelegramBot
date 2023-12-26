package Bot.telegram;

import Bot.EmailService;
import Bot.MainClass;
import Bot.telegram.handler.UserRequestHandler;
import Bot.telegram.handler.impl.*;
import Bot.telegram.model.UserRequest;
import Bot.telegram.model.UserSession;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    private TelegramDispatcher dispatcher;
    private KeyboardHelper keyboardHelper = new KeyboardHelper();
    private final UserSessionService userSessionService = new UserSessionService();
    private final RequestList requestList;
    public TelegramBot() throws TelegramApiException, IOException {
        System.out.println("Bot initializing ");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
        List<UserRequestHandler> handlerList = new ArrayList<>();
        handlerList.add(new StartHelpCommandHandler(this,keyboardHelper));
        handlerList.add(new EnterUUIDHandler(this,keyboardHelper,userSessionService));
        handlerList.add(new CodeEnteredHandler(this,keyboardHelper));
        handlerList.add(new WorkMenuHandler(this,keyboardHelper));
        handlerList.add(new SendEmailHandler(this,keyboardHelper));
        dispatcher = new TelegramDispatcher(handlerList);
        requestList = new RequestList();
    }
    @Override
    public String getBotUsername() {
        return "Finance2003Bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();
            Long userId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            UserSession session = userSessionService.getSession(userId);
            System.out.println("[{%s}, {%s}] : {%s}".formatted( userId, userFirstName, textFromUser));
            UserRequest request = new UserRequest(userFirstName,textFromUser,session);

            if (!dispatcher.dispatch(request)){
                request.setText("/start");
                dispatch(request);
            }
        } else {
           System.out.println("Unexpected update from user");
        }
    }
    public void sendMessage(Long chatId, String message){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(message)
                    .build();
            this.sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public RequestList getRequestList(){
        return requestList;
    }
    public void sendMail(String to, String message){
        EmailService emailService = MainClass.getEmail();
        if (emailService == null) return;
        emailService.sendMessage(to,"Consult on FinFlow",message);
    }
    public boolean dispatch(UserRequest request){
        return dispatcher.dispatch(request);
    }
    public void sendMessage(Long chatId, String message, ReplyKeyboardMarkup markup){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .replyMarkup(markup)
                    .text(message)
                    .build();
            this.sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
