const API_BASE = 'http://localhost:8080';

function headersComUsuario(extra = {}) {
    const usuarioId = localStorage.getItem('usuarioId');
    return usuarioId ? {...extra, 'X-Usuario-Id': usuarioId} : extra;
}

async function readResponse(res) {
    const text = await res.text();
    if (!text) return null;
    try {
        return JSON.parse(text);
    } catch {
        return text;
    }
}

async function apiGet(url) {
    const res = await fetch(API_BASE + url, {
        headers: headersComUsuario()
    });
    const data = await readResponse(res);
    if (!res.ok) throw new Error(typeof data === 'string' ? data : JSON.stringify(data));
    return data;
}

async function apiPost(url, dados) {
    const res = await fetch(API_BASE + url, {
        method: 'POST',
        headers: headersComUsuario({'Content-Type': 'application/json'}),
        body: JSON.stringify(dados)
    });
    const data = await readResponse(res);
    if (!res.ok) throw new Error(typeof data === 'string' ? data : JSON.stringify(data));
    return data;
}

async function apiPatch(url, dados = null) {
    const res = await fetch(API_BASE + url, {
        method: 'PATCH',
        headers: headersComUsuario({'Content-Type': 'application/json'}),
        body: dados ? JSON.stringify(dados) : undefined
    });
    const data = await readResponse(res);
    if (!res.ok) throw new Error(typeof data === 'string' ? data : JSON.stringify(data));
    return data;
}