package wholemusic.web.controller;

import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Album;
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/album")
@SuppressWarnings("unused")
public class AlbumController extends ControllerWithSession {
    @GetMapping("/{providerName}/{albumId}")
    public String detail(@PathVariable("providerName") String providerName, @PathVariable("albumId") String albumId,
                         HttpServletRequest request, ModelMap map) throws IOException, TemplateModelException {
        addUserInfo(map, request);
        MusicProvider provider = MusicProvider.valueOf(providerName);
        if (provider != null) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null) {
                Album album = api.getAlbumInfoByIdSync(albumId, true);
                map.addAttribute("album", album);
            }
        }
        CommonUtils.insertStatics(map);
        return "album/detail";
    }
}
