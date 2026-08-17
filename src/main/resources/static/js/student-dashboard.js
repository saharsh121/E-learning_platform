/**
 * Student Dashboard - JavaScript
 * EduMania Learning Platform
 */

document.addEventListener('DOMContentLoaded', function() {
    
    console.log('🎓 EduMania Student Dashboard Loaded');

    // ==========================================
    // 1. SIDEBAR TOGGLE (for mobile)
    // ==========================================
    
    const sidebar = document.querySelector('.sidebar');
    
    // Create hamburger menu button
    const headerRight = document.querySelector('.header-right');
    const menuToggle = document.createElement('button');
    menuToggle.className = 'menu-toggle btn btn-outline-secondary d-md-none';
    menuToggle.innerHTML = '<i class="bi bi-list"></i>';
    menuToggle.style.marginRight = '10px';
    menuToggle.style.border = 'none';
    menuToggle.style.background = 'transparent';
    menuToggle.style.fontSize = '24px';
    menuToggle.style.color = '#333';
    
    const topHeader = document.querySelector('.top-header');
    topHeader.insertBefore(menuToggle, headerRight);

    // Toggle sidebar on mobile
    menuToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        sidebar.classList.toggle('active');
    });

    // Close sidebar when clicking outside
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 768) {
            if (!sidebar.contains(e.target) && !menuToggle.contains(e.target)) {
                sidebar.classList.remove('active');
            }
        }
    });

    // ==========================================
    // 2. NAVIGATION - Active State & Smooth Scroll
    // ==========================================
    
    const menuItems = document.querySelectorAll('.sidebar-menu li');
    
    menuItems.forEach(item => {
        item.addEventListener('click', function() {
            menuItems.forEach(i => i.classList.remove('active'));
            this.classList.add('active');
            
            if (window.innerWidth <= 768) {
                sidebar.classList.remove('active');
            }
            
            // Smooth scroll to section
            const link = this.querySelector('a');
            if (link && link.getAttribute('href') && link.getAttribute('href').startsWith('#')) {
                const targetId = link.getAttribute('href');
                const target = document.querySelector(targetId);
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
        });
    });

    // ==========================================
    // 3. STATS ANIMATION
    // ==========================================
    
    function animateCounters() {
        const statNumbers = document.querySelectorAll('.stat-card h3');
        
        statNumbers.forEach(stat => {
            const text = stat.textContent;
            if (!isNaN(text) && text.trim() !== '') {
                const target = parseInt(text);
                const duration = 1000;
                const stepTime = 20;
                const steps = duration / stepTime;
                const increment = target / steps;
                let current = 0;
                
                const counter = setInterval(() => {
                    current += increment;
                    if (current >= target) {
                        stat.textContent = target;
                        clearInterval(counter);
                    } else {
                        stat.textContent = Math.ceil(current);
                    }
                }, stepTime);
            }
        });
    }

    setTimeout(animateCounters, 500);

    // ==========================================
    // 4. COURSE ENROLLMENT CONFIRMATION
    // ==========================================
    
    const enrollForms = document.querySelectorAll('form[action="/student/enroll"]');
    
    enrollForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const confirmEnroll = confirm('Are you sure you want to enroll in this course?');
            if (!confirmEnroll) {
                e.preventDefault();
            }
        });
    });

    // ==========================================
    // 5. NOTIFICATION BELL
    // ==========================================
    
    const notificationBell = document.querySelector('.notification');
    if (notificationBell) {
        notificationBell.addEventListener('click', function() {
            alert('📬 You have 3 new notifications!');
        });
    }

    // ==========================================
    // 6. VIEW COURSE BUTTONS
    // ==========================================
    
    const viewCourseBtns = document.querySelectorAll('.course-card .btn-primary');
    
    viewCourseBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            // Let the link do its job - just a small animation
            this.innerHTML = '<i class="bi bi-hourglass-split"></i> Loading...';
            this.classList.add('disabled');
        });
    });

    // ==========================================
    // 7. LOGOUT CONFIRMATION
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
    // 8. KEYBOARD SHORTCUTS
    // ==========================================
    
    document.addEventListener('keydown', function(e) {
        // Ctrl + D -> Dashboard
        if (e.ctrlKey && e.key === 'd') {
            e.preventDefault();
            window.location.href = '/student-dashboard';
        }
        // Ctrl + C -> Courses
        if (e.ctrlKey && e.key === 'c') {
            e.preventDefault();
            document.getElementById('enrolled-courses')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
        // Ctrl + A -> Available Courses
        if (e.ctrlKey && e.key === 'a') {
            e.preventDefault();
            document.getElementById('available-courses')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
    });

    // ==========================================
    // 9. CONSOLE HELPERS
    // ==========================================
    
    console.log('ℹ️ Type showStudentInfo() to see student details');
    
    window.showStudentInfo = function() {
        const studentName = document.querySelector('.user-info h6')?.textContent || 'Student';
        const enrolledCount = document.querySelectorAll('.course-card.enrolled').length;
        const availableCount = document.querySelectorAll('.course-card.available').length;
        
        console.log(`📊 Student: ${studentName}`);
        console.log(`📚 Enrolled Courses: ${enrolledCount}`);
        console.log(`📖 Available Courses: ${availableCount}`);
        console.log(`✅ Total Achievements: 2`);
    };

    // Auto-execute on load
    setTimeout(() => {
        console.log('✅ Dashboard ready!');
        console.log('💡 Tip: Type showStudentInfo() for student details');
        console.log('💡 Shortcuts: Ctrl+D (Dashboard) | Ctrl+C (Courses) | Ctrl+A (Available)');
    }, 1000);

});

console.log('🎓 EduMania Student Dashboard - Fully Loaded!');