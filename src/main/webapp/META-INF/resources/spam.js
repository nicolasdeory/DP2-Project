$("#add-keyword").click(function(e) {
    e.preventDefault();
    const newkw = $("#newKeyword").val().replace(/,/g, '');
    $("#keywords").append(`<option value="${newkw}">${newkw}</option>`);
    $("#newKeyword").val("");
});

$("#remove-keyword").click(function(e) {
    e.preventDefault();
    if ($("#keywords").val())
        $(`#keywords option[value="${$("#keywords").val()}"]`).remove();
});

$('form').submit(() =>
{
    const s = $('option').map(function(){
        return $.trim($(this).text().replace(/,/g, ''));
    }).get();
    $('input[name="keywords"]').val(s.join(','));
});
