package interfaces;

import java.util.List;

public interface DAO<T> {
    void create(List<T> models);
    void read();
    void update(List<T> models);
    void delete(int id);
}
