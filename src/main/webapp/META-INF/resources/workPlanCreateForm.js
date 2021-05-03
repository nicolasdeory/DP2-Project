$(document).ready(() => {

    function calcularSuggestion()
    {
    
    	const lang = $("html").attr("lang") == "es" ? "es-ES" : "en-US";
    
        let minDate = new Date();
        let maxDate = new Date();
        
        let liststart = [];
        let listfinish = [];

        $('input:checked:not(input[name="isPublic$proxy"])').each(function() {
        	const id = $(this).val();
            const start = new Date($("#startDateTime"+id).attr('date'));
            const end= new Date($("#finishDateTime"+id).attr('date'));
            
            liststart.push(start);
            listfinish.push(end);
        });
		
		if(liststart.length > 0) {
			minDate = liststart.reduce(function (a, b) { return a < b ? a : b; });
			maxDate = listfinish.reduce(function (a, b) { return a > b ? a : b; });
		}
        //minDate = "2021-07-11 23:59:00.0"; // aqui le restas para que sea las 8 de la mañana del dia anterior
        //maxDate = "2021-07-12 23:59:00.0"; // aqui le sumas para que sean las 5 de la mañana

		let startDateStringSugg;
		let finishDateStringSugg;
		if (lang == "en-US")
		{
			startDateStringSugg = minDate.getFullYear() + "/" + 
							(minDate.getMonth() + 1) + "/" + minDate.getDate() + " " + 
							("0" + minDate.getHours()).slice(-2) + ":" + ("0" + minDate.getMinutes()).slice(-2);
							
			finishDateStringSugg = maxDate.getFullYear() + "/" + 
			(maxDate.getMonth() + 1) + "/" + maxDate.getDate() + " " +
			("0" + maxDate.getHours()).slice(-2) + ":" + ("0" + maxDate.getMinutes()).slice(-2);
			
			//startDateStringSugg = minDate.toISOString().slice(0,10) + ' ' + maxDate.toLocaleTimeString(lang);
			//finishDateStringSugg = maxDate.toISOString().slice(0,10) + ' ' + maxDate.toLocaleTimeString(lang);
		} else 
		{
			startDateStringSugg = minDate.toLocaleDateString(lang) + ' ' + maxDate.toLocaleTimeString(lang);
			finishDateStringSugg = maxDate.toLocaleDateString(lang) + ' ' + maxDate.toLocaleTimeString(lang);
		}
		
        $("#startDateTime").val(startDateStringSugg);
        $("#finishDateTime").val(finishDateStringSugg);
    }

    $(`input[type="checkbox"]`).change(function() {
        calcularSuggestion();
    });
    
    calcularSuggestion();
});