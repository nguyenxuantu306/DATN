//Toast
document.addEventListener("DOMContentLoaded", function () {
    const currentURL = window.location.pathname;

    if (currentURL === "/login") {
        localStorage.setItem("loggedIn", "true");
    } else {
        localStorage.removeItem("loggedIn");
    }
});

//Profile
document.getElementById('fileInput').addEventListener('change', function(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var imageSrc = reader.result;
        document.getElementById('previewImage').setAttribute('src', imageSrc);
    };
    reader.readAsDataURL(event.target.files[0]);
});