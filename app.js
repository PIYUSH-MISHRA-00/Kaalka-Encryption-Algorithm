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

    // Theme Toggle
    const themeToggle = document.getElementById('themeToggle');
    const body = document.body;

    // Load saved theme
    const savedTheme = localStorage.getItem('kaalka-theme') || 'dark';
    setTheme(savedTheme);

    themeToggle.addEventListener('click', () => {
        const currentTheme = body.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
        setTheme(currentTheme);
    });

    function setTheme(theme) {
        body.setAttribute('data-theme', theme);
        localStorage.setItem('kaalka-theme', theme);
        const icon = themeToggle.querySelector('[data-lucide]');
        icon.setAttribute('data-lucide', theme === 'light' ? 'moon' : 'sun');
        if (window.lucide) lucide.createIcons();
    }

    // Kaalka Instance
    const kaalka = new Kaalka();
    let cH = 0, cM = 0, cS = 0;
    let isDragging = false;
    let activeHand = null;

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
        if (window.lucide) lucide.createIcons();
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
        
        const q = Math.floor(aHM / 90) + 1;
        const ops = ["SIN", "COS", "TAN", "COT"];
        monitorTrig.textContent = ops[q-1] || "SIN";
        
        const entropy = ((aHM + aMS + aHS) / 1080) * 100;
        entropyBar.style.width = `${Math.min(100, Math.max(5, entropy))}%`;
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

        // Minimalist Face
        ctx.beginPath();
        ctx.arc(0, 0, r - 30, 0, 2 * Math.PI);
        const isLight = body.getAttribute('data-theme') === 'light';
        ctx.strokeStyle = isLight ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)';
        ctx.lineWidth = 1;
        ctx.stroke();

        // Ticks
        for (let i = 0; i < 12; i++) {
            const ang = (i * Math.PI) / 6;
            ctx.rotate(ang);
            ctx.beginPath();
            ctx.moveTo(0, -(r - 35));
            ctx.lineTo(0, -(r - 50));
            ctx.strokeStyle = isLight ? 'rgba(0,0,0,0.2)' : 'rgba(255,255,255,0.2)';
            ctx.lineWidth = 2;
            ctx.stroke();
            ctx.rotate(-ang);
        }

        const hp = (cH * Math.PI / 6) + (cM * Math.PI / 360);
        const mp = (cM * Math.PI / 30) + (cS * Math.PI / 1800);
        const sp = (cS * Math.PI / 30);
        
        const handColor = isLight ? '#171717' : '#EDEDED';
        const accentColor = '#06B6D4';

        dHand(ctx, hp, r * 0.5, 8, handColor); // Hour
        dHand(ctx, mp, r * 0.75, 5, handColor); // Minute
        dHand(ctx, sp, r * 0.85, 2, accentColor); // Second

        ctx.beginPath();
        ctx.arc(0, 0, 5, 0, 2 * Math.PI);
        ctx.fillStyle = accentColor;
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

    function getAngle(x, y) {
        return (Math.atan2(y, x) + Math.PI / 2 + 2 * Math.PI) % (2 * Math.PI);
    }

    function setupEvents() {
        syncBtn.addEventListener('click', () => {
            syncWithSystemTime();
        });

        encryptBtn.addEventListener('click', () => {
            const data = inputData.value.trim();
            if (data) {
                try {
                    const res = kaalka.encrypt(data);
                    outputData.value = res;
                } catch (e) {
                    outputData.value = "Encryption failed.";
                }
            } else {
                outputData.value = "Please enter data to encrypt.";
            }
        });

        decryptBtn.addEventListener('click', () => {
            const data = inputData.value.trim();
            if (data) {
                const res = kaalka.decrypt(data);
                outputData.value = res;
            }
        });

        document.getElementById('genSundial').addEventListener('click', () => {
            const sd = Kaalka.generateSundial();
            outputData.value = `Beacon: ${sd.beacon}`;
        });

        document.getElementById('sealBtn').addEventListener('click', () => {
            const msg = document.getElementById('envInput').value;
            const bcn = document.getElementById('recipientBeacon').value;
            if (msg && bcn) {
                const env = Kaalka.encryptEnvelope(msg, bcn, null);
                outputData.value = JSON.stringify(env, null, 2);
            }
        });

        // Portfolio Style Copy Button
        const copyBtn = document.getElementById('copyBtn');
        copyBtn.addEventListener('click', () => {
            if (!outputData.value) return;
            
            navigator.clipboard.writeText(outputData.value).then(() => {
                const icon = copyBtn.querySelector('[data-lucide]');
                const text = copyBtn.querySelector('.copy-text');
                
                const originalIcon = icon.getAttribute('data-lucide');
                const originalText = text.textContent;

                // Success State
                const currentIcon = copyBtn.querySelector('[data-lucide]');
                currentIcon.setAttribute('data-lucide', 'check');
                text.textContent = 'Copied!';
                copyBtn.classList.add('success');
                if (window.lucide) lucide.createIcons();

                setTimeout(() => {
                    const newIcon = copyBtn.querySelector('[data-lucide]');
                    newIcon.setAttribute('data-lucide', originalIcon);
                    text.textContent = originalText;
                    copyBtn.classList.remove('success');
                    if (window.lucide) lucide.createIcons();
                }, 2000);
            });
        });

        // Interaction
        canvas.addEventListener('mousedown', (e) => {
            const rect = canvas.getBoundingClientRect();
            const x = e.clientX - rect.left - canvas.width / 2;
            const y = e.clientY - rect.top - canvas.height / 2;
            const clickAngle = getAngle(x, y);

            const hp = (cH * Math.PI / 6) % (2 * Math.PI);
            const mp = (cM * Math.PI / 30) % (2 * Math.PI);
            const sp = (cS * Math.PI / 30) % (2 * Math.PI);

            const dh = Math.abs(clickAngle - hp);
            const dm = Math.abs(clickAngle - mp);
            const ds = Math.abs(clickAngle - sp);

            const min = Math.min(dh, dm, ds);
            if (min < 0.4) {
                isDragging = true;
                if (min === ds) activeHand = 'S';
                else if (min === dm) activeHand = 'M';
                else activeHand = 'H';
            }
        });

        window.addEventListener('mouseup', () => {
            isDragging = false;
            activeHand = null;
        });

        canvas.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            const rect = canvas.getBoundingClientRect();
            const x = e.clientX - rect.left - canvas.width / 2;
            const y = e.clientY - rect.top - canvas.height / 2;
            const angle = getAngle(x, y);

            if (activeHand === 'S') cS = (angle / (2 * Math.PI)) * 60;
            else if (activeHand === 'M') cM = (angle / (2 * Math.PI)) * 60;
            else if (activeHand === 'H') cH = (angle / (2 * Math.PI)) * 12;

            updateDisplay();
        });
    }

    init();
});
