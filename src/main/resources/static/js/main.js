
$(document).ready(function () {
    "use strict";

    var window_width = $(window).width(),
        window_height = window.innerHeight,
        header_height = $(".default-header").height(),
        header_height_static = $(".site-header.static").outerHeight(),
        fitscreen = window_height - header_height,
        options = {
            max_value: 5,
            selected_symbol_type: 'fontawesome_star',
            step_size: 1,
            initial_value: 4,
        };

    /*---------- JavaScript for rating product ----------*/

    $(".rate").rate(options);
    $(".rate").on("change", function (ev, data) {
        $('#point').text(data.to);
    });

    $("#send_rating").click(function (e) {
        e.preventDefault();
        $.post("/Product/ProductDetail/", {
            id: model_id,
            number_of_stars: $(".rate").rate("getValue"),
        }, function (result) {
            //Load lại toàn bộ rating
            $("#average-rate").load(location.href + " #average-rate");
            $("#total-rate-1").load(location.href + " #total-rate-1");
            $("#total-rate-2").load(location.href + " #total-rate-2");
            $("#count-rate-0").load(location.href + " #count-rate-0");
            $("#count-rate-1").load(location.href + " #count-rate-1");
            $("#count-rate-2").load(location.href + " #count-rate-2");
            $("#count-rate-3").load(location.href + " #count-rate-3");
            $("#count-rate-4").load(location.href + " #count-rate-4");
            $.snackbar({ content: "Lưu xếp hạng thành công!" });
        }).fail(function () {
            $.snackbar({ content: "Lưu xếp hạng thất bại!" });
        })
    });

    (function (b) {
    b.toast = function (a, h, g, l, k) {
        b("#toast-container").length || (b("body").prepend('<div id="toast-container" aria-live="polite" aria-atomic="true"></div>'), b("#toast-container").append('<div id="toast-wrapper"></div>')); var c = "", d = "", e = "text-muted", f = "", m = "object" === typeof a ? a.title || "" : a || "Notice!"; h = "object" === typeof a ? a.subtitle || "" : h || ""; g = "object" === typeof a ? a.content || "" : g || ""; k = "object" === typeof a ? a.delay || 3E3 : k || 3E3; switch ("object" === typeof a ? a.type || "" : l || "info") {
            case "info": c = "bg-info";
                f = e = d = "text-white"; break; case "success": c = "bg-success"; f = e = d = "text-white"; break; case "warning": case "warn": c = "bg-warning"; f = e = d = "text-white"; break; case "error": case "danger": c = "bg-danger", f = e = d = "text-white"
        }a = '<div class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-delay="' + k + '">' + ('<div class="toast-header ' + c + " " + d + '">') + ('<strong class="mr-auto">' + m + "</strong>"); a += '<small class="' + e + '">' + h + "</small>"; a += '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">';
        a += '<span aria-hidden="true" class="' + f + '">&times;</span>'; a += "</button>"; a += "</div>"; "" !== g && (a += '<div class="toast-body">', a += g, a += "</div>"); a += "</div>"; b("#toast-wrapper").append(a); b("#toast-wrapper .toast:last").toast("show")
    }
    })(jQuery);


    function show_snack(text) {
        var type = 'info',
            content = text;

        $.toast({
            title: content,
            type: type,
            delay: 5000
        });
    }

    /*---------- End JavaScript for rating product ----------*/


    /*---------- Share button -----------*/

    $("#tw-share-btn").each(function () {
        var href = $(this).attr("href");
        href = href.replace("URL", document.URL);
        href = href.replace("Handle", "GuitarShop.UIT");
        href = href.replace("Title", "Chi tiết sản phẩm " + model_name);
        $(this).attr("href", href);
    });

    $("#tb-share-btn").each(function () {
        var href = $(this).attr("href");
        href = href.replace("Domain", window.location.origin);
        href = href.replace("ProductURL", document.URL);
        href = href.replace("Description", "Chi tiết sản phẩm " + model_name);
        $(this).attr("href", href);
    });

    $("#pi-share-btn").each(function () {
        var href = $(this).attr("href");
        href = href.replace("URL", document.URL);
        href = href.replace("Image_URL", "https://i.pinimg.com/originals/3e/65/b8/3e65b8aec35a7caaae6373209bec796e.jpg");
        href = href.replace("Description", "Chi tiết sản phẩm " + model_name);
        $(this).attr("href", href);
    });

    function PopupCenter(url, title, w, h) {
        // Fixes dual-screen position                         Most browsers      Firefox
        var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
        var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;

        var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
        var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

        var systemZoom = width / window.screen.availWidth;
        var left = (width - w) / 2 / systemZoom + dualScreenLeft
        var top = (height - h) / 2 / systemZoom + dualScreenTop
        var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w / systemZoom + ', height=' + h / systemZoom + ', top=' + top + ', left=' + left);

        // Puts focus on the newWindow
        if (window.focus) newWindow.focus();
    }

    $(".share-popup").click(function () {
        var width = 625, height = 350;
        var url = this.href;
        var domain = url.split("/")[2];
        switch (domain) {
            case "pinterest.com":
                width = 750;
                height = 657;
                break;
        }
        PopupCenter(url, '', width, height);
        return false;
    });

    /*---------- End share button -------*/

    $(".fullscreen").css("height", window_height)
    $(".fitscreen").css("height", fitscreen);

    //------- Active Nice Select --------//

    $('select').niceSelect();


    $('.navbar-nav li.dropdown').hover(function () {
        $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn(500);
    }, function () {
        $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut(500);
    });

    $('.img-pop-up').magnificPopup({
        type: 'image',
        gallery: {
            enabled: true
        }
    });

    // Search Toggle
    $("#search_input_box").hide();
    $("#search").on("click", function () {
        $("#search_input_box").slideToggle();
        $("#search_input").focus();
    });
    $("#close_search").on("click", function () {
        $('#search_input_box').slideUp(500);
    });

    function refreshMe() {
        var _brand = document.getElementsByName("brand").item.valueOf().toString();

        var radioValue = $("input[name='brand']:checked").val().toString();
        console.log(radioValue);
        var _style = document.getElementsByName("style");

        var f = {};
        f.url = '@Url.Action("ProductGrid", "Category")';
        f.type = "POST";
        f.dataType = "json";
        f.data = JSON.stringify({ id: 1, brand: radioValue, style: "dad" });
        f.contentType = "application/json";
        f.success = function (response) {
            alert("success");
        };
        f.error = function (response) {
            alert("failed");
        };
        $.ajax(f);

        $("#refreshDiv").load(" #refreshDiv ");
        console.log("refresh");
    }
    $(".pixel-radio").on("click", function () {
        refreshMe();
    })

	/*==========================
		javaScript for sticky header
		============================*/
    $(".sticky-header").sticky();

	/*=================================
	Javascript for banner area carousel
	==================================*/
    $(".active-banner-slider").owlCarousel({
        items: 1,
        autoplay: true,
        autoplayTimeout: 4000,
        autoplayHoverPause: true,
        autoplaySpeed: 1000,
        loop: true,
        nav: true,
        navSpeed:1000,
        navText: ["<img src='../../Content/img/banner/prev.png'>", "<img src='../../Content/img/banner/next.png'>"],
        dots: false        
    });

	/*=================================
	Javascript for product area carousel
	==================================*/
    $(".active-product-area").owlCarousel({
        items: 1,
        autoplay: false,
        autoplayTimeout: 5000,
        loop: true,
        nav: true,
        navText: ["<img src='../../Content/img/product/prev.png'>", "<img src='../../Content/img/product/next.png'>"],
        dots: false
    });

	/*=================================
	Javascript for single product area carousel
	==================================*/
    $(".s_Product_carousel").owlCarousel({
        items: 1,
        autoplay: true,
        autoplayTimeout: 4000,
        autoplayHoverPause: true,
        autoplaySpeed:1000,
        loop: true,
        nav: false,
        dots: true,
        dotsSpeed: 1000,
    });

	/*=================================
	Javascript for exclusive area carousel
	==================================*/
    $(".active-exclusive-product-slider").owlCarousel({
        items: 1,
        autoplay: false,
        autoplayTimeout: 5000,
        loop: true,
        nav: true,
        navText: ["<img src='../../Content/img/product/prev.png'>", "<img src='../../Content/img/product/next.png'>"],
        dots: false
    });

    //--------- Accordion Icon Change ---------//

    $('.collapse').on('shown.bs.collapse', function () {
        $(this).parent().find(".lnr-arrow-right").removeClass("lnr-arrow-right").addClass("lnr-arrow-left");
    }).on('hidden.bs.collapse', function () {
        $(this).parent().find(".lnr-arrow-left").removeClass("lnr-arrow-left").addClass("lnr-arrow-right");
    });

    // Select all links with hashes
    $('.main-menubar a[href*="#"]')
        // Remove links that don't actually link to anything
        .not('[href="#"]')
        .not('[href="#0"]')
        .click(function (event) {
            // On-page links
            if (
                location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '')
                &&
                location.hostname == this.hostname
            ) {
                // Figure out element to scroll to
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                // Does a scroll target exist?
                if (target.length) {
                    // Only prevent default if animation is actually gonna happen
                    event.preventDefault();
                    $('html, body').animate({
                        scrollTop: target.offset().top - 70
                    }, 1000, function () {
                        // Callback after animation
                        // Must change focus!
                        var $target = $(target);
                        $target.focus();
                        if ($target.is(":focus")) { // Checking if the target was focused
                            return false;
                        } else {
                            $target.attr('tabindex', '-1'); // Adding tabindex for elements not focusable
                            $target.focus(); // Set focus again
                        };
                    });
                }
            }
        });



    // -------   Mail Send ajax

    $(document).ready(function () {
        var form = $('#booking'); // contact form
        var submit = $('.submit-btn'); // submit button
        var alert = $('.alert-msg'); // alert div for show alert message

        // form submit event
        form.on('submit', function (e) {
            e.preventDefault(); // prevent default form submit

            $.ajax({
                url: 'booking.php', // form action url
                type: 'POST', // form submit method get/post
                dataType: 'html', // request type html/json/xml
                data: form.serialize(), // serialize form data
                beforeSend: function () {
                    alert.fadeOut();
                    submit.html('Sending....'); // change submit button text
                },
                success: function (data) {
                    alert.html(data).fadeIn(); // fade in response data
                    form.trigger('reset'); // reset form
                    submit.attr("style", "display: none !important");; // reset submit button text
                },
                error: function (e) {
                    console.log(e)
                }
            });
        });
    });




    $(document).ready(function () {
        $('#mc_embed_signup').find('form').ajaxChimp();
    });




    $('.quick-view-carousel-details').owlCarousel({
        loop: true,
        dots: true,
        items: 1,
    })



    //----- Active No ui slider --------//



    $(function () {

        if (document.getElementById("price-range")) {

            var nonLinearSlider = document.getElementById('price-range');

            noUiSlider.create(nonLinearSlider, {
                connect: true,
                behaviour: 'tap',
                start: [500, 4000],
                range: {
                    // Starting at 500, step the value by 500,
                    // until 4000 is reached. From there, step by 1000.
                    'min': [0],
                    '10%': [500, 500],
                    '50%': [4000, 1000],
                    'max': [10000]
                }
            });


            var nodes = [
                document.getElementById('lower-value'), // 0
                document.getElementById('upper-value')  // 1
            ];

            // Display the slider value and how far the handle moved
            // from the left edge of the slider.
            nonLinearSlider.noUiSlider.on('update', function (values, handle, unencoded, isTap, positions) {
                nodes[handle].innerHTML = values[handle];
            });

        }

    });


    //-------- Have Cupon Button Text Toggle Change -------//

    $('.have-btn').on('click', function (e) {
        e.preventDefault();
        $('.have-btn span').text(function (i, text) {
            return text === "Have a Coupon?" ? "Close Coupon" : "Have a Coupon?";
        })
        $('.cupon-code').fadeToggle("slow");
    });

    $('.load-more-btn').on('click', function (e) {
        e.preventDefault();
        $('.load-product').fadeIn('slow');
        $(this).fadeOut();
    });





    //------- Start Quantity Increase & Decrease Value --------//




    var value,
        quantity = document.getElementsByClassName('quantity-container');

    function createBindings(quantityContainer) {
        var quantityAmount = quantityContainer.getElementsByClassName('quantity-amount')[0];
        var increase = quantityContainer.getElementsByClassName('increase')[0];
        var decrease = quantityContainer.getElementsByClassName('decrease')[0];
        increase.addEventListener('click', function () { increaseValue(quantityAmount); });
        decrease.addEventListener('click', function () { decreaseValue(quantityAmount); });
    }

    function init() {
        for (var i = 0; i < quantity.length; i++) {
            createBindings(quantity[i]);
        }
    };

    function increaseValue(quantityAmount) {
        value = parseInt(quantityAmount.value, 10);

        console.log(quantityAmount, quantityAmount.value);

        value = isNaN(value) ? 0 : value;
        value++;
        quantityAmount.value = value;
    }

    function decreaseValue(quantityAmount) {
        value = parseInt(quantityAmount.value, 10);

        value = isNaN(value) ? 0 : value;
        if (value > 0) value--;

        quantityAmount.value = value;
    }

    init();

    //------- End Quantity Increase & Decrease Value --------//

    /*----------------------------------------------------*/
	/*  Google map js
	  /*----------------------------------------------------*/

    if ($("#mapBox").length) {
        var $lat = $("#mapBox").data("lat");
        var $lon = $("#mapBox").data("lon");
        var $zoom = $("#mapBox").data("zoom");
        var $marker = $("#mapBox").data("marker");
        var $info = $("#mapBox").data("info");
        var $markerLat = $("#mapBox").data("mlat");
        var $markerLon = $("#mapBox").data("mlon");
        var map = new GMaps({
            el: "#mapBox",
            lat: $lat,
            lng: $lon,
            scrollwheel: false,
            scaleControl: true,
            streetViewControl: false,
            panControl: true,
            disableDoubleClickZoom: true,
            mapTypeControl: false,
            zoom: $zoom,
            styles: [
                {
                    featureType: "water",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            color: "#dcdfe6"
                        }
                    ]
                },
                {
                    featureType: "transit",
                    stylers: [
                        {
                            color: "#808080"
                        },
                        {
                            visibility: "off"
                        }
                    ]
                },
                {
                    featureType: "road.highway",
                    elementType: "geometry.stroke",
                    stylers: [
                        {
                            visibility: "on"
                        },
                        {
                            color: "#dcdfe6"
                        }
                    ]
                },
                {
                    featureType: "road.highway",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            color: "#ffffff"
                        }
                    ]
                },
                {
                    featureType: "road.local",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            visibility: "on"
                        },
                        {
                            color: "#ffffff"
                        },
                        {
                            weight: 1.8
                        }
                    ]
                },
                {
                    featureType: "road.local",
                    elementType: "geometry.stroke",
                    stylers: [
                        {
                            color: "#d7d7d7"
                        }
                    ]
                },
                {
                    featureType: "poi",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            visibility: "on"
                        },
                        {
                            color: "#ebebeb"
                        }
                    ]
                },
                {
                    featureType: "administrative",
                    elementType: "geometry",
                    stylers: [
                        {
                            color: "#a7a7a7"
                        }
                    ]
                },
                {
                    featureType: "road.arterial",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            color: "#ffffff"
                        }
                    ]
                },
                {
                    featureType: "road.arterial",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            color: "#ffffff"
                        }
                    ]
                },
                {
                    featureType: "landscape",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            visibility: "on"
                        },
                        {
                            color: "#efefef"
                        }
                    ]
                },
                {
                    featureType: "road",
                    elementType: "labels.text.fill",
                    stylers: [
                        {
                            color: "#696969"
                        }
                    ]
                },
                {
                    featureType: "administrative",
                    elementType: "labels.text.fill",
                    stylers: [
                        {
                            visibility: "on"
                        },
                        {
                            color: "#737373"
                        }
                    ]
                },
                {
                    featureType: "poi",
                    elementType: "labels.icon",
                    stylers: [
                        {
                            visibility: "off"
                        }
                    ]
                },
                {
                    featureType: "poi",
                    elementType: "labels",
                    stylers: [
                        {
                            visibility: "off"
                        }
                    ]
                },
                {
                    featureType: "road.arterial",
                    elementType: "geometry.stroke",
                    stylers: [
                        {
                            color: "#d6d6d6"
                        }
                    ]
                },
                {
                    featureType: "road",
                    elementType: "labels.icon",
                    stylers: [
                        {
                            visibility: "off"
                        }
                    ]
                },
                {},
                {
                    featureType: "poi",
                    elementType: "geometry.fill",
                    stylers: [
                        {
                            color: "#dadada"
                        }
                    ]
                }
            ]
        });
    }

});
