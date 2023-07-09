package com.techelevator.tebucks.services;

import com.techelevator.tebucks.model.ReturnUser;
import com.techelevator.tebucks.model.TearsLoginUser;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;



@Component
public class AuthenticationService {

    private static final String API_BASE_URL = "https://te-pgh-api.azurewebsites.net";
    private final RestTemplate restTemplate = new RestTemplate();


    public ReturnUser login(TearsLoginUser loginUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);;
        HttpEntity<TearsLoginUser> entity = new HttpEntity<>(loginUser, headers);

        try {
            ResponseEntity<ReturnUser> response = restTemplate.exchange(API_BASE_URL + "/api/Login", HttpMethod.POST, entity, ReturnUser.class);
            return response.getBody();

        } catch (RestClientResponseException e) {

        } catch (ResourceAccessException e) {
            throw new ResourceAccessException(e.getMessage());
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to Authenticate");
        }
        return null;
    }


}
