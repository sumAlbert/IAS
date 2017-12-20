//全局变量
var websocket = null;
var docData={
    currentPage:1,
    tablePerPage:8,
    showTablePanel: 1,
    tableIsChanging: false
};

//初始化

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new WebSocket("ws://localhost:8080/playGame");
} else{
    alert('Not support websocket');
}

//连接发生错误的回调方法
websocket.onerror = function(){
    exitHandler();
};

//连接成功建立的回调方法
websocket.onopen = function(event){
    console.log("open");
};

//接收到消息的回调方法
websocket.onmessage = function(){
    messageController(event.data)
};

//连接关闭的回调方法
websocket.onclose = function(){
    console.log("close");
};

/**
 * 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
 */
window.onbeforeunload = function(){
    websocket.close();
};

/**
 * 给桌面监听点击事件（即进入游戏桌）
 * @type {NodeList}
 */
var mainTables=document.getElementsByClassName("mainTable");
for(var i=0;i<16;i++){
    (function (arg) {
        mainTables[arg].addEventListener("click",function(){
            websocket.send("commandId=0&currentPage="+docData.currentPage+"&tableId="+arg);
        })
    })(i)
}


/**
 * websocket处理相应事件控制器
 * @param data
 */
var messageController=function (data) {
    console.log(data);
    var JSONObject=JSON.parse(data);
    switch (JSONObject["commandBack"]){
        case "exit":{
            exitHandler();
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
    setTimeout(function () {
        window.location.href="./login.html"
    },2000);
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

var turnPage=function (direction,pageNum) {
    switch (direction){
        case "left":{
            var hiddenTablePanel=(docData.showTablePanel+1)%2;
            for(var i=0;i<docData.tablePerPage;i++){
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[0].innerHTML="桌"+(docData.tablePerPage*pageNum-7+i);
            }
            document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="transition: none;left: 100%;";
            document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="transition: none;left: 0;";
            setTimeout(function () {
                document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="left: 0%;";
                document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="left: -100%;";
            },0);
            break;
        }
        case "right":{
            var hiddenTablePanel=(docData.showTablePanel+1)%2;
            for(var i=0;i<docData.tablePerPage;i++){
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[0].innerHTML="桌"+(docData.tablePerPage*pageNum-7+i);
            }
            document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="transition: none;left: -100%;";
            document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="transition: none;left: 0;";
            setTimeout(function () {
                document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="left: 0%;";
                document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="left: 100%;";
            },0);
            break;
        }
        default:
            break;
    }
};


