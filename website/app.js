document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('analogClock');
    const ctx = canvas.getContext('2d');
    const timeDisplay = document.getElementById('timeDisplay');
    const syncBtn = document.getElementById('syncBtn');
    
    // Core Elements
    const kaalka = new Kaalka();
    let currentHour = 0, currentMinute = 0, currentSecond = 0;
    let isDragging = false;

    // Mode Selection Logic
    const modeBtns = document.querySelectorAll('.mode-btn');
    const basicMode = document.getElementById('basicMode');
    const envelopeMode = document.getElementById('envelopeMode');

    modeBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            modeBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            const mode = btn.dataset.mode;
            basicMode.style.display = mode === 'basic' ? 'block' : 'none';
            envelopeMode.style.display = mode === 'envelope' ? 'block' : 'none';
        });
    });

    // Basic Mode Elements
    const inputData = document.getElementById('inputData');
    const outputData = document.getElementById('outputData');
    const encryptBtn = document.getElementById('encryptBtn');
    const decryptBtn = document.getElementById('decryptBtn');

    // Envelope Mode Elements
    const envInput = document.getElementById('envInput');
    const recipientBeacon = document.getElementById('recipientBeacon');
    const sealBtn = document.getElementById('sealBtn');
    const genSundialBtn = document.getElementById('genSundial');

    function init() {
        syncWithSystemTime();
        setupEvents();
        requestAnimationFrame(animate);
    }

    function syncWithSystemTime() {
        const now = new Date();
        currentHour = now.getHours() % 12;
        currentMinute = now.getMinutes();
        currentSecond = now.getSeconds();
        updateDisplay();
    }

    function updateDisplay() {
        const h = String(Math.floor(currentHour)).padStart(2, '0');
        const m = String(Math.floor(currentMinute)).padStart(2, '0');
        const s = String(Math.floor(currentSecond)).padStart(2, '0');
        timeDisplay.textContent = `${h}:${m}:${s}`;
        kaalka.setTime(currentHour, currentMinute, currentSecond);
    }

    function animate() {
        drawAppleClock();
        requestAnimationFrame(animate);
    }

    function drawAppleClock() {
        const radius = canvas.width / 2;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.save();
        ctx.translate(radius, radius);

        // Apple Watch Face Style
        ctx.beginPath();
        ctx.arc(0, 0, radius - 20, 0, 2 * Math.PI);
        ctx.strokeStyle = '#38383a';
        ctx.lineWidth = 1;
        ctx.stroke();

        // High-precision ticks
        for (let i = 0; i < 60; i++) {
            const angle = (i * Math.PI) / 30;
            const isHour = i % 5 === 0;
            ctx.rotate(angle);
            ctx.beginPath();
            ctx.moveTo(0, -(radius - 22));
            ctx.lineTo(0, -(radius - (isHour ? 35 : 28)));
            ctx.strokeStyle = isHour ? '#ffffff' : '#444446';
            ctx.lineWidth = isHour ? 2 : 1;
            ctx.stroke();
            ctx.rotate(-angle);
        }

        // Draggable Hands
        const hPos = (currentHour * Math.PI / 6) + (currentMinute * Math.PI / 360);
        const mPos = (currentMinute * Math.PI / 30) + (currentSecond * Math.PI / 1800);
        const sPos = (currentSecond * Math.PI / 30);

        // Shadow for hands
        ctx.shadowColor = 'rgba(0,0,0,0.5)';
        ctx.shadowBlur = 10;
        ctx.shadowOffsetY = 5;

        drawHand(ctx, hPos, radius * 0.55, 6, '#ffffff'); // Hour
        drawHand(ctx, mPos, radius * 0.8, 4, '#ffffff');  // Minute
        
        ctx.shadowBlur = 0; // No shadow for second hand
        drawHand(ctx, sPos, radius * 0.85, 2, '#ff3b30'); // Second (Apple Red)

        // Center hub
        ctx.beginPath();
        ctx.arc(0, 0, 4, 0, 2 * Math.PI);
        ctx.fillStyle = '#ff3b30';
        ctx.fill();
        ctx.beginPath();
        ctx.arc(0, 0, 1.5, 0, 2 * Math.PI);
        ctx.fillStyle = '#000000';
        ctx.fill();

        ctx.restore();
    }

    function drawHand(ctx, pos, len, width, color) {
        ctx.save();
        ctx.rotate(pos);
        ctx.beginPath();
        ctx.lineWidth = width;
        ctx.lineCap = 'round';
        ctx.strokeStyle = color;
        ctx.moveTo(0, 8); // Tail
        ctx.lineTo(0, -len);
        ctx.stroke();
        ctx.restore();
    }

    function setupEvents() {
        syncBtn.addEventListener('click', syncWithSystemTime);

        // Basic Action
        encryptBtn.addEventListener('click', () => {
            if (inputData.value) outputData.value = kaalka.encrypt(inputData.value);
        });

        decryptBtn.addEventListener('click', () => {
            if (inputData.value) outputData.value = kaalka.decrypt(inputData.value);
        });

        // Envelope Action
        genSundialBtn.addEventListener('click', () => {
            const sd = Kaalka.generateSundial();
            outputData.value = `Sundial Generated:\nBeacon: ${sd.beacon}\nSecret: (Stored in memory)`;
            // Provide beacon to user
            console.log("Sundial Secret (KEEP PRIVATE):", sd.secret);
            alert("Sundial Beacon copied to Output. Keep your Secret safe!");
        });

        sealBtn.addEventListener('click', () => {
            if (!envInput.value || !recipientBeacon.value) {
                alert("Please provide message and recipient beacon.");
                return;
            }
            const env = Kaalka.encryptEnvelope(envInput.value, recipientBeacon.value, null);
            outputData.value = JSON.stringify(env, null, 2);
        });

        // Copy
        document.getElementById('copyBtn').addEventListener('click', () => {
            outputData.select();
            document.execCommand('copy');
        });

        // Interaction (Simplified drag)
        canvas.addEventListener('mousedown', () => isDragging = true);
        window.addEventListener('mouseup', () => isDragging = false);
        canvas.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            const rect = canvas.getBoundingClientRect();
            const x = e.clientX - rect.left - canvas.width / 2;
            const y = e.clientY - rect.top - canvas.height / 2;
            const angle = (Math.atan2(y, x) + Math.PI / 2 + 2 * Math.PI) % (2 * Math.PI);
            currentMinute = (angle / (2 * Math.PI)) * 60;
            updateDisplay();
        });
    }

    init();
});
