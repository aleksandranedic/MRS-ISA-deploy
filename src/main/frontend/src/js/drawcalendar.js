$(function(){
    months = ["Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"]
    days = ["Pon", "Uto", "Sre", "Čet", "Pet", "Sub", "Ned"]
    current_date = new Date();
    date = new Date();
    month_curr = date.getMonth();
    dayOfWeek = date.getDay();
    year_curr = date.getFullYear();
    firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    lastDay = new Date(date. getFullYear(), date. getMonth() + 1, 0);

    writeMonth(months[month_curr], year_curr);
    writeDates();
    

})

function writeMonth(month, year){
    str = '<div class="carousel-item active"> <div class="d-block w-100 text-center text-light font-monospace" style="background-color: #ED7301;">' + month + " " + year + '</div></div>';
    $("#months").append(str);
}

function monthDiff(d1, d2) {
    var months_difference;
    months_difference = (d2.getFullYear() - d1.getFullYear()) * 12;
    months_difference -= d1.getMonth();
    months_difference += d2.getMonth();
    return months_difference <= 0 ? 0 : months_difference;
}

function nextMonth(){
    console.log(monthDiff(current_date, date))
    if (monthDiff(current_date, date) >= 11) {
        return;
    }
    date.setMonth(date.getMonth()+1);
    writeMonth(months[date.getMonth()], date.getFullYear())
    firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    lastDay = new Date(date. getFullYear(), date. getMonth() + 1, 0);
    writeDates();
}
function prevMonth(){
    if (date.getMonth() == current_date.getMonth()){
        return;
    }
    date.setMonth(date.getMonth()-1);
    writeMonth(months[date.getMonth()], date.getFullYear())
    firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    lastDay = new Date(date. getFullYear(), date. getMonth() + 1, 0);
    writeDates();
}
function writeDates() {
    $(dates).text("");
    var nextDay = new Date(firstDay);
    for (var i= nextDay.getDate(); i <= lastDay.getDate(); i++){
        day = nextDay.getDay() - 1
        if (day == -1) {
            day = 6
        }
        id = i +'_' + (date.getMonth()+1) +'_' + date.getFullYear();
        str = '<div id="' + id + '"class="pb-4 pt-0 date" style="min-width: 14.28%; max-width: 14.28%;" onclick="showEvents(this.id)"> <div class="d-flex justify-content-center font-monospace">' +  days[day]  + '</div> <div class="d-flex justify-content-center fw-light"> ' + i + '</div><div class="events"></div></div>' 
        $(dates).append(str);
        if (i % 7 == 0){
            written = true;
            str = '<div class="mt-1 mb-0 pb-1 pt-0 event text-light" id="' + i/7 + '"> </div>'
            $(dates).append(str);
        }
        else{
            written = false;
        }
        nextDay.setDate(nextDay.getDate() + 1);
    }
    while (i%7 != 0){
        id = i +'_' + (date.getMonth()+1) +'_' + date.getFullYear();
        str = '<div id="' + id + '"class="pb-4 pt-0 date" style="min-width: 14.28%; max-width: 14.28%;"> <div class="d-flex justify-content-center font-monospace"> </div> <div class="d-flex justify-content-center fw-light"> </div></div>' 
        $(dates).append(str);
        i++;
    }
    str = '<div class="pb-4 pt-0 date" style="min-width: 14.28%; max-width: 14.28%;"> <div class="d-flex justify-content-center font-monospace"> </div> <div class="d-flex justify-content-center fw-light"> </div></div>' 
        $(dates).append(str);
    if (!written){
        str = '<div class="mt-1 mb-0 pt-1 pb-0 text-light event" style="background-color: #01504D;" id="' + (Math.ceil(i/7)) + '"> </div>'
        $(dates).append(str);
    }
}

var clicked = true;
var previously_clicked = "";
var div_showed = "";
function showEvents(data) {
    if (data.split("_")[0] < date.getDate() && date.getMonth() == current_date.getMonth()){
        return;
    }
    changePadding(data, 'pb-4', 'pb-0')
    if (previously_clicked == ""){
        previously_clicked = data;
        div_showed = Math.ceil(data.split('_')[0]/7);
    }
    current_div = Math.ceil(data.split('_')[0]/7) ;
    selected_item = "#" + (Math.ceil(data.split('_')[0]/7))
    
    if(clicked)
    {
        if (div_showed != current_div){
            clicked=false;
            $("#" + div_showed).css({"top": "-50px"});
            
            $("#" + current_div).text("");
            $("#" + current_div).append(getEvents(data));
            $("#" + current_div).css({"top": 0});
        }
        clicked=false;
        $(selected_item).text("");
        $(selected_item).append(getEvents(data));
        $(selected_item).css({"top": 0});
        
    }
    else
    {
        if (previously_clicked != data && div_showed == current_div) {
            $(selected_item).text("");
            $(selected_item).append(getEvents(data));
        }
        else if (previously_clicked != data && div_showed != current_div) {
            clicked=true;
            $("#" + div_showed).css({"top": "-50px"});
    
            $("#" + current_div).text("");
            $("#" + current_div).append(getEvents(data));
            $("#" + current_div).css({"top": 0});
        }
        else {
            clicked=true;
            $(selected_item).css({"top": "-50px"});
        }
    }
    previously_clicked = data;
    div_showed = current_div;
}

function getEvents(date) {
    return "Ništa nije rezervisano za " + date;
}

function changePadding(data, rClass, aClass) {
    ids = getIds(data);
    for (var id of ids){
        $("#" + id).removeClass(rClass).addClass(aClass);
    }
}

function getIds(data) {
    ids = []
    current_id = data.split("_");
    day= current_id[0]
    days_back = ((day%7-1) == -1) ? 6 : day%7-1;
    start = day - days_back;
    for (var i = start; i < start+7; i++){
        id = i + "_" + current_id[1] + "_" + current_id[2];
        ids.push(id);
    }
    return ids;
}
function changeColor(icon){
    console.log(icon.setAttribute("fill", "#0c5bcf"));//0853c2
}

function returnColor(icon){
    console.log(icon.setAttribute("fill", "#0d6efd"));
}