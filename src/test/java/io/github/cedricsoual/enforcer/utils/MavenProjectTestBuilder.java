package io.github.cedricsoual.enforcer.utils;

import org.apache.maven.model.*;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

public class MavenProjectTestBuilder {

    public static MavenProjectBuilder builder() {
        return new MavenProjectBuilder();
    }

    public static MavenProject build() {
        return builder().build();
    }

    public static final class MavenProjectBuilder {
        private String groupId = "io.github.cedricsoual.enforcer.rules";
        private String artifactId = "enforcer-rules";
        private String version = "1.0.0";
        private List<Dependency> dependencies = new ArrayList<>();
        private List<Dependency> dependencyManagement = new ArrayList<>();
        private String packaging = "jar";

        private MavenProjectBuilder() {
        }

        public MavenProjectBuilder groupId(final String groupId) {
            this.groupId = groupId;
            return this;
        }

        public MavenProjectBuilder artifactId(final String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public MavenProjectBuilder version(final String version) {
            this.version = version;
            return this;
        }

        public MavenProjectBuilder dependency(final Dependency dependency) {
            this.dependencies.add(dependency);
            return this;
        }

        public MavenProjectBuilder dependencies(final List<Dependency> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public MavenProjectBuilder dependencyManagement(final Dependency dependency) {
            this.dependencyManagement.add(dependency);
            return this;
        }

        public MavenProjectBuilder dependencyManagement(final List<Dependency> dependencyManagement) {
            this.dependencyManagement = dependencyManagement;
            return this;
        }

        public MavenProjectBuilder packaging(final String packaging) {
            this.packaging = packaging;
            return this;
        }

        public MavenProject build() {
            MavenProject mavenProject = new MavenProject();
            Model model = new Model();
            model.setGroupId(groupId);
            model.setArtifactId(artifactId);
            model.setVersion(version);
            model.setPackaging(packaging);
            model.setDependencies(dependencies);
            DependencyManagement dependencyManagement = new DependencyManagement();
            dependencyManagement.setDependencies(this.dependencyManagement);
            model.setDependencyManagement(dependencyManagement);
            mavenProject.setModel(model);
            return mavenProject;
        }
    }

}
