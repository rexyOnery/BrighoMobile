
///////////////////////////////////////////////////////////////////////////
// Loader
$(document).ready(function () {
    setTimeout(() => {
        $("#loader").fadeToggle(250);
    }, 800); // hide delay when page load
});
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Go Back
$(".goBack").click(function () {
    window.history.go(-1);
});
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Tooltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Input
$(".clear-input").click(function () {
    $(this).parent(".input-wrapper").find(".form-control").focus();
    $(this).parent(".input-wrapper").find(".form-control").val("");
    $(this).parent(".input-wrapper").removeClass("not-empty");
});
// active
$(".form-group .form-control").focus(function () {
    $(this).parent(".input-wrapper").addClass("active");
}).blur(function () {
    $(this).parent(".input-wrapper").removeClass("active");
})
// empty check
$(".form-group .form-control").keyup(function () {
    var inputCheck = $(this).val().length;
    if (inputCheck > 0) {
        $(this).parent(".input-wrapper").addClass("not-empty");
    }
    else {
        $(this).parent(".input-wrapper").removeClass("not-empty");
    }
});
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Searchbox Toggle
$(".toggle-searchbox").click(function () {
    $("#search").fadeToggle(200);
    $("#search .form-control").focus();
});
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Owl Carousel
$('.carousel-full').owlCarousel({
    loop: true,
    margin: 8,
    nav: false,
    items: 1,
    dots: false,
});
$('.carousel-single').owlCarousel({
    stagePadding: 30,
    loop: true,
    margin: 16,
    nav: false,
    items: 1,
    dots: false,
});
$('.carousel-multiple').owlCarousel({
    stagePadding: 32,
    loop: true,
    margin: 16,
    nav: false,
    items: 2,
    dots: false,
});
$('.carousel-small').owlCarousel({
    stagePadding: 32,
    loop: true,
    margin: 8,
    nav: false,
    items: 4,
    dots: false,
});
$('.carousel-slider').owlCarousel({
    loop: true,
    margin: 8,
    nav: false,
    items: 1,
    dots: true,
});
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
$('.custom-file-upload input[type="file"]').each(function () {
    // Refs
    var $fileUpload = $(this),
        $filelabel = $fileUpload.next('label'),
        $filelabelText = $filelabel.find('span'),
        filelabelDefault = $filelabelText.text();
    $fileUpload.on('change', function (event) {
        var name = $fileUpload.val().split('\\').pop(),
            tmppath = URL.createObjectURL(event.target.files[0]);
        if (name) {
            $filelabel
                .addClass('file-uploaded')
                .css('background-image', 'url(' + tmppath + ')');
            $filelabelText.text(name);
        } else {
            $filelabel.removeClass('file-uploaded');
            $filelabelText.text(filelabelDefault);
        }
    });
});
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
// Notification
// trigger notification
function notification(target, time) {
    var a = "#" + target;
    $(".notification-box").removeClass("show");
    setTimeout(() => {
        $(a).addClass("show");
    }, 300);
    if (time) {
        time = time + 300;
        setTimeout(() => {
            $(".notification-box").removeClass("show");
        }, time);
    }
};
// close button notification
$(".notification-box .close-button").click(function (event) {
    event.preventDefault();
    $(".notification-box.show").removeClass("show");
});
// tap to close notification
$(".notification-box.tap-to-close .notification-dialog").click(function () {
    $(".notification-box.show").removeClass("show");
});
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
// Toast
// trigger toast
function toastbox(target, time) {
    var a = "#" + target;
    $(".toast-box").removeClass("show");
    setTimeout(() => {
        $(a).addClass("show");
    }, 100);
    if (time) {
        time = time + 100;
        setTimeout(() => {
            $(".toast-box").removeClass("show");
        }, time);
    }
};
// close button toast
$(".toast-box .close-button").click(function (event) {
    event.preventDefault();
    $(".toast-box.show").removeClass("show");
});
// tap to close toast
$(".toast-box.tap-to-close").click(function () {
    $(this).removeClass("show");
});
///////////////////////////////////////////////////////////////////////////


var uri = "https://localhost:44326/api/ahhtapi";



