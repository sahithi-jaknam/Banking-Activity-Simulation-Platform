const BASE_URL = "https://banking-platform-uyt2.onrender.com";

/* ── Navigation ─────────────────────────────────────────── */
function showSection(id, btn) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active-section'));
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    document.getElementById(id).classList.add('active-section');
    if (btn) btn.classList.add('active');
}

/* ── Result helpers ─────────────────────────────────────── */
function showResult(elementId, html, isError) {
    const el = document.getElementById(elementId);
    el.classList.remove('hidden', 'success', 'error');
    el.classList.add(isError ? 'error' : 'success');
    el.innerHTML = html;
}

function hideResult(elementId) {
    const el = document.getElementById(elementId);
    el.classList.add('hidden');
    el.innerHTML = '';
}

function isApiError(data) {
    return data && data.status && data.status >= 400;
}

/* ── Create Account ─────────────────────────────────────── */
function createAccount() {
    const name           = document.getElementById('c-name').value.trim();
    const email          = document.getElementById('c-email').value.trim();
    const openingBalance = document.getElementById('c-balance').value.trim();

    if (!name || !email || !openingBalance) {
        showResult('create-result', '⚠️ All fields are required.', true);
        return;
    }

    fetch(`${BASE_URL}/api/accounts`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, openingBalance: parseFloat(openingBalance) })
    })
    .then(res => res.json())
    .then(data => {
        if (isApiError(data)) {
            showResult('create-result', `❌ ${data.message}`, true);
        } else {
            showResult('create-result', `
                ✅ <strong>Account Created Successfully!</strong><br><br>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">Account Number</div>
                        <div class="detail-value">${data.accountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Holder Name</div>
                        <div class="detail-value">${data.holderName}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Email</div>
                        <div class="detail-value">${data.email}</div>
                    </div>
                    <div class="detail-item balance-highlight">
                        <div class="detail-label">Opening Balance</div>
                        <div class="detail-value">$${parseFloat(data.balance).toFixed(2)}</div>
                    </div>
                </div>`, false);
        }
    })
    .catch(err => showResult('create-result', `❌ Request failed: ${err.message}`, true));
}

