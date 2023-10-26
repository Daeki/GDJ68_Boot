
function memberCheck(){
    $.ajax({
        type:'GET',
        url:'/chatRoom/check',
        success:function(r){
            let login=$("#username").html();
            $("#memberCount").text(r.length-1)
            r.forEach((v, idx) => {
                if(v !=login){
                    $("#memberList").append(`
                    <a class="dropdown-item d-flex align-items-center" href="/chatRoom/enter?receiver=${v}">
                    <div class="dropdown-list-image mr-3">
                        <img class="rounded-circle" src="/img/undraw_profile_1.svg"
                            alt="...">
                        <div class="status-indicator bg-success"></div>
                    </div>
                    <div class="font-weight-bold">
                        <div class="text-truncate" id="${v}">Hi there! I am wondering if you can help me with a
                            problem I've been having.</div>
                        <div class="small text-gray-500">${v}</div>
                    </div>
                </a>
                    `)
                }
            });

           
        }
    })
}


const ws = new WebSocket("ws://localhost:82/chat");;




ws.onopen = function () {
    console.log('Info: connection opened.');
    console.log(typeof ws);
    console.log(ws);
    memberCheck();
    //setTimeout( function(){ connect(); }, 1000); // retry connection!!
};


ws.onmessage = function (event) {
    let rn= $("#messages").attr("data-chat-num");
    console.log("ROOMNUM : ", event.data+'\n');
    let message=JSON.parse(event.data);
    
    if(rn == message.roomnum){

        let m = `<div class="alert alert-primary" role="alert">
        ${event.data}
        </div>`

        $("#messages").append(m);
    }else {
        $("#"+message.sender).text(message.message)
    }
};


ws.onclose = function (event) { console.log('Info: connection closed.'); };
ws.onerror = function (event) { console.log('Info: connection closed.'); };
   
$('#btnSend').on('click', function(evt) {
    evt.preventDefault();
//   if (socket.readyState !== 1) return;
    let msg = $('#inputmsg').val();

    let rn= $("#messages").attr("data-chat-num");

    const message = new MessageData(rn, msg)



  	ws.send(JSON.stringify(message));
});

class MessageData{
    roomnum=0;
    message='';

    constructor(roomnum, message){
        this.roomnum=roomnum;
        this.message=message;
    }

}