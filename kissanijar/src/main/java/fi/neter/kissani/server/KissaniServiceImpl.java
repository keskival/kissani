package fi.neter.kissani.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import fi.neter.kissani.dao.Cat;
import fi.neter.kissani.dao.CatJdoDao;
import fi.neter.kissani.dao.PersonJdoDao;
import fi.neter.kissani.fb.FBCookie;
import fi.neter.kissani.fb.FBService;
import fi.neter.kissani.fb.Photo;
import fi.neter.kissani.fb.Profile;
import fi.neter.kissani.filters.FB;
import fi.neter.kissani.shared.CatTO;
import fi.neter.kissani.shared.ProfileTO;

@Controller
@RequestMapping("/kissani/kis")
public class KissaniServiceImpl {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(KissaniServiceImpl.class);

    @Autowired
    private CatJdoDao catDao;

    @Autowired
    private PersonJdoDao personDao;
    
    @Autowired
    private FBService fbService;

    @Autowired
    private FB fb;
    
    @PostConstruct
    public void postConstruct() {
    }
    
	@RequestMapping(value="/setDefaultPhoto/{catId}/{photoId}", method = RequestMethod.GET)
    public void setDefaultPhoto(HttpServletRequest req, @PathVariable Long catId,
    		@PathVariable Long photoId) {
        Cat cat = catDao.getCat(catId);

        Profile profile = getProf(req, getMe(req));

        boolean canEdit = canEditCat(cat, profile);

        if (canEdit) {
            cat.setDefaultPhoto(String.valueOf(photoId));
            catDao.persistCat(cat);
        }
    }

	@RequestMapping(value="/canEditPerson/{personId}", method = RequestMethod.GET)
	@ResponseBody
    public Boolean canEditPerson(HttpServletRequest req, @PathVariable Long personId) {
        Profile profile = getProf(req, getMe(req));
        return canEditPerson(personId, profile);
    }

    private Boolean canEditPerson(Long id, Profile profile) {
        if (profile.getId().equals(id)) {
            logger.debug("Can edit person " + id + ".");
            return true;
        }
        else {
            logger.debug("Cannot edit person " + id + ".");
            return false;
        }
    }

	@RequestMapping(value="/canEditCat/{catId}", method = RequestMethod.GET)
	@ResponseBody
    public Boolean canEditCat(HttpServletRequest req, @PathVariable Long catId) {
        Profile profile = getProf(req, getMe(req));
        Cat cat = catDao.getCat(catId);
        return canEditCat(cat, profile);
    }

    private Boolean canEditCat(Set<Long> owners, Long catId, Profile profile) {
        if (owners.contains(profile.getId())) {
            logger.debug("Can edit cat " + catId + ".");
            return true;
        }
        else {
            logger.debug("Cannot edit cat " + catId + ".");
            return false;
        }
    }
    
    private Boolean canEditCat(Cat cat, Profile profile) {
        Set<Long> owners = cat.getOwnersAsSet();
        return canEditCat(owners, cat.getId(), profile);
    }
    
	@RequestMapping(value="/addFriendForCat/{catId}/{friendId}", method = RequestMethod.GET)
    public void addFriendForCat(HttpServletRequest req, @PathVariable Long catId,
    		@PathVariable Long friendId) {
        // Retrieve the person (From FB)
        Profile profile = getProf(req, getMe(req));
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        boolean canEdit = canEditCat(req, friendId);
        Cat friend = catDao.getCat(friendId);

        if (canEdit) {
        	// Update the cat friends.
            Cat cat = catDao.getCat(catId);
        	if (cat != null) {
            	Set<Long> catFriends = cat.getFriendsAsSet();
            	catFriends.add(friendId);
            	cat.setFriendsAsSet(catFriends);
            	Set<Long> friendsFriends = friend.getFriendsAsSet();
            	friendsFriends.add(catId);
            	friend.setFriendsAsSet(friendsFriends);
            	catDao.persistCat(cat);
            	catDao.persistCat(friend);
        	}
        }
    }

