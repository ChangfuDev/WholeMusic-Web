package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/14.
 */

import com.sun.media.jfxmediaimpl.MediaUtils;
import org.apache.http.client.utils.URIUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;
import wholemusic.web.model.domain.User;
import wholemusic.web.util.FileUtils;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Controller
@RequestMapping(AdminDiskController.PATH_PREFIX)
@SuppressWarnings("unused")
public class AdminDiskController extends ControllerWithSession {

    public static final String PATH_PREFIX = "/admin/disk";

    @GetMapping
    public String redirect() {
        return "redirect:" + PATH_PREFIX + "/";
    }

    @GetMapping({"", "/**"})
    public Object index(HttpServletRequest request, ModelMap map) throws MalformedURLException,
            UnsupportedEncodingException {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            URL url = new URL(request.getRequestURL().toString());
            String relativePath = URLDecoder.decode(
                    url.getPath().replaceFirst(PATH_PREFIX + "/", ""), StandardCharsets.UTF_8.name());
            File target = getFile(relativePath);
            if (!target.exists()) {
                throw new ResourceNotFoundException();
            } else {
                if (target.isDirectory()) {
                    // 文件夹
                    File[] children = target.listFiles();
                    ArrayList<RelativeFile> files = new ArrayList<>();
                    for (File child : children) {
                        files.add(new RelativeFile(relativePath, child.getName(), child.isDirectory()));
                    }
                    map.addAttribute("path", relativePath);
                    map.addAttribute("files", files);
                    return "admin/disk/index";
                } else {
                    // 文件
                    HttpHeaders headers = new HttpHeaders();
                    // TODO: dynamic file type
                    headers.setContentType(MediaType.valueOf(MediaUtils.CONTENT_TYPE_MPA));
                    FileSystemResource fileSystemResource = new FileSystemResource(target);
                    return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
                }
            }
        } else {
            return "redirect:/";
        }
    }

    private static class RelativeFile {
        public String name;
        public String relativeParentDir;
        public boolean isDir;

        public RelativeFile(String relativeParentDir, String name, boolean isDir) {
            this.relativeParentDir = relativeParentDir;
            this.name = name;
            this.isDir = isDir;
        }

        public String getRelativePath() {
            return relativeParentDir + "/" + name;
        }
    }

    private File getFile(String relativePath) {
        File downloadDir = FileUtils.getDownloadDir(false);
        File target = new File(downloadDir, relativePath);
        return target;
    }
}
