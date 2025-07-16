package PatchNotes.patchNoteGenerator;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class RunGitDiff {
    public String gitDiff(String repoPath,String beforeSha,String afterSha) throws IOException,InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(
                "git", "diff", beforeSha, afterSha
        );
        builder.directory(new File(repoPath)); // local repo directory
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("git diff failed with exit code " + exitCode);
        }

        return output.toString();
    }
}
