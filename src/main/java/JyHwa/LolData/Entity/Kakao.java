package JyHwa.LolData.Entity;

import JyHwa.LolData.Dto.KakaoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "kakao")
public class Kakao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickname;
    @OneToMany(mappedBy = "kakao",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<User> users;

    public Kakao(KakaoDto kakaoDto){
        this.id= kakaoDto.getId();
        this.email=kakaoDto.getEmail();
        this.nickname =kakaoDto.getNickname();
        this.users =kakaoDto.getUsers();
    }
}
