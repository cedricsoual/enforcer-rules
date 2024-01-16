package io.github.cedricsoual.enforcer.rules;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;

import static java.util.Objects.requireNonNull;

/**
 * Rule that requires artifact id match base directory.
 */
@Named("requireArtifactIdMatchBaseDirectory")
public class RequireArtifactIdMatchBaseDirectory extends AbstractStandardEnforcerRule {

    /**
     * Condition to ignore pom file with pom packaging.
     */
    public boolean ignorePomPackaging = true;
    private final MavenProject mavenProject;

    @Inject
    RequireArtifactIdMatchBaseDirectory(final MavenProject project) {
        this.mavenProject = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final String baseDir = mavenProject.getBasedir().getName();
        final String artifactId = mavenProject.getArtifactId();
        if (baseDir.equals(artifactId)) {
            return;
        }
        final boolean isPomAndIgnored = "pom".equals(mavenProject.getPackaging()) && ignorePomPackaging;
        if (!isPomAndIgnored) {
            final String template = "Artifact id: [%s] is not the same with base dir: [%s]. Difference is started at: [%s]";
            throw new EnforcerRuleException(String.format(template, artifactId, baseDir, StringUtils.difference(baseDir, artifactId)));
        }
    }

}