	@RequestMapping(value="/addOwnerForCat/{ownerId}/{catId}", method = RequestMethod.GET)
    public void addOwnerForCat(HttpServletRequest req, Long ownerId, Long catId) {
        // Retrieve the person (From FB)
        Profile profile = getProf(req, getMe(req));
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        boolean canEdit = canEditCat(req, catId);
        Cat cat = catDao.getCat(catId);

        if (canEdit) {
        	// Update the owners.
        	List<Long> personsCats = personDao.getPersonOrCreate(ownerId).getCats();
        	if (personsCats == null) {
        		return;
        	}
        	personDao.add(ownerId, catId);
        	
        	Set<Long> owners = cat.getOwnersAsSet();
        	owners.add(ownerId);
        	cat.setOwnersAsSet(owners);
        	catDao.persistCat(cat);
        }
    }

	@RequestMapping(value="/removeOwnerFromCat/{ownerId}/{catId}", method = RequestMethod.GET)
    public void removeOwnerFromCat(HttpServletRequest req, Long ownerId, Long catId) {
        // Retrieve the person (From FB)
        Profile profile = getProf(req, getMe(req));
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        boolean canEdit = canEditCat(req, catId);
        Cat cat = catDao.getCat(catId);

        if (canEdit) {
        	// Update the owners.
        	List<Long> personsCats = personDao.getPersonOrCreate(ownerId).getCats();
        	if ((personsCats == null) || (ownerId == profile.getId())) {
        		// You can't remove yourself.
        		return;
        	}
        	personDao.delete(ownerId, catId);
        	
        	Set<Long> owners = cat.getOwnersAsSet();
        	owners.remove(ownerId);
        	cat.setOwnersAsSet(owners);
        	catDao.persistCat(cat);
        }
    }

	@RequestMapping(value="/editCat/{catId}/{nickName}/{name}", method = RequestMethod.GET)
    public void editCat(HttpServletRequest req, Long catId, String nickName, String name) {
        Cat cat = catDao.getCat(catId);

        Profile profile = getProf(req, getMe(req));

        boolean canEdit = canEditCat(cat, profile);

        if (canEdit) {
            cat.setName(name);
            cat.setNickName(nickName);
            Set<Long> owners = new HashSet<Long>();
            owners.add(profile.getId());
            cat.setOwnersAsSet(owners);

            catDao.persistCat(cat);

            // Retrieve the owners
            ArrayList<Profile> persons = new ArrayList<Profile>();

            for (Long personFbId : cat.getOwnersAsSet()) {
                Profile owner = getProf(req, String.valueOf(personFbId));
                persons.add(owner);
            }
        }
    }

	@RequestMapping(value="/deleteCat/{catId}", method = RequestMethod.GET)
    public void deleteCat(HttpServletRequest req, Long catId) {
        // Retrieve the cat
        Cat cat = catDao.getCat(catId);

        Profile profile = getProf(req, getMe(req));

        if (canEditCat(cat, profile)) {
            Set<Long> owners = cat.getOwnersAsSet();

            if (owners.size() > 1) {
                // Only remove this owner.
                removeOwnerFromCat(req, catId, profile.getId());
            } else {
            	// Was the last owner.
                catDao.delete(catId);
                personDao.delete(profile.getId(), catId);
            }
        }
    }

	@RequestMapping(value="/removePhoto/{catId}/{photoId}", method = RequestMethod.GET)
    public void removePhoto(HttpServletRequest req, Long catId, Long photoId) {

        if (canEditCat(req, catId)) {
            Cat cat = catDao.getCat(catId);
            Set<Photo> photos = cat.getPhotosAsSet();
            Set<Photo> newPhotos = new HashSet<Photo>();
            for (Photo photo: photos) {
            	if (!photo.getId().equals(photoId)) {
            		newPhotos.add(photo);
            	}
            }
            cat.setPhotosAsSet(newPhotos);
            catDao.persistCat(cat);
        }
    }
    
