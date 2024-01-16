package io.github.cedricsoual.enforcer.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rules.AbstractStandardEnforcerRule;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Rule that requires groupId match base package.
 */
@Named("requireGroupIdMatchBasePackage")
public class RequireGroupIdMatchBasePackage extends AbstractStandardEnforcerRule {

    /** Condition to ignore pom file with pom packaging. */
    public boolean ignorePomPackaging = true;
    private final MavenProject project;

    @Inject
    RequireGroupIdMatchBasePackage(final MavenProject project) {
        this.project = requireNonNull(project, "project");
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final String groupId = project.getGroupId();
        final Optional<File> basePackageMatch = project.getCompileSourceRoots()
                .stream()
                .map(src -> new File(src + File.separator + groupId.replace('.', File.separatorChar)))
                .filter(File::exists)
                .findFirst();
        if (basePackageMatch.isEmpty() && !(ignorePomPackaging && "pom".equals(project.getPackaging()))) {
            throw new EnforcerRuleException("No matching Java package for groupId [" + groupId + "]");
        }
    }

}
