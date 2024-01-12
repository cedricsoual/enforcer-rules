package io.github.cedricsoual.enforcer.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.model.Profile;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Rule that enforces that no profile is in use.
 */
@Named("banProfile")
public class BanProfile extends AbstractStandardEnforcerRule {

    /** The list of profile ids to be excluded. */
    public List<String> excludedProfileIds = List.of();
    private final MavenProject project;


    @Inject
    BanProfile(final MavenProject project) {
        this.project = requireNonNull(project);
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final Set<Profile> activeProfiles = project.getActiveProfiles()
                .stream()
                .filter(profile -> !excludedProfileIds.contains(profile.getId()))
                .collect(Collectors.toSet());
        if (!activeProfiles.isEmpty()) {
            final String template = "Profile usage rule enforces that no profile is in use and the following active profiles have been found: [%s]";
            throw new EnforcerRuleException(String.format(template, activeProfiles
                    .stream()
                    .map(Profile::getId)
                    .collect(Collectors.joining(", "))));
        }
    }

}
