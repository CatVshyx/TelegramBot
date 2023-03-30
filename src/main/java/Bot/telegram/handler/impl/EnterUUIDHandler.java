package Bot.telegram.handler.impl;


import Bot.telegram.ConversationState;
import Bot.telegram.KeyboardHelper;
import Bot.telegram.TelegramBot;
import Bot.telegram.UserSessionService;
import Bot.telegram.handler.UserRequestHandler;
import Bot.telegram.model.UserRequest;
import Bot.telegram.model.UserSession;

public class EnterUUIDHandler extends UserRequestHandler {
    private final String command = "Start help ❗";
    private final UserSessionService sessionService;
    public EnterUUIDHandler(TelegramBot telegramConfig, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        super(telegramConfig, keyboardHelper);
        this.sessionService = userSessionService;
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession session = userRequest.getUserSession();
        session.setState(ConversationState.WAITING_FOR_CODE);
        sessionService.saveSession(session.getChatId(),session);

        telegramConfig.sendMessage(userRequest.getUserSession().getChatId(),"Please enter your Unique code");
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getText(),command);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
