package JyHwa.LolData.Dto;

import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class KakaoDto {
    private Long id;
    private String email;
    private String nickname;

    private List<User> users;

    public KakaoDto(Kakao kakao){
        this.id= kakao.getId();
        this.email=kakao.getEmail();
        this.nickname =kakao.getNickname();
        this.users=kakao.getUsers();
    }

    public Kakao toEntity(){
        Kakao kakao = new Kakao(this);
        return kakao;
    }
}
