package com.finago.interview.task.utils;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.ReceiverHolder;
import com.finago.interview.task.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {

    public static final int ARRAY_SIZE = 1024;
    public static final int MASK_SET = 0xff;
    public static final int MASK_INCLUDE = 0x100;
    public static final int RADIX = 16;

    private static AppProperties appProperties;

    public static void setAppProperties(AppProperties appProperties) {
        FileUtils.appProperties = appProperties;
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("No MD5 algorithm found in the system");
            System.exit(-1);
        }
        return digest;
    }

    private static MessageDigest updateMessageDigest(MessageDigest digest, FileInputStream fis) {
        byte[] byteArray = new byte[ARRAY_SIZE];
        int byteCount = 0;
        while (true)
        {
            try {
                if (!((byteCount = fis.read(byteArray)) != -1)){
                    break;
                }
            } catch (IOException e) {
                log.error("FileInputStream IOException: "+e.getMessage());
            }
            digest.update(byteArray, 0, byteCount);
        }
        return digest;
    }

    private static void moveFile(Path sourcePath, Path destinationPath){
        try {
            destinationPath = Path.of(destinationPath.toString(), sourcePath.getFileName().toString());
            if(Files.exists(sourcePath)){
                Files.move( sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("MOVE File: {}; moved to: {}",sourcePath.getFileName(), destinationPath);
            }
        } catch (IOException e) {
            log.info("moveFile IOException: "+e);
        }
    }

    private static void copyXmlFile(Path sourcePath, Path destinationPath){
        try {
            destinationPath = Path.of(destinationPath.toString(), sourcePath.getFileName().toString());
            if(!Files.exists(destinationPath)){
                Files.copy( sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("COPY File: {}; copied to: {}",sourcePath.getFileName(), destinationPath);
            }
        } catch (IOException e) {
            log.error("copyXmlFile IOException: "+e);
        }
    }

    private static void deletePdfFiles(List<Receiver> receivers){
        receivers.forEach(receiver -> {
            try {
                Path path = Paths.get(Path.of(getFileAbsolutePath(appProperties.getDataIn()), receiver.getFile()).toString());
                if(Files.exists(path) && Files.isRegularFile(path)){
                    Files.deleteIfExists(path);
                    log.info("File: {}; deleted if exist from {}",receiver.getFile(), getFileAbsolutePath(appProperties.getDataIn()));
                }
            } catch (IOException e) {
                log.warn("deletePdfFiles File doesn't exist: "+receiver.getFile());
            }
        });
    }

    private static String getSubDirName(String input){
        String subDir = "";
        try {
            int receiver_id =  Integer.parseInt(input);
            subDir = String.valueOf(receiver_id%100);
        } catch (NumberFormatException e){
            log.error("getSubDirName NumberFormatException: "+e.getMessage());
        }
        return subDir;
    }

    private static StringBuilder buildCheckSum(MessageDigest digest) {
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer
                    .toString((bytes[i] & MASK_SET) + MASK_INCLUDE, RADIX)
                    .substring(1));
        }
        return sb;
    }

    private static Path createSubDirectory(String dataPath, String receiver_id){
        Path cratedDirPath;
        String dirPath = getFileAbsolutePath(dataPath);
        Path subDirPath = Path.of(dirPath, getSubDirName(receiver_id));
        if(!Files.exists(subDirPath)){
            try {
                cratedDirPath = Files.createDirectories(subDirPath);
                cratedDirPath = Files.createDirectories(Path.of(cratedDirPath.toString(), receiver_id));
            } catch (IOException e) {
                log.error("createSubDirectory IOException: "+e.getMessage());
                cratedDirPath = Path.of(dirPath, getSubDirName(receiver_id));
                cratedDirPath = Path.of(cratedDirPath.toString(), receiver_id);
            }
        }else{
            cratedDirPath = subDirPath;
            try {
                cratedDirPath = Files.createDirectories(Path.of(cratedDirPath.toString(), receiver_id));
            } catch (IOException e) {
                log.error("createSubDirectory IOException: "+e.getMessage());
                cratedDirPath = Path.of(dirPath, getSubDirName(receiver_id));
                cratedDirPath = Path.of(cratedDirPath.toString(), receiver_id);
            }
        }
        return cratedDirPath;
    }

    private static void processReceiver(Receiver receiver, Path xmlPath) {
        File file = new File(getFileAbsolutePath(appProperties.getDataIn())+receiver.getFile());
        if(isValidFile(file, receiver.getFile_md5())){
            Path subDirPath = createSubDirectory(appProperties.getDataOut(), receiver.getReceiver_id());
            moveFile(Path.of(getFileAbsolutePath(appProperties.getDataIn()), receiver.getFile()), subDirPath);
            copyXmlFile(xmlPath, subDirPath);
        }else{
            Path subDirPath = createSubDirectory(appProperties.getDataError(), receiver.getReceiver_id());
            moveFile(Path.of(getFileAbsolutePath(appProperties.getDataIn()), receiver.getFile()), subDirPath);
            copyXmlFile(xmlPath, subDirPath);
        }
    }

    private static FileInputStream getFileInputStream(String fileName) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            log.error("FileInputStream FileNotFoundException: "+ fileName);
            return null;
        }
        return fis;
    }

    private static void closeFileInputStream(FileInputStream fis) {
        try {
            fis.close();
        } catch (IOException e) {
            log.error("closeFileInputStream IOException:  "+e.getMessage());
        }
    }

    public static String getFileAbsolutePath(String path){
        try {
            return new File(".").getCanonicalPath()+path;
        } catch (FileNotFoundException e) {
            log.error("Path not found: "+e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("IOException: "+e.getMessage());
            return null;
        }
    }

    public static String getChecksum(File fileName) {
        MessageDigest digest = FileUtils.getMessageDigest();
        FileInputStream fis = FileUtils.getFileInputStream(fileName.getAbsolutePath());
        if (fis == null){
            log.warn("File not found: "+fileName);
            return "";
        }
        digest = FileUtils.updateMessageDigest(digest, fis);
        StringBuilder sb = FileUtils.buildCheckSum(digest);
        FileUtils.closeFileInputStream(fis);
        return sb.toString();
    }

    public static void readFilesForDirectory()  {
        try (Stream<Path> paths = Files.walk(Paths.get(getFileAbsolutePath(appProperties.getDataIn())))) {
            paths.filter(Files::isRegularFile)
                    .forEach( item -> {
                        if(item.getFileName().toString().contains(".xml")){
                            FileUtils.readXml(item);
                        }
                    });
        }catch (Exception e){
            log.error("readFilesForDirectory exception: "+e.getMessage());
            String[] split = e.getMessage().split(": ");
            if(!split[1].isEmpty()){
                moveFile(Path.of(split[1]), Paths.get(getFileAbsolutePath(appProperties.getDataError())));
            }

        }
    }

    public static boolean isValidFile(File file, String file_md5){
        boolean isValid = false;
        if(file.exists()){
            if( file_md5.equals(getChecksum(file))){
                log.debug("file checksums are equal {}",file_md5);
                isValid = true;
            }else{
                log.debug("file checksums are not equal {}",file_md5);
            }
        }else{
            log.debug("File is not exist {}",file.getAbsolutePath());
        }
        return  isValid;
    }

    public static void readXml(Path xmlPath)  {
        try {
            JAXBContext context = JAXBContext.newInstance(ReceiverHolder.class);
            Unmarshaller un = context.createUnmarshaller();
            ReceiverHolder receiverHolder = (ReceiverHolder) un.unmarshal(xmlPath.toFile());
            receiverHolder.getReceiver().forEach(item -> {
                processReceiver(item, xmlPath);
            });
            moveFile(xmlPath, Path.of(getFileAbsolutePath(appProperties.getDataArchive())));
            deletePdfFiles(receiverHolder.getReceiver());
        } catch (JAXBException e) {
            log.error("readXml JAXBException: "+e);
            moveFile(xmlPath, Paths.get(getFileAbsolutePath(appProperties.getDataError())));
        }
    }
}
