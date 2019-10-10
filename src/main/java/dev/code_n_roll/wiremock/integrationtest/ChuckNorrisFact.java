package dev.code_n_roll.wiremock.integrationtest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ChuckNorrisFact {
  private final Long id;
  private final String joke;

  public ChuckNorrisFact(@JsonProperty("id") Long id, @JsonProperty("joke") String joke) {
    this.id = id;
    this.joke = joke;
  }

  public Long getId() {
    return id;
  }

  public String getJoke() {
    return joke;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChuckNorrisFact that = (ChuckNorrisFact) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(joke, that.joke);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, joke);
  }

  @Override
  public String toString() {
    return "ChuckNorrisFact{" +
        "id=" + id +
        ", joke='" + joke + '\'' +
        '}';
  }
}
