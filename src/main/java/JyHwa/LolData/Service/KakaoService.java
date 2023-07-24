package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoApi.template_object;
import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.*;

@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:kakaoApiKey.properties")
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final KakaoRepository kakaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.restapi.key}")
    private String client_id;

//    @Value("${redirect.uri}")
    private String redirect_uri = "http://localhost:8080/oauth/kakao";

    private String grantType = "authorization_code";
    private String scope = "talk_message"; //메세지 보내기 항목 추가
    private String target_id_type="user_id";
    private String sendMeUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private String logoutUrl = "https://kapi.kakao.com/v1/user/logout";


    public boolean isSaveKakao(Long kakaoId){

        if(kakaoId == null){
            log.info("kakaoId is null");
            return false;
        }

        Optional<Kakao> byId = kakaoRepository.findById(kakaoId);
        if(byId.isPresent()){
            log.info("isSaveKakao = "+byId.get().toString());
            return true;
        } else {
            log.info("isSaveKakao = false");
            return false;
        }
    }
    public String getKakaoCodeUrl(){
        String tokenRequestUrl = String.format("https://kauth.kakao.com/oauth/authorize" +
                "?response_type=code&client_id=%s&redirect_uri=%s&response_type=code&scope=%s"
                , client_id,redirect_uri,scope);
        return tokenRequestUrl;
    }
    public JsonNode getKakaoInfo(String accessToken) {
        String getInfoUrl = "https://kapi.kakao.com/v2/user/me";

        try{

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(getInfoUrl);
            httpPost.setHeader("Authorization","Bearer "+accessToken);
            httpPost.setHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");


            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("response = "+response.toString());
            HttpEntity entity = response.getEntity();
            JsonNode kakaoInfoNode = objectMapper.readTree(entity.getContent());

            return kakaoInfoNode;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Long kakaoLogout(String accessToken){

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(logoutUrl);

        log.info("KakaoLogout accessToken = "+accessToken);
        JsonNode kakaoInfo = getKakaoInfo(accessToken);

        Long target_id = Long.valueOf(kakaoInfo.get("id").asText());

        try{
            httpPost.setHeader("Authorization","Bearer "+accessToken);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("target_id_type",target_id_type));
            params.add(new BasicNameValuePair("target_id",target_id.toString()));

            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));

            CloseableHttpResponse response = httpClient.execute(httpPost);

            if(response.getStatusLine().getStatusCode()!=200){
                log.error("kakao logout CodeStatus = "+response.getStatusLine().getStatusCode());
                return null;
            }
            JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());
            long logoutKakaoId = jsonNode.get("id").asLong();
//            Long logoutKakaoId = objectMapper.readValue(response.getEntity().getContent(), Long.class);

            return logoutKakaoId;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String sendNotification(String message,String accessToken) throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(sendMeUrl);

        try{
            httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization","Bearer "+accessToken);

            template_object template_object = new template_object(message);
            String templateObjectJson = template_object.toJson(objectMapper);
            log.info("templateObjectJson.toString(); = "+templateObjectJson);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("template_object",templateObjectJson));

//            StringEntity entity = new StringEntity(templateObjectJson,"UTF-8");
//            entity.setContentType("application/json");
//            log.info("entity = "+entity);
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            log.info("httpPost = "+httpPost.getEntity().getContent());

            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            log.info("responseBody Message 성공시 0"+responseBody);
        return responseBody;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            httpClient.close();
        }
    }
    public String getKakaoAccessToken(String code) {

        try{
            String getTokenUrl="https://kauth.kakao.com/oauth/token";

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type",grantType));
            params.add(new BasicNameValuePair("client_id",client_id));
            params.add(new BasicNameValuePair("redirect_uri",redirect_uri));
            params.add(new BasicNameValuePair("code",code));


            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(getTokenUrl);
            httpPost.setHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("getKakaoAccessToken response = "+response);
            HttpEntity entity = response.getEntity();
            log.info("entity = "+entity.toString());

            JsonNode jsonToken = objectMapper.readTree(entity.getContent());
            log.info("accessToken = " + jsonToken.toString());
            String accessToken = jsonToken.get("access_token").asText();
            log.info("string AccessToken = "+accessToken);

            return accessToken;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public Kakao saveKakao(JsonNode kakaoInfo,String accessToken){

//      properties{
//          nickname
//        }
//      kakao_account{
//          email
//      }
        String email = kakaoInfo.get("kakao_account").get("email").asText();
        String nickname = kakaoInfo.get("properties").get("nickname").asText();

        KakaoDto kakaoDto = new KakaoDto();

        kakaoDto.setEmail(email);
        kakaoDto.setNickname(nickname);
        kakaoDto.setToken(accessToken); //accessToken 넣는곳, 양방향 암호화 해서 처리해야함

        Kakao kakao = kakaoDto.toEntity();
        log.info("savedKakao before = "+kakao.toString());
        Optional<Kakao> byEmail = kakaoRepository.findByEmail(kakao.getEmail());
        if(byEmail.isEmpty()){
            Kakao savedKakao = kakaoRepository.save(kakao);
            log.info("savedKakao after = "+savedKakao.toString());
            return savedKakao;
        }
        return byEmail.get();
    }

    @Transactional
    public void saveUserInKakao(User user, Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        if(kakao.isPresent()){
            UserDto usersDto = new UserDto(user);

            usersDto.setKakao(kakao.get()); // UserDto 객체에 Kakao 객체 삽입

            user = usersDto.toEntity();

            kakao.get().getUsers().add(user);

            kakaoRepository.save(kakao.get()); // Kakao 객체 DB에 저장
        }
    }
    @Transactional
    public List<User> findUsers(Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        if (kakao.isPresent()){
            List<User> users = kakao.get().getUsers();
            return users;
        }
        return null;
    }
    @Transactional
    public void kakaoDeleteUser(Long kakaoId, Long userId){
        List<User> users = findUsers(kakaoId);
        Optional<User> userById = userRepository.findById(userId);
        if(userById.isPresent()){
            users.remove(userById);
        }
    }
}
