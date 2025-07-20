package PatchNotes.patchNoteGenerator;

import PatchNotes.patchNoteGenerator.util.Prompts;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;

@Component
public class PatchNoteGeneratorService {

    public String askGemini(String repo_full_name, String commit_sha, String commit_message, String git_diff, String use) {
        Client client = new Client();
        String prompt;

        switch (use.toLowerCase()) {
            case "telegram" -> prompt = Prompts.getTelegramPromptTemplate(repo_full_name, commit_sha, commit_message, git_diff);
            case "email" -> prompt = Prompts.getEmailPromptTemplate(repo_full_name, commit_sha, commit_message, git_diff);
            default -> throw new IllegalArgumentException("Unsupported prompt use type: " + use);
        }

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash", // Consider using gemini-1.5-flash or gemini-1.5-pro for better quality if needed
                        prompt,
                        null);

        return response.text();
    }
}