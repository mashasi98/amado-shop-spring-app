$(document).ready(function () {
    $(".minusButton").on("click", function (evt) {
        console.log( "ready!" );
        evt.preventDefault();
        id= $(this).attr("pid ");
        qtyInput = $("#quantity" + productId);
        newQty = parseInt(qtyInput.val())-1;
        if(newQty > 0 ) qtyInput.val(newQty);

    });

    $(".plusButton").on("click", function (evt) {
        evt.preventDefault();
        id= $(this).attr("pid ");
        let qtyInput = $("#quantity" + productId);
        newQty = parseInt(qtyInput.val())+1;
        if(newQty > 0) qtyInput.val(newQty);

    });

});

$(function(){

    var valueElement = $('#value');
    function incrementValue(e){
        valueElement.text(Math.max(parseInt(valueElement.text()) + e.data.increment, 0));
        return false;
    }

    $('#plus').bind('click', {increment: 1}, incrementValue);

    $('#minus').bind('click', {increment: -1}, incrementValue);

});