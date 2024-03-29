package dev.code_n_roll.wiremock.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ChuckNorrisServiceIntegrationTest.WireMockPortInitializer.class)
@TestPropertySource(properties = {
    "fact.url=http://localhost:${wiremock.port}/jokes/random"
})
public class ChuckNorrisServiceIntegrationTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @ClassRule
  public static WireMockRule wireMockRule = new WireMockRule();

  @Autowired
  private ChuckNorrisService service;

  /**
   * The initializer makes it easier to refer to the dynamic WireMock port from @TestPropertySource.
   */
  static class WireMockPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of("wiremock.port:" + wireMockRule.port())
          .applyTo(configurableApplicationContext);
    }
  }

  @Test
  public void shouldReturnFactSuccessful() throws JsonProcessingException {
    ChuckNorrisFact fact = new ChuckNorrisFact(1L, "joke");
    configureWireMockForOkResponse(fact);

    ChuckNorrisFact retrieved = service.retrieveFact();

    assertThat(retrieved).isEqualTo(fact);
  }

  public void configureWireMockForOkResponse(ChuckNorrisFact fact) throws JsonProcessingException {
    ChuckNorrisFactResponse chuckNorrisFactResponse = new ChuckNorrisFactResponse("success", fact);
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(okJson(OBJECT_MAPPER.writeValueAsString(chuckNorrisFactResponse))));
  }

  @Test
  public void shouldReturnBackupFactInCaseOfError() {
    configureWireMockForErrorResponse();

    ChuckNorrisFact retrieved = service.retrieveFact();

    assertThat(retrieved).isEqualTo(ChuckNorrisService.BACKUP_FACT);
  }

  private void configureWireMockForErrorResponse() {
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(serverError()));
  }

  @Test
  public void shouldReturnBackupFactInCaseOfTimeout() throws JsonProcessingException {
    configureWireMockForSlowResponse();

    ChuckNorrisFact retrieved = service.retrieveFact();

    assertThat(retrieved).isEqualTo(ChuckNorrisService.BACKUP_FACT);
  }

  private void configureWireMockForSlowResponse() throws JsonProcessingException {
    ChuckNorrisFactResponse chuckNorrisFactResponse = new ChuckNorrisFactResponse("success", new ChuckNorrisFact(1L, ""));
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(
            okJson(OBJECT_MAPPER.writeValueAsString(chuckNorrisFactResponse))
                .withFixedDelay((int) Duration.ofSeconds(10L).toMillis())));
  }
}