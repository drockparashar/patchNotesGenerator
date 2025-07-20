package PatchNotes.patchNoteGenerator;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;

@Component
public class PatchNoteGeneratorService {

    public String askGemini(String repo_full_name, String commit_sha, String commit_message, String git_diff) {
        Client client = new Client();
        String template = """
You are a developer assistant specialized in creating clear, structured, and developer-friendly patch notes suitable for a Telegram group update. Your goal is to summarize code changes concisely, making them easy to digest for team members.

---

Repository: %s
Commit SHA: %s

Commit Message:
%s

Git Diff:
%s

---
Requirements for Telegram Message:
1.  **Overall Structure**:
    * Start with a clear, concise title indicating "New Patch Notes" and the commit SHA.
    * Include a brief, high-level overview of the main purpose of this patch.
    * Add a dedicated section for "Key Changes".
    * Include a dedicated section for "Files Modified".
    * Conclude with a call to action or relevant link (e.g., "See full diff: [Link to GitHub Commit]").
2.  **Key Changes Section**:
    * Summarize the most important additions, removals, or modifications at a high level.
    * Focus on features, bug fixes, or significant refactorings.
    * Use bullet points.
3.  **Files Modified Section**:
    * List each modified file.
    * For each file:
        * Briefly describe what was added (new features, functions, classes).
        * Briefly describe what was removed (deprecated code, old logic).
        * Mention significant modifications to existing functions/logic.
    * Omit trivial formatting, whitespace-only changes, or minor typos.
    * Use bullet points for sub-items.
    * Indicate newly added files clearly with "(New File)".
    * Indicate deleted files clearly with "(Deleted File)".
4.  **Formatting**:
    * Use **bold** for section headers (e.g., **Key Changes:**, **Files Modified:**).
    * Use `monospace` for file names, function names, and small code snippets.
    * Use emojis sparingly and appropriately to enhance readability (e.g., 🚀 for new features, 🐛 for bug fixes).
    * Keep the entire message concise, ideally under 200 words, to be Telegram-friendly.
5.  **Tone & Tense**:
    * Maintain a neutral, factual, and informative tone.
    * Use present tense (e.g., "Adds new feature," "Fixes bug").
6.  **Example Snippets**:
    - `MyClass.java` (modified)
        - Added `calculateTotal()` method to process order totals.
        - Removed deprecated `oldMethod()` call from `init()`.
    - `README.md` (new)
        - Added initial project setup instructions.

---

Output format (Telegram-friendly Markdown):

✨ **New Patch Notes for Commit %s in the repository %s** ✨

_A brief overview of the main purpose of this update._

---

🚀 **Key Changes:**
* Added `UserManagementService` for handling user authentication.
* Fixed a critical bug in `PaymentProcessor` that caused transaction failures.
* Refactored `DatabaseConnector` for improved performance.

---

📄 **Files Modified:**
* `src/main/java/com/example/UserManagementService.java` (New File)
    * Initial implementation of user creation and login.
* `src/main/java/com/example/PaymentProcessor.java`
    * Corrected `processTransaction()` logic to handle edge cases.
* `src/main/java/com/example/DatabaseConnector.java`
    * Optimized connection pooling.
* `config/application.properties` (modified)
    * Added `db.connection.timeout` property.
* `old_feature.js` (Deleted File)
    * Removed obsolete client-side script.

---

🔗 **See full diff:** [Link to GitHub Commit: https://github.com/%s/commit/%s]
""";

        // Fill in placeholders
        String prompt = String.format(
                template,
                repo_full_name,
                commit_sha,
                commit_message,
                git_diff,
                // These placeholders are for the example output template, repeated for convenience
                commit_sha,
                repo_full_name,
                commit_sha,
                repo_full_name
        );

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash", // Consider using gemini-1.5-flash or gemini-1.5-pro for better quality if needed
                        prompt,
                        null);

        return response.text();
    }
}