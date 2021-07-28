package edu.alenkin.aws_deployer.rest.v1;

import edu.alenkin.aws_deployer.ValidationException;
import edu.alenkin.aws_deployer.deploymanager.DeployManager;
import edu.alenkin.aws_deployer.upload_utils.UploadFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@RestController
@RequestMapping(AppController.REST_URL)
@Slf4j
public class AppController {
    public final static String REST_URL = "rest/v1/apps/";

    private final DeployManager manager;

    @Autowired
    public AppController(DeployManager manager) {
        this.manager = manager;
    }

    @PostMapping(consumes = {"application/zip"})
    public ResponseEntity pushApp(@RequestParam(name = "zip") MultipartFile archive) {
        log.debug("POST rest/v1/apps - trying to upload project to S3");
        UploadFileResponse response = null;
        try {
            response = manager.upload(archive);
            log.debug("Succesfuly uploaded {}", response.getFileName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
