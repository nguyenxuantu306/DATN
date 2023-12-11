
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
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


const serviceWorkerPath = '/assets/js/firebase-messaging-sw.js';
//useServiceWorker(new URL('/assets/js/firebase-messaging-sw.js', import.meta.url));
export const askForNotification = async () => {
  const tempMessaging = await messaging();

  if (tempMessaging) {
    const newSw = await navigator.serviceWorker.register(serviceWorkerPath);

    return getNotificationPermission(
      async () => await getToken(tempMessaging, { vapidKey, serviceWorkerRegistration: newSw })
    );
  }
};
// Add the public key generated from the console here.


/*getToken(messaging, {vapidKey: "BAJU3eiDZ5Q2BakqLBU8o7LKnV1JZLeuy0l7YqIS55QBymWZIvOZcIvzAJseqQAlxQ9slhHhDAq_z2RLp077gbY"});
*/


// Get registration token. Initially this makes a network call, once retrieved
// subsequent calls to getToken will return from cache.




// Handle incoming messages. Called when:
// - a message is received while the app has focus
// - the user clicks on an app notification created by a service worker
//   `messaging.onBackgroundMessage` handler.
/*
export const askForNotification = async () => {
		  const tempMessaging = await messaging();

		  if (tempMessaging) {
		    const newSw = await navigator.serviceWorker.register(
		      '/assets/js/firebase-messaging-sw.js'
		    );

		    return getNotificationPermission(
		      async () => await getToken(tempMessaging, { vapidKey, serviceWorkerRegistration:newSw })
		    );
		  }
		};
*/
// Add the public key generated from the console here.
/*getToken(messaging, {vapidKey: "BAJU3eiDZ5Q2BakqLBU8o7LKnV1JZLeuy0l7YqIS55QBymWZIvOZcIvzAJseqQAlxQ9slhHhDAq_z2RLp077gbY"});
*/
messaging.getToken({ vapidKey: '<BAJU3eiDZ5Q2BakqLBU8o7LKnV1JZLeuy0l7YqIS55QBymW>' }).then((currentToken) => {
				if (currentToken) {
					console.log("curen token : " + currentToken);
					return currentToken;
					// Send the token to your server and update the UI if necessary
					// ...
				} else {
					// Show permission request UI
					console.log('No registration token available. Request permission to generate one.');
					// ...
				}
			}).catch((err) => {
				console.log('An error occurred while retrieving token. ', err);
				console.error('An error occurred while retrieving token. ', err);
				// ...
			});

function requestPermission() {
	console.log('Requesting permission...');


	Notification.requestPermission().then((permission) => {
		if (permission === 'granted') {
			console.log('Notification permission granted.');
console.log('Notification permission granted. hahahh');
	try {
        // Xin quyền thông báo
        const permission =  Notification.requestPermission();
        
        if (permission === 'granted') {
            console.log('Notification permission granted.');

            // Lấy đối tượng messaging từ Firebase
            const messaging = firebase.messaging();

            // Lấy token từ FCM
            const currentToken =  messaging.getToken({ vapidKey: '<BAJU3eiDZ5Q2BakqLBU8o7LKnV1JZLeuy0l7YqIS55QBymW>' });

            if (currentToken) {
                console.log("Current token: " + currentToken);

                // Gửi token đến máy chủ của bạn và cập nhật giao diện người dùng nếu cần
                // ...

                return currentToken;
            } else {
                console.log('No registration token available. Request permission to generate one.');
                // Hiển thị giao diện yêu cầu quyền nếu cần
                // ...
            }
        } 
    } catch (error) {
        console.error('An error occurred while requesting permission or retrieving token. ', error);
    }

			
		}else {
        console.warn('Notification permission denied');
    }
	})
};

requestPermission();

/*
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
});*/

/*
async function requestPermission() {
  console.log('Requesting permission...');

  try {
    await Notification.requestPermission();
    console.log('Notification permission granted.');

    const currentToken = await messaging.getToken();
    if (currentToken) {
      console.log('Current token:', currentToken);
      // Send the token to your server and update the UI if necessary
    } else {
      console.log('No registration token available. Request permission to generate one.');
      // Handle the case where no registration token is available
    }
  } catch (error) {
    console.error('An error occurred while requesting permission or retrieving token. ', error);
  }
}

requestPermission();*/

