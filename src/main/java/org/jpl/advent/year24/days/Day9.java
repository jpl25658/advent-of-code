package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.Comparator;
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

  /* Part 1 */

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


  /* Part 2 */

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

  private Pair<Map<Long, TreeSet<Block>>, LinkedList<File>> parseInput2() {
    var digits = dayDigits();
    LinkedList<File> files = new LinkedList<>();
    Map<Long, TreeSet<Block>> emptyBlocks = new HashMap<>();
    long fileId = 0L;
    long start = 0L;
    boolean bFile = true;
    for (long size : digits) {
      if (size != 0) {
        var block = new Block(start, size);
        if (bFile) {
          files.addLast(new File(fileId++, block));
        } else {
          addEmptyBlock(emptyBlocks, block);
        }
      }
      start += (int) size;
      bFile = !bFile;
    }
    return new Pair<>(emptyBlocks, files);
  }

  private void addEmptyBlock(Map<Long, TreeSet<Block>> emptyBlocks, Block block) {
    var blocksOfSize = emptyBlocks.getOrDefault(block.size(), new TreeSet<>());
    blocksOfSize.add(block);
    emptyBlocks.put(block.size(), blocksOfSize);
  }

  private File moveFile(File file, Map<Long, TreeSet<Block>> emptyBlocks) {
    var entry = emptyBlocks.entrySet().stream()
        .filter(e -> e.getKey() >= file.block().size())
        .filter(e -> !e.getValue().isEmpty())
        .filter(e -> e.getValue().first().start() < file.block().start())
        .min(Map.Entry.comparingByKey());

    if (entry.isEmpty()) {
      return file;
    }

    var blocks = entry.get().getValue();
    var emptyBlock = blocks.pollFirst();
    // if there is spare empty space, keep it
    if (emptyBlock.size() > file.block().size()) {
      var newEmptyBlock = new Block(emptyBlock.start() + file.block().size(), emptyBlock.size() - file.block().size());
      addEmptyBlock(emptyBlocks, newEmptyBlock);
    }
    // move file to emptyBlock
    return new File(file.fileId(), new Block(emptyBlock.start(), file.block().size()));
  }

  private long calcChecksum(File file) {
    return file.fileId() * LongStream.range(file.block().start(), file.block().start() + file.block().size()).sum();
  }

}