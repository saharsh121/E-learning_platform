package com.edumania.services;

import com.edumania.documents.Note;
import com.edumania.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // Get all notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Get active notes only
    public List<Note> getActiveNotes() {
        return noteRepository.findByIsActiveTrue();
    }

    // Get notes by course ID
    public List<Note> getNotesByCourseId(String courseId) {
        return noteRepository.findByCourseId(courseId);
    }

    // Get active notes by course ID
    public List<Note> getActiveNotesByCourseId(String courseId) {
        return noteRepository.findByCourseIdAndIsActiveTrue(courseId);
    }

    // Get notes by uploader
    public List<Note> getNotesByUploadedBy(String uploadedBy) {
        return noteRepository.findByUploadedBy(uploadedBy);
    }

    // Create new note
    public Note createNote(Note note) {
        note.setUploadDate(LocalDateTime.now());
        note.setDownloads(0);
        note.setActive(true);
        return noteRepository.save(note);
    }
    // Get note by ID
    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    // Update note
    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }

    // Delete note
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    // Soft delete (deactivate)
    public void deactivateNote(String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setActive(false);
            noteRepository.save(note);
        }
    }

    // Increment download count
    public void incrementDownloads(String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setDownloads(note.getDownloads() + 1);
            noteRepository.save(note);
        }
    }

    // Get note count by course
    public long getNoteCountByCourseId(String courseId) {
        return noteRepository.findByCourseId(courseId).size();
    }
}