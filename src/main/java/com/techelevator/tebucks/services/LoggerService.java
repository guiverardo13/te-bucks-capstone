package com.techelevator.tebucks.services;

import com.techelevator.tebucks.dao.JdbcErrorLogDao;
import com.techelevator.tebucks.model.TearsLoginUser;
import com.techelevator.tebucks.model.ReturnUser;
import com.techelevator.tebucks.model.TxLog;
import com.techelevator.tebucks.model.TxLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class LoggerService {

    private static final String API_BASE_URL = "https://te-pgh-api.azurewebsites.net/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticationService authenticationService = new AuthenticationService();
    private static final TearsLoginUser LOGIN_USER = new TearsLoginUser("Team<01>", "password");
    @Autowired
    private JdbcErrorLogDao jdbcErrorLogDao;

    public TxLog logTransaction (TxLogDTO txLogDTO) {
        ReturnUser authenticatedUser = authenticationService.login(LOGIN_USER);
        String token = authenticatedUser.getToken();
        HttpHeaders newHeader = new HttpHeaders();
        newHeader.setContentType(MediaType.APPLICATION_JSON);
        newHeader.setBearerAuth(token);
        HttpEntity<TxLogDTO> entity = new HttpEntity<>(txLogDTO, newHeader);

        try {
            return restTemplate.postForObject(API_BASE_URL + "api/TxLog", entity, TxLog.class);

        } catch (RestClientResponseException e) {

            txLogDTO.setDescription(e.getMessage());
            jdbcErrorLogDao.createLoggerExceptionLog(txLogDTO);
        }
        return null;
    }
}
