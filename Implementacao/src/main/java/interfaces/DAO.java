package interfaces;

import java.util.List;

public interface DAO<T> {
    void create(List<T> models);
    List<T> read();
    void update(List<T> models);
    void delete(String id);
}
