package fi.neter.kissani.dao;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PersonJdoDao {
	private static final Logger logger = LoggerFactory
			.getLogger(PersonJdoDao.class);
	private PersistenceManager pm = PMF.get().getPersistenceManager();

	public Person getPerson(Long personFbId) {
		Person person = null;
		try {
			person = pm.getObjectById(Person.class, personFbId);
			System.out.println("Cats fetched from db: " + person.getCats());
		} catch (JDOObjectNotFoundException e) {
		}
		return person;
	}
	
	public Person getPersonOrCreate(Long personFbId) {
		Person person = getPerson(personFbId);
		if (person == null) {
			person = pm.newInstance(Person.class);
			person.setId(personFbId);
			pm.makePersistent(person);
		}
		return person;
	}

	/**
	 * Removes a cat from a person.
	 * @param personFbId
	 * @param catId
	 */
	public void delete(Long personFbId, Long catId) {
		Person person = getPersonOrCreate(personFbId);
		person.getCats().remove(catId);
	}
	
	public void deletePerson(Long personFbId) {
		Person person = pm.getObjectById(Person.class,
				personFbId);
		pm.deletePersistent(person);
	}
		
	/**
	 * Adds a new cat for the person.
	 * @param personFbId
	 * @param catId
	 */
	public void add(Long personFbId, Long catId) {
		Person person = getPersonOrCreate(personFbId);
		person.getCats().add(catId);
	}
}
