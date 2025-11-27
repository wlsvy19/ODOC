let timerElement = document.getElementById("timer");
let startButton = document.getElementById("start");
let pauseButton = document.getElementById("pause");
let resetButton = document.getElementById("reset");

let interval; // 타이머를 저장할 변수
let elapsedTime = 0; // 경과 시간 (밀리초 단위)
let isRunning = false; // 타이머 실행 상태

// 시간 형식 변환 함수
function formatTime(ms) {
  let totalSeconds = Math.floor(ms / 1000);
  let hours = String(Math.floor(totalSeconds / 3600)).padStart(2, "0");
  let minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, "0");
  let seconds = String(totalSeconds % 60).padStart(2, "0");
  return `${hours}:${minutes}:${seconds}`;
}

// 타이머 업데이트 함수
function updateTimer() {
  timerElement.textContent = formatTime(elapsedTime);
}

// 시작 버튼 클릭 이벤트
startButton.addEventListener("click", () => {
  if (!isRunning) {
    isRunning = true;
    let startTime = Date.now() - elapsedTime; // 재개 시 이전 시간부터 시작
    interval = setInterval(() => {
      elapsedTime = Date.now() - startTime;
      updateTimer();
    }, 100);
  }
});

// 일시정지 버튼 클릭 이벤트
pauseButton.addEventListener("click", () => {
  if (isRunning) {
    isRunning = false;
    clearInterval(interval);
  }
});

// 초기화 버튼 클릭 이벤트
resetButton.addEventListener("click", () => {
  isRunning = false;
  clearInterval(interval);
  elapsedTime = 0;
  updateTimer();
});

// 초기 상태 표시
updateTimer();
