import http from 'k6/http';

export let options = {
  vus: 200,
  duration: '30s',
};

export default function () {
   http.get('http://localhost:8080/4c93', {
    redirects: 0,
  });
}