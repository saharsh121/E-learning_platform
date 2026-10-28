/**
 * Admin Dashboard - JavaScript
 * EduMania - Admin Portal
 */

document.addEventListener('DOMContentLoaded', function() {
    
    console.log('👑 EduMania Admin Dashboard Loaded');

    // ==========================================
    // 1. SIDEBAR TOGGLE (Mobile)
    // ==========================================
    
    const sidebar = document.querySelector('.sidebar');
    const headerRight = document.querySelector('.header-right');
    
    const menuToggle = document.createElement('button');
    menuToggle.className = 'menu-toggle d-md-none';
    menuToggle.innerHTML = '<i class="bi bi-list"></i>';
    menuToggle.style.background = 'transparent';
    menuToggle.style.border = 'none';
    menuToggle.style.fontSize = '24px';
    menuToggle.style.color = '#333';
    
    const topHeader = document.querySelector('.top-header');
    topHeader.insertBefore(menuToggle, headerRight);

    menuToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        sidebar.classList.toggle('active');
    });

    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 768) {
            if (!sidebar.contains(e.target) && !menuToggle.contains(e.target)) {
                sidebar.classList.remove('active');
            }
        }
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
    // 4. LOGOUT CONFIRMATION
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
    // 5. AUTO-DISMISS ALERTS
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
    // 6. DELETE CONFIRMATION (for table delete buttons)
    // ==========================================
    
    document.querySelectorAll('.btn-delete-sm').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const form = this.closest('form');
            if (form) {
                const confirmDelete = confirm('⚠️ Are you sure you want to delete this user?');
                if (confirmDelete) {
                    form.submit();
                }
            }
        });
    });

    // ==========================================
    // 7. KEYBOARD SHORTCUTS
    // ==========================================
    
    document.addEventListener('keydown', function(e) {
        // Ctrl + D -> Dashboard
        if (e.ctrlKey && e.key === 'd') {
            e.preventDefault();
            window.location.href = '/admin-dashboard';
        }
        // Ctrl + S -> Add Student
        if (e.ctrlKey && e.key === 's') {
            e.preventDefault();
            document.getElementById('add-student')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
        // Ctrl + F -> Add Faculty
        if (e.ctrlKey && e.key === 'f') {
            e.preventDefault();
            document.getElementById('add-faculty')?.scrollIntoView({
                behavior: 'smooth'
            });
        }
    });

    // ==========================================
    // 8. CONSOLE HELPERS
    // ==========================================
    
    console.log('ℹ️ Type showAdminInfo() to see admin details');
    console.log('💡 Shortcuts: Ctrl+D (Dashboard) | Ctrl+S (Add Student) | Ctrl+F (Add Faculty)');
    
    window.showAdminInfo = function() {
        const adminName = document.querySelector('.user-info h6')?.textContent || 'Admin';
        const studentsCount = document.querySelector('#all-students .badge')?.textContent || '0';
        const facultyCount = document.querySelector('#all-faculty .badge')?.textContent || '0';
        
        console.log(`👑 Admin: ${adminName}`);
        console.log(`👨‍🎓 Total Students: ${studentsCount}`);
        console.log(`👨‍🏫 Total Faculty: ${facultyCount}`);
    };

    console.log('✅ Admin Dashboard ready!');
});