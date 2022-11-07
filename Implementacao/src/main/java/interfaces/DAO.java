package interfaces;

import java.util.List;

public interface DAO<T> {
    void create(List<T> models);
    void create(T model);
    List<T> read();
    void update(List<T> models);
    void delete(String id);
}
