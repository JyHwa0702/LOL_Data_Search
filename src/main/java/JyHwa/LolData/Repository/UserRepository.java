package JyHwa.LolData.Repository;

import JyHwa.LolData.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public List<User> findByCheckField(int CheckField);
    public Optional<User> findBySummonerName(String summonerName);
    public List<User> findByKakaoId(Long kakaoId);
}
