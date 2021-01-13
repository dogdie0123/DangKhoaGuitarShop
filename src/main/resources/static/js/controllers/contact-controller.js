var dialog = document.querySelector('dialog');

$(document).ready(function () {
    $("#contactForm").submit(function (e) {
        e.preventDefault();

        if(!(validateName() & validateEmail() & validateTitle() & validateContent())){
            return false;
        }

        ajaxPost();
    })

    function ajaxPost() {
        var fromData = {
            name: $("#name").val(),
            email: $("#email").val(),
            title: $("#title").val(),
            messageContent: $("#messageContent").val()
        }

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/lien-he",
            data: JSON.stringify(fromData),
            success: function (res) {
                if(res) {
                    dialog.showModal();
                }
            },
            error: function (e) {
                alert("Gửi tin nhắn không thành công!")
            }
        })
    }

    $(".closeDialog").on("click", function () {
        dialog.close();
    })

    function validateName(){
        var error = document.getElementById("contactNameError");
        debugger
        error.innerHTML="";

        if($("#name").val() == ""){
            error.innerHTML = "*Vui lòng nhập tên";
        }

        return "" === error.innerHTML;
    }

    function validateEmail(){
        var error = document.getElementById("contactEmailError");
        debugger
        error.innerHTML="";

        var regx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        if($("#email").val() == ""){
            error.innerHTML = "*Vui lòng nhập email";
        }
        else if(!regx.test($("#email").val().toString())){
            error.innerHTML = "*Vui lòng nhập vào địa chỉ email hợp lệ";
        }

        return "" === error.innerHTML;
    }

    function validateTitle(){
        var error = document.getElementById("contactTitleError");
        debugger
        error.innerHTML="";

        if($("#title").val() == ""){
            error.innerHTML = "*Vui lòng nhập tiêu đề";
        }

        return "" === error.innerHTML;
    }

    function validateContent(){
        var error = document.getElementById("contactContentError");
        debugger
        error.innerHTML="";

        if($("#messageContent").val() == ""){
            error.innerHTML = "*Vui lòng nhập nội dung tin nhắn";
        }

        return "" === error.innerHTML;
    }
});
