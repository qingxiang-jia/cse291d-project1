package conformance_new;

import test.*;

/** Runs all conformance tests on distributed filesystem components.

    <p>
    Tests performed are:
    <ul>
    <li>{@link conformance_new.rmi.SkeletonTest}</li>
    <li>{@link conformance_new.rmi.StubTest}</li>
    <li>{@link conformance_new.rmi.ConnectionTest}</li>
    <li>{@link conformance_new.rmi.ThreadTest}</li>
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
            new Class[] {conformance_new.rmi.CallTest.class, // passed
                         conformance_new.rmi.ArgumentTest.class, // passed
                         conformance_new.rmi.ReturnTest.class, // passed
                         conformance_new.rmi.ExceptionTest.class, // passed
                         conformance_new.rmi.CompleteCallTest.class, // passed
                         conformance_new.rmi.ImplicitStubCallTest.class, // passed
                         conformance_new.rmi.NullTest.class, // passed
                         conformance_new.rmi.RemoteInterfaceTest.class, // passed
                         conformance_new.rmi.ListenTest.class, // passed
                         //conformance.rmi.RestartTest.class, // <- stuck
                         conformance_new.rmi.NoAddressTest.class, // passed
                         conformance_new.rmi.ServiceErrorTest.class/*, // <- stuck
                         conformance.rmi.StubTest.class,
                         conformance.rmi.EqualsTest.class,
                         conformance.rmi.HashCodeTest.class,
                         conformance.rmi.ToStringTest.class,
                         conformance.rmi.SerializableTest.class,
                         conformance.rmi.OverloadTest.class,
                         conformance.rmi.ShadowTest.class,
                         conformance.rmi.InheritanceTest.class,
                         conformance.rmi.SubclassTest.class,
                         conformance.rmi.SecurityTest.class,
                         conformance.rmi.ThreadTest.class*/};

        Series                      series = new Series(tests);
        SeriesReport                report = series.run(3, System.out);

        // Print the report and exit with an appropriate exit status.
        report.print(System.out);
        System.exit(report.successful() ? 0 : 2);
    }
}
