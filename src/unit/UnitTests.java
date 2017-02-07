package unit;

import test.Series;
import test.SeriesReport;
import test.Test;
import unit.rmi.SampleUnitTest;

/**
 * Runs all unit tests.
 * <p>
 * <p>
 * Tests run are:
 * <ul>
 * <li>{@link SampleUnitTest}</li>
 * </ul>
 */
public class UnitTests {
  /**
   * Runs the tests.
   *
   * @param arguments Ignored.
   */
  public static void main(String[] arguments) {
    // Create the test list, the series object, and run the test series.
    @SuppressWarnings("unchecked")
    Class<? extends Test>[] tests = new Class[] {SampleUnitTest.class};
    Series series = new Series(tests);
    SeriesReport report = series.run(3, System.out);

    // Print the report and exit with an appropriate exit status.
    report.print(System.out);
    System.exit(report.successful() ? 0 : 2);
  }
}