/* ── Deposit ────────────────────────────────────────────── */
function deposite() {
    const accountNumber = document.getElementById('d-acc').value.trim();
    const amount        = document.getElementById('d-amount').value.trim();

    if (!accountNumber || !amount) {
        showResult('deposit-result', '⚠️ Account number and amount are required.', true);
        return;
    }

    fetch(`${BASE_URL}/api/accounts/${accountNumber}/deposit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount: parseFloat(amount) })
    })
    .then(res => res.json())
    .then(data => {
        if (isApiError(data)) {
            showResult('deposit-result', `❌ ${data.message}`, true);
        } else {
            showResult('deposit-result', `
                ✅ <strong>Deposit Successful!</strong><br><br>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">Account</div>
                        <div class="detail-value">${data.accountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Deposited Amount</div>
                        <div class="detail-value">$${parseFloat(data.amount).toFixed(2)}</div>
                    </div>
                    <div class="detail-item balance-highlight">
                        <div class="detail-label">New Balance</div>
                        <div class="detail-value">$${parseFloat(data.balanceAfter).toFixed(2)}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Type</div>
                        <div class="detail-value">${data.type}</div>
                    </div>
                </div>`, false);
        }
    })
    .catch(err => showResult('deposit-result', `❌ Request failed: ${err.message}`, true));
}

/* ── Withdraw ───────────────────────────────────────────── */
function withdraw() {
    const accountNumber = document.getElementById('w-acc').value.trim();
    const amount        = document.getElementById('w-amount').value.trim();

    if (!accountNumber || !amount) {
        showResult('withdraw-result', '⚠️ Account number and amount are required.', true);
        return;
    }

    fetch(`${BASE_URL}/api/accounts/${accountNumber}/withdraw`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount: parseFloat(amount) })
    })
    .then(res => res.json())
    .then(data => {
        if (isApiError(data)) {
            showResult('withdraw-result', `❌ ${data.message}`, true);
        } else {
            showResult('withdraw-result', `
                ✅ <strong>Withdrawal Successful!</strong><br><br>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">Account</div>
                        <div class="detail-value">${data.accountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Withdrawn Amount</div>
                        <div class="detail-value">$${parseFloat(data.amount).toFixed(2)}</div>
                    </div>
                    <div class="detail-item balance-highlight">
                        <div class="detail-label">Remaining Balance</div>
                        <div class="detail-value">$${parseFloat(data.balanceAfter).toFixed(2)}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Type</div>
                        <div class="detail-value">${data.type}</div>
                    </div>
                </div>`, false);
        }
    })
    .catch(err => showResult('withdraw-result', `❌ Request failed: ${err.message}`, true));
}

/* ── Transfer ───────────────────────────────────────────── */
function transfer() {
    const fromAccountNumber = document.getElementById('t-from').value.trim();
    const toAccountNumber   = document.getElementById('t-to').value.trim();
    const amount            = document.getElementById('t-amount').value.trim();

    if (!fromAccountNumber || !toAccountNumber || !amount) {
        showResult('transfer-result', '⚠️ All fields are required.', true);
        return;
    }

    fetch(`${BASE_URL}/api/transactions/transfer`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ fromAccountNumber, toAccountNumber, amount: parseFloat(amount) })
    })
    .then(res => res.json())
    .then(data => {
        if (isApiError(data)) {
            showResult('transfer-result', `❌ ${data.message}`, true);
        } else {
            const tx = Array.isArray(data) ? data[0] : data;
            showResult('transfer-result', `
                ✅ <strong>Transfer Successful!</strong><br><br>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">From Account</div>
                        <div class="detail-value">${tx.accountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">To Account</div>
                        <div class="detail-value">${tx.targetAccountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Amount Transferred</div>
                        <div class="detail-value">$${parseFloat(tx.amount).toFixed(2)}</div>
                    </div>
                    <div class="detail-item balance-highlight">
                        <div class="detail-label">Sender Balance After</div>
                        <div class="detail-value">$${parseFloat(tx.balanceAfter).toFixed(2)}</div>
                    </div>
                </div>`, false);
        }
    })
    .catch(err => showResult('transfer-result', `❌ Request failed: ${err.message}`, true));
}

/* ── View Account ───────────────────────────────────────── */
function viewAccount() {
    const accountNumber = document.getElementById('v-acc').value.trim();

    if (!accountNumber) {
        showResult('view-result', '⚠️ Account number is required.', true);
        return;
    }

    fetch(`${BASE_URL}/api/accounts/${accountNumber}`)
    .then(res => res.json())
    .then(data => {
        if (isApiError(data)) {
            showResult('view-result', `❌ ${data.message}`, true);
        } else {
            const created = new Date(data.createdAt).toLocaleDateString('en-US', {
                year: 'numeric', month: 'long', day: 'numeric'
            });
            showResult('view-result', `
                🔍 <strong>Account Details</strong><br><br>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">Account Number</div>
                        <div class="detail-value">${data.accountNumber}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Holder Name</div>
                        <div class="detail-value">${data.holderName}</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Email</div>
                        <div class="detail-value">${data.email}</div>
                    </div>
                    <div class="detail-item balance-highlight">
                        <div class="detail-label">Current Balance</div>
                        <div class="detail-value">$${parseFloat(data.balance).toFixed(2)}</div>
                    </div>
                    <div class="detail-item" style="grid-column: span 2">
                        <div class="detail-label">Account Opened</div>
                        <div class="detail-value">${created}</div>
                    </div>
                </div>`, false);
        }
    })
    .catch(err => showResult('view-result', `❌ Request failed: ${err.message}`, true));
}

/* ── List All Accounts ──────────────────────────────────── */
function listAccounts() {
    fetch(`${BASE_URL}/api/accounts`)
    .then(res => res.json())
    .then(data => {
        if (!Array.isArray(data)) {
            showResult('list-result', `❌ ${data.message}`, true);
            return;
        }
        if (data.length === 0) {
            showResult('list-result', '📭 No accounts found. Create one first!', false);
            return;
        }
        const rows = data.map((a, i) => `
            <tr>
                <td>${i + 1}</td>
                <td><strong>${a.accountNumber}</strong></td>
                <td>${a.holderName}</td>
                <td>${a.email}</td>
                <td><strong style="color:#276749">$${parseFloat(a.balance).toFixed(2)}</strong></td>
            </tr>`).join('');

        showResult('list-result', `
            📋 <strong>${data.length} Account(s) Found</strong><br><br>
            <table class="account-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Account No.</th>
                        <th>Holder Name</th>
                        <th>Email</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>${rows}</tbody>
            </table>`, false);
    })
    .catch(err => showResult('list-result', `❌ Request failed: ${err.message}`, true));
}