    private String getMe(HttpServletRequest req) {
    	FBCookie id = fb.requireLogin(req);

    	if (id != null) {
    		return String.valueOf(id.getFbId());
    	}
    	
    	return "me";
    }
    
	@RequestMapping(value="/addCat/{nickName}/{name}", method = RequestMethod.GET)
    public void addCat(HttpServletRequest req, String nickName,
            String name) {

        Profile profile = getProf(req, getMe(req));

        Cat cat = catDao.newCat();
        cat.setName(name);
        cat.setNickName(nickName);

        Set<Long> owners = new HashSet<Long>();
        owners.add(profile.getId());
        cat.setOwnersAsSet(owners);

        System.out.println("Cat: " + cat.getId() + ", " + cat.getName());
        catDao.persistCat(cat);
        personDao.add(profile.getId(), cat.getId());

        Queue queue = QueueFactory.getDefaultQueue();
        FBCookie session = fb.requireLogin(req);
        queue.add(TaskOptions.Builder.withUrl("/addCatPhotos.task")
                .param("accessToken", session.getAccessToken())
                .param("catId", String.valueOf(cat.getId())));

        // Retrieve the owners
        ArrayList<Profile> persons = new ArrayList<Profile>();

        for (Long personFbId : cat.getOwnersAsSet()) {
            Profile owner = getProf(req, String.valueOf(personFbId));
            persons.add(owner);
        }
    }

	@RequestMapping(value="/tagScan/{catId}", method = RequestMethod.GET)
    public void tagScan(HttpServletRequest req, Long catId) {
        if (canEditCat(req, catId)) {
        	Queue queue = QueueFactory.getDefaultQueue();
            FBCookie session = fb.requireLogin(req);
            queue.add(withUrl("/tagScanTask.task").param("catId", String.valueOf(catId))
            		.param("accessToken", session.getAccessToken())
            		);
        }
    }

    private Profile getProf(HttpServletRequest req, String id) {
        FBCookie session = fb.requireLogin(req);
        if (id.equals("me")) {
        	id = String.valueOf(session.getFbId());
        }
    	String accessToken = session.getAccessToken();

    	Profile prof = fbService.getProfile(id, accessToken);
		List<Long> cats = personDao.getPersonOrCreate(Long.valueOf(id)).getCats();
		
		List<Cat> catList = new ArrayList<Cat>();

		for (Long catId : cats) {
			Cat cat = catDao.getCat(catId);
			catList.add(cat);
		}
		prof.setCats(catList);
		
		return prof;
    }
    
	@RequestMapping(value="/tagScan/{personId}", method = RequestMethod.GET)
	@ResponseBody
    public ProfileTO getProfile(HttpServletRequest req, String personId) {
    	return getProf(req, personId).getProfileTO();
    }

    public void setCatDao(CatJdoDao catDao) {
        this.catDao = catDao;
    }

    public void setPersonDao(PersonJdoDao personDao) {
        this.personDao = personDao;
    }

    public void setFbService(FBService fbService) {
        this.fbService = fbService;
    }

	@RequestMapping(value="/getCat/{catId}", method = RequestMethod.GET)
	@ResponseBody
	public CatTO getCat(Long catId) {
		CatTO result = catDao.getCat(catId).getCatTO();
		// Prefetching friends.
		List<CatTO> catFriends = new ArrayList<CatTO>();
		Set<Long> friendsToFetch = result.getFriends();
		if (friendsToFetch != null) {
			for (Long catFriend : friendsToFetch) {
				CatTO friend = catDao.getCat(catFriend).getCatTO();
				catFriends.add(friend);
			}
		}
		result.setPrefetchedFriends(catFriends);
		return result;
	}

	public Collection<Long> getCatsOrCreate(Long id) {
		return personDao.getPersonOrCreate(id).getCats();
	}

	public Collection<Long> getCats(Long id) {
		return personDao.getPersonOrCreate(id).getCats();
	}

	public void persistCat(CatTO cat) {
		catDao.persistCat(new Cat(cat));
	}
}
