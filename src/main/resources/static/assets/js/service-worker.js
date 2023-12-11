
// See: https://github.com/microsoft/TypeScript/issues/14877
/** @type {ServiceWorkerGlobalScope} */
let self;

function initInSw() {
  // [START messaging_init_in_sw]
  // Give the service worker access to Firebase Messaging.
  // Note that you can only use Firebase Messaging here. Other Firebase libraries
  // are not available in the service worker.
  importScripts('https://www.gstatic.com/firebasejs/8.10.1/firebase-app.js');
  importScripts('https://www.gstatic.com/firebasejs/8.10.1/firebase-messaging.js');

  // Initialize the Firebase app in the service worker by passing in
  // your app's Firebase config object.
  // https://firebase.google.com/docs/web/setup#config-object
  firebase.initializeApp({
    apiKey: "AIzaSyBZ7fGTR8U0_K23g6AQbVjggBMHfxqDzps",
	authDomain: "luuanh-7c9d9.firebaseapp.com",
	projectId: "luuanh-7c9d9",
	storageBucket: "luuanh-7c9d9.appspot.com",
	messagingSenderId: "823176678071",
	appId: "1:823176678071:web:15b3891160ca6485499b32",
	measurementId: "G-1PXRNZHJEB"
  });
 

  // Retrieve an instance of Firebase Messaging so that it can handle background
  // messages.
  const messaging = firebase.messaging();
  // [END messaging_init_in_sw]
}


function onBackgroundMessage() {
  const messaging = firebase.messaging();

  // [START messaging_on_background_message]
  messaging.onBackgroundMessage((payload) => {
    console.log(
      '[firebase-messaging-sw.js] Received background message ',
      payload
    );
    // Customize notification here
    const notificationTitle = 'Background Message Title';
    const notificationOptions = {
      body: 'Background Message body.'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
  });
  // [END messaging_on_background_message]
}
