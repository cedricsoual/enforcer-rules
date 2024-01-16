package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.DependencyTestBuilder;
import io.github.cedricsoual.enforcer.utils.MavenProjectTestBuilder;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BanRedundantDependencyVersionTest {

    @Test
    void execute() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                ))
                .dependencyManagement(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.1")
                                .build()
                )).build();

        Assertions.assertThatNoException().isThrownBy(() -> new BanRedundantDependencyVersion(project).execute());
    }

    @Test
    void executeWhenFails() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                ))
                .dependencyManagement(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                )).build();
        Assertions.assertThatThrownBy(() -> new BanRedundantDependencyVersion(project).execute());
    }
}