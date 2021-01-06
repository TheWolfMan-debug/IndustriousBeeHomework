//点击新建文件夹收起文件目录
let clickMyDownloadCount = 1
//记录文件夹数量
let newDirectoryNumber = 1;
//记录用户文件夹名称
let userDirs = new Array(1000);

//在页面加载完成后
$(function () {
    //加载用户结构目录
    loadUserDirStruct();

    //加载用户下载历史记录
    updateDownloadResources();

    //添加文件夹
    appendUserDir()

    //改变文件夹高度
    changeDirHeight();

    //退出登录
    exitLogin();

});

//添加用户文件夹目录
function appendUserDir() {
    $(".my-download-btn").click(function (e) {
        e.stopPropagation()
        let inputStr = prompt('请输入文件夹名', '新建文件夹' + newDirectoryNumber)
        let div = document.createElement('div')
        if (inputStr) {
            for (let i = 0; i < userDirs.length; i++) {
                if (inputStr === userDirs[i]) {
                    return;
                }
            }

            if (inputStr === ('新建文件夹' + newDirectoryNumber)) {
                newDirectoryNumber++
            }
            userDirs.push(inputStr);
            // console.log(inputStr)
            div.innerHTML = inputStr

            let appBtn = document.createElement('button')
            appBtn.innerHTML = '+'
            appBtn.className = 'file-btn'
            div.className = 'append-file'
            div.onclick = handleFilesClick
            div.setAttribute("fileNumber", "0");
            div.setAttribute("userDirName", inputStr);
            div.appendChild(appBtn)

            const addFileWapper = document.querySelector('.add-file-wapper')
            addFileWapper.appendChild(div)

            const appendFile = document.querySelectorAll('.append-file')
            appendFile.forEach(item => {
                item.style.height = '30px'
                item.style.border = "1px solid #ddd";
                item.style.borderLeft = 'none';
                item.style.borderRight = 'none';
            })
            handleFileBtnClick()


            /**
             * 这里添加新建文件夹的代码
             *      1.获取文件夹名
             *      2.通过ajax发送请求
             *      3.后端接受请求保存文件名
             */

            $.get(
                "/wolf/saveUserDirStructServlet",
                {userDirName: inputStr, isDir: true},
                function (data) {
                    // alert("成功了")
                });
        }
    })

}

//改变文件夹高度
function changeDirHeight() {
    $(".my-download").click(function () {
        const appendFile = document.querySelectorAll('.append-file')
        appendFile.forEach(item => {
            if (clickMyDownloadCount % 2 === 0) {
                item.style.height = '30px'
                item.style.border = "1px solid #ddd";
                item.style.borderLeft = 'none';
                item.style.borderRight = 'none';
            } else {
                item.style.height = '0px'
                item.style.border = 'none'
            }

            item.style.transition = "height 0.5s"
        })
        clickMyDownloadCount++;
        if (clickMyDownloadCount == 10) {
            //重置计数变量
            clickMyDownloadCount = 1;
        }
    })


}

//加载用户结构目录
function loadUserDirStruct() {
    $.post(
        "/wolf/loadUserDirStructServlet",
        function (data) {
            let userDirStruct = data.userDirStructData;
            let userDirStructIndex = data.userDirStructDataIndex;
            let userDir;
            for (let i = 0; i < userDirStructIndex.length; i++) {
                /**
                 * 这里处理文件夹
                 * userDir为文件夹的名称
                 */
                userDir = userDirStructIndex[i];
                // alert(userDir);
                if (userDir === '新建文件夹' + newDirectoryNumber) {
                    newDirectoryNumber++
                }

                userDirs.push(userDir);

                loadUserDir(userDir, userDirStruct[userDir].length)


                /**
                 * 这里处理文件
                 * fileLists为文件列表
                 */
                // alert(userDir+":"+ userDirStruct[userDir].length)
                if (userDirStruct[userDir].length) {
                    let fileLists = userDirStruct[userDir]
                    for (let i = 0; i < fileLists.length; i++) {
                        // alert(fileLists[i])
                        loadUserFiles(fileLists[i], userDir)
                    }
                }

            }

        });
}

