package fi.neter.kissani.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import fi.neter.kissani.BaseGWTService;
import fi.neter.kissani.client.KissaniService;
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
public class KissaniServiceImpl extends BaseGWTService implements KissaniService {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(KissaniServiceImpl.class);

    @Autowired
    private CatJdoDao catDao;

    @Autowired
    private PersonJdoDao personDao;
    
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private FBService fbService;

    @Autowired
    private FB fb;
    
    @PostConstruct
    public void postConstruct() {
    	Cache catCache = cacheManager.getCache("catCache");
    	Cache personCache = cacheManager.getCache("personCache");
    	
    	catDao.setCache(catCache);
    	personDao.setCache(personCache);
    }
    
    @Override
    public void setDefaultPhoto(Long catId, Long photoId) {
        Cat cat = catDao.getCat(catId);

        Profile profile = getProf(getMe());

        boolean canEdit = canEditCat(cat, profile);

        if (canEdit) {
            cat.setDefaultPhoto(String.valueOf(photoId));
            catDao.persistCat(cat);
        }
    }

    @Override
    public Boolean canEditPerson(Long id) {
        Profile profile = getProf(getMe());
        return canEditPerson(id, profile);
    }

    public Boolean canEditPerson(Long id, Profile profile) {
        if (profile.getId().equals(id)) {
            logger.debug("Can edit person " + id + ".");
            return true;
        }
        else {
            logger.debug("Cannot edit person " + id + ".");
            return false;
        }
    }

    @Override
    public Boolean canEditCat(CatTO cat) {
        Profile profile = getProf(getMe());
        return canEditCat(cat, profile);
    }

    private Boolean canEditCat(Cat cat) {
        Profile profile = getProf(getMe());
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
    
    private Boolean canEditCat(CatTO cat, Profile profile) {
        Set<Long> owners = cat.getOwners();
        return canEditCat(owners, cat.getId(), profile);
    }

    private Boolean canEditCat(Cat cat, Profile profile) {
        Set<Long> owners = cat.getOwnersAsSet();
        return canEditCat(owners, cat.getId(), profile);
    }
    
    @Override
    public void addFriendForCat(Long catId, Long friendId) {
    	HttpServletRequest req = this.getThreadLocalRequest();
        // Retrieve the person (From FB)
        Profile profile = getProf(getMe());
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        Cat friend = catDao.getCat(friendId);
        boolean canEdit = canEditCat(friend);

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

    @Override
    public void addOwnerForCat(Long ownerId, Long catId) {
    	HttpServletRequest req = this.getThreadLocalRequest();
        // Retrieve the person (From FB)
        Profile profile = getProf(getMe());
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        Cat cat = catDao.getCat(catId);
        boolean canEdit = canEditCat(cat);

        if (canEdit) {
        	// Update the owners.
        	List<Long> personsCats = personDao.getCats(ownerId);
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

    @Override
    public void removeOwnerFromCat(Long ownerId, Long catId) {
    	HttpServletRequest req = this.getThreadLocalRequest();
        // Retrieve the person (From FB)
        Profile profile = getProf(getMe());
        if (profile == null) {
        	FB.clearApiToken(req);
            throw new RuntimeException("Token not valid.");
        }

        Cat cat = catDao.getCat(catId);
        boolean canEdit = canEditCat(cat);

        if (canEdit) {
        	// Update the owners.
        	List<Long> personsCats = personDao.getCats(ownerId);
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

    @Override
    public void editCat(Long id, String nickName, String name) {
        Cat cat = catDao.getCat(id);

        Profile profile = getProf(getMe());

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
                Profile owner = getProf(String.valueOf(personFbId));
                persons.add(owner);
            }
        }
    }

    @Override
    public void deleteCat(Long id) {
        // Retrieve the cat
        Cat cat = catDao.getCat(id);

        Profile profile = getProf(getMe());

        if (canEditCat(cat, profile)) {
            Set<Long> owners = cat.getOwnersAsSet();

            if (owners.size() > 1) {
                // Only remove this owner.
                removeOwnerFromCat(id, profile.getId());
            }
            else {
                catDao.delete(id);
                personDao.delete(profile.getId(), id);
            }
        }
    }

    @Override
    public void removePhoto(Long catId, Long photoId) {
        Cat cat = catDao.getCat(catId);

        if (canEditCat(cat)) {
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
    
    private String getMe() {
    	HttpServletRequest req = this.getThreadLocalRequest();

    	FBCookie id = fb.requireLogin(req);

    	if (id != null) {
    		return String.valueOf(id.getFbId());
    	}
    	
    	return "me";
    }
    
    @Override
    public void addCat(String nickName,
            String name) {
    	HttpServletRequest req = this.getThreadLocalRequest();

        Profile profile = getProf(getMe());

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
            Profile owner = getProf(String.valueOf(personFbId));
            persons.add(owner);
        }
    }

    @Override
    public void tagScan(Long id) {
    	HttpServletRequest req = this.getThreadLocalRequest();
    	
        Cat cat = catDao.getCat(id);
        if (canEditCat(cat)) {
        	Queue queue = QueueFactory.getDefaultQueue();
            FBCookie session = fb.requireLogin(req);
            queue.add(withUrl("/tagScanTask.task").param("catId", String.valueOf(id))
            		.param("accessToken", session.getAccessToken())
            		);
        }
    }

    private Profile getProf(String id) {
    	HttpServletRequest req = this.getThreadLocalRequest();
        FBCookie session = fb.requireLogin(req);
        if (id.equals("me")) {
        	id = String.valueOf(session.getFbId());
        }
    	String accessToken = session.getAccessToken();

    	Profile prof = fbService.getProfile(id, accessToken);
		List<Long> cats = personDao.getCats(Long.valueOf(id));
		
		List<Cat> catList = new ArrayList<Cat>();

		for (Long catId : cats) {
			Cat cat = catDao.getCat(catId);
			catList.add(cat);
		}
		prof.setCats(catList);
		
		return prof;
    }
    
    @Override
    public ProfileTO getProfile(String id) {
    	return getProf(id).getProfileTO();
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

	@Override
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

	@Override
	public Collection<Long> getCatsOrCreate(Long id) {
		return personDao.getCatsOrCreate(id);
	}

	@Override
	public Collection<Long> getCats(Long id) {
		return personDao.getCats(id);
	}

	@Override
	public void persistCat(CatTO cat) {
		catDao.persistCat(new Cat(cat));
	}
}