const showHidden = async () => {
    $("#veriSpinner").removeClass('hidden');
    if ($("#seller_transaction_code").val() != "") {
        var requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        try {
            const res = await fetch(uri + "/gettransaction?transactioncode=" + $("#seller_transaction_code").val(), requestOptions);
            if (!res.ok) {
                throw new Error(res.status);
            }
            const data = await res.json();

            console.log(data);
            if (!Object.keys(data).length) {
                $("#no-item").removeClass('hidden');
                $("#veriSpinner").addClass('hidden');
            }
            else {

                data.forEach(item => {
                    $("#agreed_price").val(item.agreedPrice);
                    $("#shipping").val(item.shippingCost);
                    $("#commission").val(item.ahhtCommission);
                    $("#transaction_cost").val(item.totalCost);

                });

                $("#showHidden").removeClass("hidden");
                $("#veriSpinner").addClass('hidden');
            }


        } catch (error) {
            console.log(error);
            $("#veriSpinner").addClass('hidden');
        }
    } else {
        $("#showHidden").addClass("hidden");
        $("#veriSpinner").addClass('hidden');
    }
}



var showButton = function () {
    if ($('#chkTOC').is(":checked")) {

        $('#btnSubmit').attr("disabled", false);
    } else {
        $('#btnSubmit').attr("disabled", true);
    }
}

var showSubmitButton = function () {
    if ($('#chkTOC').is(":checked")) {
        if ($("#transaction_cost").val() != "" && $("#transaction_cost").val() != "0") {
            $('#btnSubmit').attr("disabled", false);
        }
    } else {
        $('#btnSubmit').attr("disabled", true);
    }
}


$("#btnSubmit").click(function () {
    
    var user = {
        SellerBankName: $("#bank_name").val(),
        SellerAccountName: $("#account_name").val(),
        SellerAccountNumber: $("#account_number").val(),
        SellerMobileNumber: $("#phone_number").val(),
        TransactionDescription: $("#transaction_description").val(),
        AgreedPrice: $("#agreed_price").val(),
        ShippingCost: $("#shipping").val(),
        AHHTCommission: $("#commission").val(),
        TotalCost: $("#transaction_cost").val()
    };
    if (user.AgreedPrice == "" || user.SellerAccountName == "" || user.SellerAccountNumber == "" || user.SellerBankName == "" || user.SellerMobileNumber == "" || user.TransactionDescription == "") {

    } else {
        addTransaction(user);
    }
})

