var docData={
    isLogin:true
};
var URL="192.168.1.104";

/**
 * 登录按钮事件监听
 */
document.getElementById("login").addEventListener("click",function () {
    docData.isLogin=true;//修改记录数据
    if(docData.isLogin){
        document.getElementById("login").className="mainChoice-active";
        document.getElementById("register").className="mainChoice-inactive";
        document.getElementsByClassName("mainName")[0].style.opacity="0";
        document.getElementsByClassName("mainAccount")[0].style.top="-2em";
        document.getElementsByClassName("mainPw")[0].style.top="-2em";
    }
    else{
        document.getElementById("login").className="mainChoice-inactive";
        document.getElementById("register").className="mainChoice-active";
        document.getElementsByClassName("mainName")[0].style.opacity="1";
        document.getElementsByClassName("mainAccount")[0].style.top="0em";
        document.getElementsByClassName("mainPw")[0].style.top="0em";
    }
});
/**
 * 注册按钮事件监听
 */
document.getElementById("register").addEventListener("click",function () {
    docData.isLogin=false;//修改记录数据
    if(docData.isLogin){
        document.getElementById("login").className="mainChoice-active";
        document.getElementById("register").className="mainChoice-inactive";
        document.getElementsByClassName("mainName")[0].style.opacity="0";
        document.getElementsByClassName("mainAccount")[0].style.top="-2em";
        document.getElementsByClassName("mainPw")[0].style.top="-2em";
    }
    else{
        document.getElementById("login").className="mainChoice-inactive";
        document.getElementById("register").className="mainChoice-active";
        document.getElementsByClassName("mainName")[0].style.opacity="1";
        document.getElementsByClassName("mainAccount")[0].style.top="0em";
        document.getElementsByClassName("mainPw")[0].style.top="0em";
    }
});
/**
 * 发送按钮事件监听
 */
document.getElementById("submit").addEventListener("click",function () {
    //获取需要发送的信息,密码经过sha1加密
    var account=document.getElementById("account").value;
    var pw=document.getElementById("pw").value;
    var name=document.getElementById("name").value;

    //检测输入是否符合规范
    // if(account.length===0){
    //     toast("请输入账号");
    //     return;
    // }
    // if(pw.length===0){
    //     toast("请输入密码");
    //     return;
    // }
    // if(!docData.isLogin){
    //     if(name.length===0){
    //         toast("请输入昵称");
    //         return;
    //     }
    // }
    // if(!isEmail(account)){
    //     toast("账号格式错误，请输入正确的邮箱");
    //     return;
    // }
    // if(pw.length<6){
    //     toast("密码长度不小于6位数");
    //     return;
    // }

    pw=hex_sha1(pw);
    //ajax发送信息到后台
    if(docData.isLogin){
        var xhr=new XMLHttpRequest();
        xhr.open("post","http://"+URL+":8080/Login");
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
        xhr.onreadystatechange=function () {
            if(xhr.readyState===4){
                if(xhr.status===200){
                    if(!JSON.parse(xhr.responseText).result){
                        toast("账号或者密码错误~！");
                    }
                    else{
                        window.location.href="http://"+URL+":8080/gameLobby.html";
                    }
                }
                else{
                    toast("网络连接问题~！")
                }
            }
        };
        xhr.send("account="+account+"&pw="+pw+"&name="+name);
    }else{
        var xhr=new XMLHttpRequest();
        xhr.open("post","http://"+URL+":8080/Register");
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
        xhr.onreadystatechange=function () {
            if(xhr.readyState===4){
                if(xhr.status===200){
                    if(!JSON.parse(xhr.responseText).result){
                        toast("用户已存在，请重新注册~！");
                    }
                    else{
                        window.location.href="http://"+URL+":8080/gameLobby.html";
                    }
                }
                else{
                    toast("网络连接问题")
                }
            }
        };
        xhr.send("&account="+account+"&pw="+pw+"&name="+name);
    }

});

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
 * 检测是否是Email格式
 * @param str 待检测的Email
 * @returns {boolean} 检测结果
 */
var isEmail=function (str) {
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
};
