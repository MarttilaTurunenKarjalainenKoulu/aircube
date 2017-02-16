// Load Charts and the corechart package.
google.charts.load('current', {'packages':['corechart']});

// Draw the chart for Humidity when Charts is loaded.
google.charts.setOnLoadCallback(drawHumChart);

// Draw the chart for the Temperature when Charts is loaded.
google.charts.setOnLoadCallback(drawTempChart);

// Draw the chart for the Airquality when Charts is loaded.
google.charts.setOnLoadCallback(drawAirqualityChart);

// Callback that draws the pie chart for Humidity.
function drawHumChart() {

    // Create the data table for Humidity
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Time');
    data.addColumn('number', 'Humidity');
    data.addRows([
      [1, 21],
      [2, 22],
      [3, 23],
      [4, 22],
      [5, 23]
    ]);

    // Set options for Humidity chart.
    var options = {title:'Humidity over Time'};

    // Instantiate and draw the chart for Humidity.
    var chart = new google.visualization.LineChart(document.getElementById('Hum_chart_div'));
    chart.draw(data, options);
}

/*
// Callback that draws the pie chart for Humidity.
function drawHumChart() {

    // Create the data table for Humidity
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Time');
    data.addColumn('number', 'Humidity');
    data.addRows([
      [1, 21],
      [2, 22],
      [3, 23],
      [4, 22],
      [5, 23]
    ]);

    // Set options for Humidity chart.
    var options = {title:'Humidity over Time'};

    // Instantiate and draw the chart for Humidity.
    var chart = new google.visualization.LineChart(document.getElementById('Hum_chart_div'));
    chart.draw(data, options);
}
*/

// Callback that draws the pie chart for Temperature.
function drawTempChart() {

    // Create the data table for Temperature.
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Time');
    data.addColumn('number', 'Temperature');
    data.addRows([
      [1, 21],
      [2, 21],
      [3, 23],
      [4, 22],
      [5, 21]
    ]);

    // Set options for Temperature chart.
    var options = {title:'Temperature over time'};

    // Instantiate and draw the chart for Temperature.
    var chart = new google.visualization.LineChart(document.getElementById('Temp_chart_div'));
    chart.draw(data, options);
}

// Callback that draws the pie chart for Airquality.
function drawAirqualityChart() {

    // Create the data table for Temperature.
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Time');
    data.addColumn('number', 'Airquality');
    data.addRows([
      [1, 21],
      [2, 21],
      [3, 23],
      [4, 22],
      [5, 21]
    ]);

    // Set options for Temperature chart.
    var options = {title:'Airquality over time'};

    // Instantiate and draw the chart for Temperature.
    var chart = new google.visualization.LineChart(document.getElementById('Airquality_chart_div'));
    chart.draw(data, options);
}