package JyHwa.LolData.Repository;

import JyHwa.LolData.Entity.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoRepository extends JpaRepository<Kakao,Long> {
    public Optional<Kakao> findByEmail(String email);
}
