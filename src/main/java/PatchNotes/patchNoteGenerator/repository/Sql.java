package PatchNotes.patchNoteGenerator.repository;

import PatchNotes.patchNoteGenerator.entities.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Sql extends JpaRepository<Repo, Long> {

}