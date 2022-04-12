package tasklist.backendspringboot.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tasklist.backendspringboot.entity.Stat;

// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

}
