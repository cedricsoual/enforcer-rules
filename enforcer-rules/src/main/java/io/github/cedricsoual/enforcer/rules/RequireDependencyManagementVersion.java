package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.MavenProjectUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;


/**
 * Rule that requires dependency management version declaration for every dependency.
 */
@Named("requireDependencyManagementVersion")
public class RequireDependencyManagementVersion extends AbstractStandardEnforcerRule {

    /** If true, check that the dependency version is identical to the declared one in dependency management . */
    public boolean versionMatch = true;
    /** The list of groupIds to be ignored. */
    public List<String> ignoreGroupIds = List.of();
    /** The list of artifacts to be ignored. */
    public List<String> ignoreArtifacts = List.of();
    /** The list of scopes to be ignored. */
    public List<String> ignoreScopes = List.of();

    private final MavenProject project;

    @Inject
    RequireDependencyManagementVersion(final MavenProject project) {
        this.project = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final String newLine = System.getProperty("line.separator");
        final Map<String, Dependency> depMgtMap = MavenProjectUtils.dependencyManagement(project.getDependencyManagement());

        final List<Dependency> dependencies = project.getDependencies()
                .stream()
                .filter(dependency -> !ignoreScopes.contains(dependency.getScope()))
                .filter(dependency -> !ignoreGroupIds.contains(dependency.getGroupId()))
                .filter(dependency -> !ignoreArtifacts.contains(dependency.getGroupId() + ":" + dependency.getArtifactId()))
                .collect(Collectors.toList());

        final Stream<String> notManagedDependencyErrors = dependencies
                .stream()
                .filter(dependency -> {
                    final Dependency depMgtDependency = depMgtMap.get(dependency.getManagementKey());
                    return depMgtDependency == null || (versionMatch && depMgtDependency.getVersion() == null);
                })
                .map(dependency -> String.format("- '%s' version is not managed by dependency management", dependency.getManagementKey()));

        final Stream<String> divergentDependencyVersionErrors = dependencies
                .stream()
                .filter(dependency -> {
                    final Dependency depMgtDependency = depMgtMap.get(dependency.getManagementKey());
                    return versionMatch && depMgtDependency != null
                            && depMgtDependency.getVersion() != null
                            && !depMgtDependency.getVersion().equals(dependency.getVersion());
                })
                .map(dependency -> {
                    final Dependency depMgtDependency = depMgtMap.get(dependency.getManagementKey());
                    return String.format("- '%s:%s' version is different from the version defined in dependency management (%s)", dependency.getManagementKey(), dependency.getVersion(), depMgtDependency.getVersion());
                });

        final String errors = Stream.concat(notManagedDependencyErrors, divergentDependencyVersionErrors)
                .collect(Collectors.joining(newLine));
        if (!errors.isEmpty()) {
            throw new EnforcerRuleException(errors + newLine +
                    (getMessage() == null ? "Please update the dependency management" : getMessage()));
        }
    }

}
