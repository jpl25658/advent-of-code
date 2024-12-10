package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

  //<editor-fold desc="Part 1">
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

  private static final int EMPTY_BLOCK = -1;

  private int[] parseInput() {
    var digits = dayDigits();
    List<Integer> disk = new ArrayList<>();
    int fileId = 0;
    boolean bFile = true;
    for (long digit : digits) {
      for (var n = 0; n < digit; n++) {
        disk.add(bFile ? fileId : EMPTY_BLOCK);
      }
      if (bFile) {
        fileId++;
      }
      bFile = !bFile;
    }
    return disk.stream().mapToInt(Integer::intValue).toArray();
  }
  //</editor-fold>

  //<editor-fold desc="Part 2">
  @Override
  public Object part2() {
    var input = parseInput2();
    var emptyBlocks = input.a();
    var files = input.b();

    return files.stream()
        .sorted(Comparator.comparing(File::fileId).reversed())
        .map(file -> moveFile(file, emptyBlocks))
        .mapToLong(this::calcChecksum)
        .sum();
  }

  private record Block(long start, long size) implements Comparable<Block> {
    @Override
    public int compareTo(Block other) {
      return (int) (start - other.start);
    }
  }

  private record File(long fileId, Block block) {
  }

  private Pair<TreeSet<Block>, List<File>> parseInput2() {
    List<File> files = new ArrayList<>();
    TreeSet<Block> emptyBlocks = new TreeSet<>();
    long fileId = 0L;
    long start = 0L;
    boolean bFile = true;
    for (long size : dayDigits()) {
      var block = new Block(start, size);
      start += size;
      if (bFile) {
        files.add(new File(fileId++, block));
      } else {
        if (size != 0) {
          emptyBlocks.add(block);
        }
      }
      bFile = !bFile;
    }
    return new Pair<>(emptyBlocks, files);
  }

  private File moveFile(File file, TreeSet<Block> emptyBlocks) {
    var block = emptyBlocks.stream()
        .filter(b -> b.size() >= file.block().size())
        .filter(b -> b.start() < file.block().start())
        .findFirst();

    if (block.isEmpty()) {
      return file;
    }

    var empty = block.get();
    emptyBlocks.remove(empty);
    if (empty.size() > file.block().size()) {
      emptyBlocks.add(new Block(empty.start() + file.block().size(), empty.size() - file.block().size()));
    }
    return new File(file.fileId(), new Block(empty.start(), file.block().size()));
  }

  private long calcChecksum(File file) {
    return file.fileId() * LongStream.range(file.block().start(), file.block().start() + file.block().size()).sum();
  }
  //</editor-fold>

}