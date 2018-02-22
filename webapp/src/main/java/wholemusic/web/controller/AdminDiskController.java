package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/14.
 */

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.util.TextUtils;
import wholemusic.web.model.domain.User;
import wholemusic.web.util.FileUtils;
import wholemusic.web.util.WeiboAccountHelper;
import wholemusic.web.util.ZipUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    @GetMapping({"/", "/**"})
    public Object index(HttpServletRequest request, ModelMap map) throws IOException {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            String relativePath = getRelativePathFromRequest(request);
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
                    MediaType type = guessMimeType(target);
                    headers.setContentType(type);
                    headers.setContentDispositionFormData("attachment", target.getName(), StandardCharsets.UTF_8);
                    FileSystemResource fileSystemResource = new FileSystemResource(target);
                    return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
                }
            }
        } else {
            return "redirect:/";
        }
    }

    private static MediaType guessMimeType(File target) throws IOException {
        String mime = Files.probeContentType(target.toPath());
        if (mime == null) {
            mime = URLConnection.guessContentTypeFromName(target.getAbsolutePath());
        }
        if (mime != null) {
            return MediaType.valueOf(mime);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private static String getRelativePathFromRequest(HttpServletRequest request) throws MalformedURLException,
            UnsupportedEncodingException {
        URL url = new URL(request.getRequestURL().toString());
        return URLDecoder.decode(
                url.getPath().replaceFirst(PATH_PREFIX + "/", ""), StandardCharsets.UTF_8.name());
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
            String path = relativeParentDir + name;
            if (isDir) {
                path += "/";
            }
            return path;
        }
    }

    private File getFile(String relativePath) {
        File downloadDir = FileUtils.getDownloadDir(false);
        File target = new File(downloadDir, relativePath);
        return target;
    }

    @GetMapping("/zip/**")
    public String prepareZip(HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            String relativePath = getRelativePathFromRequest(request);
            relativePath = relativePath.replaceFirst("zip/", "");
            final File source = new File(FileUtils.getDownloadDir(true), relativePath);
            if (source.exists()) {
                File zip = ZipUtil.zip(source.getAbsolutePath());
                String redirectPath = PATH_PREFIX + "/" + relativePath.substring(0, relativePath.length() - 1) + ".zip";
                redirectPath = urlEncodePath(redirectPath);
                return "redirect:" + redirectPath;
            } else {
                throw new ResourceNotFoundException();
            }
        } else {
            return "redirect:/";
        }
    }

    private static String urlEncodePath(String path) throws UnsupportedEncodingException {
        String[] parts = path.split("/");
        ArrayList<String> list = new ArrayList<>();
        for (String part : parts) {
            list.add(URLEncoder.encode(part, StandardCharsets.UTF_8.name()));
        }
        return TextUtils.join("/", list);
    }
}
