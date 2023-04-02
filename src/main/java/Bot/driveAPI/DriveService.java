package Bot.driveAPI;

import com.google.api.client.http.*;
import com.google.api.services.drive.Drive;
import java.io.*;
import java.util.*;
public class DriveService{
    private static final Drive drive = DriveRunner.getDrive();
    public static final String PHOTO_FOLDER = "1-MJtymNF3KoGw85gdDbk1C1Owj0icij-";
    public static final String INFO_ID = "1DfbPIAKPZjLq4kjgVYXKl4Xxw_BYLrel";
    public static final String HELP_ID = "1KUBaJwGAaIPT66e7yX32txtew-0cnoH1";
    public DriveService(){
        getAllFiles(null);
    }
    public boolean isDownloadable(com.google.api.services.drive.model.File f){
        System.out.println(f.getMimeType() + " MIME TYPE");
        if (f.getMimeType().contains("folder")
                || f.getMimeType().contains("shortcut")
                || f.getMimeType().contains("vnd.google-apps")){
            System.out.println("THIS IS a FOLDER or shortcut on supports google apps only");
            return false;
        }
        return true;
    }
    public Map.Entry<InputStream,String> downloadFileAsInputStream(String fileId){
        if (fileId == null){return null;}
        com.google.api.services.drive.model.File file;
        try {
            file = drive.files().get(fileId).execute();
            if (!isDownloadable(file)){ return null; }
            return new AbstractMap.SimpleEntry<>(drive.files().get(fileId).executeMediaAsInputStream(), file.getMimeType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void downloadFile(String fileId) {
       try{
           if (fileId == null){return;}
           com.google.api.services.drive.model.File file = drive.files().get(fileId).execute();
           if (file.getMimeType().contains("folder")){
               System.out.println("THIS IS FOLDER");
               return;
           }
           System.out.printf("%s (%s) - %s \n DOWNLOAD FILE -%s-\n", file.getName(), file.getId(),file.getMimeType(),file.getName());
           File f = new File(System.getProperty("user.home") + File.separator + "Desktop/" + file.getName());

           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

           byte[] bytes = outputStream.toByteArray();
           FileOutputStream fos = new FileOutputStream(f);
           fos.write(bytes);
       }catch (IOException e){
           e.printStackTrace();
       }

    }
    public List<com.google.api.services.drive.model.File> getAllFiles(String folder){
        List<com.google.api.services.drive.model.File> files;
        try {
            Drive.Files.List list = drive.files().list();
            if (folder != null){ list.setQ("'%s' in parents".formatted(PHOTO_FOLDER)); }

            files = list.setFields("nextPageToken, files(id, name, mimeType)").execute().getFiles();
            files.forEach(file -> {
                System.out.printf("%s (%s) - %s \n", file.getName(), file.getId(),file.getMimeType());
            });
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  boolean updateFile(String fileId, InputStream metadata) {
        try{
            com.google.api.services.drive.model.File fileFromDrive = drive.files().get(fileId).execute();
            com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();

            file.setMimeType(fileFromDrive.getMimeType());
//
//            FileContent mediaContent = new FileContent(fileFromDrive.getMimeType(),metadata);
            InputStreamContent fileContent = new InputStreamContent(file.getMimeType(), metadata);

            drive.files().update(fileFromDrive.getId(),null,fileContent).execute();
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public  boolean uploadFile(String filename,InputStream uploadedFile, String folderId){
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//        String fileName = uploadedFile.getName();
        String ext = filename.substring(filename.indexOf('.'));
        fileMetadata.setName(filename);
        fileMetadata.setParents(Collections.singletonList(folderId));

//        FileContent mediaContent = new FileContent(MimeTypeParser.getMimeByExtension(ext),uploadedFile);

        InputStreamContent fileContent = new InputStreamContent(MimeTypeParser.getMimeByExtension(ext), uploadedFile);
        try {
            com.google.api.services.drive.model.File created = drive.files().create(fileMetadata, fileContent).setFields("id").execute();
            System.out.println(created.getId());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public  boolean deleteFile(String fileId){
        try {
            drive.files().delete(fileId).execute();
            System.out.println( " DELETED successfully" + fileId);
            return true;
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND OR STH ELSE");
            return false;
        }
    }
    public static String readStreamAsString(InputStream is) throws IOException {
        if (is == null) return null;
        int max = 0;
        try(BufferedInputStream bis = new BufferedInputStream(is)){
            int c;
            StringBuilder builder = new StringBuilder();
            while((c=bis.read()) >= 0){
                char ch = (char) c;
                builder.append(ch);
                if (ch == '-'){
                    if (max > 20){
                        break;
                    }
                    max++;
                }
            }
            return builder.toString();
        }
    }
}
