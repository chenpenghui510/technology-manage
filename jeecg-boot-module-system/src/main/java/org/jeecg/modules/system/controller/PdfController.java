package org.jeecg.modules.system.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
* @Description: 业务列表
* @Author: jeecg-boot
* @Date:   2019-07-02
* @Version: V1.0
*/
@Slf4j
@Api(tags="业务列表")
@RestController
@RequestMapping("/system/pdf")
public class PdfController {
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @GetMapping(value = "/downloadPdf")
    public void download(HttpServletRequest req, HttpServletResponse response) throws Exception {

        // ISO-8859-1 ==> UTF-8 进行编码转换
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String downloadFilePath = req.getParameter("id");
            downloadFilePath=uploadpath+'/'+downloadFilePath;
            File file = new File(downloadFilePath);
            if (file.exists()) {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            }
        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
