import http from 'k6/http';

export let options = {
  vus: 1000, // concurrent users
  duration: '30s',
};

export default function () {
  const payload = JSON.stringify({
    longUrl: "https://example.com/test-" + Math.random()
  });

  http.post('http://localhost:8080/api/v1/shorten', payload, {
    headers: { 'Content-Type': 'application/json' },
  });
}