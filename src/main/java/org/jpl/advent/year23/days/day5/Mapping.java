package org.jpl.advent.year23.days.day5;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class Mapping {

  private Long dest;
  private Long src;
  private Long range;

  public Optional<Long> map(Long value) {
    return (value >= src && value < src + range)
        ? Optional.of(dest + value - src)
        : Optional.empty();
  }
}
