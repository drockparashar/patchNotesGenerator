package PatchNotes.patchNoteGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/webhook")
public class GitHubWebhookController {
    @Autowired
    private RunGitDiff diff;
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
                System.out.println("Diff:"+diffOutput);
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(500).body("Git diff failed: "+ e.getMessage());
            }

        }

        return ResponseEntity.ok("Recieved");
    }
}
