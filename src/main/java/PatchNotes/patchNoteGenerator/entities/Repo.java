package PatchNotes.patchNoteGenerator.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String repository_name;

    @NotNull
    private String previousSha;

    @NotNull
    private  String currentSha;

    @NotNull
    @Lob
    private  String diff;

    @NotNull
    private  String[] uses;

    @NotNull
    private String commitMessage;
}
