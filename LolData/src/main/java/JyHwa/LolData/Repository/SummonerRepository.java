package JyHwa.LolData.Repository;

import JyHwa.LolData.Entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SummonerRepository extends JpaRepository<Summoner,String> {
    Optional<Summoner> findByName(String name);
    List<Summoner> findByCheckField(int checkField);
}

