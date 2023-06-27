package com.netpan.util;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ExportUtil {
    /**
     * 文件以流的形式读取
     *
     * @param in 字符输入流
     * @param fileName 文件名字
     * @return 返回结果
     */
    public static ResponseEntity<InputStreamResource> downloadFile(InputStream in, String fileName) {

        try {
            byte[] testBytes = new byte[in.available()];
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", (fileName)));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Language", "UTF-8");
            //最终这句，让文件内容以流的形式输出
            return ResponseEntity.ok().headers(headers).contentLength(testBytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(in));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
