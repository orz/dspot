package eu.stamp_project.utils.options;

import eu.stamp_project.automaticbuilder.AutomaticBuilder;
import eu.stamp_project.dspot.selector.ChangeDetectorSelector;
import eu.stamp_project.dspot.selector.JacocoCoverageSelector;
import eu.stamp_project.dspot.selector.PitMutantScoreSelector;
import eu.stamp_project.dspot.selector.TakeAllSelector;
import eu.stamp_project.dspot.selector.TestSelector;
import eu.stamp_project.utils.program.InputConfiguration;

public enum SelectorEnum {
    PitMutantScoreSelector {
        @Override
        public TestSelector buildSelector(AutomaticBuilder builder, InputConfiguration configuration) {
            return new PitMutantScoreSelector(builder, configuration);
        }
    },
    JacocoCoverageSelector {
        @Override
        public TestSelector buildSelector(AutomaticBuilder builder, InputConfiguration configuration) {
            return new JacocoCoverageSelector(builder, configuration);
        }
    },
    TakeAllSelector {
        @Override
        public TestSelector buildSelector(AutomaticBuilder builder, InputConfiguration configuration) {
            return new TakeAllSelector(builder, configuration);
        }
    },
    ChangeDetectorSelector {
        @Override
        public TestSelector buildSelector(AutomaticBuilder builder, InputConfiguration configuration) {
            return new ChangeDetectorSelector(builder, configuration);
        }
    };

    public abstract TestSelector buildSelector(AutomaticBuilder builder, InputConfiguration configuration);

}
