package fi.neter.kissani.dao;

import java.util.Collection;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import net.sf.ehcache.writer.CacheWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class CatJdoDao {
	private static final Logger logger = LoggerFactory
			.getLogger(CatJdoDao.class);

	private Ehcache cache;

	public void setCache(Ehcache cache) {
		cache.registerCacheWriter(new CatCacheWriter());
		this.cache = new SelfPopulatingCache(cache, new CatCacheEntryFactory());
	}

	/**
	 * Implement the CacheEntryFactory that allows the cache to provide the
	 * read-through strategy
	 */
	private class CatCacheEntryFactory implements CacheEntryFactory {
		@Override
		public Object createEntry(Object key) throws Exception {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Cat cat = pm.getObjectById(Cat.class, Long.valueOf((String)key));
				return cat;
			} catch (JDOObjectNotFoundException e) {
				return null;
			} finally {
				pm.close();
			}
		}
	}

	/**
	 * Implement the CacheWriter interface which allows the cache to provide the
	 * write-through or write-behind strategy.
	 */
	private class CatCacheWriter implements CacheWriter {
		@Override
		public CacheWriter clone(Ehcache cache)
				throws CloneNotSupportedException {
			throw new CloneNotSupportedException();
		}

		@Override
		public void init() {
		}

		@Override
		public void dispose() throws CacheException {
		}

		@Override
		public void write(Element element) throws CacheException {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Cat cat = (Cat) element.getValue();
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
			} finally {
				pm.close();
			}
		}

		@Override
		public void delete(CacheEntry entry) throws CacheException {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Cat cat = pm.getObjectById(Cat.class, (Long) entry.getKey());
				pm.deletePersistent(cat);
			} finally {
				pm.close();
			}
		}

		@Override
		public void writeAll(Collection<Element> elements)
				throws CacheException {
			for (Element element : elements) {
				write(element);
			}
		}

		@Override
		public void deleteAll(Collection<CacheEntry> entries)
				throws CacheException {
			for (CacheEntry element : entries) {
				delete(element);
			}
		}
	}

	public Cat getCat(Long id) {
		Element element = cache.get(id.toString());
		if (element == null) {
			return null;
		}
		Cat cat = (Cat) element.getValue();
		return cat;
	}

	public Set<Long> getPersons(Long catId) {
		Cat cat = getCat(catId);
		Set<Long> list = cat.getOwnersAsSet();
		return list;
	}

	public void persistCat(Cat cat) {
        cache.putWithWriter(new Element(cat.getId().toString(), cat));
    }

	public void delete(Long catId) {
        cache.removeWithWriter(catId.toString());
    }

	public Cat newCat() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Cat cat = pm.newInstance(Cat.class);
			pm.makePersistent(cat);
			return cat;
		} finally {
			pm.close();
		}
	}
}
