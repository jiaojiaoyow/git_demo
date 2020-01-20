function webpageClose(){
    window.parent.location.reload()
    window.parent.layer.closeAll();
}

var path;
var localUrl = window.location.href;
var localUrlSplit = localUrl.split("?");
if(localUrlSplit.length===1){
    path = "/";
}else{
    path = decodeURIComponent(localUrlSplit[1].split("=")[1]);
}
console.log(path);

var fileMd5;
var $list = $("#thelist");
var $btn = $("#ctlBtn");
var state = 'pending'; // 上传文件初始化
var timer;
var fileArray = [];
var GUID;
var chunkSize = 30 * 1024 * 1024; // 每片3M
var thisFile;

//监听分块上传过程中的三个时间点
WebUploader.Uploader.register({
    "before-send-file" : "beforeSendFile",
    "before-send" : "beforeSend",
    "after-send-file" : "afterSendFile",
}, {
    //时间点1：所有分块进行上传之前调用此函数
    beforeSendFile : function(file) {
        thisFile = file;
        var deferred = WebUploader.Deferred();
        var owner = this.owner;
        //1、计算文件的唯一标记，用于断点续传
        (new WebUploader.Uploader()).md5File(file, 0, chunkSize)
            .progress(function(percentage) {
                $('#' + file.id).find('p.state').text("正在读取文件信息...");
            }).then(function(val) {
            fileMd5 = val;
            $('#' + file.id).find("p.state").text("成功获取文件信息...");
            //2、判断文件是否已存在
            $.ajax({
                type : "GET",
                url : "/bigFile/findResourceFileExistsByMd5",
                data : {
                    //文件唯一标记
                    md5value : fileMd5,
                    fileName : file.name,
                    caozuoPath : path
                },
                dataType : "json",
                async:false,
                success : function(response) {
                    if (response.statusCode==200) {
                        //文件已存在
                        layer.msg("上传成功",{icon: 1,time:1200});
                        $('#' + file.id).find('p.state').text('秒传');
                        //文件已上传，跳过
 						console.log("文件已上传，跳过");
                        owner.skipFile(file);
                        deferred.reject();
                    } else {
                        //文件不存在
                        deferred.resolve();
                    }
                }
            });

        });

        return deferred.promise();
    },
    //时间点2：如果有分块上传，则每个分块上传之前调用此函数
    beforeSend : function(block) {

        var deferred = WebUploader.Deferred();
        $.ajax({
            type : "GET",
            url : "/bigFile/findResourceChunkExists",
            data : {
                //文件唯一标记
                guid : GUID,
                //当前分块下标
                chunk : block.chunk
            },
            dataType : "json",
            async:false,
            success : function(response) {
                if (response.statusCode==200) {
                    //分块存在，跳过
                    console.log("分块存在，跳过");
                    deferred.reject();
                } else {
                    //分块不存在或不完整，重新发送该分块内容
                    console.log("分块"+block.chunk+"，开始发送");
                    deferred.resolve();
                }
            }
        });

        this.owner.options.formData.md5value =fileMd5;
        this.owner.options.formData.chunk =block.chunk;
        deferred.resolve();
        return deferred.promise();
    },
    //时间点3：所有分块上传成功后调用此函数
    afterSendFile : function() {
        //如果分块上传成功，则获取上传结果
        $.post('/bigFile/saveFileInfo', { guid: GUID, fileName: thisFile.name, md5value : fileMd5, caozuoPath: path, fileSize:thisFile.size}, function (data) {
            if(data.statusCode == 200){
                layer.msg("上传成功",{icon: 1,time:1200});
            }else{
                layer.msg("上传失败",{icon: 2,time:1200});
            }
        });
    }
});

var uploader = WebUploader.create({
    // swf文件路径
    swf: '/static/js/Uploader.swf',
    // 文件接收服务端。
    server: '/bigFile/upload',
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',
    chunked : true, // 分片处理
    chunkSize : chunkSize,
    chunkRetry : false,// 如果失败，则不重试
    threads : 2,// 上传并发数。允许同时最大上传进程数。
    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传
    resize: false
});

//点击上传之前调用的方法
uploader.on("uploadStart", function (file) {
    GUID = WebUploader.Base.guid();
    var paramOb = {"guid": GUID, "filedId": file.source.ruid}
    uploader.options.formData.guid = GUID;
    fileArray.push(paramOb);
});

//当有文件被添加进队列的时候
uploader.on('beforeFileQueued', function(file) {
    //清空队列
    uploader.reset();
});
//当有文件被添加进队列的时候
uploader.on('fileQueued', function (file) {
    $list.append('<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '</div>');
    $("#useTime").html("0");
});

//文件上传过程中创建进度条实时显示。
uploader.on('uploadProgress', function (file, percentage) {
    var $li = $('#' + file.id),
        $percent = $li.find('.progress .progress-bar');
    // 避免重复创建
    if (!$percent.length) {
        $percent = $('<div class="progress progress-striped active">' +
            '<div class="progress-bar" role="progressbar" style="width: 0%">' +
            '</div>' +
            '</div>').appendTo($li).find('.progress-bar');
    }
    $li.find('p.state').text('上传中');
    $percent.css('width', percentage * 100 + '%');
});
//文件成功时处理
uploader.on('uploadSuccess', function (file) {
    var successFileId = file.source.ruid;
    var successDuid;
    if (fileArray.length > 0) {
        for (var i = 0; i < fileArray.length; i++) {
            if (fileArray[i].filedId === successFileId) {
                successDuid=fileArray[i].guid;
                fileArray.splice(i, 1);
            }
        }
    }
    clearInterval(timer);
    $('#' + file.id).find('p.state').text('已上传');
});
//文件报错时处理
uploader.on('uploadError', function (file) {
    clearInterval(timer);
});
//文件完成时处理
uploader.on('uploadComplete', function (file) {
    $('#' + file.id).find('.progress').fadeOut();
    state = 'pending';
    $btn.text("开始上传");
    clearInterval(timer);
});
//按钮点击事件
$btn.on('click', function () {

    if (state === 'uploading') {
        uploader.stop(true);
        state = 'pending';
        $btn.text("继续上传");
        clearInterval(timer);
    } else {
        uploader.upload();
        state = 'uploading';
        $btn.text("暂停上传");
        timer = setInterval(function () {
            var useTime = parseInt($("#useTime").html());
            useTime = useTime + 1;
            $("#useTime").html(useTime);
        }, 1000);
    }
});