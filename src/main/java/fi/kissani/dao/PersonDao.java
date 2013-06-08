package fi.kissani.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao {
    private static final String GET_PERSONS_CATS = "select cat_id from persons_cats where person_id=?";
    private static final String GET_CATS_PERSONS = "select person_id from persons_cats where cat_id=?";
    private static final String DELETE_PERSONS_CAT = "delete from persons_cats where person_id=? and cat_id=?";
    private static final String ADD_PERSONS_CAT = "insert into persons_cats (person_id, cat_id) VALUES (?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> getCats(Long personFbId) {
        return jdbcTemplate.queryForList(GET_PERSONS_CATS, Long.class, personFbId);
    }

    public List<Long> getPersons(Long catId) {
        return jdbcTemplate.queryForList(GET_CATS_PERSONS, Long.class, catId);
    }

    public void delete(Long personId, Long catId) {
        jdbcTemplate.update(DELETE_PERSONS_CAT, personId, catId);
    }

    public void add(Long personId, Long catId) {
        jdbcTemplate.update(ADD_PERSONS_CAT, personId, catId);
    }
}
