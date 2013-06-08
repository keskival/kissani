package fi.neter.kissani.dao;

import java.util.Collection;
import java.util.List;

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
public class PersonJdoDao {
	private static final Logger logger = LoggerFactory
			.getLogger(PersonJdoDao.class);

	private Ehcache cache;

	public void setCache(Ehcache cache) {
		cache.registerCacheWriter(new PersonCacheWriter());
		this.cache = new SelfPopulatingCache(cache,
				new PersonCacheEntryFactory());
	}

	/**
	 * Implement the CacheEntryFactory that allows the cache to provide the
	 * read-through strategy
	 */
	private class PersonCacheEntryFactory implements CacheEntryFactory {
		@Override
		public Object createEntry(Object key) throws Exception {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Person person = pm.getObjectById(Person.class, Long.valueOf((String)key));
				System.out.println("Cats fetched from db: " + person.getCats());
				return person;
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
	private class PersonCacheWriter implements CacheWriter {
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
				Person person = (Person) element.getValue();
				if ((person != null) && (person.getId() != null)) {
					try {
						Person newPerson = (Person) pm.getObjectById(person.getId());
						if (newPerson != null) {
							newPerson.setCats(person.getCats());
							return;
						}
					} catch (JDOObjectNotFoundException e) {
					}
				}
				pm.makePersistent(person);
			} finally {
				pm.close();
			}
		}

		@Override
		public void delete(CacheEntry entry) throws CacheException {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Person person = pm.getObjectById(Person.class,
						(Long) entry.getKey());
				pm.deletePersistent(person);
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

	public List<Long> getCats(Long personFbId) {
		try {
			Element element = cache.get(personFbId.toString());
			if (element == null) {
				return null;
			}
			Person person = (Person) element.getValue();
			if (person == null) {
				return null;
			}
			List<Long> cats = person.getCats();
			System.out.println("Cats fetched: " + cats);
			return cats;
		} catch (CacheException e) {
			return null;
		}
	}

	public List<Long> getCatsOrCreate(Long personFbId) {
		Element element = cache.get(personFbId.toString());

		Person person = null;

		if (element != null) {
			person = (Person) element.getValue();
		}

		if (person == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				person = pm.newInstance(Person.class);
				person.setId(personFbId);
				pm.makePersistent(person);
			} finally {
				pm.close();
			}
		}
		System.out.println("Cats fetched2: " + person.getCats());
		return person.getCats();
	}

	public void delete(Long personFbId, Long catId) {
		Element element = cache.get(personFbId.toString());
		if (element != null) {
			Person person = (Person) element.getValue();
			person.getCats().remove(catId);
			cache.putWithWriter(new Element(person.getId(), person));
		}
	}

	public void add(Long personFbId, Long catId) {
		Element element = cache.get(personFbId.toString());
		Person person = null;

		if (element != null) {
			person = (Person) element.getValue();
		}
		if (person == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				person = pm.newInstance(Person.class);
				person.setId(personFbId);
			} finally {
				pm.close();
			}
		}

		person.getCats().add(catId);
		cache.putWithWriter(new Element(person.getId().toString(), person));
	}
}
