package dev.code_n_roll.wiremock.integrationtest;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChuckNorrisServiceTest {

  /**
   * This test matches with the real world conditions but it's only the good case
   */
  @Test
  public void shouldReturnFactSuccessful() {
    String url = "http://localhost:8080";
    RestTemplate mockTemplate = mock(RestTemplate.class);
    ChuckNorrisFact fact = new ChuckNorrisFact(1L, "joke");
    ChuckNorrisFactResponse chuckNorrisFactResponse = new ChuckNorrisFactResponse("success", fact);
    ResponseEntity<ChuckNorrisFactResponse> responseEntity = new ResponseEntity<>(chuckNorrisFactResponse, HttpStatus.OK);
    when(mockTemplate.getForEntity(url, ChuckNorrisFactResponse.class)).thenReturn(responseEntity);
    var service = new ChuckNorrisService(mockTemplate, url);

    ChuckNorrisFact retrieved = service.retrieveFact();

    assertThat(retrieved).isEqualTo(fact);
  }

  /**
   * This test does not match the real world conditions. The RestTemplate would throw an exception.
   */
  @Test
  public void shouldReturnNullInCaseOfError() {
    String url = "http://localhost:8080";
    RestTemplate mockTemplate = mock(RestTemplate.class);
    ResponseEntity<ChuckNorrisFactResponse> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    when(mockTemplate.getForEntity(url, ChuckNorrisFactResponse.class)).thenReturn(responseEntity);
    var service = new ChuckNorrisService(mockTemplate, url);

    ChuckNorrisFact retrieved = service.retrieveFact();

    assertThat(retrieved).isNull();
  }
}