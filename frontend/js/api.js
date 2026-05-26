const BASE_URL = 'http://localhost:8081';

async function apiGet(endpoint) {
    const res = await fetch(BASE_URL + endpoint);
    if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        throw new Error(err.message || `요청 실패 (${res.status})`);
    }
    return res.json();
}

async function apiPost(endpoint, data) {
    const res = await fetch(BASE_URL + endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: data !== undefined ? JSON.stringify(data) : undefined,
    });
    if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        throw new Error(err.message || `요청 실패 (${res.status})`);
    }
    return res.json();
}

const api = {
    register:              (data)           => apiPost('/users/register', data),
    login:                 (data)           => apiPost('/users/login', data),
    checkEmail:            (email)          => apiGet(`/users/check-email?email=${encodeURIComponent(email)}`),
    getPlayState:          (userId)         => apiGet(`/play-states/users/${userId}`),
    createOrResetPlayState:(userId)         => apiPost(`/play-states/users/${userId}`),
    getCurrentScenario:    (userId)         => apiGet(`/scenarios/current/users/${userId}`),
    submitOption:          (userId, data)   => apiPost(`/play-logs/users/${userId}`, data),
    getEnding:             (userId)         => apiGet(`/api/play-states/users/${userId}/ending`),
    getSummary:            (userId)         => apiGet(`/api/play-states/users/${userId}/summary`),
};
