var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        dataFromTS = JSON.parse(this.responseText);
        document.getElementById("temperature").innerHTML = dataFromTS.feeds[0].field1 + "°C";
        document.getElementById("humidity").innerHTML = "Ilmankosteus on " + dataFromTS.feeds[0].field2 + "%";;
        document.getElementById("ppm").innerHTML = "Ilmanlaatu on " + dataFromTS.feeds[0].field3 + "ppm";
    }
};
xmlhttp.open("GET", "https://api.thingspeak.com/channels/180604/feeds.json?results=1", true);
xmlhttp.send();
