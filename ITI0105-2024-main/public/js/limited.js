window.addEventListener('load', () => {
    const circle = document.getElementById('circle');

    if (circle) {
        circle.style.animation = "fadeInScale 1s ease forwards";
    } else {
        console.error("Element 'circle' not found!");
    }
});