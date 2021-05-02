$(document).ready(() => {

    function calcularSuggestion()
    {
        let minDate = new Date();
        let maxDate = new Date();

        $('input:checked').each(function() {
            const start = new Date($(this).attr(""));
            const end= new Date($(this).attr("end"));
            if (start < minDate)
                minDate = start;
            if (end > maxDate)
                maxDate = end;
        });

        minDate // aqui le restas para que sea las 8 de la mañana del dia anterior
        maxDate // aqui le sumas para que sean las 5 de la mañana

        ///$("#suggestionText").text(`te recomiendo entre esta fecha ${start} y esta otra ${end}`);
        $("#startDateTime").val(minDate)
        $("#finishDateTime").val(maxDate)
    }

    $(`input[type="checkbox"]`).change(function() {
        calcularSuggestion();
    });

    calcularSuggestion();
});