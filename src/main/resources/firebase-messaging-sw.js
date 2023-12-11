
console.log('Service Worker registration successful');


const firebaseConfig = {
	apiKey: "AIzaSyBZ7fGTR8U0_K23g6AQbVjggBMHfxqDzps",
	authDomain: "luuanh-7c9d9.firebaseapp.com",
	projectId: "luuanh-7c9d9",
	storageBucket: "luuanh-7c9d9.appspot.com",
	messagingSenderId: "823176678071",
	appId: "1:823176678071:web:15b3891160ca6485499b32",
	measurementId: "G-1PXRNZHJEB"
};

firebase.initializeApp(firebaseConfig);


const messaging = firebase.messaging();


/*messaging.onMessage((payload) => {
	console.log('Message received. ', payload);
	// ...
});*/


/*
// Import các tệp script cần thiết
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');


const Config = {
	apiKey: "AIzaSyBZ7fGTR8U0_K23g6AQbVjggBMHfxqDzps",
	authDomain: "luuanh-7c9d9.firebaseapp.com",
	projectId: "luuanh-7c9d9",
	storageBucket: "luuanh-7c9d9.appspot.com",
	messagingSenderId: "823176678071",
	appId: "1:823176678071:web:15b3891160ca6485499b32",
	measurementId: "G-1PXRNZHJEB"
};

firebase.initializeApp(Config);





// Lấy đối tượng messaging từ Firebase
const messaging = firebase.messaging();

// Lắng nghe sự kiện khi tin nhắn được nhận trong trạng thái background
messaging.onBackgroundMessage((payload) => {
  console.log('Background message received:', payload);

  // Thực hiện xử lý tin nhắn khi ứng dụng ở trạng thái background
  // Ví dụ: Hiển thị thông báo
  const notificationOptions = {
    body: payload.notification.body,
    icon: payload.notification.icon
  };

  self.registration.showNotification(
    payload.notification.title,
    notificationOptions
  );
});
*/