package Bot.telegram;


import Bot.telegram.model.UserSession;

import java.util.HashMap;
import java.util.Map;


public class UserSessionService {
    private Map<Long, UserSession> userSessionMap = new HashMap<>();
    public UserSessionService(){
        System.out.println("SS initialized");
    }
    public UserSession getSession(Long chatId){
        return userSessionMap.getOrDefault(chatId,new UserSession(chatId).setState(ConversationState.CONVERSATION_STARTED));
    }
    public void saveSession(Long id, UserSession userSession){
        userSessionMap.put(id,userSession);
    }
}
