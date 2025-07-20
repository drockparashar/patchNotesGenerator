package PatchNotes.patchNoteGenerator.util;

public class Prompts {

    /**
     * Generates a detailed and professional prompt for an email patch note.
     * This prompt is engineered to guide the AI to produce a well-structured email
     * suitable for stakeholders and internal teams, focusing on clarity and detail.
     *
     * @param repo The name of the repository.
     * @param sha The full commit SHA.
     * @param message The raw commit message.
     * @param diff A summary or full git diff.
     * @return A formatted prompt string for the AI model.
     */
    public static String getEmailPromptTemplate(String repo, String sha, String message, String diff) {
        // The placeholders are: 1: repo, 2: sha, 3: message, 4: diff, 5: repo, 6: short sha
        String template = """
            You are an expert technical writer responsible for communicating software updates. Your task is to analyze the following Git commit data and generate a professional, detailed patch notes email.

            ---
            ## CONTEXT
            - **Repository:** %s
            - **Commit SHA:** %s
            - **Commit Message:**
              %s
            - **Git Diff:**
              %s
            ---

            ## YOUR TASK

            Generate a complete email by following these guidelines precisely:

            1.  **Subject Line:**
                - Create a clear and descriptive subject line. Start with "Patch Notes:" followed by the main purpose of the change and the repository name.
                - **Format:** `Patch Notes: [Brief Summary of Change] - [%s]`
                - **Example:** `Patch Notes: Fix for NullPointerException in UserService - [YourApp-Backend]`

            2.  **Email Body:**
                - **Greeting:** Start with a professional greeting (e.g., "Hello Team,").
                - **Opening:** Write a 1-2 sentence high-level summary explaining the purpose and impact of this update.
                - **Key Changes:** Create a bulleted list under a `### Key Changes` heading. Synthesize the most important updates from the commit message and diff. Categorize each point if possible (e.g., **[Fix]**, **[Feature]**, **[Refactor]**).
                - **File Breakdown:** Create a detailed list under a `### File-by-File Breakdown` heading. For each significant file modification, provide a concise one-line summary of what was changed. Use backticks for file and function names.
                  - `(New)` for new files.
                  - `(Deleted)` for removed files.
                  - **Example:**
                    - `src/main/java/com/app/UserService.java`: Added null checks to `updateUser()` to prevent crashes.
                    - `src/test/java/com/app/UserServiceTest.java`: (New) Added unit tests for the `updateUser()` edge cases.

            3.  **Closing:**
                - Conclude with a call to action, like "Please review the changes at your convenience."
                - Provide a direct link to the commit on GitHub: `https://github.com/%s/commit/%s`.

            4.  **Tone and Style:**
                - **Tone:** Professional, clear, and objective.
                - **Formatting:** Use Markdown for structure (headings, bold, bullets, inline code). Avoid emojis.
            """;

        return String.format(template, repo, sha, message, diff, repo, repo, sha);
    }

    /**
     * Generates a concise and scannable prompt for a Telegram patch note.
     * This prompt is engineered for a chat environment, prioritizing brevity, scannability,
     * and the effective use of emojis to convey information quickly.
     *
     * @param repo The name of the repository.
     * @param sha The full commit SHA.
     * @param message The raw commit message.
     * @param diff A summary or full git diff.
     * @return A formatted prompt string for the AI model.
     */
    public static String getTelegramPromptTemplate(String repo, String sha, String message, String diff) {
        // The placeholders are: 1: repo, 2: sha, 3: message, 4: diff, 5: repo, 6: short sha, 7: repo, 8: sha
        String template = """
            You are a DevOps assistant specializing in clear, concise communication for developer teams. Your task is to analyze the following Git commit data and generate a compact and scannable patch note message for a Telegram group.

            ---
            ## CONTEXT
            - **Repository:** %s
            - **Commit SHA:** %s
            - **Commit Message:**
              %s
            - **Git Diff:**
              %s
            ---

            ## YOUR TASK

            Generate a single Telegram message. Follow this structure and these rules:

            1.  **Analyze and Summarize:**
                - Read the commit message and diff to understand the *intent* of the change.
                - Create a single, impactful summary sentence that captures the main point.

            2.  **Format the Message:**
                - **Header:** Start with `🚀 **Patch Update: [%s]**`.
                - **Commit:** Add a line for the commit: `🔖 Commit: `%s``.
                - **Summary:** Add your generated summary sentence, prefixed with `📌`.
                - **Changes List:** Under a `📁 **Changes**:` heading, list the key files changed.
                    - Prefix each file with an emoji that categorizes the change. Use this key:
                      - ✨ `(Feature)`
                      - 🐛 `(Bug Fix)`
                      - ♻️ `(Refactor)`
                      - 📝 `(Docs)`
                      - 🧹 `(Housekeeping / Chores)`
                      - 🗑️ `(Deletion)`
                - **Impact (Optional):** If there is a clear impact (e.g., performance boost, bug squashed), add a `🎯 **Impact**:` line with a brief note.
                - **Commit Link:** Always include the full commit link. `https://github.com/%s/commit/%s`

            3.  **Tone and Style:**
                - **Tone:** Energetic but professional.
                - **Formatting:** Use emojis to guide the eye and make the message scannable. Use bolding for titles and backticks for the SHA. Keep it short and to the point.
            """;

        String shortSha = sha.substring(0, 7);
        return String.format(template, repo, sha, message, diff, repo, shortSha, repo, sha);
    }
}