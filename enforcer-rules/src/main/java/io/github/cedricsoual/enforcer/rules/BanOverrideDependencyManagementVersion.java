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
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Rule that bans overriding dependency management version defined in parent.
 */
@Named("banOverrideDependencyManagementVersion")
public class BanOverrideDependencyManagementVersion extends AbstractStandardEnforcerRule {

    /** The list of groupIds to be ignored. */
    public List<String> ignoreGroupIds = List.of();
    /** The list of artifacts to be ignored. */
    public List<String> ignoreArtifacts = List.of();
    /** The list of scopes to be ignored. */
    public List<String> ignoreScopes = List.of();
    private final MavenProject project;

    @Inject
    BanOverrideDependencyManagementVersion(final MavenProject project) {
        this.project = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final String newLine = System.getProperty("line.separator");
        final Map<String, Dependency> dependencyMgt = MavenProjectUtils.dependencyManagement(project);
        final Map<String, Dependency> dependencyMgtParent = MavenProjectUtils.dependencyManagement(project.getParent());
        final Set<Dependency> overridenDependencies = dependencyMgtParent.entrySet()
                .stream()
                .filter(dependencyByKey -> dependencyByKey.getValue().getScope() == null || !ignoreScopes.contains(dependencyByKey.getValue().getScope()))
                .filter(dependencyByKey -> !ignoreGroupIds.contains(dependencyByKey.getValue().getGroupId()))
                .filter(dependencyByKey -> !ignoreArtifacts.contains(dependencyByKey.getValue().getGroupId() + ":" + dependencyByKey.getValue().getArtifactId()))
                .filter(dependencyByKey -> dependencyMgt.containsKey(dependencyByKey.getKey()))
                .filter(dependencyByKey -> !dependencyMgt.get(dependencyByKey.getKey()).getVersion().equals(dependencyByKey.getValue().getVersion()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

        if (!overridenDependencies.isEmpty()) {
            final String errors = overridenDependencies.stream()
                    .map(Dependency::getManagementKey)
                    .map(managementKey -> String.format("- '%s' version is already managed by the parent and is not allowed to be overridden", managementKey))
                    .collect(Collectors.joining(newLine));

            throw new EnforcerRuleException(errors + newLine +
                    (getMessage() == null ? "Please update the dependency management" : getMessage()));
        }
    }

}