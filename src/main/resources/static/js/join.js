function checkID(input) {
    var rules = [/[0-9]/, /[A-Z]/, /[a-z]/]
    return rules.every(function(r) { return r.test(input) });
}
function checkPass(input) {
    var rules = [/[0-9]/, /[A-Z]/, /[a-z]/,/[^A-Z a-z0-9]/]
    return rules.every(function(r) { return r.test(input) });
}

function isEmail(email) {
    var regex = /^([\w-\.]+@([\w-])+.([\w]){2,4})?$/;
    return regex.test(email);
}

let arrError = ["","","",""];

function showAlert(num) {
    $('.alert').show();
    $('.alert').text(arrError[num]);
    $("button[type='submit']").prop('disabled', true);
}

function hideAlert(){
    $('.alert').hide();
    $("button[type='submit']").prop('disabled', false);
}

function showFirst() {
    for (let i = 0; i < arrError.length; i++) {
        if (arrError[i].length > 1) {
            showAlert(i);
            return;
        }
    }
    hideAlert();
}

$(document).ready(function () {

    $("#upload_files").change(function (event) {
        var src = "";
        var mime_type = event.target.files[0].type.split("/");
        if(mime_type[0] == "image") {
            var imageObj = new Image();
            imageObj.src = URL.createObjectURL(event.target.files[0]);
            imageObj.onload = function () {
                if (imageObj.height <= 512 || imageObj.width <= 512) {
                    src = URL.createObjectURL(event.target.files[0]);
                    $("#add-image").attr('src', src)
                    arrError[0] = "";
                    showFirst();
                } else {
                    arrError[0] = 'Only images smaller than 512px*512px can be saved';
                    showAlert(0);
                }
            };
        } else {
            arrError[0] = 'Only image type';
            showAlert(0);
        }
    });

    $("input[name='email']").change(function (){
        const email = $(this).val()
        if(!isEmail(email)) {
            arrError[1] = 'Invalid email.';
            showAlert(1);
        } else {
            arrError[1] = ""
            showFirst();
        }
    });

    $("input[name='password']").change(function (){
        const pass = $(this).val()
        if (pass.length < 8) {
            arrError[2] = 'Password must contain at least 8 digits.';
            showAlert(2);
            return
        }
        if(!checkPass(pass)) {
            arrError[2] = 'Password must be at least one lowercase letter, one uppercase letter, one number, one special character each.';
            showAlert(2);
        } else {
            arrError[2] = "";
            showFirst();
        }

    });
    $("input[name='phoneNumber']").keyup(function (){
        let val = $(this).val();
        const phone = val.replace(/\D/g, '').replace(/(\d{1,3})(\d{1,4})?(\d{1,4})?/g, function(txt, f, s, t) {
            if (t) {
                return `${f}-${s}-${t}`
            } else if (s) {
                return `${f}-${s}`
            } else if (f) {
                return `${f}`
            }
        });

        $(this).val(phone);
    });
});