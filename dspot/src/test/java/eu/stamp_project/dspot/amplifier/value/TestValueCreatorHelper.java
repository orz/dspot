package eu.stamp_project.dspot.amplifier.value;

import eu.stamp_project.dspot.AbstractTestOnSample;
import org.junit.Test;
import spoon.reflect.factory.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 10/10/17
 */
public class TestValueCreatorHelper extends AbstractTestOnSample {

    @Test
    public void testCanGenerateValueForObjectThatAreChained() {

        /*
            test that we can check that we can generate a value for an object A that need am Object B, while be need an Object A
         */

        final Factory factory = launcher.getFactory();
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Class().get("fr.inria.linkedobjects.ObjectA").getReference()));
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Class().get("fr.inria.linkedobjects.ObjectB").getReference()));
    }

    @Test
    public void testCanGenerateValueFor() throws Exception {
        final Factory factory = launcher.getFactory();
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Class().get(Integer.class).getReference()));
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Type().createReference(List.class)));
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Class().get("fr.inria.inheritance.InheritanceSource").getReference()));
        assertFalse(ValueCreatorHelper.canGenerateAValueForType(factory.Class().get("fr.inria.inheritance.Inherited").getReference()));
        assertFalse(ValueCreatorHelper.canGenerateAValueForType(factory.Type().createReference(Iterator.class)));
        ArrayList<? extends Object> list = new ArrayList<>();
        assertTrue(ValueCreatorHelper.canGenerateAValueForType(factory.Type().createReference(list.getClass())));
    }
}
