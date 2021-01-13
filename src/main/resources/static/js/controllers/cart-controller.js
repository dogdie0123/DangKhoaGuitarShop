var cart = {
    init: function () {
        cart.regEvents();
    },
    regEvents: function () {
        $('#add_to_cart').off('click').on('click', function (e) {
            e.preventDefault();
            var result = document.getElementById('qty');
            var quantity = result.value;
            var productId = $(result).data('id');

            window.location.href = "/add-to-cart?productId=" + productId + "&quantity=" + quantity;
        });
        $('#add_to_wishlist').off('click').on('click', function (e) {
            e.preventDefault();
            var productId = $(result).data('id');

            window.location.href = "/add-to-wishlist?productId=" + productId;
        });

        $('.delete_item').off('click').on('click', function (e) {
            e.preventDefault();
            var productId = $(this).data('id');
            window.location.href = "/remove-from-cart?productId=" + productId;
        });


        $('.items-count').off('click').on('click', function (){
            var productId=$(this).data('productid');
            var isIncreasing=$(this).data('isincreasing');
            var inputQuantity=document.getElementById(productId);

            var sst=inputQuantity.value;
            if(isIncreasing===1){
                if( !isNaN( sst )) {
                    inputQuantity.value++;
                }
            }else{
                if( !isNaN( sst )&& sst > 1 ) {
                    inputQuantity.value--;
                }
            }

            var quantity = inputQuantity.value;

            $.ajax({
                contentType: 'application/json',
                url: '/update-cart',
                data: {"productId": productId, "quantity": quantity},
                dataType: 'json',
                type: 'GET',
                success: function (data) {
                    window.location.href="/gio-hang";
                }
            });
        });

        $('#delete_discount_code').off('click').on('click', function (e) {
            e.preventDefault();
            window.location.href = "/remove-discount-code";
        });
    }
};
cart.init();

var dialog = document.querySelector('dialog');

$(document).ready(function () {
    $("#checkoutForm").submit(function (e) {
        e.preventDefault();

        if(!(validateName() & validatePhone() & validateAddress())){
            return false;
        }

        ajaxPost();
    })

    function ajaxPost() {
        var fromData = {
            customerName: $("#customerName").val(),
            customerPhone: $("#customerPhone").val(),
            customerEmail: $("#customerEmail").val(),
            customerAddress: $("#customerAddress").val(),
            customerMessage: $("#customerMessage").val(),
            paymentMethod: $("input[type='radio']:checked").val(),
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/thanh-toan",
            data: JSON.stringify(fromData),
            success: function (res) {
                if(res) {
                    dialog.showModal();
                }
            },
            error: function (e) {
                alert("Đã xảy ra lỗi, đặt hàng không thành công!")
            }
        })
    }

    $(".closeDialog").on("click", function () {
        dialog.close();
    })

    function validateName(){
        var error = document.getElementById("customerNameError");
        error.innerHTML="";

        if($("#customerName").val() == ""){
            error.innerHTML = "Vui lòng nhập tên";
        }

        return "" === error.innerHTML;
    }

    function validatePhone(){
        var error = document.getElementById("customerPhoneError");
        error.innerHTML="";

        if($("#customerPhone").val() == ""){
            error.innerHTML = "Vui lòng nhập số điện thoại";
        }

        return "" === error.innerHTML;
    }

    function validateAddress(){
        var error = document.getElementById("customerAddressError");
        debugger
        error.innerHTML="";

        if($("#customerAddress").val() == ""){
            error.innerHTML = "Vui lòng nhập địa chỉ";
        }


        return "" === error.innerHTML;
    }
});

function fnCheckoutSuccess(res) {
    if (res.result) {
        //$("#checkoutForm").html(res.data);
        dialog.showModal();
    }
}

$(document).ready(function () {
    $(".closeDialog").on("click", function () {
        dialog.close();
    })
});