document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('analogClock');
    const ctx = canvas.getContext('2d');
    const timeDisplay = document.getElementById('timeDisplay');
    const syncBtn = document.getElementById('syncBtn');
    
    // Monitor Elements
    const monitorHM = document.getElementById('angleHM');
    const monitorMS = document.getElementById('angleMS');
    const monitorTrig = document.getElementById('trigOp');

    // Kaalka Instance
    const kaalka = new Kaalka();
    let cH = 0, cM = 0, cS = 0;
    let isDragging = false;

    // Tabs
    const tabBtns = document.querySelectorAll('.tab-btn');
    const standardTab = document.getElementById('standardTab');
    const envelopeTab = document.getElementById('envelopeTab');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            const mode = btn.dataset.mode;
            standardTab.style.display = mode === 'basic' ? 'block' : 'none';
            envelopeTab.style.display = mode === 'envelope' ? 'block' : 'none';
        });
    });

    const inputData = document.getElementById('inputData');
    const outputData = document.getElementById('outputData');
    const encryptBtn = document.getElementById('encryptBtn');
    const decryptBtn = document.getElementById('decryptBtn');

    function init() {
        syncWithSystemTime();
        setupEvents();
        requestAnimationFrame(tick);
    }

    function syncWithSystemTime() {
        const now = new Date();
        cH = now.getHours() % 12;
        cM = now.getMinutes();
        cS = now.getSeconds();
        updateDisplay();
    }

    function updateDisplay() {
        const h = String(Math.floor(cH)).padStart(2, '0');
        const m = String(Math.floor(cM)).padStart(2, '0');
        const s = String(Math.floor(cS)).padStart(2, '0');
        timeDisplay.textContent = `${h}:${m}:${s}`;
        
        kaalka.setTime(cH, cM, cS);
        updateMonitor();
    }

    function updateMonitor() {
        const [aHM, aMS, aHS] = kaalka._getAngles();
        monitorHM.textContent = `${aHM.toFixed(2)}°`;
        monitorMS.textContent = `${aMS.toFixed(2)}°`;
        
        // Determine Trig Op based on active angle quadrant (Simplified visualization)
        const q = Math.floor(aHM / 90) + 1;
        const ops = ["SIN", "COS", "TAN", "COT"];
        monitorTrig.textContent = ops[q-1] || "SIN";
    }

    function tick() {
        drawClock();
        requestAnimationFrame(tick);
    }

    function drawClock() {
        const r = canvas.width / 2;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.save();
        ctx.translate(r, r);

        // Face
        ctx.beginPath();
        ctx.arc(0, 0, r - 30, 0, 2 * Math.PI);
        ctx.strokeStyle = 'rgba(255,255,255,0.05)';
        ctx.stroke();

        // High-precision ticks
        for (let i = 0; i < 60; i++) {
            const ang = (i * Math.PI) / 30;
            const isH = i % 5 === 0;
            ctx.rotate(ang);
            ctx.beginPath();
            ctx.moveTo(0, -(r - 35));
            ctx.lineTo(0, -(r - (isH ? 50 : 40)));
            ctx.strokeStyle = isH ? '#fff' : '#444';
            ctx.lineWidth = isH ? 2 : 1;
            ctx.stroke();
            ctx.rotate(-ang);
        }

        const hp = (cH * Math.PI / 6) + (cM * Math.PI / 360);
        const mp = (cM * Math.PI / 30) + (cS * Math.PI / 1800);
        const sp = (cS * Math.PI / 30);

        ctx.shadowBlur = 15;
        ctx.shadowColor = 'rgba(0,0,0,0.8)';

        dHand(ctx, hp, r * 0.5, 8, '#fff');
        dHand(ctx, mp, r * 0.75, 5, '#fff');
        ctx.shadowBlur = 0;
        dHand(ctx, sp, r * 0.85, 2, '#6366f1');

        ctx.beginPath();
        ctx.arc(0,0, 6, 0, 2*Math.PI);
        ctx.fillStyle = '#6366f1';
        ctx.fill();

        ctx.restore();
    }

    function dHand(ctx, pos, len, w, color) {
        ctx.save();
        ctx.rotate(pos);
        ctx.beginPath();
        ctx.lineWidth = w;
        ctx.lineCap = 'round';
        ctx.strokeStyle = color;
        ctx.moveTo(0, 10);
        ctx.lineTo(0, -len);
        ctx.stroke();
        ctx.restore();
    }

    function setupEvents() {
        syncBtn.addEventListener('click', syncWithSystemTime);

        encryptBtn.addEventListener('click', () => {
            if (inputData.value) {
                const res = kaalka.encrypt(inputData.value);
                outputData.value = res;
            }
        });

        decryptBtn.addEventListener('click', () => {
            if (inputData.value) {
                const res = kaalka.decrypt(inputData.value);
                outputData.value = res;
            }
        });

        // Envelope Mode
        document.getElementById('genSundial').addEventListener('click', () => {
            const sd = Kaalka.generateSundial();
            outputData.value = `Sundial Beacon: ${sd.beacon}\n\n(Secret kept in ephemeral memory)`;
        });

        document.getElementById('sealBtn').addEventListener('click', () => {
            const msg = document.getElementById('envInput').value;
            const bcn = document.getElementById('recipientBeacon').value;
            if (msg && bcn) {
                const env = Kaalka.encryptEnvelope(msg, bcn, null);
                outputData.value = JSON.stringify(env, null, 2);
            }
        });

        document.getElementById('copyBtn').addEventListener('click', () => {
            outputData.select();
            document.execCommand('copy');
        });

        // Drag
        canvas.addEventListener('mousedown', () => isDragging = true);
        window.addEventListener('mouseup', () => isDragging = false);
        canvas.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            const rect = canvas.getBoundingClientRect();
            const x = e.clientX - rect.left - canvas.width / 2;
            const y = e.clientY - rect.top - canvas.height / 2;
            const angle = (Math.atan2(y, x) + Math.PI / 2 + 2 * Math.PI) % (2 * Math.PI);
            cM = (angle / (2 * Math.PI)) * 60;
            updateDisplay();
        });
    }

    init();
});
