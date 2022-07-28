package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.AppUserMapper;
import survivor.models.AppUser;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public List<AppUser> findAllAppUsers() {
        final String sql = "select user_id, username, password_hash, disabled from app_user;";
        return jdbcTemplate.query(sql, new AppUserMapper(List.of("USER")));
    }

    @Transactional
    public AppUser findAppUserByUsername(String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = "select user_id, username, password_hash, disabled "
                + "from app_user "
                + "where username = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), username)
                .stream()
                .findFirst().orElse(null);
    }

    @Transactional
    public AppUser findAppUserById(int id) {
        List<String> roles = getRolesByUserId(id);

        final String sql = "select user_id, username, password_hash, disabled "
                + "from app_user "
                + "where user_id = ?;";

        AppUser user =  jdbcTemplate.query(sql, new AppUserMapper(roles), id)
                .stream()
                .findFirst().orElse(null);

        return user;
    }

    @Transactional
    public AppUser createAppUser(AppUser user) {

        final String sql = "insert into app_user (username, password_hash) values (?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setAppUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Transactional
    public boolean updateAppUser(AppUser user) {

        final String sql = "update app_user set "
                + "username = ?, "
                + "disabled = ? "
                + "where app_user_id = ?";

        int rowsUpdated = jdbcTemplate.update(sql,
                user.getUsername(), !user.isEnabled(), user.getAppUserId());

        updateRoles(user);

        return rowsUpdated > 0;
    }

    @Transactional
    public boolean deleteAppUserById(int appUserID) {

        final String sql = "update app_user set "
                + "disabled = 1 "
                + "where user_id = ?";

        return jdbcTemplate.update(sql,
                appUserID) > 0;
    }

    /////////////////////////////////////// Helper Methods ////////////////////////////////////////////////

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_roles where user_id = ?;", user.getAppUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : AppUser.convertAuthoritiesToRoles(authorities)) {
            String sql = "insert into app_user_roles (user_id, role_id) "
                    + "select ?, role_id from app_roles where `role` = ?;";
            jdbcTemplate.update(sql, user.getAppUserId(), role);
        }
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.role "
                + "from app_user_roles ur "
                + "inner join app_roles r on ur.role_id = r.role_id "
                + "inner join app_user au on ur.user_id = au.user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("role"), username);
    }


    private List<String> getRolesByUserId(int id) {
        final String sql = "select r.role "
                + "from app_user_roles ur "
                + "inner join app_roles r on ur.role_id = r.role_id "
                + "inner join app_user au on ur.user_id = au.user_id "
                + "where au.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("role"), id);
    }

}

