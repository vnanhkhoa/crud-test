$(document).ready(function (){
    $("#password-field i").click(function (){
        $(this).toggleClass("fa-eye-slash");
        const input = $("input[name='password']");
        if (input.attr("type") == "password") {
            input.attr("type", "text");
        } else {
            input.attr("type", "password");
        }
    });
});