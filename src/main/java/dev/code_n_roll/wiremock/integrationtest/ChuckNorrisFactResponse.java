package dev.code_n_roll.wiremock.integrationtest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChuckNorrisFactResponse {

  private final String type;
  private final ChuckNorrisFact fact;

  @JsonCreator
  public ChuckNorrisFactResponse(@JsonProperty("type") String type, @JsonProperty("value") ChuckNorrisFact fact){
    this.type = type;
    this.fact = fact;
  }

  public String getType() {
    return type;
  }

  public ChuckNorrisFact getFact() {
    return fact;
  }

}
