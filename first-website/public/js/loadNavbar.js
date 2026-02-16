document.addEventListener('DOMContentLoaded', function() {
    const mobileNavbar = `
        <nav class="mobile-nav">
            <a href="index.html" class="home-link"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M12 2L2 12h3v8h6v-6h2v6h6v-8h3L12 2z" fill=#000000/>
                </svg>
                <span class="nav-text">Kodu</span>
            </a>
            <a href="tooted.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M3 6h18v2H3V6zm0 5h18v2H3v-2zm0 5h18v2H3v-2z" fill=#000000/>
                </svg>
                <span class="nav-text">Tooted</span>
            </a>
            <a href="limited.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2zm-2 1H8v-6c0-2.48 1.51-4.5 4-4.5s4 2.02 4 4.5v6z" fill=#000000/>
                </svg>
                <span class="nav-text">limited</span>
            </a>
            <a href="asukohad.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M17 3H7c-1.1 0-2 .9-2 2v16l7-3 7 3V5c0-1.1-.9-2-2-2z"/>
                </svg>
                <span class="nav-text">Asukohad</span>
            </a>
            <a href="kättesaamine.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M20 8h-3V4H3c-1.1 0-2 .9-2 2v11h2c0 1.66 1.34 3 3 3s3-1.34 3-3h6c0 1.66 1.34 3 3 3s3-1.34 3-3h2v-5l-3-4zm-7 7H8V9h5v6zm7 0h-5v-3h2l3 3z" fill=#000000/>
                </svg>
                <span class="nav-text">Kättesaamine</span>
            </a>
            <a href="meist.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 17h-2v-2h2v2zm2.07-7.75l-.9.92C13.45 12.9 13 13.5 13 15h-2v-.5c0-1.1.45-2.1 1.17-2.83l1.24-1.26c.37-.36.59-.86.59-1.41 0-1.1-.9-2-2-2s-2 .9-2 2H8c0-2.21 1.79-4 4-4s4 1.79 4 4c0 .88-.36 1.68-.93 2.25z" fill=#000000/>
                </svg>
                <span class="nav-text">Meist</span>
            </a>
            <a href="login.html"/a>
                <svg class="nav-icon" viewBox="0 0 24 24">
                    <path d="M10.09 15.59L11.5 17l5-5-5-5-1.41 1.41L12.67 11H3v2h9.67l-2.58 2.59zM19 3H5c-1.11 0-2 .9-2 2v4h2V5h14v14H5v-4H3v4c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z" fill=#000000/>
                </svg>
                <span class="nav-text">Logi sisse</span>
            </a>
        </nav>
    `;

    function loadAppropriateNavbar() {
        const navbarContainer = document.getElementById('navbar-container');
        
        navbarContainer.innerHTML = '';
    
        if (window.innerWidth < 768) {
            navbarContainer.innerHTML = mobileNavbar;
        } else {
            fetch('navbar.html')
                .then(response => response.text())
                .then(data => {
                    navbarContainer.innerHTML = data;
                })
                .catch(error => console.error('Error loading navbar:', error));
        }
    }

    loadAppropriateNavbar();

    let resizeTimeout;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(loadAppropriateNavbar, 250);
    });
});