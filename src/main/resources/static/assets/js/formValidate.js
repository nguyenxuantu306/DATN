const form = document.getElementById('form');
const password1El = document.getElementById('newpass');
const password2El = document.getElementById('confirmpass');
const message = document.getElementById('message');

let passwordsMatch = false;

function validateForm() {
    isValid = form.checkValidity();

    const password = password1El.value;

    // Kiểm tra các yếu tố thiếu trong mật khẩu
    const hasUpperCase = /[A-Z]/.test(password);
    const hasNumber = /\d/.test(password);

    // Xác định yếu tố nào đang thiếu
    let missingRequirements = [];
    if (!hasUpperCase) {
        missingRequirements.push('ít nhất 1 chữ hoa');
    }
    if (!hasNumber) {
        missingRequirements.push('ít nhất 1 số');
    }


    // Hiển thị thông báo về yếu tố thiếu (nếu có)
    if (missingRequirements.length > 0) {
        const missingText = missingRequirements.join(', ');
        message.textContent = `Mật khẩu cần chứa ${missingText}.`;
        message.style.color = 'red';
        password1El.style.borderColor = 'red';
        password2El.style.borderColor = 'red';
        return; // Dừng lại nếu có yếu tố thiếu
    }

    if (password1El.value === password2El.value) {
        passwordsMatch = true;
        password1El.style.borderColor = 'green';
        password2El.style.borderColor = 'green';
        message.textContent = 'Mật khẩu đã khớp';
        message.style.color = 'green';
    } else {
        passwordsMatch = false;
        message.textContent = 'Mật khẩu không khớp.';
        message.style.color = 'red';
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


function togglePasswordVisibility(fieldId) {
    var passwordField = document.getElementById(fieldId); // Lấy trường input password
    var passwordIcon = document.getElementById(fieldId + 'ToggleIcon'); // Lấy icon để thay đổi trạng thái (hiển thị/ẩn)

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