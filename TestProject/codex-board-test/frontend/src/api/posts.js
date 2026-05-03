const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function getPosts() {
  return request('/api/posts');
}

export function getPost(id) {
  return request(`/api/posts/${id}`);
}

export function createPost(payload) {
  return request('/api/posts', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function updatePost(id, payload) {
  return request(`/api/posts/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export function deletePost(id) {
  return request(`/api/posts/${id}`, {
    method: 'DELETE',
  });
}

