package dev.code_n_roll.wiremock.integrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ChuckNorrisService {

  static final ChuckNorrisFact BACKUP_FACT = new ChuckNorrisFact(-1L, "&quot;It works on my machine&quot; always holds true for Chuck Norris.");

  private final RestTemplate restTemplate;
  private final String url;

  @Autowired
  public ChuckNorrisService(RestTemplate restTemplate, @Value("${fact.url}") String url) {
    this.restTemplate = restTemplate;
    this.url = url;
  }

  public ChuckNorrisFact retrieveFact() {
    try {
      ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(url, ChuckNorrisFactResponse.class);
      return Optional.ofNullable(response.getBody()).map(ChuckNorrisFactResponse::getFact).orElse(BACKUP_FACT);
    } catch (HttpStatusCodeException | ResourceAccessException e){
      return BACKUP_FACT;
    }
  }

}
