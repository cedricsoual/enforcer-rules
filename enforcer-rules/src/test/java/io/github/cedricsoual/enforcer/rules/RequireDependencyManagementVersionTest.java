package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.DependencyTestBuilder;
import io.github.cedricsoual.enforcer.utils.MavenProjectTestBuilder;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RequireDependencyManagementVersionTest {

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
                                .version("1.0.0")
                                .build()
                )).build();

        Assertions.assertThatNoException().isThrownBy(() -> new RequireDependencyManagementVersion(project).execute());
    }

    @Test
    void executeWhenArtifactIgnored() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                ))
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> {
            RequireDependencyManagementVersion requireDependencyManagementVersion = new RequireDependencyManagementVersion(project);
            requireDependencyManagementVersion.ignoreArtifacts = List.of("io.github.cedricsoual.enforcer.rules:enforcer-rules");
            requireDependencyManagementVersion.execute();
        });
    }

    @Test
    void executeWhenGroupIdIgnored() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                ))
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> {
            RequireDependencyManagementVersion requireDependencyManagementVersion = new RequireDependencyManagementVersion(project);
            requireDependencyManagementVersion.ignoreGroupIds = List.of("io.github.cedricsoual.enforcer.rules");
            requireDependencyManagementVersion.execute();
        });
    }

    @Test
    void executeWhenScopeIgnored() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .scope("test")
                                .build()
                ))
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> {
            RequireDependencyManagementVersion requireDependencyManagementVersion = new RequireDependencyManagementVersion(project);
            requireDependencyManagementVersion.ignoreScopes = List.of("test");
            requireDependencyManagementVersion.execute();
        });
    }

    @Test
    void executeWhenFailsOnMissingDependencyManagement() {
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependencies(List.of(
                        DependencyTestBuilder.builder()
                                .groupId("io.github.cedricsoual.enforcer.rules")
                                .artifactId("enforcer-rules")
                                .version("1.0.0")
                                .build()
                ))
                .build();
        Assertions.assertThatThrownBy(() -> new RequireDependencyManagementVersion(project).execute());
    }

    @Test
    void executeWhenFailsOnDifferentVersion() {
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
        Assertions.assertThatThrownBy(() -> new RequireDependencyManagementVersion(project).execute());
    }

    @Test
    void executeWhenMatchVersionDisabledAndDifferentVersion() {
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
        Assertions.assertThatNoException().isThrownBy(() -> {
            RequireDependencyManagementVersion requireDependencyManagementVersion = new RequireDependencyManagementVersion(project);
            requireDependencyManagementVersion.versionMatch = false;
            requireDependencyManagementVersion.execute();
        });
    }

}