const addTransaction = async (user) => {

    $("#btnSubmit").addClass('hidden');
    $("#btnProgress").removeClass('hidden');

    const options = {
        method: 'POST',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const res = await fetch(uri + "/post", options);
    if (!res.ok) {
        $("#DialogIconedDanger").modal('show');
        $("#btnSubmit").removeClass('hidden');
        $("#btnProgress").addClass('hidden');
    }
    const data = await res.json();

    console.log(data);
    if (!data) {
        $("#DialogIconedDanger").modal('show');
        $("#btnSubmit").removeClass('hidden');
        $("#btnProgress").addClass('hidden');
    }
    else {
        $("#DialogIconedSuccess").modal('show');
        $("#btnSubmit").addClass('hidden');
        $("#btnProgress").addClass('hidden');
    }

}


var showAccept = function () {
    if ($("#seller_transaction_code").val() != "") {
        if ($('#chkTOC').is(":checked")) {

            $('#btnAccept').attr("disabled", false);
        } else {
            $('#btnAccept').attr("disabled", true);
            $("#btnProgress").addClass('hidden');
            $("#btnAccept").removeClass('hidden');
        }
    } else {
        let inputs = document.getElementById('chkTOC');
        inputs.checked = false;
    }
}

$("#btnAccept").click(function () {
    if ($("#seller_transaction_code").val() != "") {
        //validate on server 
        $("#btnAccept").addClass('hidden');
        $("#btnProgress").removeClass('hidden');

        var user = {
            BuyerBankName: $("#bank_name").val(),
            BuyerAccountName: $("#account_name").val(),
            BuyerAccountNumber: $("#account_number").val(),
            BuyerMobileNumber: $("#phone_number").val(),
            BuyerEmailAddress: $("#email_address").val(),
            TransactionCode: $("#seller_transaction_code").val(),
            TotalCost: $("#transaction_cost").val()
        };
        showPayment(user);

    }
})

const showPayment = async (user) => {

    const options = {
        method: 'PUT',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const res = await fetch(uri + "/buyeracceptpayment", options);
    if (!res.ok) {
        throw new Error(res.status);
    }
    const data = await res.json();

    console.log(data);
    if (!data) {
        $("#no-item").removeClass('hidden');
        $("#veriSpinner").addClass('hidden');
    }
    else {
         
        location.href = '/buyer/transverification?transcode=' + $("#seller_transaction_code").val() + "&account_name=" + user.BuyerAccountName + "&email_address=" + user.BuyerEmailAddress + "&bank_name=" + user.BuyerBankName + "&transaction_cost=" + user.TotalCost;
    }
    
}
 

var calcCommission = function () {
    if ($("#agreed_price").val() != "") {
        var agreed_price = parseFloat($("#agreed_price").val());
        var percentage = 2.5 / 100;
        var commission = parseFloat(agreed_price * percentage);
        $("#commission").val(commission);
        var shipping = "0";
        if ($("#shipping").val() != "") {
            shipping = parseFloat($("#shipping").val());
        }
        if (shipping == "0")
            $("#transaction_cost").val(commission + agreed_price);
        else
            $("#transaction_cost").val(commission + agreed_price + shipping);
    } else {
        $("#commission").val(0);
        $("#transaction_cost").val(0);
        $('#btnSubmit').attr("disabled", true);
        let inputs = document.getElementById('chkTOC');
        inputs.checked = false;
    }
}

var addShipping = function () {
    if ($("#shipping").val() != "") {
        if ($("#agreed_price").val() != "") {
            var commission = parseFloat($("#commission").val());
            var agreed_price = parseFloat($("#agreed_price").val());
            var shipping = parseFloat($("#shipping").val());
            var transaction_cost = parseFloat(commission + agreed_price + shipping);
            $("#transaction_cost").val(transaction_cost);
        }
    } else {
        var commission = parseFloat($("#commission").val());
        var agreed_price = parseFloat($("#agreed_price").val());
        var transaction_cost = parseFloat(commission + agreed_price);
        $("#transaction_cost").val(transaction_cost);
    }
}

const _key = "pk_test_eda062a81ed9102f087935cbf3d78dbbe5297105";

function payArtisanWithPaystack() {
    // e.preventDefault();
    var transcode = localStorage.getItem('TransCode');
    var name_splitter = localStorage.getItem('account_name').split('%20');

    var first = name_splitter[0];
    var last = name_splitter[1];

    var email = localStorage.getItem('email_address'); 
    var price = localStorage.getItem('transaction_cost');

    var handler = PaystackPop.setup({
        key: _key, // Replace with your public key
        email: email,//document.getElementById("email-address").value,
        amount: price * 100,
        firstname: first,//document.getElementById("first-name").value,
        lastname: last,//document.getElementById("first-name").value,
        ref: '' + Math.floor((Math.random() * 1000000000) + 1), // generates a pseudo-unique reference. Please replace with a reference you generated. Or remove the line entirely so our API will generate one for you
        // label: "Optional string that replaces customer email"
        onClose: function () {
            $("#btnAccept").removeClass('hidden');
            $("#btnProgress").addClass('hidden');
        },
        callback: function (response) {
            processPayment(transcode);
        }
    });

    handler.openIframe();
}

var processPayment = async (_transcode) => {

    const option = {
        method: 'PUT',
        redirect: 'follow'
    }


    const res = await fetch(uri + "/setbuyerpayment?id=" + _transcode, option);
    if (!res.ok) {
        throw new Error(res.status);
    }
    const data = await res.json();

    console.log(data);
    if (!data) {
        $("#no-item").removeClass('hidden');
        $("#veriSpinner").addClass('hidden');
    }
    else {
        location.href = "/buyer/success?id=" + _transcode;
    }
}


$("#btnVerify").click(function () {
    if ($("#smscode").val() != "") {
        $("#btnVerify").addClass('hidden');
        $("#veriSpinner").removeClass('hidden');

        var checki = veriCheck($("#smscode").val());
        if (checki) {
            $("#DialogIconedSuccess").modal('show');
            $("#btnVerify").removeClass('hidden');
            $("#veriSpinner").addClass('hidden');
        }
        else
            $("#DialogIconedDanger").modal('show');
    } else {
        $("#DialogIconedDanger").modal('show');
    }
     
})

var veriCheck = async (_transcode) => {

    const option = {
        method: 'GET',
        redirect: 'follow'
    }


    const res = await fetch(uri + "/confirm?confirmcode=" + _transcode, option);
    if (!res.ok) {
        return false;
    }
    const data = await res.json();

    console.log(data);
    if (!data) {
        return false;
    }
    else {
        data.forEach(item => {
            document.getElementById('transDec').innerHTML = item.transactionDescription;  
        });
        return true;
    }
}