//加载用户目录
function loadUserDir(inputStr, userDirLength) {
    let div = document.createElement('div')
    const addFileWapper = document.querySelector('.add-file-wapper')
    if (inputStr) {
        // console.log(inputStr)
        div.innerHTML = inputStr

        let appBtn = document.createElement('button')
        appBtn.innerHTML = '+'
        appBtn.className = 'file-btn'
        div.className = 'append-file'
        div.onclick = handleFilesClick
        if (!userDirLength.length) {
            div.setAttribute("fileNumber", '0');
        }
        div.setAttribute("userDirName", inputStr);
        div.appendChild(appBtn)
        addFileWapper.appendChild(div)

        const appendFile = document.querySelectorAll('.append-file')
        appendFile.forEach(item => {
            item.style.height = '30px'
            item.style.border = "1px solid #ddd";
            item.style.borderLeft = 'none';
            item.style.borderRight = 'none';
        })
        handleFileBtnClick()

    }
}

//加载用户目录下的文件
function loadUserFiles(inputStr, userDir) {

    let div = document.createElement('div')
    let preButton = document.createElement('button')
    let downloadButton = document.createElement('button')

    preButton.className = 'preButton'
    preButton.innerHTML = '预览'

    downloadButton.className = 'downloadButton'
    downloadButton.innerHTML = '下载'

    if (inputStr) {

        div.className = 'append-file-item'
        div.innerHTML = inputStr
        div.appendChild(downloadButton)
        div.appendChild(preButton)

        let parentDir = $("div[ userDirName=" + userDir + "]")
        // alert(inputStr.substring(0,inputStr.length-1))

        if (inputStr.substring(0, inputStr.length - 1) === '我的文件') {
            let fileNumber = parseInt(parentDir[0].getAttribute("fileNumber"))
            if (!fileNumber) {
                parentDir[0].setAttribute("fileNumber", "0");
            }
            fileNumber = parseInt(parentDir[0].getAttribute("fileNumber"))
            fileNumber++
            parentDir[0].setAttribute("fileNumber", fileNumber);
        }

        parentDir[0].appendChild(div);

        handlePreBtnClick()
        handleDownloadBtnClick()

    }
}

//退出登录
function exitLogin() {

    $("#exitButton").click(function () {
            window.location.href = "/wolf/exitServlet";
        }
    )
}

//加载已下载文件列表
function updateDownloadResources() {
    $.ajax({
        url: "/wolf/loadDownloadResourcesServlet", // 请求路径
        type: "POST", //请求方式
        success: function (data) {
            if (data.hasData) {
                $(".downloadContent").empty();
                let userResources = data.userDownloadRecord;
                for (let i = 0; i < userResources.length; i++) {
                    $(".downloadContent").append("<li>" + userResources[i] + "</li>");
                }
            } else {
                $(".downloadContent").text("没有文件");
            }

        },//响应成功后的回调函数
        error: function () {
            $(".downloadContent").text("没有文件");
        },//表示如果请求响应出现错误，会执行的回调函数
    });
}


//改变文件夹高度
function handleFilesClick() {
    let fileNum = this.querySelectorAll('.append-file-item').length
    let flag = this.style.height === '30px'
    this.style.height = flag ? 30 + fileNum * 20 + 'px' : 30 + 'px'
    this.style.transition = "height 0.5s"

}

