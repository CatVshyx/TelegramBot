package Bot.telegram.handler.impl;


import Bot.telegram.ConversationState;
import Bot.telegram.KeyboardHelper;
import Bot.telegram.TelegramBot;
import Bot.telegram.handler.UserRequestHandler;
import Bot.telegram.model.UserRequest;

public class CodeEnteredHandler extends UserRequestHandler {

    private String appCode = "1234567890";
    public CodeEnteredHandler(TelegramBot telegramConfig, KeyboardHelper keyboardHelper) {
        super(telegramConfig, keyboardHelper);
    }
    @Override
    public void handle(UserRequest userRequest) {
        String code = userRequest.getText();
        boolean isApplied = code.equals(appCode);


        if(isApplied){
            userRequest.getUserSession().setState(ConversationState.CODE_APPLIED);
            userRequest.setText("/workMenu");
            telegramConfig.dispatch(userRequest);
        }else {
            telegramConfig.sendMessage(userRequest.getUserSession().getChatId(), "Please enter your code again");
        }
    }
    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return userRequest.getUserSession().getState().equals(ConversationState.WAITING_FOR_CODE);
    }
    @Override
    public boolean isGlobal() {
        return false;
    }
}
