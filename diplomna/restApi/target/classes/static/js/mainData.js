const Http = new XMLHttpRequest();
const url='/getMainPageData';
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  
  if( Http.responseText != "") {
    var mainData = JSON.parse(Http.responseText);
    document.getElementById("totalOfLogs").firstChild.data  = mainData["totalOfLogs"];
    document.getElementById("mostActiveLog").firstChild.data  = mainData["mostActiveLog"];
    document.getElementById("totalOfUsers").firstChild.data  = mainData["totalOfUsers"];
    document.getElementById("numberOfCourses").firstChild.data  = mainData["numberOfCourses"];

  }
}




