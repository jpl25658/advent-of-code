package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.LongStream;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year24.Day2024;

public class Day9 extends Day2024 {
  public Day9() {
    super(9);
  }

  public static void main(String[] args) {
    new Day9().printParts();
  }

  @Override
  public Object part1() {
    var disk = parseInput();

    var start = 0;
    var end = disk.length - 1;
    while (start < end) {
      while (disk[start] != EMPTY_BLOCK) start++;
      while (disk[end] == EMPTY_BLOCK) end--;
      if (start < end) {
        disk[start] = disk[end];
        disk[end] = EMPTY_BLOCK;
      }
    }

    long checksum = 0L;
    for (var i = 0; i < disk.length; i++) {
      if (disk[i] != EMPTY_BLOCK) {
        checksum += ((long) disk[i] * i);
      }
    }
    return checksum;
  }

  @Override
  public Object part2() {
    var input = parseInput2();
    var empty = input.a();
    var files = input.b();

    for (var i = files.size() - 1; i >= 0; i--) {
      Block file = files.get(i);
      var optCandidate = empty.entrySet().stream()
          .filter(entry -> entry.getKey() >= file.size())
          .filter(entry -> !entry.getValue().isEmpty())
          .filter(entry -> entry.getValue().first().start() < file.start())
          .findFirst();
      if (optCandidate.isPresent()) {
        var candidate = optCandidate.get();
        var emptyBlocks = candidate.getValue();
        var first = emptyBlocks.pollFirst();
        if (first != null) {
          files.set(i, new Block(first.start(), file.fileId(), file.size()));
          if (first.size() > file.size()) {
            var remaining = new Block(first.start() + file.size(), Block.NO_FILE, first.size() - file.size());
            var set = empty.getOrDefault(remaining.size(), new TreeSet<>());
            set.add(remaining);
            empty.put(remaining.size(), set);
          }
        }
      }
    }

    //var filessorted = files.stream().sorted(Comparator.comparing(Block::start)).toList();

    long result = 0L;
    for (Block file : files) {
      long sum = LongStream.range(file.start(), file.start() + file.size())
          .sum();
      result += sum * file.fileId();
    }
    return result;
  }

  private static final int EMPTY_BLOCK = -1;

  private int[] parseInput() {
    var digits = dayDigits();
    List<Integer> disk = new ArrayList<>();
    int fileId = 0;
    boolean bFile = true;
    for (var i = 0; i < digits.length; i++) {
      long digit = digits[i];
      for (var n = 0; n < digits[i]; n++) {
        disk.add(bFile ? fileId : EMPTY_BLOCK);
      }
      if (bFile) {
        fileId++;
      }
      bFile = !bFile;
    }
    return disk.stream().mapToInt(Integer::intValue).toArray();
  }

  private record Block(long start, long fileId, long size) implements Comparable<Block> {
    private static final long NO_FILE = -1L;

    @Override
    public int compareTo(Block o) {
      return (int) (start - o.start);
    }
  }

  private Pair<Map<Long, TreeSet<Block>>, LinkedList<Block>> parseInput2() {
    var digits = dayDigits();
    LinkedList<Block> files = new LinkedList<>();
    Map<Long, TreeSet<Block>> empty = new HashMap<>();
    long fileId = 0L;
    long start = 0L;
    boolean bFile = true;
    for (long size : digits) {
      if (size != 0) {
        if (bFile) {
          files.addLast(new Block(start, fileId, size));
          fileId++;
        } else {
          var set = empty.getOrDefault(size, new TreeSet<>());
          set.add(new Block(start, Block.NO_FILE, size));
          empty.put(size, set);
        }
      }
      start += (int) size;
      bFile = !bFile;
    }
    return new Pair<>(empty, files);
  }
}
