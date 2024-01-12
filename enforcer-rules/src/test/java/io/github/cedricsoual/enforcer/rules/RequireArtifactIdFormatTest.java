package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.MavenProjectTestBuilder;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RequireArtifactIdFormatTest {

    @Test
    void execute() {
        MavenProject project = MavenProjectTestBuilder.build();
        Assertions.assertThatNoException().isThrownBy(() -> new RequireArtifactIdFormat(project).execute());
    }

    @Test
    void executeWhenFails() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .artifactId("&@()^")
                .build();
        Assertions.assertThatThrownBy(() -> new RequireArtifactIdFormat(project).execute())
                .isInstanceOf(EnforcerRuleException.class);
    }

}