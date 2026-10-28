package com.edumania.services;

import com.edumania.documents.Note;
import com.edumania.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    
    public List<Note> getActiveNotes() {
        return noteRepository.findByIsActiveTrue();
    }

    
    public List<Note> getNotesByCourseId(String courseId) {
        return noteRepository.findByCourseId(courseId);
    }

    
    public List<Note> getActiveNotesByCourseId(String courseId) {
        return noteRepository.findByCourseIdAndIsActiveTrue(courseId);
    }

    
    public List<Note> getNotesByUploadedBy(String uploadedBy) {
        return noteRepository.findByUploadedBy(uploadedBy);
    }

    
    public Note createNote(Note note) {
        note.setUploadDate(LocalDateTime.now());
        note.setDownloads(0);
        note.setActive(true);
        return noteRepository.save(note);
    }
    
    public Optional<Note> getNoteById(@NonNull String id) {
        return noteRepository.findById(id);
    }

    
    public Note updateNote(@NonNull Note note) {
        return noteRepository.save(note);
    }

    
    public void deleteNote(@NonNull String id) {
        noteRepository.deleteById(id);
    }

    
    public void deactivateNote(@NonNull String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setActive(false);
            noteRepository.save(note);
        }
    }

    
    public void incrementDownloads(@NonNull String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setDownloads(note.getDownloads() + 1);
            noteRepository.save(note);
        }
    }

    
    public long getNoteCountByCourseId(String courseId) {
        return noteRepository.findByCourseId(courseId).size();
    }
}