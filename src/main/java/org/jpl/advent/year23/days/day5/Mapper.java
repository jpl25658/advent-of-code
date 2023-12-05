package org.jpl.advent.year23.days.day5;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Builder
@Getter
public class Mapper {

  private final String source;
  private final String destination;
  private final List<Mapping> mappings;

  public Long map(Long value) {
    return mappings.stream()
        .map(m -> m.map(value))
        .filter(Optional::isPresent)
        .findFirst()
        .orElse(Optional.empty())
        .orElse(value);
  }
}
