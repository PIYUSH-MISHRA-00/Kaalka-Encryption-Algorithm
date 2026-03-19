document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('analogClock');
    const ctx = canvas.getContext('2d');
    const timeDisplay = document.getElementById('timeDisplay');
    const syncBtn = document.getElementById('syncBtn');
    
    // Monitor Elements
    const monitorHM = document.getElementById('angleHM');
    const monitorMS = document.getElementById('angleMS');
    const monitorTrig = document.getElementById('trigOp');
    const entropyBar = document.getElementById('entropyBar');

    // Kaalka Instance
    const kaalka = new Kaalka();
    let cH = 0, cM = 0, cS = 0;
    let isDragging = false;
    let lastTime = 0;

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
        
        // Determine Trig Op based on active angle quadrant
        const q = Math.floor(aHM / 90) + 1;
        const ops = ["SIN", "COS", "TAN", "COT"];
        monitorTrig.textContent = ops[q-1] || "SIN";
        
        // Update Entropy Bar
        const entropy = ((aHM + aMS + aHS) / 1080) * 100;
        entropyBar.style.width = `${Math.min(100, Math.max(5, entropy))}%`;
    }

    function tick(timestamp) {
        drawClock();
        requestAnimationFrame(tick);
    }

    function drawClock() {
        const r = canvas.width / 2;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.save();
        ctx.translate(r, r);

        // Ultra Pro Clock Face
        ctx.beginPath();
        ctx.arc(0, 0, r - 30, 0, 2 * Math.PI);
        ctx.strokeStyle = 'rgba(255,255,255,0.05)';
        ctx.lineWidth = 1;
        ctx.stroke();

        // Dots and Ticks
        for (let i = 0; i < 60; i++) {
            const ang = (i * Math.PI) / 30;
            const isH = i % 5 === 0;
            ctx.rotate(ang);
            ctx.beginPath();
            ctx.moveTo(0, -(r - 35));
            ctx.lineTo(0, -(r - (isH ? 55 : 42)));
            ctx.strokeStyle = isH ? 'rgba(255,255,255,0.8)' : 'rgba(255,255,255,0.15)';
            ctx.lineWidth = isH ? 2 : 1;
            ctx.stroke();
            ctx.rotate(-ang);
        }

        const hp = (cH * Math.PI / 6) + (cM * Math.PI / 360);
        const mp = (cM * Math.PI / 30) + (cS * Math.PI / 1800);
        const sp = (cS * Math.PI / 30);

        // Hands with Glow
        ctx.shadowBlur = 20;
        ctx.shadowColor = 'rgba(0,0,0,1)';
        
        dHand(ctx, hp, r * 0.55, 8, '#ffffff'); // Hour
        dHand(ctx, mp, r * 0.82, 5, '#ffffff');  // Minute
        
        ctx.shadowBlur = 0;
        dHand(ctx, sp, r * 0.88, 2, '#6366f1'); // Second

        // Center hub
        ctx.beginPath();
        ctx.arc(0,0, 6, 0, 2*Math.PI);
        ctx.fillStyle = '#6366f1';
        ctx.fill();
        ctx.beginPath();
        ctx.arc(0,0, 2, 0, 2*Math.PI);
        ctx.fillStyle = '#000';
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
        ctx.moveTo(0, 15); // Tail
        ctx.lineTo(0, -len);
        ctx.stroke();
        ctx.restore();
    }

    function setupEvents() {
        syncBtn.addEventListener('click', () => {
            syncWithSystemTime();
            // Pulse Effect
            syncBtn.style.transform = 'scale(0.95)';
            setTimeout(() => syncBtn.style.transform = 'scale(1)', 100);
        });

        encryptBtn.addEventListener('click', () => {
            if (inputData.value) {
                const res = kaalka.encrypt(inputData.value);
                outputData.value = res;
                inputData.style.borderColor = 'var(--success)';
                setTimeout(() => inputData.style.borderColor = 'var(--glass-border)', 1000);
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
            outputData.value = `Sundial Beacon:\n${sd.beacon}\n\n[SECRET DATA ENCRYPTED IN BROWSER MEMORY]`;
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
            const icon = document.querySelector('#copyBtn i');
            icon.setAttribute('data-lucide', 'check');
            lucide.createIcons();
            setTimeout(() => {
                icon.setAttribute('data-lucide', 'copy');
                lucide.createIcons();
            }, 2000);
        });

        // Smooth Drag Interaction
        canvas.addEventListener('mousedown', (e) => isDragging = true);
        window.addEventListener('mouseup', () => isDragging = false);
        canvas.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            const rect = canvas.getBoundingClientRect();
            const x = e.clientX - rect.left - canvas.width / 2;
            const y = e.clientY - rect.top - canvas.height / 2;
            const angle = (Math.atan2(y, x) + Math.PI / 2 + 2 * Math.PI) % (2 * Math.PI);
            
            // Map angle to minutes
            cM = (angle / (2 * Math.PI)) * 60;
            // Update hours proportionally for realism
            cH = (cH % 12) + (cM / 3600); 

            updateDisplay();
        });
    }

    init();
});
