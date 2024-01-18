package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.DependencyTestBuilder;
import io.github.cedricsoual.enforcer.utils.MavenProjectTestBuilder;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BanRedundantDependencyVersionTest {

    @Test
    void execute() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.1")
                .versionLineNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> new BanRedundantDependencyVersion(project).execute());
    }
    @Test
    void executeWhenSameVersionAndLocation() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> new BanRedundantDependencyVersion(project).execute());
    }

    @Test
    void executeWhenArtifactIsIgnored() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .versionLineNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        BanRedundantDependencyVersion banRedundantDependencyVersion = new BanRedundantDependencyVersion(project);
        banRedundantDependencyVersion.ignoreArtifacts = List.of("io.github.cedricsoual.enforcer.rules:enforcer-rules");
        Assertions.assertThatNoException().isThrownBy(banRedundantDependencyVersion::execute);
    }
    @Test
    void executeWhenGroupIdIsIgnored() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .versionLineNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        BanRedundantDependencyVersion banRedundantDependencyVersion = new BanRedundantDependencyVersion(project);
        banRedundantDependencyVersion.ignoreGroupIds = List.of("io.github.cedricsoual.enforcer.rules");
        Assertions.assertThatNoException().isThrownBy(banRedundantDependencyVersion::execute);
    }
    @Test
    void executeWhenScopeIsIgnored() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .versionLineNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        BanRedundantDependencyVersion banRedundantDependencyVersion = new BanRedundantDependencyVersion(project);
        banRedundantDependencyVersion.ignoreScopes = List.of("compile");
        Assertions.assertThatNoException().isThrownBy(banRedundantDependencyVersion::execute);
    }

    @Test
    void executeWhenFailsWithDifferentVersionLocationLine() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .versionLineNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        Assertions.assertThatThrownBy(() -> new BanRedundantDependencyVersion(project).execute())
                .hasMessageContaining("Please remove redundant dependency versions")
                .hasMessageContaining("'io.github.cedricsoual.enforcer.rules:enforcer-rules:jar:1.0.0' version defined in dependencies is redundant with the version defined in dependency management");
    }
    @Test
    void executeWhenFailsWithDifferentVersionLocationFile() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .modelId("pom-parent.xml")
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        Assertions.assertThatThrownBy(() -> new BanRedundantDependencyVersion(project).execute())
                .hasMessageContaining("Please remove redundant dependency versions")
                .hasMessageContaining("'io.github.cedricsoual.enforcer.rules:enforcer-rules:jar:1.0.0' version defined in dependencies is redundant with the version defined in dependency management");
    }

    @Test
    void executeWhenFailsWithDifferentVersionLocationAndSameLine() {
        Dependency dependency = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .build();
        Dependency dependencyManagement = DependencyTestBuilder.builder()
                .groupId("io.github.cedricsoual.enforcer.rules")
                .artifactId("enforcer-rules")
                .version("1.0.0")
                .versionColumnNumber(2)
                .build();
        MavenProject project = MavenProjectTestBuilder.builder()
                .dependency(dependency)
                .dependencyManagement(dependencyManagement)
                .build();

        Assertions.assertThatThrownBy(() -> new BanRedundantDependencyVersion(project).execute())
                .hasMessageContaining("'io.github.cedricsoual.enforcer.rules:enforcer-rules:jar:1.0.0' version defined in dependencies is redundant with the version defined in dependency management");
    }
}