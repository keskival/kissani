package fi.kissani.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CatDao {
    private static final String GET_CAT = "select id, name, nickname, pics from cats where id=?";
    private static final String EDIT_CAT = "update cats set name=?, nickname=?, pics=? where id=?";
    private static final String DELETE_CAT = "delete from cats where id=?";
    private static final String ADD_CAT = "insert into cats (name, nickname, pics) values (?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Cat getCat(Long id) {
        return jdbcTemplate.queryForObject(GET_CAT, new CatMapper(), id);
    }

    public void editCat(Cat cat) {
        jdbcTemplate.update(EDIT_CAT, cat.getName(), cat.getNickName(), cat.getUrlsForPicsAsString(), cat.getId());
    }

    public void delete(Long catId) {
        jdbcTemplate.update(DELETE_CAT, catId);
    }

    public void addCat(Cat cat) {
        jdbcTemplate.update(ADD_CAT, cat.getName(), cat.getNickName(), cat.getUrlsForPicsAsString());
        cat.setId(jdbcTemplate.queryForLong("select LAST_INSERT_ID()"));
    }

    public static class CatMapper implements RowMapper<Cat> {

        @Override
        public Cat mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cat result = new Cat();
            result.setId(rs.getLong("id"));
            result.setName(rs.getString("name"));
            result.setNickName(rs.getString("nickname"));
            String urlList = rs.getString("pics");
            result.setPhotos(urlList);
            return result;
        }
    }
}
