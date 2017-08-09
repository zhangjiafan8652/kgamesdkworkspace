    //第一次拿图片宽高获取都是0，
    //所以用img.load但是不知为何不支持，而且直接js无效，
    //最后用window.onload完美解决,我他么太机智了
    //(主要目的缩放图片，当图片超过屏幕宽度，缩放到屏幕宽度，同时等比缩放高度)
    window.onload = function(){
        var imgs = document.getElementsByTagName('img');
        app.log("进入javascript");
        for (var v = 0; v < imgs.length; v++) {
            var img = imgs[v];
            var originWidth = img.width;
            var originHeight = img.height;
            app.log("origin==width=="+originWidth+";height===>"+originHeight);
            var maxWidth = app.getWebViewWidth();
            if (originWidth < maxWidth) {
                continue;
            } else {
                originHeight = maxWidth/originWidth*originHeight;
                img.style.width = maxWidth+"px";
                img.style.height = originHeight+"px";
            }
            app.log("final==width=="+img.width+";height===>"+img.height);
       }
    }