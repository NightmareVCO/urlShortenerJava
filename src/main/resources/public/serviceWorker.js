const CACHE_NAME = "LinkLoomCache-v1"
const fallback = "/logged"
const urlsToCache = [
  "/logged",
  "/templates/logged.html",
  "/index.css",
  "/js/toggleAvatarMenu.js"
];

self.addEventListener('install', function (event) {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(function (cache) {
        console.log('Open cache');
        return cache.addAll(urlsToCache);
      })
  );
});

self.addEventListener('activate', evt => {
  evt.waitUntil(
    caches.keys().then(function (keyList) {
      return Promise.all(keyList.map(function (key) {
        if (CACHE_NAME.indexOf(key) === -1) {
          return caches.delete(key);
        }
      }));
    })
  );
});

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request).then(response => {
      return response || fetch(event.request);
    }).catch(function () {
      return caches.match(fallback);
    })
  );
});

