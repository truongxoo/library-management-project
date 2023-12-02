package study.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import study.demo.enums.Constants;

@Component
public class FileStorageUtils {

    public String storeFileCSV(MultipartFile file) {
        Path path = Paths.get(Constants.LOCATION_FILE);
        String fileName = file.getOriginalFilename();
        // checking file type
        if (fileName.endsWith("." + Constants.FILE_TYPE)) {
            // removing the suffix .csv
            fileName = fileName.substring(0, fileName.indexOf("."));
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                return "Error occured while storing the file";
            }

        } else {
            return "Invalid fileType";
        }
    }
}