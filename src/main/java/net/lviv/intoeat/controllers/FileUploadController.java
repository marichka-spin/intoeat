package net.lviv.intoeat.controllers;

import net.lviv.intoeat.utils.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@PropertySource("classpath:conf/configuration.properties")
public class FileUploadController {

    @Autowired
    private ServletContext context;

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo uploadFileHandler(@RequestParam(value = "file") MultipartFile file) {
        ResponseInfo responseInfo = new ResponseInfo();
        if (!file.isEmpty()) {
            try {
                String name = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                File dir = new File(getImagesLocation());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                responseInfo.setMessage("File " + name + " uploaded");
                return responseInfo;
            } catch (Exception e) {
                responseInfo.setStatus(ResponseInfo.Status.FAILED);
                responseInfo.
                        setMessage("Failed to upload file " + e.getMessage());
                return responseInfo;
            }
        } else {
            responseInfo.setStatus(ResponseInfo.Status.FAILED);
            responseInfo.
                    setMessage("Failed to upload file, file is empty");
            return responseInfo;
        }
    }

    private String getImagesLocation() {
        String activeProfile = "dev";
        if (environment.getActiveProfiles().length > 0) {
            activeProfile = environment.getActiveProfiles()[0];
        }
        String assestsDir = context.getRealPath("assets/pictures");
        String location = environment.getProperty(activeProfile + ".images.location", assestsDir);
        return location;
    }

}
