package service.user;

import java.sql.SQLException;
import java.util.List;

public interface IService <T> {
     boolean create(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(String username) throws SQLException;

    List<T> read() throws SQLException;
}
