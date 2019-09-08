// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function parseDataToArrayOfIntegers() {

    var canvasData = document.getElementById("myPieChartData").value;
    debugger;
    var length = canvasData.length;
    var dataToSplit = canvasData.substring(1, length - 1);
    var data = dataToSplit.split(',').map(function(item) {
        return parseInt(item);
    });
    
    return data;
}


// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var charData = parseDataToArrayOfIntegers();
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["Конспект", "Лекция", "Упражнение", "Други"],
    datasets: [{
      data: charData,
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#858796'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#60616f'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});
