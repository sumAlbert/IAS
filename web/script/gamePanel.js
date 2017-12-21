//全局变量
var cartoonPersonPosition={'4':[
    [1,2,3,2,5,2,7,2],
    [8.7,0,10.7,0,12.7,0,14.7,0],
    [15.2,1,17.2,1,19.2,1,21.2,1],
    [22.2,0,24.2,0,26.2,0,28.2,0],
    [31.2,1.6,33.2,1.6,35.2,1.6,37.2,1.6],
    [32.2,10,34.2,10,36.2,10,38.2,10],
    [22.8,8.7,24.8,8.7,26.8,8.7,28.8,8.7],
    [14.8,9.2,16.8,9.2,18.8,9.2,20.8,9.2],
    [7.8,8.7,9.8,8.7,11.8,8.7,13.8,8.7],
    [1,10.2,3,10.2,5,10.2,7,10.2]
],"3":[
    [1.8,2,3.8,2,5.8,2],
    [9.5,0,11.5,0,13.5,0],
    [16,1,18,1,20,1],
    [23,0,25,0,27,0],
    [32,1.6,34,1.6,36,1.6],
    [33,10,35,10,37,10],
    [23.6,8.7,25.6,8.7,27.6,8.7],
    [15.6,9.2,17.6,9.2,19.6,9.2],
    [8.6,8.7,10.6,8.7,12.6,8.7],
    [1.8,10.2,3.8,10.2,5.8,10.2]
],"2":[
    [3,2,5,2],
    [10.7,0,12.7,0],
    [17.2,1,19.2,1],
    [24.2,0,26.2,0],
    [33.2,1.6,35.2,1.6],
    [34.2,10,36.2,10],
    [24.8,8.7,26.8,8.7],
    [16.8,9.2,18.8,9.2],
    [9.8,8.7,11.8,8.7],
    [3,10.2,5,10.2]
],"1":[
    [3.8,2],
    [11.5,0],
    [18,1],
    [25,0],
    [34,1.6],
    [35,10],
    [25.6,8.7],
    [17.6,9.2],
    [10.6,8.7],
    [3.8,10.2]
]};
var websocket = null;
var docData={
    roomIdError:999999,
    maxMemberPerRoom: 4,
    currentPoint: 2,
    positionId: 0
};
var URL="192.168.1.102";
// var URL="localhost";

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new WebSocket("ws://"+URL+":8080/playGame");
} else{
    alert('Not support websocket');
}


/**
 * 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
 */
window.onbeforeunload = function(){
    websocket.close();
};

//连接发生错误的回调方法
websocket.onerror = function(){
    exitHandler();
};

//连接成功建立的回调方法
websocket.onopen = function(){
    init();
};

//接收到消息的回调方法
websocket.onmessage = function(event){
    messageController(event.data)
};

//连接关闭的回调方法
websocket.onclose = function(){
    console.log("close");
};

/**
 * websocket处理相应事件控制器
 * @param data
 */
var messageController=function (data) {
    var JSONObject=JSON.parse(data);
    switch (JSONObject["commandBack"]){
        case "exit":{
            exitHandler();
            break;
        }
        case "upgradeMember":{
            upgradeMember(JSONObject);
            break;
        }
        case "backToLobby":{
            backToLobby();
            break;
        }
        case "roomFull":{
            toast("房间已满");
            backToLobby();
            break;
        }
        default:
            break;
    }
};

/**
 * 处理页面退出的函数
 */
var exitHandler=function () {
    toast("未登陆，请重新登陆~");
    // setTimeout(function () {
    //     window.location.href="http://"+URL+":8080/login.html";
    // },2000);
};

//view控制
/**
 * 弹出信息
 * @param info 需要弹出的信息
 */
var toast=function (info) {
    if(!info){
        info="Welcome";
    }
    document.getElementById("mainContent-toastBox").style.cssText="display:block";
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="display:block;opacity: 1;";
        document.getElementById("mainContent-toastBox").children[0].innerHTML=info;
    },0);
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="display:block;opacity: 0;";
    },1000);
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="";
        document.getElementById("mainContent-toastBox").children[0].innerHTML="Welcome";
    },1300);
};
/**
 * 初始化
 */
var init=function () {
    //初始化已经进入房间的session
    var tableId=window.location.search.substr(1);
    if(!tableId){
        tableId=docData.roomIdError;
    }
    websocket.send("{\"command\":\"init\",\"position\":\"gamePanel\",\"tableId\":"+tableId+"}");

    //退出当前房间
    document.getElementById("back").addEventListener("click",function () {
        websocket.send("{\"command\":\"backToLobby\",\"tableId\":\""+tableId+"\"}");
    });

};
/**
 * 处理成员数据更新
 */
var upgradeMember=function (JSONObject) {
    console.log(JSONObject);
    for(var i=0;i<docData.maxMemberPerRoom;i++){
        document.getElementsByClassName("mainRanking-player")[i].style.cssText="display:none";
    }
    JSONObject.data.forEach(function (t,index) {
        document.getElementsByClassName("mainRanking-player")[index].style.cssText="";
        document.getElementsByClassName("mainRanking-player")[index].children[0].innerHTML="•"+t;
    });
    docData.positionId=JSONObject.positionId;
    console.log("positionId:"+docData.positionId);
};
/**
 * 当点击退出按钮以后
 */
var backToLobby=function () {
    setTimeout(function () {
        window.location.href="http://"+URL+":8080/gameLobby.html";
    },1000);
};


