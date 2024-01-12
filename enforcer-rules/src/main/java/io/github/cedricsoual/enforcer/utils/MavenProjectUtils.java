package io.github.cedricsoual.enforcer.utils;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class MavenProjectUtils {

    private MavenProjectUtils() {
        // Utility class
    }

    public static Map<String, Dependency> dependencyManagement(final DependencyManagement dependencyManagement) {
        if (dependencyManagement == null) {
            return new HashMap<>();
        } else {
            return dependencyManagement.getDependencies().stream().collect(toMap(Dependency::getManagementKey, identity()));
        }
    }

    public static Map<String, Dependency> dependencyManagement(final MavenProject project) {
        if (project == null) {
            return new HashMap<>();
        }
        return dependencyManagement(project.getDependencyManagement());
    }
}
