import axios from 'axios'

const API_BASE_URL = '/api'

export const testLangAPI = {
    validate: (sourceCode) =>
        axios.post(`${API_BASE_URL}/validate`, { source: sourceCode }),

    runTests: (sourceCode) =>
        axios.post(`${API_BASE_URL}/run`, { source: sourceCode }),

    // You can add more API calls here as your backend grows
    login: (credentials) =>
        axios.post(`${API_BASE_URL}/login`, credentials),

    getUser: (id) =>
        axios.get(`${API_BASE_URL}/users/${id}`),

    updateUser: (id, updates) =>
        axios.put(`${API_BASE_URL}/users/${id}`, updates)
}