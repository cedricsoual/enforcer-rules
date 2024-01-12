package io.github.cedricsoual.enforcer.utils;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import java.util.List;

public class DependencyTestBuilder {

    public static DependencyBuilder builder() {
        return new DependencyBuilder();
    }

    public static Dependency build() {
        return builder().build();
    }

    public static final class DependencyBuilder {
        private String groupId = "io.github.cedricsoual.enforcer.rules";
        private String artifactId = "enforcer-rules";
        private String version = "1.0.0";
        private String scope = "compile";
        private DependencyBuilder() {
        }

        public DependencyBuilder groupId(final String groupId) {
            this.groupId = groupId;
            return this;
        }

        public DependencyBuilder artifactId(final String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public DependencyBuilder version(final String version) {
            this.version = version;
            return this;
        }

        public DependencyBuilder scope(final String scope) {
            this.scope = scope;
            return this;
        }

        public Dependency build() {
            Dependency dependency = new Dependency();
            dependency.setGroupId(groupId);
            dependency.setArtifactId(artifactId);
            dependency.setVersion(version);
            dependency.setScope(scope);
            return dependency;
        }
    }

}
