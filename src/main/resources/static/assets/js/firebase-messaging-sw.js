console.log("Service Worker registration successful  asset");

const firebaseConfig = {
  apiKey: "AIzaSyBZ7fGTR8U0_K23g6AQbVjggBMHfxqDzps",
  authDomain: "luuanh-7c9d9.firebaseapp.com",
  projectId: "luuanh-7c9d9",
  storageBucket: "luuanh-7c9d9.appspot.com",
  messagingSenderId: "823176678071",
  appId: "1:823176678071:web:15b3891160ca6485499b32",
  measurementId: "G-1PXRNZHJEB",
};

// Lấy đối tượng messaging từ Firebase
/*const messaging = firebase.messaging();


messaging.onMessage((payload) => {
	console.log('Message received. ', payload);
	// ...
});
*/
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging
  .requestPermission()
  .then(function () {
    //MsgElem.innerHTML = "Notification permission granted."
    console.log("Notification permission granted.");
    // get the token in the form of promise
    return messaging.getToken();
  })
  .then(function (token) {
    // Gửi token lên máy chủ
    saveTokenToServer(token);
    console.log(token);
  })
  .catch(function (err) {
    console.log("Unable to get permission to notify.", err);
  });

//bắt sự kiện nhận thông báo
messaging.onMessage(function (payload) {
  console.log(payload);
  var notify;
  notify = new Notification(payload.notification.title, {
    body: payload.notification.body,
    icon: payload.notification.icon,
    tag: "Dummy",
  });
  console.log(payload.notification);
});
