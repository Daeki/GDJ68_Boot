const ws =null;

if(ws == null){
    ws = new WebSocket("ws://localhost:82/chat");
}


ws.onopen = function () {
    console.log('Info: connection opened.');
    //setTimeout( function(){ connect(); }, 1000); // retry connection!!
};


ws.onmessage = function (event) {
    console.log(event.data+'\n');
    let m = `<div class="alert alert-primary" role="alert">
    ${event.data}
  </div>`

  $("#messages").append(m);
};


ws.onclose = function (event) { console.log('Info: connection closed.'); };
ws.onerror = function (event) { console.log('Info: connection closed.'); };
   
$('#btnSend').on('click', function(evt) {
    evt.preventDefault();
//   if (socket.readyState !== 1) return;
    let msg = $('#inputmsg').val();
  	ws.send(msg);
});