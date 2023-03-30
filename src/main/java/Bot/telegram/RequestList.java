package Bot.telegram;

import Bot.MainClass;
import Bot.driveAPI.DriveService;
import Bot.telegram.model.ClientHelp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestList {
    private List<ClientHelp> list;
    private int queue = 0;
    private DriveService driveService = MainClass.getDriveService();
    public RequestList() throws IOException {
        readData();
    }
    private List<ClientHelp> parseMessage(String[] message){
        String title, description, email;
        List<ClientHelp> list1 = new ArrayList<>();
        for (int i = 1; i < message.length; i++){
            String sub = message[i];
            email = sub.substring(sub.indexOf("Email:")+6,sub.indexOf('|'));
            title = sub.substring(sub.indexOf("Title:")+6);
            title = title.substring(0,title.indexOf('|'));
            description = sub.substring(sub.indexOf("Message:")+8);

            list1.add(new ClientHelp(title,email,description));
        }
        return list1;
    }
    private String deparseList(List<ClientHelp> list){
        StringBuilder builder = new StringBuilder();

        list.forEach(clientHelp -> {
            builder.append('\n');
            builder.append("Email:%s".formatted(clientHelp.email()));
            builder.append("|Title:%s".formatted(clientHelp.title()));
            builder.append("|Message:%s".formatted(clientHelp.description()));
        });
        return builder.toString();
    }
    private ClientHelp chosenClient;
    private void readData() throws IOException {
        InputStream is = driveService.downloadFileAsInputStream(DriveService.HELP_ID).getKey();
        String response = DriveService.readStreamAsString(is);
        list = parseMessage(response.split("\n"));
    }
    public ClientHelp nextHelp(){
        chosenClient = list.get(queue%list.size());
        queue++;
        return chosenClient;
    }
    public boolean deleteRequest(ClientHelp help) {
       try{
           list.remove(help);
           String str = deparseList(list);
           ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
           driveService.updateFile(DriveService.HELP_ID,is);
           if (list.size() < 5){
               readData();
           }
       }catch (IOException e) {}
        return true;
    }

    public ClientHelp getChosenClient() {
        return chosenClient;
    }
}
