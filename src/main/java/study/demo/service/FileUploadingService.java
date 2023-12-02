package study.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.enums.Constants;
import study.demo.service.dto.response.MessageResponseDto;
import study.demo.service.exception.DataInvalidException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadingService {
    private final MessageSource messageSource;

    // upload file csv
    public MessageResponseDto fileUploading(MultipartFile file) {

        Path path = Paths.get(Constants.LOCATION_FILE);
        String fileName = file.getOriginalFilename();

        // checking file type
        if (!fileName.endsWith("." + Constants.FILE_TYPE)) {
            log.error("File type is invalid");
            throw new DataInvalidException(messageSource.getMessage("filetype.invalid", null, Locale.getDefault()),
                    "filetype.invalid");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return MessageResponseDto.builder()
                    .message(messageSource.getMessage("upload.success",
                            new Object[] { fileName, Constants.LOCATION_FILE.toString() }, Locale.getDefault()))
                    .messageCode("upload.success").statusCode(HttpStatus.CREATED.value()).build();
        } catch (IOException e) {
            log.error("Can not upload file with cause: {}", e.getMessage());
            throw new DataInvalidException(messageSource.getMessage("upload.error", null, Locale.getDefault()),
                    "upload.error");
        }

    }

    // uppload image
    public MessageResponseDto imageUploading(MultipartFile file) {

        Path path = Paths.get(Constants.LOCATION_IMAGE);
        String fileName = file.getOriginalFilename();

        // checking file type
        if (!fileName.endsWith("." + Constants.IMAGE_TYPE.substring(0, Constants.IMAGE_TYPE.indexOf(",")))
                || !fileName.endsWith("." + Constants.IMAGE_TYPE.substring(Constants.IMAGE_TYPE.indexOf(",") + 1))) {

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                return MessageResponseDto.builder()
                        .message(messageSource.getMessage("upload.image.successfully",
                                new Object[] { fileName, Constants.LOCATION_IMAGE.toString() }, Locale.getDefault()))
                        .messageCode("upload.image.successfully").statusCode(HttpStatus.CREATED.value()).build();
            } catch (IOException e) {
                log.error("Can not upload image with cause: {}", e.getMessage());
                throw new DataInvalidException(
                        messageSource.getMessage("upload.image.error", null, Locale.getDefault()),
                        "upload.image.error");
            }
        } else {
            log.error("Image type is invalid");
            throw new DataInvalidException(messageSource.getMessage("imagetype.invalid", null, Locale.getDefault()),
                    "imagetype.invalid");
        }
    }

    // delete image
    public MessageResponseDto deleteImage(MultipartFile file) {

        Path path = Paths.get(Constants.LOCATION_IMAGE);
        String fileName = file.getOriginalFilename();
        Path filePath = path.resolve(fileName);
        try {
            Files.delete(filePath);
            return MessageResponseDto.builder()
                    .message(messageSource.getMessage("delete.image.successfully", null, Locale.getDefault()))
                    .messageCode("delete.image.successfully").statusCode(HttpStatus.NO_CONTENT.value()).build();
        } catch (IOException e) {
            log.error("Can not delete image with cause: {}", e.getMessage());
            throw new DataInvalidException(messageSource.getMessage("delete.image.fail", null, Locale.getDefault()),
                    "delete.image.fail");
        }
    }

    // preview serivce
    public Resource previewImage(String filename) {
        try {
            Path file = Paths.get(Constants.LOCATION_IMAGE).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Can not read file image");
                throw new DataInvalidException(messageSource.getMessage("cannot.read", null, Locale.getDefault()),
                        "cannot.read");
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new DataInvalidException(messageSource.getMessage("cannot.read", null, Locale.getDefault()),
                    "cannot.read");
        }
    }

}
