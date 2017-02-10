package conformance;

import test.*;

/** Runs all conformance tests on distributed filesystem components.

    <p>
    Tests performed are:
    <ul>
    <li>{@link conformance.rmi.SkeletonTest}</li>
    <li>{@link conformance.rmi.StubTest}</li>
    <li>{@link conformance.rmi.ConnectionTest}</li>
    <li>{@link conformance.rmi.ThreadTest}</li>
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
            new Class[] {
                         conformance.rmi.CallTest.class, // passed
                         conformance.rmi.ArgumentTest.class, // passed
                         conformance.rmi.ReturnTest.class, // passed
                         conformance.rmi.ExceptionTest.class, // passed
                         conformance.rmi.CompleteCallTest.class, // passed
                         conformance.rmi.ImplicitStubCallTest.class, // passed
                         conformance.rmi.NullTest.class, // passed
                         conformance.rmi.RemoteInterfaceTest.class, // passed
                         conformance.rmi.ListenTest.class, // passed
                         conformance.rmi.RestartTest.class, // sometimes doesn't pass
                         conformance.rmi.NoAddressTest.class, // passed
                         conformance.rmi.ServiceErrorTest.class, // passed
                         conformance.rmi.StubTest.class, // sometimes doesn't pass
                         conformance.rmi.EqualsTest.class, // passed
                         conformance.rmi.HashCodeTest.class, // passed
                         conformance.rmi.ToStringTest.class, // passed
                         conformance.rmi.SerializableTest.class // passed
//                         conformance.rmi.OverloadTest.class,
//                         conformance.rmi.ShadowTest.class,
//                         conformance.rmi.InheritanceTest.class,
//                         conformance.rmi.SubclassTest.class,
//                         conformance.rmi.SecurityTest.class,
//                         conformance.rmi.ThreadTest.class
            };

        Series                      series = new Series(tests);
        SeriesReport                report = series.run(3, System.out);

        // Print the report and exit with an appropriate exit status.
        report.print(System.out);
        System.exit(report.successful() ? 0 : 2);
    }
}
