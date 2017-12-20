//全局变量
var websocket = null;
var docData={
    currentPage:1,
    tablePerPage:8,
    maxPage:10,
    showTablePanel: 0,
    maxPageNum: 10,
    tableIsChanging: false,
    tableInfo:[]
};
var URL="192.168.1.104";

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
        case "updateTableInfo":{
            docData.tableInfo=JSONObject["data"];
            upgradePage();
            break;
        }
        case "enterRoom":{
            enterRoom(JSONObject);
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
        window.location.href="http://"+URL+":8080/login.html";
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
/**
 * 翻页
 * @param direction 翻页的方向
 * @param pageNum 翻到的页数
 */
var turnPage=function (pageNum) {
    //如果页码与当前页码一致，直接return
    if(pageNum===docData.currentPage){
        docData.tableIsChanging=false;
        return;
    }
    //如果跳转的页码大于当前页码，则向左，否则向右
    var direction=(pageNum-0)>docData.currentPage?"left":"right";
    var hiddenTablePanel=(docData.showTablePanel+1)%2;
    document.getElementsByClassName("mainPage-activePage")[0].className="mainPage-normalPage";
    document.getElementsByClassName("mainPage-normalPage")[pageNum-1].className="mainPage-normalPage mainPage-activePage";
    for(var i=0;i<docData.tablePerPage;i++){
        document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[0].innerHTML="桌"+(docData.tablePerPage*pageNum-7+i);
        switch (docData.tableInfo[8*(pageNum-1)+i].state){
            case '0':{
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].className="mainTable mainTable-empty";
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[1].innerHTML="空闲中...";
                break;
            }
            case '1':{
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].className="mainTable mainTable-prepare";
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[1].innerHTML=docData.tableInfo[8*(docData.currentPage-1)+i].prepareNum+"\""+docData.tableInfo[8*(docData.currentPage-1)+i].enterNum;
                break;
            }
            case '2':{
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].className="mainTable mainTable-gaming";
                document.getElementById("mainTablePanel"+hiddenTablePanel).children[i].children[1].innerHTML="游戏中...";
                break;
            }
            default:
                break;
        }
    }
    switch (direction){
        case "left":{
            document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="transition: none;left: 100%;";
            document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="transition: none;left: 0;";
            setTimeout(function () {
                document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="left: 0%;";
                document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="left: -100%;";
            },0);
            setTimeout(function () {
                docData.showTablePanel=hiddenTablePanel;
                docData.currentPage=pageNum;
                docData.tableIsChanging=false;
            },1100);
            break;

        }
        case "right":{
            document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="transition: none;left: -100%;";
            document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="transition: none;left: 0;";
            setTimeout(function () {
                document.getElementById("mainTablePanel"+hiddenTablePanel).style.cssText="left: 0%;";
                document.getElementById("mainTablePanel"+docData.showTablePanel).style.cssText="left: 100%;";
            },0);
            setTimeout(function () {
                docData.showTablePanel=hiddenTablePanel;
                docData.currentPage=pageNum;
                docData.tableIsChanging=false;
            },1100);
            break;
        }
        default:
            break;
    }
};
/**
 * 初始化
 */
var init=function () {
    /**
     * 给桌面监听点击事件（即给桌子添加进入游戏桌事件）
     * @type {NodeList}
     */
    var mainTables=document.getElementsByClassName("mainTable");
    for(var i=0;i<16;i++){
        (function (arg) {
            mainTables[arg].addEventListener("click",function(){
                var index=arg%8;
                if(!docData.tableIsChanging){
                    websocket.send("{\"command\":\"enterRoom\",\"tableId\":\""+(docData.tablePerPage*(docData.currentPage-1)+index)+"\"}");
                }
            })
        })(i)
    }
    websocket.send("{\"command\":\"init\",\"position\":\"gameLobby\"}");

    /**
     * 给底边翻页按钮设置点击事件
     * @type {NodeList}
     */
    var mainPage_normalPage=document.getElementsByClassName("mainPage-normalPage");
    for(var i=0;i<mainPage_normalPage.length;i++){
        (function (arg) {
            mainPage_normalPage[i].addEventListener("click",function () {
                if(!docData.tableIsChanging){
                    docData.tableIsChanging=true;
                    turnPage(arg+1);
                }
            });
        })(i);
    }
    document.getElementsByClassName("mainPage-Left")[0].addEventListener("click",function () {
        if(!docData.tableIsChanging){
            docData.tableIsChanging=true;
            if(docData.currentPage>=2){
                turnPage(docData.currentPage-1);
            }
        }
    });
    document.getElementsByClassName("mainPage-Right")[0].addEventListener("click",function () {
        if(!docData.tableIsChanging){
            docData.tableIsChanging=true;
            if(docData.currentPage<docData.maxPage){
                turnPage(docData.currentPage+1);
            }
        }
    });

};
/**
 * 更新当前页面
 */
var upgradePage=function () {
    for(var i=0;i<docData.tablePerPage;i++){
        switch (docData.tableInfo[8*(docData.currentPage-1)+i].state){
            case '0':{
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].className="mainTable mainTable-empty";
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].children[1].innerHTML="空闲中";
                break;
            }
            case '1':{
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].className="mainTable mainTable-prepare";
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].children[1].innerHTML=docData.tableInfo[8*(docData.currentPage-1)+i].prepareNum+"\\"+docData.tableInfo[8*(docData.currentPage-1)+i].enterNum;
                break;
            }
            case '2':{
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].className="mainTable mainTable-gaming";
                document.getElementById("mainTablePanel"+docData.showTablePanel).children[i].children[1].innerHTML="游戏中";
                break;
            }
            default:
                break;
        }
    }
};
/**
 * 进入房间的触发事件
 */
var enterRoom=function (JSONObject) {
    console.log(JSONObject);
    if(JSONObject.enterRoomResult){
        window.location.href="http://"+URL+":8080/gamePanel.html?"+JSONObject.tableId;
    }
    else{
        toast("房间已满，请选择其他房间")
    }
};