//添加、上传文件
function handleFileBtnClick() {
    const fileBtns = document.querySelectorAll('.file-btn')
    fileBtns.forEach(item => {
        item.onclick = function (e) {
            //防止冒泡
            e.stopPropagation()
            //模拟文件上传表单被点击
            $(".choseFile").click();

            //获取父文件夹中有多少子文件
            let newUserFileNumber = parseInt(this.parentElement.getAttribute("fileNumber"))
            newUserFileNumber++;
            //弹出重命名窗口
            let inputStr = prompt('请重命名文件名', '我的文件' + newUserFileNumber)

            //如果输入文件名不为默认文件名，泽文件数量减一
            if (inputStr != '我的文件' + newUserFileNumber) {
                newUserFileNumber--;
            }

            let parentNode = this.parentElement;
            //获取父节点下的所有子文件名称
            let sonNodes = parentNode.querySelectorAll('.append-file-item')
            //设置标志变量，如果文件重名，则不添加此文件
            let appendFileFlag = true
            sonNodes.forEach(function (sonNode) {
                if (inputStr === sonNode.innerHTML.substring(0, sonNode.innerHTML.indexOf('<'))) {
                    appendFileFlag = false;
                }
            })

            //如果标志变量为真，则添加文件
            if (appendFileFlag) {
                //如果没有用户点击确定，则添加文件
                if (inputStr) {
                    if (inputStr.length > 20) {
                        inputStr = inputStr.substring(0, 20)
                    }

                    this.parentElement.setAttribute('fileNumber', '' + newUserFileNumber)
                    let div = document.createElement('div')
                    let preButton = document.createElement('button')
                    let downloadButton = document.createElement('button')

                    preButton.className = 'preButton'
                    preButton.innerHTML = '预览'

                    downloadButton.className = 'downloadButton'
                    downloadButton.innerHTML = '下载'

                    div.className = 'append-file-item'
                    div.innerHTML = inputStr

                    div.appendChild(downloadButton)
                    div.appendChild(preButton)
                    this.parentElement.appendChild(div)


                    let currentHeight = parseInt(this.parentElement.style.height)
                    if (currentHeight != 30) {
                        console.log(this)
                        this.parentElement.style.height = currentHeight + 20 + 'px'
                    }

                    /**
                     * 这里添加提交文件的代码
                     *      1.获取文件名称
                     *      2.点击确定提交---拼字符串
                     *      3.后端接收文件，并保存
                     */

                        //获取父元素名称
                    let parentInnerHtml = this.parentElement.innerHTML
                    let parentName = parentInnerHtml.substring(0, parentInnerHtml.indexOf("<"));

                    $.post(
                        "/wolf/saveUserDirStructServlet",
                        {userDirName: inputStr, isDir: false, parentName: parentName},
                        function (data) {
                            //将重命名框中的内容设置为inputStr
                            $(".renameFile").val(inputStr);
                            //提交表单、上传文件
                            setTimeout("$(\".uploadSubmitButton\").click()",1500)


                        });

                } else {
                    //0.5秒之后清除表单内容
                    setTimeout("$(\".uploadResetButton\").click()", 500)
                }
            }

            handlePreBtnClick()
            handleDownloadBtnClick()
        }

    })

}

//预览文件
function handlePreBtnClick() {
    const preBtns = document.querySelectorAll('.preButton')
    // alert("你好")
    preBtns.forEach(item => {
        item.onclick = function (e) {

            e.stopPropagation()
            alert("预览文件")
        }
    })
}

//下载文件
function handleDownloadBtnClick() {
    const fileDownButtons = document.querySelectorAll('.downloadButton')
    fileDownButtons.forEach(fileDownButton => {
        fileDownButton.onclick = function (e) {
            e.stopPropagation()
            let parentInnerHTML = fileDownButton.parentElement.innerHTML;
            let filename = parentInnerHTML.substring(0, parentInnerHTML.indexOf('<'));

            let grandparentInnerHTML = fileDownButton.parentElement.parentElement.innerHTML;

            let dir = grandparentInnerHTML.substring(0, grandparentInnerHTML.indexOf('<'));

            window.location.href = "/wolf/userDownloadServlet?filename=" + filename + "&currentFileDir=" + dir;

            //刷新历史下载
            setTimeout("updateDownloadResources()",4000);


        }
    })
}

