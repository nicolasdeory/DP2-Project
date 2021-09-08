$(document).ready(() => {
  function calcularSuggestion() {
    const lang = $("html").attr("lang") == "es" ? "es-ES" : "en-US";

    let minDate = new Date();
    let maxDate = new Date();

    let dateMinusDay = new Date();
    let datePlusDay = new Date();

    let liststart = [];
    let listfinish = [];

    $('input:checked:not(input[name="isPublic$proxy"])').each(function () {
      const id = $(this).val();
      const start = new Date($("#startDateTime" + id).attr("date"));
      const end = new Date($("#finishDateTime" + id).attr("date"));

      liststart.push(start);
      listfinish.push(end);
    });

    if (liststart.length > 0) {
      minDate = liststart.reduce(function (a, b) {
        return a < b ? a : b;
      });
      maxDate = listfinish.reduce(function (a, b) {
        return a > b ? a : b;
      });
    }
    //minDate = "2021-07-11 23:59:00.0"; // aqui le restas para que sea las 8 de la mañana del dia anterior
    //maxDate = "2021-07-12 23:59:00.0"; // aqui le sumas para que sean las 5 de la mañana

    let startDateStringSugg;
    let finishDateStringSugg;

    minDate.setDate(minDate.getDate() - 1);
    if (minDate < new Date()) minDate.setDate(minDate.getDate() + 1);
    maxDate.setDate(maxDate.getDate() + 1);

    if (lang == "en-US") {
      startDateStringSugg =
        minDate.getFullYear() +
        "/" +
        (minDate.getMonth() + 1) +
        "/" +
        minDate.getDate() +
        " " +
        "08:00";

      finishDateStringSugg =
        maxDate.getFullYear() +
        "/" +
        (maxDate.getMonth() + 1) +
        "/" +
        maxDate.getDate() +
        " " +
        "17:00";
    } else {
      startDateStringSugg =
        minDate.getDate() +
        "/" +
        (minDate.getMonth() + 1) +
        "/" +
        minDate.getFullYear() +
        " " +
        "08:00";

      finishDateStringSugg =
        maxDate.getDate() +
        "/" +
        (maxDate.getMonth() + 1) +
        "/" +
        maxDate.getFullYear() +
        " " +
        "17:00";
    }

    $("#startDateTime").val(startDateStringSugg);
    $("#finishDateTime").val(finishDateStringSugg);
  }

  $(`input[type="checkbox"]`).change(function () {
    calcularSuggestion();
  });

  calcularSuggestion();
});
