package JyHwa.LolData.Repository;

import JyHwa.LolData.Entity.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoRepository extends JpaRepository<Kakao,Long> {
}
