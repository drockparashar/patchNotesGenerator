package PatchNotes.patchNoteGenerator;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;

@Component
public class PatchNoteGeneratorService {

    public String askGemini(String repo_full_name, String commit_sha, String commit_message,String git_diff){
        Client client =new Client();
        String template = """
You are a developer assistant specialized in creating patch notes based on git diffs. When given the following inputs, generate concise, clear, developer‑friendly patch notes.

---

Repository: %s
Commit SHA: %s

Commit Message:
%s

Git Diff:
%s

---
Requirements:
1. Group changes by file.
2. For each file:
   • Summarize added code.
   • Summarize removed code.
3. Omit trivial formatting or whitespace-only changes.
4. Use bullet points.
5. Indicate newly added files clearly.
6. If functions were added/modified, mention their names.
7. Keep the total summary under 100 words.

---

Output format (Markdown):

### Patch Notes for %s

- **file.js** (new or modified)
  - ...
  - Use backticks for code.

Important:
- Use present tense.
- Tone: neutral, factual.
- Wrap code elements in backticks.
""";

        // Fill in placeholders
        String prompt = String.format(
                template,
                repo_full_name,
                commit_sha,
                commit_message,
                git_diff,
                commit_sha
        );

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        return response.text();
    }

}
