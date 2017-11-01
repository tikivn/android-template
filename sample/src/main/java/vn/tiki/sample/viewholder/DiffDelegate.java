package vn.tiki.sample.viewholder;

public interface DiffDelegate {

  boolean isSame(Object other);

  boolean isContentTheSame(Object other);
}
