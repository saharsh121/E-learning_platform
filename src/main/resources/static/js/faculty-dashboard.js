/**
 * Faculty Dashboard - JavaScript
 * EduMania - Faculty Portal
 */

document.addEventListener('DOMContentLoaded', function() {
    
    console.log('👨‍🏫 EduMania Faculty Dashboard Loaded');

    // ==========================================
    // 1. SIDEBAR TOGGLE (Mobile)
    // ==========================================
    
    const sidebar = document.querySelector('.sidebar');
    const headerRight = document.querySelector('.header-right');
    
    // Create hamburger menu button
    const menuToggle = document.createElement('button');
    menuToggle.className = 'menu-toggle d-md-none';
    menuToggle.innerHTML = '<i class="bi bi-list"></i>';
    menuToggle.setAttribute('aria-label', 'Toggle Sidebar');
    
    const topHeader = document.querySelector('.top-header');
    topHeader.insertBefore(menuToggle, headerRight);

    // Toggle sidebar on mobile
    menuToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        sidebar.classList.toggle('active');
    });

    // Close sidebar when clicking outside (on mobile)
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 768) {
            if (!sidebar.contains(e.target) && !menuToggle.contains(e.target)) {
                sidebar.classList.remove('active');
            }
        }
    });

    // Close sidebar on link click (mobile)
    document.querySelectorAll('.sidebar-menu li a').forEach(link => {
        link.addEventListener('click', function() {
            if (window.innerWidth <= 768) {
                sidebar.classList.remove('active');
            }
        });
    });

    // ==========================================
    // 2. NAVIGATION - Active State
    // ==========================================
    
    const menuItems = document.querySelectorAll('.sidebar-menu li');
    
    menuItems.forEach(item => {
        item.addEventListener('click', function() {
            menuItems.forEach(i => i.classList.remove('active'));
            this.classList.add('active');
            
            if (window.innerWidth <= 768) {
                sidebar.classList.remove('active');
            }
        });
    });

    // ==========================================
    // 3. LOGOUT CONFIRMATION
    // ==========================================
    
    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            const confirmLogout = confirm('Are you sure you want to logout?');
            if (confirmLogout) {
                window.location.href = '/logout';
            }
        });
    }

    // ==========================================
    // 4. FORM VALIDATION
    // ==========================================
    
    // Lecture Upload Form Validation
    const lectureForm = document.querySelector('form[action="/faculty/upload-lecture"]');
    if (lectureForm) {
        lectureForm.addEventListener('submit', function(e) {
            const title = this.querySelector('input[name="title"]').value.trim();
            const videoUrl = this.querySelector('input[name="videoUrl"]').value.trim();
            const duration = this.querySelector('input[name="duration"]').value.trim();
            const courseId = this.querySelector('select[name="courseId"]').value;
            
            let errors = [];
            
            if (!courseId) {
                errors.push('Please select a course');
            }
            if (!title) {
                errors.push('Please enter a lecture title');
            }
            if (!videoUrl) {
                errors.push('Please enter a video URL');
            }
            if (!duration || isNaN(duration) || parseInt(duration) <= 0) {
                errors.push('Please enter a valid duration');
            }
            
            if (errors.length > 0) {
                e.preventDefault();
                alert('❌ Please fix the following:\n\n- ' + errors.join('\n- '));
            }
        });
    }

    // Note Upload Form Validation
    const noteForm = document.querySelector('form[action="/faculty/upload-note"]');
    if (noteForm) {
        noteForm.addEventListener('submit', function(e) {
            const title = this.querySelector('input[name="title"]').value.trim();
            const courseId = this.querySelector('select[name="courseId"]').value;
            const file = this.querySelector('input[type="file"]');
            
            let errors = [];
            
            if (!courseId) {
                errors.push('Please select a course');
            }
            if (!title) {
                errors.push('Please enter a note title');
            }
            if (!file || !file.files || file.files.length === 0) {
                errors.push('Please select a PDF file to upload');
            } else {
                const fileName = file.files[0].name;
                const fileSize = file.files[0].size;
                const fileType = file.files[0].type;
                
                if (!fileName.toLowerCase().endsWith('.pdf')) {
                    errors.push('Only PDF files are allowed');
                }
                if (fileSize > 10 * 1024 * 1024) {
                    errors.push('File size must be less than 10MB');
                }
            }
            
            if (errors.length > 0) {
                e.preventDefault();
                alert('❌ Please fix the following:\n\n- ' + errors.join('\n- '));
            }
        });
    }

    // ==========================================
    // 5. DELETE CONFIRMATION
    // ==========================================
    
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const form = this.closest('form');
            if (form) {
                const confirmDelete = confirm('⚠️ Are you sure you want to delete this item?');
                if (confirmDelete) {
                    form.submit();
                }
            }
        });
    });

    // ==========================================
    // 6. VIEW LECTURE (Open in New Tab)
    // ==========================================
    
    document.querySelectorAll('.btn-outline-primary').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const url = this.getAttribute('href');
            if (url && url.startsWith('http')) {
                window.open(url, '_blank');
            }
        });
    });

    // ==========================================
    // 7. AUTO-DISMISS ALERTS
    // ==========================================
    
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(function() {
            const closeBtn = alert.querySelector('.btn-close');
            if (closeBtn) {
                closeBtn.click();
            }
        }, 5000);
    });

    // ==========================================
    // 8. KEYBOARD SHORTCUTS
    // ==========================================
    
    document.addEventListener('keydown', function(e) {
        // Ctrl + D -> Dashboard
        if (e.ctrlKey && e.key === 'd') {
            e.preventDefault();
            window.location.href = '/faculty-dashboard';
        }
        // Ctrl + U -> Upload Lectures
        if (e.ctrlKey && e.key === 'u') {
            e.preventDefault();
            document.getElementById('upload-lectures')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
        // Ctrl + N -> Upload Notes
        if (e.ctrlKey && e.key === 'n') {
            e.preventDefault();
            document.getElementById('upload-notes')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
    });

    // ==========================================
    // 9. CONSOLE HELPERS
    // ==========================================
    
    console.log('ℹ️ Type showFacultyInfo() to see faculty details');
    console.log('💡 Shortcuts: Ctrl+D (Dashboard) | Ctrl+U (Upload Lectures) | Ctrl+N (Upload Notes)');
    
    window.showFacultyInfo = function() {
        const facultyName = document.querySelector('.user-info h6')?.textContent || 'Faculty';
        const lecturesCount = document.querySelectorAll('.lecture-item').length;
        const notesCount = document.querySelectorAll('.lecture-item .pdf-icon').length;
        
        console.log(`👨‍🏫 Faculty: ${facultyName}`);
        console.log(`📹 Total Lectures: ${lecturesCount}`);
        console.log(`📄 Total Notes: ${notesCount}`);
    };

    // ==========================================
    // 10. FILE NAME DISPLAY (for note upload)
    // ==========================================
    
    const fileInput = document.querySelector('input[type="file"][name="file"]');
    if (fileInput) {
        fileInput.addEventListener('change', function() {
            const fileName = this.files[0]?.name || 'No file selected';
            const fileSize = this.files[0]?.size || 0;
            const sizeInKB = (fileSize / 1024).toFixed(1);
            const sizeInMB = (fileSize / (1024 * 1024)).toFixed(1);
            
            const sizeDisplay = fileSize > 1024 * 1024 ? sizeInMB + ' MB' : sizeInKB + ' KB';
            
            // Show file info
            let infoElement = document.querySelector('.file-info');
            if (!infoElement) {
                infoElement = document.createElement('small');
                infoElement.className = 'file-info d-block mt-1';
                fileInput.parentNode.appendChild(infoElement);
            }
            infoElement.textContent = `📎 Selected: ${fileName} (${sizeDisplay})`;
            
            // Validate file type
            if (fileName && !fileName.toLowerCase().endsWith('.pdf')) {
                infoElement.style.color = '#dc3545';
                infoElement.textContent = '⚠️ Only PDF files are allowed!';
            } else {
                infoElement.style.color = '#0B4F3A';
            }
        });
    }

    console.log('✅ Faculty Dashboard ready!');
});