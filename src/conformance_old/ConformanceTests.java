package conformance_old;

import test.*;

/** Runs all conformance tests on distributed filesystem components.

    <p>
    Tests performed are:
    <ul>
    <li>{@link conformance_old.rmi.SkeletonTest}</li>
    <li>{@link conformance_old.rmi.StubTest}</li>
    <li>{@link conformance_old.rmi.ConnectionTest}</li>
    <li>{@link conformance_old.rmi.ThreadTest}</li>
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
            new Class[] {conformance_old.rmi.CallTest.class,
                         conformance_old.rmi.ArgumentTest.class,
                         conformance_old.rmi.ReturnTest.class,
                         conformance_old.rmi.ExceptionTest.class,
                         conformance_old.rmi.CompleteCallTest.class,
                         conformance_old.rmi.ImplicitStubCallTest.class,
                         conformance_old.rmi.NullTest.class,
                         conformance_old.rmi.RemoteInterfaceTest.class,
                         conformance_old.rmi.ListenTest.class,
                         conformance_old.rmi.RestartTest.class,
                         conformance_old.rmi.NoAddressTest.class,
                         conformance_old.rmi.ServiceErrorTest.class,
                         conformance_old.rmi.StubTest.class,
                         conformance_old.rmi.EqualsTest.class,
                         conformance_old.rmi.HashCodeTest.class,
                         conformance_old.rmi.ToStringTest.class,
                         conformance_old.rmi.SerializableTest.class,
                         conformance_old.rmi.OverloadTest.class,
                         conformance_old.rmi.ShadowTest.class,
                         conformance_old.rmi.InheritanceTest.class,
                         conformance_old.rmi.SubclassTest.class,
                         conformance_old.rmi.SecurityTest.class,
                         conformance_old.rmi.ThreadTest.class};

        Series                      series = new Series(tests);
        SeriesReport                report = series.run(3, System.out);

        // Print the report and exit with an appropriate exit status.
        report.print(System.out);
        System.exit(report.successful() ? 0 : 2);
    }
}
