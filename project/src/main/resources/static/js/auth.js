const Auth = {
    save(userId, nickname) {
        localStorage.setItem('user_id', userId);
        localStorage.setItem('nickname', nickname);
    },

    getUserId() {
        const id = localStorage.getItem('user_id');
        return id ? Number(id) : null;
    },

    getNickname() {
        return localStorage.getItem('nickname');
    },

    clear() {
        localStorage.removeItem('user_id');
        localStorage.removeItem('nickname');
    },

    isLoggedIn() {
        return localStorage.getItem('user_id') !== null;
    },

    requireLogin() {
        if (!this.isLoggedIn()) {
            location.href = 'index.html';
            return false;
        }
        return true;
    },
};
