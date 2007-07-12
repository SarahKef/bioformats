//
// CrosshairStrategy.java
//

package loci.formats.cache;

/**
 * A crosshair strategy caches planes extending from the the current
 * dimensional position along each individual axis, but not combinations of
 * axes. For example, if the current position is Z5-C2-T18, the strategy will
 * preload the next and previous focal planes (Z6-C2-T18 and Z4-C2-T18),
 * the next and previous channels (Z5-C3-T18 and Z5-C1-T18),
 * and the next and previous time points (Z5-C2-T19 and Z5-C2-T17),
 * but nothing diverging on multiple axes (e.g., Z6-C3-T19 or Z4-C1-T17).
 * <p>
 * Planes closest to the current position are loaded first, with axes
 * prioritized according to the cache strategy's priority settings.
 * <p>
 * To illustrate the crosshair strategy, here is a diagram showing a case
 * in 2D with 56 dimensional positions (7Z x 8T). For both Z and T, order is
 * centered, range is 2, and priority is normal.
 * The numbers indicate the order planes will be cached, with "0"
 * corresponding to the current dimensional position.
 * <pre>
 *      T  0  1  2  3  4  5  6  7
 *    Z /------------------------
 *    0 |
 *    1 |           8
 *    2 |           4
 *    3 |     7  3  0  1  5
 *    4 |           2
 *    5 |           6
 *    6 |
 * </pre>
 */
public class CrosshairStrategy extends CacheStrategy {

  // -- Constructor --

  /** Constructs a crosshair strategy. */
  public CrosshairStrategy(int[] lengths) { super(lengths); }

  // -- CacheStrategy API methods --

  /* @see CacheStrategy#getPossiblePositions() */
  protected int[][] getPossiblePositions() {
    // only positions diverging along a single axis can ever be cached
    int len = 1;
    for (int i=0; i<lengths.length; i++) len += lengths[i] - 1;
    int[][] p = new int[len][lengths.length];
    for (int i=0, c=0; i<lengths.length; i++) {
      for (int j=1; j<lengths[i]; j++) {
        p[++c][i] = j;
      }
    }
    return p;
  }

}
