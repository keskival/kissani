package fi.neter.kissani;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import fi.neter.kissani.dao.Cat;
import fi.neter.kissani.dao.CatJdoDao;
import fi.neter.kissani.fb.FBService;
import fi.neter.kissani.fb.Photo;

@Controller
public class KissaniController {
    private static final Logger logger = LoggerFactory.getLogger(KissaniController.class);

    @Autowired
    private CatJdoDao catDao;
    
    @Autowired
    private FBService fbService;

    @RequestMapping("/addCatPhotos.task")
	@ResponseStatus(HttpStatus.OK)
    public void addCatPhotos(
            @RequestParam Long catId,
            @RequestParam String accessToken) throws IOException {
        Cat cat = catDao.getCat(catId);
        cat.setPhotosAsSet(fbService.getPhotosOfCat(cat, accessToken));
        catDao.persistCat(cat);
    }
    

    @RequestMapping("/tagScanTask.task")
	@ResponseStatus(HttpStatus.OK)
    public void tagScanTask(HttpServletRequest req, @RequestParam Long catId,
    		@RequestParam String accessToken) throws IOException {
        Cat cat = catDao.getCat(catId);
        cat.setOwnersAsSet(catDao.getPersons(catId));
        Set<Photo> photos = fbService.getPhotosOfCat(cat, accessToken);

        cat.setPhotosAsSet(photos);

        catDao.persistCat(cat);
    }

    public void setCatDao(CatJdoDao catDao) {
        this.catDao = catDao;
    }

    public void setFbService(FBService fbService) {
        this.fbService = fbService;
    }
}
