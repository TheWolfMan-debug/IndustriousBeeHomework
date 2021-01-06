//在页面加载完成后
$(function () {
    //请求注册registerServlet
    registerServletRequest();

    //返回登陆
    backLogin();

    //当按下enter时提交注册信息
    enterDown()
});

//请求注册registerServlet
function registerServletRequest() {
    $("#register").click(function () {
        //获取username文本输入框的值
        let username = $("#username").val();
        let password = $("#password").val();
        let isVip = $("input:radio:checked").val();
        //发送ajax请求
        $.get(
            "/wolf/registerServlet",
            {username: username, password: password, vip: isVip},
            function (data) {
                //判断userExist键的值是否是true
                let tip = $("#s_username");
                let registerResult = data.registerResult;
                if (registerResult) {
                    //注册成功
                    tip.css("color", "rgb(0, 183, 232)");
                    tip.html("注册成功！");

                    tip.fadeIn(2000);
                    tip.fadeOut(2000);

                    setTimeout("window.location.href = \"/wolf/loginPage.html\"", 1000);
                } else {
                    //注册失败
                    tip.css("color", "red");
                    tip.html("注册失败！");

                    tip.fadeIn(2000);
                    tip.fadeOut(2000);


                }
            });

    });
}

//返回登陆
function backLogin() {
    $("#back").click(function () {
        window.location.href = "/wolf/loginPage.html";
    })
}

//当按下enter时提交注册信息
function enterDown() {
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            $("#register").click();
        }
    });
}

