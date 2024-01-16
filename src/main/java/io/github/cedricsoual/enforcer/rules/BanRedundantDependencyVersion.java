package io.github.cedricsoual.enforcer.rules;

import io.github.cedricsoual.enforcer.utils.MavenProjectUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.InputLocation;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Rule that bans redundant dependency versions declaration with dependency management.
 */
@Named("banRedundantDependencyVersion")
public class BanRedundantDependencyVersion extends AbstractStandardEnforcerRule {

    /** The list of groupIds to be ignored. */
    public List<String> ignoreGroupIds = List.of();
    /** The list of artifacts to be ignored. */
    public List<String> ignoreArtifacts = List.of();
    /** The list of scopes to be ignored. */
    public List<String> ignoreScopes = List.of();
    private final MavenProject project;

    @Inject
    BanRedundantDependencyVersion(final MavenProject project) {
        this.project = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final String newLine = System.getProperty("line.separator");

        final Map<String, Dependency> dependencyMgt = MavenProjectUtils.dependencyManagement(project.getDependencyManagement());
        final List<Dependency> dependencies = project.getDependencies();

        final Set<Dependency> redundantDependencyVersions = dependencies
                .stream()
                .filter(dependency -> !ignoreScopes.contains(dependency.getScope()))
                .filter(dependency -> !ignoreGroupIds.contains(dependency.getGroupId()))
                .filter(dependency -> !ignoreArtifacts.contains(dependency.getGroupId() + ":" + dependency.getArtifactId()))
                .filter(dependency -> {
                    final Dependency depMgtDependency = dependencyMgt.get(dependency.getManagementKey());
                    if (isIdenticalVersion(dependency, depMgtDependency)) {
                        final InputLocation locationVersionDepMgt = depMgtDependency.getLocation("version");
                        final InputLocation locationVersionDep = dependency.getLocation("version");
                        return isDifferentVersionDefinition(locationVersionDep, locationVersionDepMgt);
                    }
                    return false;
                })
                .collect(Collectors.toSet());

        if (!redundantDependencyVersions.isEmpty()) {
            final String errors = redundantDependencyVersions.stream()
                    .map(dependency -> String.format("- '%s:%s' version defined in dependencies is redundant with the version defined in dependency management", dependency.getManagementKey(), dependency.getVersion()))
                    .collect(Collectors.joining(newLine));

            throw new EnforcerRuleException(errors + newLine +
                    (getMessage() == null ? "Please remove redundant dependency versions" : getMessage()));
        }

    }

    private static boolean isIdenticalVersion(final Dependency dependency, final Dependency depMgtDependency) {
        return depMgtDependency != null
                && depMgtDependency.getVersion() != null
                && depMgtDependency.getVersion().equals(dependency.getVersion());
    }

    private static boolean isDifferentVersionDefinition(final InputLocation locationVersionDep, final InputLocation locationVersionDepMgt) {
        return !locationVersionDep.getSource().getModelId().equals(locationVersionDepMgt.getSource().getModelId()) ||
                (locationVersionDep.getSource().getModelId().equals(locationVersionDepMgt.getSource().getModelId())
                        && locationVersionDepMgt.getLineNumber() != locationVersionDep.getLineNumber()
                        && locationVersionDepMgt.getColumnNumber() != locationVersionDep.getColumnNumber());
    }

}
