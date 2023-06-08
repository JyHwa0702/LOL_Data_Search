package JyHwa.LolData.Repository;

import JyHwa.LolData.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public List<User> findByCheckField(int CheckField);

}
