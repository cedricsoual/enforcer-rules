package io.github.cedricsoual.enforcer.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Rule that requires artifact id match pattern.
 */
@Named("requireArtifactIdFormat")
public class RequireArtifactIdFormat extends AbstractStandardEnforcerRule {

    /** The pattern to match artifact id. */
    public String pattern = "^[a-z-]+$";
    private final MavenProject project;

    @Inject
    RequireArtifactIdFormat(final MavenProject project) {
        this.project = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        if (!Pattern.compile(pattern).matcher(project.getArtifactId()).matches()) {
            throw new EnforcerRuleException(String.format("Artifact id: [%s] does not match pattern: [%s]", project.getArtifactId(), pattern));
        }
    }

}
