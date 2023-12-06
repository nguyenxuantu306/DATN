const form = document.getElementById('form');
const password1El = document.getElementById('newpass');
const password2El = document.getElementById('confirmpass');
const messageContainer = document.querySelector('.message-container');
const message = document.getElementById('message');

let passwordsMatch = false;

function validateForm() {
  isValid = form.checkValidity();

  if (password1El.value === password2El.value) {
    passwordsMatch = true;
    password1El.style.borderColor = 'green';
    password2El.style.borderColor = 'green';
    message.textContent = 'Passwords match.';
    message.style.color = 'green';
    messageContainer.style.borderColor = 'green';
  } else {
    passwordsMatch = false;
    message.textContent = 'Make sure passwords match.';
    message.style.color = 'red';
    messageContainer.style.borderColor = 'red';
    password1El.style.borderColor = 'red';
    password2El.style.borderColor = 'red';
  }
}

function processFormData(e) {
  e.preventDefault();
  validateForm();
  if (isValid && passwordsMatch) {
    // Nếu form hợp lệ và mật khẩu khớp nhau, thực hiện xử lý dữ liệu ở đây
    storeFormData();
  }
}

// Thêm sự kiện nghe cho các trường mật khẩu để kiểm tra khi người dùng nhập dữ liệu
password1El.addEventListener('input', validateForm);
password2El.addEventListener('input', validateForm);

function storeFormData() {
  const user = {
    name: form.name.value,
    phone: form.phone.value,
    email: form.email.value,
    website: form.website.value,
    password: form.password.value,
  };
  console.log(user);
}



// Event Listeners
form.addEventListener('submit', processFormData);

function togglePasswordVisibility() {
    var passwordField = document.getElementById('password'); // Lấy trường input password
    var passwordIcon = document.getElementById('passwordToggleIcon'); // Lấy icon để thay đổi trạng thái (hiển thị/ẩn)

    if (passwordField.type === 'password') {
        passwordField.type = 'text'; // Nếu đang là password, chuyển sang kiểu text để hiển thị mật khẩu
        passwordIcon.classList.remove('fa-eye-slash');
        passwordIcon.classList.add('fa-eye');
    } else {
        passwordField.type = 'password'; // Nếu đang là text, chuyển lại sang kiểu password để ẩn mật khẩu
        passwordIcon.classList.remove('fa-eye');
        passwordIcon.classList.add('fa-eye-slash');
    }
}