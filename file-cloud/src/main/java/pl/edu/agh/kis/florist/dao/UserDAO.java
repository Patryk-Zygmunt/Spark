package pl.edu.agh.kis.florist.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import pl.edu.agh.kis.florist.db.tables.pojos.Users;
import pl.edu.agh.kis.florist.db.tables.records.UsersRecord;
import pl.edu.agh.kis.florist.model.User;
import static pl.edu.agh.kis.florist.db.Tables.USERS;
/**
 * Created by kantor on 21.01.17.
 */
public class UserDAO {
    private final String DB_URL = "jdbc:sqlite:test.db";
    private final AuthorDAO authorRepository = new AuthorDAO();

    public User createUser(String userName) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if (userExist(userName)) {
                throw new IllegalStateException("Użytkownik o podanym nicku istnieje");
            }
            User newUser = new User(1, userName, userName);
            UsersRecord record = create.newRecord(USERS, newUser);
            record.store();
            User user = new User(record.into(Users.class));
            return user;
        }
        }
        private boolean userExist(String name) {
            try (DSLContext create = DSL.using(DB_URL)) {


                UsersRecord record = create.selectFrom(USERS).where(USERS.USER_NAME.equal(name)).fetchAny();
                if (record == null)
                    return false;
                else {
                    return true;
                }
            }
        }
}
