package fi.neter.kissani.dao;

import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CatJdoDao {
	private static final Logger logger = LoggerFactory
			.getLogger(CatJdoDao.class);
	private PersistenceManager pm = PMF.get().getPersistenceManager();

	public Cat getCat(Long id) {
		Cat cat = pm.getObjectById(Cat.class, id);
		return cat;
	}

	public Set<Long> getPersons(Long catId) {
		Cat cat = getCat(catId);
		Set<Long> list = cat.getOwnersAsSet();
		return list;
	}

	public void persistCat(Cat cat) {
		if ((cat != null) && (cat.getId() != null)) {
			try {
				Cat newCat = (Cat) pm.getObjectById(cat.getId());
				if (newCat != null) {
					newCat.setCatFriends(cat.getCatFriends());
					newCat.setDefaultPhoto(cat.getDefaultPhoto());
					newCat.setName(cat.getName());
					newCat.setNickName(cat.getNickName());
					newCat.setOwners(cat.getOwners());
					newCat.setPhotos(cat.getPhotos());
					return;
				}
			} catch (JDOObjectNotFoundException e) {
			}
		}
		pm.makePersistent(cat);
	}

	public void delete(Long catId) {
		Cat cat = pm.getObjectById(Cat.class, catId);
		pm.deletePersistent(cat);
	}

	public Cat newCat() {
		try {
			Cat cat = pm.newInstance(Cat.class);
			pm.makePersistent(cat);
			return cat;
		} finally {
			pm.close();
		}
	}
}
