	
console.log('Service Worker registration successful  asset');


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






// Lấy đối tượng messaging từ Firebase
/*const messaging = firebase.messaging();


messaging.onMessage((payload) => {
	console.log('Message received. ', payload);
	// ...
});
*/
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

