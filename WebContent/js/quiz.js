/**
 * Interactive Quiz Timer and Form Helper
 */

let secondsElapsed = 0;
let quizTimer = null;

document.addEventListener("DOMContentLoaded", () => {
    const timerDisplay = document.getElementById("quizTimerDisplay");
    if (timerDisplay) {
        quizTimer = setInterval(() => {
            secondsElapsed++;
            const mins = Math.floor(secondsElapsed / 60);
            const secs = secondsElapsed % 60;
            timerDisplay.innerText = `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
            
            const hiddenTime = document.getElementById("timeTakenInput");
            if (hiddenTime) hiddenTime.value = secondsElapsed;
        }, 1000);
    }
});
