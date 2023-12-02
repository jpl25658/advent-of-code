package org.jpl.advent.year23.days;

import static java.util.Arrays.stream;
import org.jpl.advent.year23.Day2023;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


public class Day2 extends Day2023 {

  public Day2() {
    super(2);
  }

  public static void main(String[] args) {
    new Day2().printParts();
  }

  record Game(int gameId, List<GameRound> rounds) {}
  record GameRound(List<Cube> cubes) {}
  record Cube(String color, int num) {}
  record MaxCubes(int red, int green, int blue) {}

  private static class Parser {
    public static Game parseGame(String game) {
      String[] gameParts = game.split(": ");
      int id = Integer.parseInt(gameParts[0].split(" ")[1]);
      List<GameRound> rounds = stream(gameParts[1].split("; ")).map(Parser::parseGameRound).toList();
      return new Game(id, rounds);
    }

    private static GameRound parseGameRound(String round) {
      List<Cube> cubes = stream(round.split(", ")).map(Parser::parseCube).toList();
      return new GameRound(cubes);
    }

    private static Cube parseCube(String cube) {
      String[] cubeParts = cube.split(" ");
      int num = Integer.parseInt(cubeParts[0]);
      String color = cubeParts[1];
      return new Cube(color, num);
    }
  }

  @Override
  public Object part1() {

    final Map<String, Integer> AVAILABLE_CUBES = Map.of("red", 12, "green", 13, "blue", 14);

    Predicate<Cube> isCubePossible = cube -> cube.num <= AVAILABLE_CUBES.get(cube.color);
    Predicate<GameRound> isGameRoundPossible = round -> round.cubes.stream().allMatch(isCubePossible);
    Predicate<Game> isGamePossible = game -> game.rounds.stream().allMatch(isGameRoundPossible);

    return dayStream()
        .map(Parser::parseGame)
        .filter(isGamePossible)
        .mapToInt(game -> game.gameId)
        .sum();

  }

  @Override
  public Object part2() {

    Function<Game, MaxCubes> calcMaximumCubes = game -> {
      Map<String, Integer> map = game.rounds.stream().flatMap(round -> round.cubes.stream())
          .collect(Collectors.toMap(Cube::color, Cube::num, Integer::max));
      return new MaxCubes(map.getOrDefault("red", 0), map.getOrDefault("green", 0), map.getOrDefault("blue", 0));
    };
    ToIntFunction<MaxCubes> calcGamePower = maxCubes -> maxCubes.red * maxCubes.green * maxCubes.blue;

    return dayStream()
        .map(Parser::parseGame)
        .map(calcMaximumCubes)
        .mapToInt(calcGamePower)
        .sum();

  }

}
