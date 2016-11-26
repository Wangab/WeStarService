package com.wangab.dao;

import com.wangab.entity.po.EasemobPO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanganbang on 11/26/16.
 */
@Repository("easemobDAO")
public class EasemobDAO {
    @Value("${application.easemob.user-url}")
    private String addUserUrl;
    @Value("${application.easemob.token-url}")
    private String token_url;
    @Value("${application.easemob.client-id}")
    private String client_id;
    @Value("${application.easemob.client-secret}")
    private String client_secret;

    @Autowired
    private RestTemplate restTemplate;

    public String getToken() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        Map<String, Object> params = new HashMap<String, Object>();
            params.put("grant_type", "client_credentials");
            params.put("client_id", client_id);
            params.put("client_secret", client_secret);

        JSONObject jsonObject = new JSONObject(params);

        HttpEntity<String> formEnty = new HttpEntity<>(jsonObject.toString(), headers);
        URI url = new URI(token_url);
        String result = restTemplate.postForObject(url, formEnty, String.class);
        String accessToken = new JSONObject(result).getString("access_token");
        return accessToken;
    }

    public String addUser(EasemobPO easemobPO) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = easemobPO.toJsonObject();

        HttpEntity<String> formEnty = new HttpEntity<>(jsonObject.toString(), headers);
        URI url = new URI(addUserUrl);
        String result = restTemplate.postForObject(url, formEnty, String.class);
        return result;
    }

    public String getAddUserUrl() {
        return addUserUrl;
    }

    public void setAddUserUrl(String addUserUrl) {
        this.addUserUrl = addUserUrl;
    }

    public String getToken_url() {
        return token_url;
    }

    public void setToken_url(String token_url) {
        this.token_url = token_url;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
