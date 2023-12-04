package org.jpl.advent.common;

import java.util.function.BiFunction;

public record Pair<A, B>(A a, B b) implements Comparable<Pair<A, B>> {

  public static <A, B> Pair<A, B> pair(A a, B b) {
    return new Pair<>(a, b);
  }

  public A getLeft() {
    return a;
  }

  public B getRight() {
    return b;
  }

  public <C, D> Pair<C, D> map(BiFunction<A, B, Pair<C, D>> func) {
    return func.apply(a(), b());
  }

  @Override
  public int compareTo(Pair<A, B> t) {
    if (a instanceof Comparable && t.a instanceof Comparable) {
      return ((Comparable) a).compareTo(t.a);
    }
    return 0;
  }
}
