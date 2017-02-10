package conformance_basic;

import test.*;

/** Runs all conformance tests on distributed filesystem components.

    <p>
    Tests performed are:
    <ul>
    <li>{@link conformance_basic.rmi.SkeletonTest}</li>
    <li>{@link conformance_basic.rmi.StubTest}</li>
    <li>{@link conformance_basic.rmi.ConnectionTest}</li>
    <li>{@link conformance_basic.rmi.ThreadTest}</li>
    </ul>
 */
public class ConformanceTests
{
    /** Runs the tests.

        @param arguments Ignored.
     */
    public static void main(String[] arguments)
    {
        // Create the test list, the series object, and run the test series.
        @SuppressWarnings("unchecked")
        Class<? extends Test>[]     tests =
            new Class[] {conformance_basic.rmi.SkeletonTest.class, conformance_basic.rmi.StubTest.class,
                conformance_basic.rmi.ConnectionTest.class, conformance_basic.rmi.ThreadTest.class};

        Series                      series = new Series(tests);
        SeriesReport                report = series.run(3, System.out);

        // Print the report and exit with an appropriate exit status.
        report.print(System.out);
        System.exit(report.successful() ? 0 : 2);
    }
}
