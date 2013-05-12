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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Cat getCat(String id) {
        return jdbcTemplate.queryForObject(GET_CAT, new CatMapper(), id);
    }

    public static class CatMapper implements RowMapper<Cat> {

        @Override
        public Cat mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cat result = new Cat();
            result.setId(rs.getIn)
            rs.getString("");
            // TODO Auto-generated method stub
            return null;
        }
    }
}
