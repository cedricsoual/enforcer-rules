package io.github.cedricsoual.enforcer.utils;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.InputSource;

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
        private Integer versionLineNumber = 1;
        private Integer versionColumnNumber = 1;
        private String modelId = "pom.xml";
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

        public DependencyBuilder versionLineNumber(final Integer versionLineNumber) {
            this.versionLineNumber = versionLineNumber;
            return this;
        }

        public DependencyBuilder versionColumnNumber(final Integer versionColumnNumber) {
            this.versionColumnNumber = versionColumnNumber;
            return this;
        }

        public DependencyBuilder modelId(final String modelId) {
            this.modelId = modelId;
            return this;
        }

        public Dependency build() {
            Dependency dependency = new Dependency();
            dependency.setGroupId(groupId);
            dependency.setArtifactId(artifactId);
            dependency.setVersion(version);
            dependency.setScope(scope);
            InputSource sourceDependency = new InputSource();
            sourceDependency.setModelId(modelId);
            dependency.setLocation("version", new InputLocation(versionLineNumber, versionColumnNumber, sourceDependency));
            return dependency;
        }
    }

}
