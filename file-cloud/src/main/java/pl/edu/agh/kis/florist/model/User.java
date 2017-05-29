package pl.edu.agh.kis.florist.model;

import pl.edu.agh.kis.florist.db.tables.pojos.Users;

/**
 * Created by kantor on 21.01.17.
 */
public class User extends Users {
    public User(Users value) {
        super(value);
    }

    public User(Integer id, String userName, String displayName) {
        super(id, userName, displayName, null);
    }
}
