package eu.stamp_project.dspot.amplifier;

import eu.stamp_project.dspot.AbstractTestOnSample;
import eu.stamp_project.utils.RandomHelper;
import org.junit.Test;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 19/07/18
 */
public class MethodAdderOnExistingObjectsAmplifierTest extends AbstractTestOnSample {

    @Test
    public void testInLoop() throws Exception {

        /*
            Test that MethodAdderOnExistingObjectsAmplifier amplifier is able to add statement inside a loop if this loop has not brackets
         */

        final String packageName = "fr.inria.statementadd";
        final Factory factory = launcher.getFactory();
        RandomHelper.setSeedRandom(32L);
        MethodAdderOnExistingObjectsAmplifier amplifier = new MethodAdderOnExistingObjectsAmplifier();
        amplifier.reset(factory.Class().get(packageName + ".ClassTarget"));

        CtMethod<?> ctMethod = factory.Class().get(packageName + ".TestClassTarget").getMethodsByName("testWithLoop").get(0);
        // the original body of the loop has one statement
        assertEquals(1,
                ((CtBlock<?>) ctMethod
                        .getElements(new TypeFilter<>(CtForEach.class))
                        .get(0)
                        .getBody()).getStatements().size()
        );
        CtMethod<?> amplifiedMethod = amplifier.amplify(ctMethod, 0).collect(Collectors.toList()).get(0);
        // elements has been added by the amplification: a method call and a local variable (needed to call the method)
        assertEquals(3,
                ((CtBlock<?>) amplifiedMethod
                        .getElements(new TypeFilter<>(CtForEach.class))
                        .get(0)
                        .getBody()).getStatements().size()
        );
    }

    @Test
    public void testOnClassWithJavaObjects() throws Exception {

        /*
            Test that the MethodAdderOnExistingObjectsAmplifier amplifier is able to generate, and manage Collection and Map from java.util
         */

        final String packageName = "fr.inria.statementadd";
        final Factory factory = launcher.getFactory();
        RandomHelper.setSeedRandom(32L);
        MethodAdderOnExistingObjectsAmplifier amplifier = new MethodAdderOnExistingObjectsAmplifier();
        amplifier.reset(factory.Class().get(packageName + ".ClassTarget"));

        CtMethod<?> ctMethod = factory.Class().get(packageName + ".TestClassTarget").getMethodsByName("test").get(0);
        List<CtMethod> amplifiedMethods = amplifier.amplify(ctMethod, 0).collect(Collectors.toList());

        assertEquals(7, amplifiedMethods.size());

        List<String> expectedCalledMethod = Arrays.asList(
                "getList",
                "getSizeOf",
                "getSizeOfTypedCollection",
                "getSizeOfTypedMap"
        );
        assertTrue(amplifiedMethods.stream()
                .allMatch(amplifiedMethod ->
                        amplifiedMethod.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class) {
                            @Override
                            public boolean matches(CtInvocation<?> element) {
                                return expectedCalledMethod.contains(element.getExecutable().getSimpleName());
                            }
                        }).first() != null
                ));
    }

    @Test
    public void testStatementAddOnArrayObjects() throws Exception {
        final String packageName = "fr.inria.statementaddarray";
        final Factory factory = launcher.getFactory();
        RandomHelper.setSeedRandom(32L);
        MethodAdderOnExistingObjectsAmplifier amplifier = new MethodAdderOnExistingObjectsAmplifier();
        amplifier.reset(factory.Class().get(packageName + ".ClassTargetAmplify"));

        CtMethod<?> ctMethod = factory.Class().get(packageName + ".TestClassTargetAmplify").getMethodsByName("test").get(0);
        List<CtMethod> amplifiedMethods = amplifier.amplify(ctMethod, 0).collect(Collectors.toList());

        assertEquals(4, amplifiedMethods.size());

        List<String> expectedCalledMethod = Arrays.asList(
                "methodWithArrayParatemeter",
                "methodWithArrayParatemeterFromDomain",
                "methodWithDomainParameter",
                "methodWithReturn"
        );
        assertTrue(amplifiedMethods.stream()
                .allMatch(amplifiedMethod ->
                        amplifiedMethod.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class) {
                            @Override
                            public boolean matches(CtInvocation<?> element) {
                                return expectedCalledMethod.contains(element.getExecutable().getSimpleName());
                            }
                        }).first() != null
                ));
    }

    @Test
    public void testStatementAddOnUnderTest() throws Exception {
        Factory factory = launcher.getFactory();
        CtClass<Object> ctClass = factory.Class().get("fr.inria.mutation.ClassUnderTestTest");
        RandomHelper.setSeedRandom(23L);

        MethodAdderOnExistingObjectsAmplifier amplifier = new MethodAdderOnExistingObjectsAmplifier();
        amplifier.reset(ctClass);

        CtMethod originalMethod = ctClass.getMethodsByName("testLit").get(0);

        List<CtMethod> amplifiedMethods = amplifier.amplify(originalMethod, 0).collect(Collectors.toList());

        System.out.println(amplifiedMethods);

        assertEquals(2, amplifiedMethods.size());

        List<String> expectedCalledMethod = Arrays.asList(
                "plusOne",
                "minusOne"
        );
        assertTrue(amplifiedMethods.stream()
                .allMatch(amplifiedMethod ->
                        amplifiedMethod.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class) {
                            @Override
                            public boolean matches(CtInvocation<?> element) {
                                return expectedCalledMethod.contains(element.getExecutable().getSimpleName());
                            }
                        }).first() != null
                ));
    }


    @Test
    public void testStatementAdd() throws Exception {

        /*
            Test the StatementAdd amplifier. It reuse existing object to add method call of accessible method.
            It can reuse return value to add method call. It results here with 7 new test cases.
         */

        final String packageName = "fr.inria.statementadd";
        final Factory factory = launcher.getFactory();
        RandomHelper.setSeedRandom(42L);
        MethodAdderOnExistingObjectsAmplifier amplifier = new MethodAdderOnExistingObjectsAmplifier();
        amplifier.reset(factory.Class().get(packageName + ".TestClassTargetAmplify"));

        CtMethod<?> ctMethod = factory.Class().get(packageName + ".TestClassTargetAmplify").getMethodsByName("test").get(0);
        List<CtMethod> amplifiedMethods = amplifier.amplify(ctMethod, 0).collect(Collectors.toList());

        System.out.println(amplifiedMethods);

        assertEquals(5, amplifiedMethods.size());

        List<String> expectedCalledMethod = Arrays.asList(
                "method",
                "methodWithDomainParameter",
                "methodWithPrimitifParameters",
                "methodWithPrimitifParameters",
                "methodWithReturn"
        );
        assertTrue(amplifiedMethods.stream()
                .allMatch(amplifiedMethod ->
                        amplifiedMethod.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class) {
                            @Override
                            public boolean matches(CtInvocation<?> element) {
                                return expectedCalledMethod.contains(element.getExecutable().getSimpleName());
                            }
                        }).first() != null
                ));
    }

}
