(function(){

var oReq = new XMLHttpRequest();
oReq.open("GET", "/myfile.webm", true);
oReq.responseType = "blob";

oReq.onload = function(oEvent) {
  var blob = oReq.response;
  // ...
};

oReq.send();

})();