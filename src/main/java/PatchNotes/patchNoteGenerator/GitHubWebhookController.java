package PatchNotes.patchNoteGenerator;

import PatchNotes.patchNoteGenerator.entities.Repo;
import PatchNotes.patchNoteGenerator.repository.Sql;
import PatchNotes.patchNoteGenerator.service.EmailService;
import PatchNotes.patchNoteGenerator.service.TelegramMessageService;
import PatchNotes.patchNoteGenerator.util.RecipientList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@RestController
@RequestMapping("/webhook")
public class GitHubWebhookController {
    @Autowired
    private RunGitDiff diff;
    @Autowired
    private PatchNoteGeneratorService geminiResponse;
    @Autowired
    private TelegramMessageService telegramMessageService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RecipientList recipientList;
    @Autowired
    private Sql sql;

    private Repo repo=new Repo();

    //Configure according to your use case
    //uses={"telegram","email"}
    private final String[] uses={"telegram"};

    private static final Logger logger = LoggerFactory.getLogger(GitHubWebhookController.class);


    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestHeader("X-GitHub-Event") String eventType, @RequestBody WebhookPayload payload) throws IOException, InterruptedException {
        if(eventType.equals("ping")) return ResponseEntity.ok("pong");

        else if(eventType.equals("push")){
            String fullName=payload.getRepository().getFullName();
            String getBeforeCommitSha=payload.getBefore();
            String getAfterCommitSha=payload.getAfter();
            String message=payload.getHeadCommit().getMessage();
            System.out.println("Payload received:\t"+fullName+"\n"+"\n"+message);

            try{
                String diffOutput=diff.gitDiff("D:\\Disk E\\Java\\githubWebhook",getBeforeCommitSha,getAfterCommitSha);
                repo.setRepository_name(fullName);
                repo.setCommitMessage(message);
                repo.setUses(uses);
                repo.setPreviousSha(getBeforeCommitSha);
                repo.setCurrentSha(getAfterCommitSha);
                repo.setDiff(diffOutput);
                Repo savedRepo = sql.save(repo);
                if (savedRepo.getId() != null) {
                    logger.info("Repo data saved successfully with ID: {}", savedRepo.getId());
                } else {
                    logger.error("Failed to save repo data to the database.");
                }
                for(String use : uses){
                    String aiPatchSummary= geminiResponse.askGemini(fullName,getAfterCommitSha,message,diffOutput,use);
                    System.out.println("Ai Response:"+aiPatchSummary);

                    switch (use.toLowerCase()) {
                        case "telegram" ->telegramMessageService.sendMessageToGroup(aiPatchSummary);
                        case "email" -> emailService.sendEmail(recipientList.getRecipientList(),"Repo updates",aiPatchSummary);
                        default -> throw new IllegalArgumentException("Unsupported prompt use type: " + use);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(500).body("Git diff failed: "+ e.getMessage());
            }

        }

        return ResponseEntity.ok("Recieved");
    }
}
