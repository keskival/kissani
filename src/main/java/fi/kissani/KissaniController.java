package fi.kissani;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.kissani.dao.Cat;
import fi.kissani.dao.CatDao;
import fi.kissani.dao.PersonDao;
import fi.kissani.fb.FBService;
import fi.kissani.fb.Photo;
import fi.kissani.fb.Profile;
import fi.kissani.filters.FacebookUserInterceptor;

@Controller
public class KissaniController {
    private static final Logger logger = LoggerFactory.getLogger(KissaniController.class);

    @Autowired
    private CatDao catDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private FBService fbService;

    public Boolean canEditPerson(HttpServletRequest req, Long id) throws HttpException, UnsupportedEncodingException,
            IOException {
        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));
        return canEditPerson(id, profile);
    }

    public Boolean canEditPerson(Long id, Profile profile) throws HttpException, UnsupportedEncodingException,
            IOException {
        if (profile.getId().equals(id)) {
            logger.debug("Can edit person " + id + ".");
            return true;
        }
        else {
            logger.debug("Cannot edit person " + id + ".");
            return false;
        }
    }

    public Boolean canEditCat(HttpServletRequest req, Long id) throws HttpException, UnsupportedEncodingException,
            IOException {
        Cat cat = catDao.getCat(id);
        return canEditCat(req, cat);
    }

    public Boolean canEditCat(HttpServletRequest req, Cat cat) throws HttpException, UnsupportedEncodingException,
            IOException {
        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));
        return canEditCat(cat, profile);
    }

    public Boolean canEditCat(Cat cat, Profile profile) {
        List<Long> owners = cat.getOwners();
        if (owners.contains(profile.getId())) {
            logger.debug("Can edit cat " + cat.getId() + ".");
            return true;
        }
        else {
            logger.debug("Cannot edit cat " + cat.getId() + ".");
            return false;
        }
    }

    @RequestMapping("/addCat.do")
    public String addCatJsp(HttpServletRequest req) {
        return "addCat";
    }

    @RequestMapping("/editCat.do")
    public String addCatJsp(HttpServletRequest req, @RequestParam Long id) {
        Cat cat = catDao.getCat(id);
        req.setAttribute("cat", cat);

        return "editCat";
    }

    @RequestMapping("/")
    public String defaultEndpoint(HttpServletRequest req) throws HttpException, UnsupportedEncodingException,
            IOException {
        return showSelf(req);
    }

    @RequestMapping("/showSelf.do")
    public String showSelf(HttpServletRequest req) throws HttpException, UnsupportedEncodingException, IOException {
        // Retrieve the person (From FB)
        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));
        logger.debug("Fetched profile: " + profile.toString());

        req.setAttribute("profile", profile);

        // Retrieve the cats owned by the person
        List<Long> cats = personDao.getCats(profile.getId());
        logger.debug("Fetched cats: " + cats.toString());

        ArrayList<Cat> filledCats = new ArrayList<Cat>();
        for (Long catId : cats) {
            Cat cat = catDao.getCat(catId);
            filledCats.add(cat);
        }

        req.setAttribute("cats", filledCats);

        req.setAttribute("canEdit", new Boolean(true));

        return "showPerson";
    }

    @RequestMapping("/showPerson.do")
    public String showPerson(HttpServletRequest req, @RequestParam Long id) throws HttpException,
            UnsupportedEncodingException, IOException {
        // Retrieve the person (From FB)
        Profile profile = fbService.getProfile(id, FacebookUserInterceptor.getApiToken(req));
        if (profile == null) {
            return showSelf(req);
        }
        if (profile.getLink() == null) {
            return showSelf(req);
        }
        req.setAttribute("profile", profile);

        // Retrieve the cats owned by the person
        List<Long> cats = personDao.getCats(id);

        ArrayList<Cat> filledCats = new ArrayList<Cat>();
        for (Long catId : cats) {
            Cat cat = catDao.getCat(catId);
            filledCats.add(cat);
        }

        req.setAttribute("cats", filledCats);

        req.setAttribute("canEdit", canEditPerson(req, id));

        return "showPerson";
    }

    @RequestMapping("/showCat.do")
    public String showCat(HttpServletRequest req, @RequestParam Long id) throws HttpException,
            UnsupportedEncodingException, IOException {
        // Retrieve the cat
        Cat cat = catDao.getCat(id);
        req.setAttribute("cat", cat);
        cat.setOwners(personDao.getPersons(cat.getId()));

        // Retrieve the owners
        ArrayList<Profile> persons = new ArrayList<Profile>();

        for (Long personFbId : cat.getOwners()) {
            Profile profile = fbService.getProfile(personFbId, FacebookUserInterceptor.getApiToken(req));
            persons.add(profile);
        }
        req.setAttribute("owners", persons);

        req.setAttribute("canEdit", canEditCat(req, cat));

        return "showCat";
    }

    @RequestMapping("/submitAddCat.do")
    public String addCat(HttpServletRequest req, @RequestParam String nickName, @RequestParam String name)
            throws HttpException, UnsupportedEncodingException, IOException {

        Cat cat = new Cat();
        cat.setName(name);
        cat.setNickName(nickName);

        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));

        ArrayList<Long> owners = new ArrayList<Long>();
        owners.add(profile.getId());
        cat.setOwners(owners);

        cat.setPhotos(fbService.getPhotosOfCat(cat, FacebookUserInterceptor.getApiToken(req)));

        catDao.addCat(cat);
        personDao.add(profile.getId(), cat.getId());

        req.setAttribute("cat", cat);

        // Retrieve the owners
        ArrayList<Profile> persons = new ArrayList<Profile>();

        for (Long personFbId : cat.getOwners()) {
            Profile owner = fbService.getProfile(personFbId, FacebookUserInterceptor.getApiToken(req));
            persons.add(owner);
        }
        req.setAttribute("owners", persons);

        req.setAttribute("canEdit", canEditCat(req, cat));

        return "showCat";
    }

    @RequestMapping("/submitEditCat.do")
    public String editCat(HttpServletRequest req, @RequestParam Long id, @RequestParam String nickName, @RequestParam String name)
            throws HttpException, UnsupportedEncodingException, IOException {

        Cat cat = catDao.getCat(id);
        cat.setName(name);
        cat.setNickName(nickName);

        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));

        ArrayList<Long> owners = new ArrayList<Long>();
        owners.add(profile.getId());
        cat.setOwners(owners);

        catDao.editCat(cat);

        req.setAttribute("cat", cat);

        // Retrieve the owners
        ArrayList<Profile> persons = new ArrayList<Profile>();

        for (Long personFbId : cat.getOwners()) {
            Profile owner = fbService.getProfile(personFbId, FacebookUserInterceptor.getApiToken(req));
            persons.add(owner);
        }
        req.setAttribute("owners", persons);

        req.setAttribute("canEdit", canEditCat(req, cat));

        return "showCat";
    }

    @RequestMapping("/deleteCat.do")
    public String deleteCat(HttpServletRequest req, @RequestParam Long id) throws HttpException,
            UnsupportedEncodingException, IOException {
        // Retrieve the cat
        Cat cat = catDao.getCat(id);
        cat.setOwners(personDao.getPersons(id));

        Profile profile = fbService.getProfile("me", FacebookUserInterceptor.getApiToken(req));

        if (!canEditCat(cat, profile)) {
            return showSelf(req);
        }

        List<Long> owners = cat.getOwners();

        if (owners.size() > 1) {
            // Only remove this owner.
            return removeOwner(req, id, profile.getId());
        }
        else {
            catDao.delete(id);
            personDao.delete(profile.getId(), id);
        }

        return showSelf(req);
    }

    @RequestMapping("/removeOwner.do")
    public String removeOwner(HttpServletRequest req, @RequestParam Long catId, @RequestParam Long ownerId)
            throws HttpException, UnsupportedEncodingException, IOException {
        Cat cat = catDao.getCat(catId);

        if (!canEditCat(req, cat)) {
            return showSelf(req);
        }

        List<Long> owners = cat.getOwners();
        if (owners.contains(ownerId)) {
            owners.remove(ownerId);
            catDao.editCat(cat);
        }
        return showCat(req, catId);
    }

    @RequestMapping("/tagScan.do")
    public String tagScan(HttpServletRequest req, @RequestParam Long id) throws HttpException,
            UnsupportedEncodingException, IOException {
        Cat cat = catDao.getCat(id);
        cat.setOwners(personDao.getPersons(id));
        List<Photo> photos = fbService.getPhotosOfCat(cat, FacebookUserInterceptor.getApiToken(req));
        HashSet<Photo> photoSet = new HashSet<Photo>();
        photoSet.addAll(photos);
        photoSet.addAll(cat.getPhotos());

        cat.getPhotos().clear();
        cat.getPhotos().addAll(photoSet);

        catDao.editCat(cat);
        return showCat(req, id);
    }

    @RequestMapping("/invite.do")
    public String invite(HttpServletRequest req) {
        return "invite";
    }

    public void setCatDao(CatDao catDao) {
        this.catDao = catDao;
    }

    public CatDao getCatDao() {
        return catDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setFbService(FBService fbService) {
        this.fbService = fbService;
    }

    public FBService getFbService() {
        return fbService;
    }
}
