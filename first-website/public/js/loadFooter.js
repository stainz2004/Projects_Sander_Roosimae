document.addEventListener('DOMContentLoaded', function () {
    if (window.innerWidth >= 768) {
        fetch('footer.html')
            .then(response => response.text())
            .then(data => {
                const footerContainer = document.getElementById('footer-container');
                if (footerContainer) {
                    footerContainer.innerHTML = data;
                } else {
                    console.error('Footer container not found.');
                }
            })
            .catch(error => console.error('Error loading footer:', error));
    } else {
        console.log('Footer not loaded for mobile view.');
    }
});