document.addEventListener('DOMContentLoaded', function () {
    const carousel = document.querySelector('.carousel');
    const images = document.querySelectorAll('.carousel img');
    const totalImages = images.length;
    let currentIndex = 0;

    function moveToNextSlide() {
        currentIndex = (currentIndex + 1) % totalImages;
        const slideWidth = images[0].clientWidth;
        carousel.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
    }

    setInterval(moveToNextSlide, 3000);

    window.addEventListener('resize', () => {
        carousel.style.transition = 'none';
        carousel.style.transform = `translateX(-${currentIndex * images[0].clientWidth}px)`;
    });
});