if ('serviceWorker' in navigator) {
  window.addEventListener('load', function() {
    navigator.serviceWorker.register('/serviceWorker.js', { scope: '/' }).then(function(registration) {
      // Notificando el registro del service workwe
      console.log('SW: Done: ', registration.scope);
    }, function(err) {
      console.log('SW: Error: ', err);
    });
  });
}

console.log('SW Registered!');